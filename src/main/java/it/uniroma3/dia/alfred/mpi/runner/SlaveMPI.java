package it.uniroma3.dia.alfred.mpi.runner;

import it.uniroma3.dia.alfred.mpi.model.ConfigHolder;
import it.uniroma3.dia.alfred.mpi.model.serializer.ConfigHolderSerializable;
import it.uniroma3.dia.alfred.mpi.runner.MPIConstants.AbortReason;
import it.uniroma3.dia.alfred.mpi.runner.MPIConstants.TagValue;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import mpi.MPI;
import mpi.MPIException;

import com.google.common.collect.Lists;

public class SlaveMPI {
	// TODO: contains slave logic
	
	public static void run() throws MPIException {
		List<ConfigHolder> confPerWorker = Lists.newArrayList();
		int howMuchWork = 0;
		
		int[] messageSend = new int[1];
		int[] messageRecv = new int[1];
		messageSend[0] = 0;
		messageRecv[0] = 0;		
		
		try {
			MPI.COMM_WORLD.Recv(messageRecv, 0, 1, MPI.INT, MPIConstants.MASTER, TagValue.TAG_SIZE_CONF.getValue());
		} catch (MPIException e) {
			e.printStackTrace();
			RunAlfred.abort(AbortReason.WORK_SEND);
		}
		
		howMuchWork = messageRecv[0];
		System.out.println("Process[" + MPI.COMM_WORLD.Rank() + "]:recv: " + howMuchWork);
		
		messageSend[0] = messageRecv[0];
		messageRecv[0] = 0;
		try {
			MPI.COMM_WORLD.Reduce(messageSend, 0, messageRecv, 0, 1, MPI.INT, MPI.SUM, MPIConstants.MASTER);
		} catch (MPIException e) {
			e.printStackTrace();
			RunAlfred.abort(AbortReason.WORK_SEND_ACK);
		}
		
		System.out.println("Process[" + MPI.COMM_WORLD.Rank() + "]:end");
		
		// Recv confs
		char[] stringRecvBuffer;
		messageRecv[0] = 0;
		for(int i = 0; i < howMuchWork; ++i) {
			MPI.COMM_WORLD.Recv(messageRecv, 0, 1, MPI.INT, MPIConstants.MASTER, TagValue.TAG_CONF_LEN.getValue());
			stringRecvBuffer = new char[messageRecv[0]];
			MPI.COMM_WORLD.Recv(stringRecvBuffer, 0, messageRecv[0], MPI.CHAR, MPIConstants.MASTER, TagValue.TAG_CONF_DATA.getValue());
			confPerWorker.add( ConfigHolderSerializable.fromJson(String.valueOf(stringRecvBuffer)) );
		}
		
		// Execute configurations in parallel if necessary
        ExecutorService executor = Executors.newFixedThreadPool(Math.max(Runtime.getRuntime().availableProcessors() - 1, 1));
        List<Future<Boolean>> threadResults = Lists.newArrayList();
        for (ConfigHolder cfgCurr: confPerWorker) {
        	threadResults.add(executor.submit(new SlaveMPIThread(cfgCurr)));
        }
        executor.shutdown();
        while (!executor.isTerminated()) {}		
		
        // TODO: iterate futures
        // future.get() retrieves the results

        // TODO: send to master to know how it went 
	}
}
