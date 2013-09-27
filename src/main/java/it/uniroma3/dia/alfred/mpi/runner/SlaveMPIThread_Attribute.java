package it.uniroma3.dia.alfred.mpi.runner;

import it.uniroma3.dia.alfred.mpi.model.ConfigHolder;
import it.uniroma3.dia.alfred.mpi.model.constants.ConfigHolderKeys;
import it.uniroma3.dia.alfred.mpi.model.constants.DomainHolderKeys;
import it.uniroma3.dia.alfred.mpi.runner.data.ResultHolder;
import it.uniroma3.dia.alfred.mpi.runner.s3.GenerateLazyPagesFromDomain;
import it.uniroma3.dia.alfred.xpath.XPathHandler;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import model.Page;
import model.Rule;
import rules.xpath.XPathRule;

import com.google.common.collect.Maps;

import experiment.OutputManager;
import experiment.runner.ExperimentCrowdManagerRunner;
import experiment.runner.ExperimentCrowdManagerRunner.WORKER_FUNCTION;

public class SlaveMPIThread_Attribute implements Callable<ResultHolder> {
	
	private ConfigHolder myCfg;
	private String attribute;
	private int processRank;
	private OutputManager output;
	
	public SlaveMPIThread_Attribute(ConfigHolder cfg, String attribute, int rankName) {
		this.myCfg = cfg;
		this.attribute = attribute;
		this.processRank = rankName;
		this.output = new OutputManager();
	}

	@Override
	public ResultHolder call() throws Exception {
        List<Page> allPages;
        Page goldenPage;
        int trainingSet;
        int testSet;
        
        System.out.println("Starting thread - " + getOutputName());
        
        // Here we run alf on a single attribute of a domain with the given configuration
		this.output.open( this.getOutputName() );
		trainingSet = Integer.valueOf( this.myCfg.getConfigurationValue(ConfigHolderKeys.TRAINING_SIZE_KEY) );
		testSet = Integer.valueOf( this.myCfg.getConfigurationValue(ConfigHolderKeys.TESTING_SIZE_KEY) );

		allPages = GenerateLazyPagesFromDomain.getPages(this.myCfg.getAssociatedDomain(), trainingSet + testSet);
		goldenPage = GenerateLazyPagesFromDomain.getGoldenPage(this.myCfg.getAssociatedDomain());
		// Add also golden
		allPages.add(goldenPage);
		
		Rule rule = new XPathRule(this.myCfg.getAssociatedDomain().getGoldenXPath(this.attribute));
		Map<String, String> url2Value = Maps.newHashMap();
		
		int maxExpressiveness = Integer.valueOf( this.myCfg.getConfigurationValue(ConfigHolderKeys.MAX_EXPRESSIVENESS_KEY) );
		String workerSimulation = this.myCfg.getConfigurationValue(ConfigHolderKeys.WORKER_SIMULATION_KEY);
		
		System.out.println(getOutputName() + "] Filtering pages");
		for (Page page : allPages) {
			try {
				url2Value.put(page.getTitle(), XPathHandler.executeQueryAsText(page, rule));
			} catch(Exception e) {
				System.out.println(getOutputName() + "] problem while fetching/parsing \" " + page.getTitle() + "\" - Ex: " + e.toString());
			}
		}
		int occ = 1; // this.experiments.getOccurrence(domain, attribute);
		
		// Call alfred crowd (disheng says)

		
		String experimentResult = null;
		try {
			System.out.println(getOutputName() + "] Building experiment class");
			ExperimentCrowdManagerRunner e = 
					new ExperimentCrowdManagerRunner(allPages, goldenPage, url2Value, occ, 
							null, maxExpressiveness, this.getWorkerFunction(workerSimulation), 0.20);
			System.out.println(getOutputName() + "] Calling experiment class");
			experimentResult = e.call();
		} catch (Exception e) {
			experimentResult = null;
			e.printStackTrace();
		}
		
		if (experimentResult != null) {
			// Save it?
			this.output.addLine(getOutputName(), experimentResult);	
			this.output.close(getOutputName());
		}
		
		return new ResultHolder(((experimentResult != null) && (experimentResult.length() > 0)), experimentResult);
	}
	
	private String getOutputName() {
		return this.myCfg.getUid() + "-" + 
			this.myCfg.getAssociatedDomain().getConfigurationValue(DomainHolderKeys.DOMAIN_ID_KEY) +
			"-" + this.attribute + "-" + this.processRank + "-" + Thread.currentThread().getId();
	}
	
	private WORKER_FUNCTION getWorkerFunction(String wf) {
		WORKER_FUNCTION wfDefault = WORKER_FUNCTION.EXPONENTIAL;
		
		if (wf.equalsIgnoreCase(ConfigHolderKeys.WORKER_SIMULATION.REAL.getReason() )) {
			wfDefault = WORKER_FUNCTION.REAL;
		} else if (wf.equalsIgnoreCase(ConfigHolderKeys.WORKER_SIMULATION.EXPONENTIAL.getReason() )) {
			wfDefault = WORKER_FUNCTION.EXPONENTIAL;
		}
		
		return wfDefault;
	}
}