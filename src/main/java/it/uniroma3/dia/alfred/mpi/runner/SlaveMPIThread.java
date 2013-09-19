package it.uniroma3.dia.alfred.mpi.runner;

import java.util.concurrent.Callable;

import it.uniroma3.dia.alfred.mpi.model.ConfigHolder;

//see http://www.vogella.com/articles/JavaConcurrency/article.html#threadpools
public class SlaveMPIThread implements Callable<Boolean> {
	
	private ConfigHolder myCfg;
	public SlaveMPIThread(ConfigHolder cfg) {
		this.myCfg = cfg;
	}

	@Override
	public Boolean call() throws Exception {
		// TODO Auto-generated method stub
		return true;
	}

}
