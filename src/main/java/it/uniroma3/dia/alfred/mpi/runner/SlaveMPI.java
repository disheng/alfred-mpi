package it.uniroma3.dia.alfred.mpi.runner;

import it.uniroma3.dia.alfred.mpi.model.ConfigHolder;
import it.uniroma3.dia.alfred.mpi.model.constants.ConfigHolderKeys;
import it.uniroma3.dia.alfred.mpi.model.serializer.ConfigHolderSerializable;
import it.uniroma3.dia.alfred.mpi.runner.MPIConstants.AbortReason;
import it.uniroma3.dia.alfred.mpi.runner.MPIConstants.TagValue;
import it.uniroma3.dia.alfred.mpi.runner.data.ResultHolder;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;

import mpi.MPI;
import mpi.MPIException;

import com.google.common.collect.Lists;

class SlaveMPI {
	private static int MIN_THREADS = 1;
	private static int MAX_THREADS = 8;
	private static Logger currentLogger = Logger.getLogger(SlaveMPI.class);
	
	private SlaveMPI() {}

	public static void run() throws MPIException {
		List<ConfigHolder> confPerWorker;
		int howMuchWork;
		//
		
		howMuchWork = recvWorkQty();

		currentLogger.debug("Process[" + MPI.COMM_WORLD.Rank() + "]:recv: " + howMuchWork);
//		System.out.println("Process[" + MPI.COMM_WORLD.Rank() + "]:recv: " + howMuchWork);
		
		ackWorkQty(howMuchWork);
		
		currentLogger.debug("Process[" + MPI.COMM_WORLD.Rank() + "]:end");
//		System.out.println("Process[" + MPI.COMM_WORLD.Rank() + "]:end");
		
		// Recv confs
		confPerWorker = recvConfigs(howMuchWork);
		currentLogger.info("Process[" + MPI.COMM_WORLD.Rank() + "]: Conf("+confPerWorker.size()+") receive done");
		// System.out.println("Process[" + MPI.COMM_WORLD.Rank() + "]: Conf("+confPerWorker.size()+") receive done");
		
//		RunAlfred.dumpConf(MPI.COMM_WORLD.Rank(), confPerWorker);
		
		if (confPerWorker.size() != howMuchWork) {
			RunAlfred.abort(AbortReason.WORK_SIZE_MISMATCH);
		}
		
		List<ResultHolder> execResults = runThreads(MPI.COMM_WORLD.Rank(), confPerWorker);
		currentLogger.info("Process[" + MPI.COMM_WORLD.Rank() + "]: ConfResults: " + execResults);
		// System.out.println("Process[" + MPI.COMM_WORLD.Rank() + "]: ConfResults: " + execResults);
		
		// Synchro after thread execution
		MPI.COMM_WORLD.Barrier();
		
		// Send to master to know how it went
		sendResults(execResults);
	}
	
	private static int recvWorkQty() {
		int[] messageSend = new int[1];
		int[] messageRecv = new int[1];
		messageSend[0] = 0;
		messageRecv[0] = 0;		
		
		try {
			MPI.COMM_WORLD.Recv(messageRecv, 0, 1, MPI.INT, MPIConstants.MASTER, TagValue.TAG_SIZE_CONF.getValue());
		} catch (MPIException e) {
			currentLogger.error("recvWorkQty()", e);
			// e.printStackTrace();
			RunAlfred.abort(AbortReason.WORK_SEND);
		}
		
		return messageRecv[0];
	}
	
	private static void ackWorkQty(int howMuchWork) {
		int[] messageSend = new int[1];
		int[] messageRecv = new int[1];
		messageSend[0] = howMuchWork;
		messageRecv[0] = 0;	
		
		try {
			MPI.COMM_WORLD.Reduce(messageSend, 0, messageRecv, 0, 1, MPI.INT, MPI.SUM, MPIConstants.MASTER);
		} catch (MPIException e) {
			currentLogger.error("ackWorkQty()", e);
			RunAlfred.abort(AbortReason.WORK_SEND_ACK);
		}
	}
	
	private static List<ConfigHolder> recvConfigs(int howMuchWork) {
		List<ConfigHolder> confPerWorker = Lists.newArrayList();
		
		char[] stringRecvBuffer;
		int[] messageRecv = new int[1];
		messageRecv[0] = 0;
		
//		System.out.println("Process[" + MPI.COMM_WORLD.Rank() + "]: Want to receive " + howMuchWork);
		for(int i = 0; i < howMuchWork; ++i) {
			try {
				MPI.COMM_WORLD.Recv(messageRecv, 0, 1, MPI.INT, MPIConstants.MASTER, TagValue.TAG_CONF_LEN.getValue());
				stringRecvBuffer = new char[messageRecv[0]];
//				System.out.println("Process[" + MPI.COMM_WORLD.Rank() + "]: Receiving conf len " + messageRecv[0]);				
				MPI.COMM_WORLD.Recv(stringRecvBuffer, 0, messageRecv[0], MPI.CHAR, MPIConstants.MASTER, TagValue.TAG_CONF_DATA.getValue());				
				confPerWorker.add( ConfigHolderSerializable.fromJson(String.valueOf(stringRecvBuffer)) );
//				System.out.println("Process[" + MPI.COMM_WORLD.Rank() + "]: Receiving conf " + confPerWorker.get(confPerWorker.size() - 1).getUid() );				
			} catch (MPIException e) {
				currentLogger.error("recvConfigs()", e);
				// e.printStackTrace();
				RunAlfred.abort(AbortReason.WORK_RECV_DATA);
			}
		}
		
		return confPerWorker;
	}
	
	private static void sendResults(List<ResultHolder> execResults) throws MPIException {
		int[] messageSend = new int[1];
		messageSend[0] = execResults.size();
		
		byte[] bDataSend = new byte[execResults.size()];
		for(int i = 0; i < bDataSend.length; ++i) {
			bDataSend[i] = (byte) (execResults.get(i).getBooleanResult() ? 1 : 0);
		}
		
		MPI.COMM_WORLD.Send(messageSend, 0, 1, MPI.INT, MPIConstants.MASTER, TagValue.TAG_CONF_RESULTS_SIZE.getValue());
		MPI.COMM_WORLD.Send(bDataSend, 0, bDataSend.length, MPI.BYTE, MPIConstants.MASTER, TagValue.TAG_CONF_RESULTS.getValue());
	}
	
	private static List<ResultHolder> runThreads(int rank, List<ConfigHolder> confPerWorker) {
		// Execute configurations in parallel if necessary
        ExecutorService executor = Executors.newFixedThreadPool(Math.max(Math.min(MAX_THREADS, Runtime.getRuntime().availableProcessors() - 1), MIN_THREADS));
        List<Future<ResultHolder>> threadResults = Lists.newArrayList();
        
        List<String> attributeList;
        
        for (ConfigHolder cfgCurr: confPerWorker) {
    		attributeList = cfgCurr.getAssociatedDomain().getXPathNames();
    		createOutputDirForConf(cfgCurr);
    		
    		for(String attrCurrent: attributeList) {
    			threadResults.add(executor.submit(new SlaveMPIThread_Attribute(cfgCurr, attrCurrent, rank)));
    		}
        }
        executor.shutdown();
        while (!executor.isTerminated()) {}	
        
        List<ResultHolder> bResult = Lists.newArrayList();
        for(Future<ResultHolder> bCurr: threadResults) {
        	try {
				bResult.add(bCurr.get());
			} catch (InterruptedException e) {
				currentLogger.error("runThreads()", e);
				// e.printStackTrace();
			} catch (ExecutionException e) {
				currentLogger.error("runThreads()", e);
				// e.printStackTrace();
			}
        }
        
        return bResult;
	}
	
	private static void createOutputDirForConf(ConfigHolder cfg) {
		String configDir = cfg.getConfigurationValue(ConfigHolderKeys.OUTPUT_FOLDER_KEY);
		
		try {
			Files.createDirectories(Paths.get(configDir));
		} catch(Exception e) {
			// System.out.println("Directory " + configDir + " might already exist.");
			currentLogger.error("Directory " + configDir + " might already exist.", e);
		}
	}
}
