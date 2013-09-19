package it.uniroma3.dia.alfredmpi.mpirunner;

import it.uniroma3.dia.alfredmpi.mpirunner.MPIConstants.AbortReason;

import java.util.List;

import mpi.MPI;
import mpi.MPIException;

import com.google.common.collect.Lists;

public class SlaveMPI {
	// TODO: contains slave logic
	
	public static void run() throws MPIException {
		List<Integer> confPerWorker = Lists.newArrayList();
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
	}
}
