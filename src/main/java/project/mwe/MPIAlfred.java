package project.mwe;

import it.uniroma3.dia.alfred.mpi.model.ConfigHolder;
import it.uniroma3.dia.alfred.mpi.runner.RunAlfred;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import mpi.MPIException;

import com.google.common.collect.Lists;

public class MPIAlfred {

	static final int CONFIG_MAX = 7;
	static final int CONFIG_VAL_MIN = 1;
	static final int CONFIG_VAL_MAX = 7;
	
	/**
	 * @param args
	 * @throws MPIException 
	 */

	
	public static void main(String[] args) throws MPIException {
		List<ConfigHolder> input = Lists.newArrayList();
		
		Random rndSeed = new Random(System.nanoTime());
		for(int i = 0; i < CONFIG_MAX; ++i) {
			input.add(generateNew( Math.max(CONFIG_VAL_MAX, rndSeed.nextInt(CONFIG_VAL_MAX)) ));
		}
		
		RunAlfred.run(args, input);
	}
	
	private static ConfigHolder generateNew(int numberOfValues) {
		ConfigHolder cRet = new ConfigHolder(UUID.randomUUID().toString());
		for(int i = 0; i < numberOfValues; ++i) {
			cRet.setConfigurationValue(UUID.randomUUID().toString(), UUID.randomUUID().toString());
		}
		
		return cRet;
	}

}
