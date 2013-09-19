package it.uniroma3.dia.alfred.mpi.runner;

import it.uniroma3.dia.alfred.mpi.model.ConfigHolder;
import it.uniroma3.dia.alfred.mpi.runner.MPIConstants.AbortReason;

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
			MPI.COMM_WORLD.Recv(messageRecv, 0, 1, MPI.INT, MPIConstants.MASTER, MPIConstants.TAG_SIZE_CONF);
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
		
		// TODO: recv confs
		
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
