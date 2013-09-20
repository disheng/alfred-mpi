package it.uniroma3.dia.alfred.mpi.runner;

import java.util.concurrent.Callable;

import it.uniroma3.dia.alfred.mpi.model.ConfigHolder;

//see http://www.vogella.com/articles/JavaConcurrency/article.html#threadpools
public class SlaveMPIThread implements Callable<Boolean> {
	
	private ConfigHolder myCfg;
	private int processRank;
	public SlaveMPIThread(ConfigHolder cfg, int rankName) {
		this.myCfg = cfg;
		this.processRank = rankName;
	}

	@Override
	public Boolean call() throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Process["+this.processRank+"] - Thread["+Thread.currentThread().getId()+"]: Working on conf " + this.myCfg.getUid());
		
		return true;
	}

}
