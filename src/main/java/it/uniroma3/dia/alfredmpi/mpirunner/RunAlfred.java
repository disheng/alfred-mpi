package it.uniroma3.dia.alfredmpi.mpirunner;

import mpi.MPI;
import mpi.MPIException;

public class RunAlfred {
	private static int RANK_MASTER = 0;
	
	public static void main(String[] args) throws MPIException {
		MPI.Init(args);

		int myrank = MPI.COMM_WORLD.Rank();
		int size = MPI.COMM_WORLD.Size() ;
		
		if (myrank == RANK_MASTER) {
			MasterMPI.run();
		} else {
			SlaveMPI.run();
		}
	 
		MPI.Finalize();
	}
}
