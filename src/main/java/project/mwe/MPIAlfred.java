package project.mwe;

import it.uniroma3.dia.alfredmpi.model.ConfigHolder;
import it.uniroma3.dia.alfredmpi.mpirunner.RunAlfred;

import java.util.List;

import mpi.MPIException;

import com.google.common.collect.Lists;

public class MPIAlfred {

	/**
	 * @param args
	 * @throws MPIException 
	 */
	public static void main(String[] args) throws MPIException {
		List<ConfigHolder> input = Lists.newArrayList();
		input.add(new ConfigHolder());
		input.add(new ConfigHolder());
		input.add(new ConfigHolder());
		input.add(new ConfigHolder());
		input.add(new ConfigHolder());
		input.add(new ConfigHolder());
		input.add(new ConfigHolder());
		
		
		RunAlfred.run(args, input);
	}

}
