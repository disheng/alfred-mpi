package it.uniroma3.dia.alfred.mpi.runner;

import it.uniroma3.dia.alfred.mpi.model.ConfigHolder;

import java.util.List;

import mpi.MPI;
import mpi.MPIException;

public class RunAlfred {
	private static int RANK_MASTER = 0;
	
	public static void run(String[] args, List<ConfigHolder> inputConfigs) throws MPIException {
		MPI.Init(args);

		int myrank = MPI.COMM_WORLD.Rank();
		int size = MPI.COMM_WORLD.Size() ;
		
		if (myrank == RANK_MASTER) {
			MasterMPI.run(inputConfigs, size - 1);
		} else {
			SlaveMPI.run();
		}
	 
		MPI.Finalize();
	}
	
	public static void abort(MPIConstants.AbortReason aReason) {
		try {
			MPI.COMM_WORLD.Abort(aReason.getReason());
		} catch(MPIException e) {
			e.printStackTrace();
		}
		
		// MPI Abort is reason for global abort execution
		System.exit(aReason.getReason());
	}
}
