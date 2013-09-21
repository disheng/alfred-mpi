package it.uniroma3.dia.alfred.mpi.runner;

import it.uniroma3.dia.alfred.mpi.model.ConfigHolder;
import it.uniroma3.dia.alfred.mpi.model.serializer.ConfigHolderSerializable;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import mpi.MPI;
import mpi.MPIException;

public class RunAlfred {
	private static int RANK_MASTER = 0;
	
	public static void run(String[] args, List<ConfigHolder> inputConfigs) throws MPIException {
		MPI.Init(args);

		int myrank = MPI.COMM_WORLD.Rank();
		int size = MPI.COMM_WORLD.Size();
		
		if (myrank == RANK_MASTER) {
			// Shuffle order
			Collections.shuffle(inputConfigs, new Random(System.nanoTime()));
			// Run master
			MasterMPI.run(inputConfigs, size - 1);
		} else {
			SlaveMPI.run();
		}
	 
		MPI.Finalize();
	}
	
	static void abort(MPIConstants.AbortReason aReason) {
		try {
			MPI.COMM_WORLD.Abort(aReason.getReason());
		} catch(MPIException e) {
			e.printStackTrace();
		}
		
		// MPI Abort is reason for global abort execution
		System.exit(aReason.getReason());
	}
	
	static void dumpConf(int rank, List<ConfigHolder> listCfg) {
		for(ConfigHolder currCfg: listCfg) {
			ConfigHolderSerializable.toJsonFile(currCfg, rank + "-" + currCfg.getUid());
		}
	}
	
	// private static
	private RunAlfred() {};
}
