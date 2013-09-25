package it.uniroma3.dia.alfred.mpi.runner;

import it.uniroma3.dia.alfred.mpi.model.ConfigHolder;
import it.uniroma3.dia.alfred.mpi.model.constants.ConfigHolderKeys;
import it.uniroma3.dia.alfred.mpi.model.constants.DomainHolderKeys;
import it.uniroma3.dia.alfred.mpi.runner.data.ResultHolder;
import it.uniroma3.dia.alfred.mpi.runner.s3.GenerateLazyPagesFromDomain;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;

import com.google.common.collect.Maps;

import rules.xpath.XPathRule;

import model.Page;
import model.Rule;

import experiment.OutputManager;

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
		for (Page page : allPages) {
			url2Value.put(page.getTitle(), rule.applyOn(page).getTextContent());
		}
		int occ = 1; // this.experiments.getOccurrence(domain, attribute);
		
		// TODO: what to call here?
		// ExperimentCrowdManagerRunner e = new ExperimentCrowdManagerRunner(allPages, goldenPage,url2Value, occ, null, # max_expressiveness, WORKER_FUNCTION.EXPONENTIAL, 0.20);
		// String s = e.call();
		// è orribile il call ... ma per ora mi sembra il male minore :)
		
		return new ResultHolder(new Random(System.nanoTime()).nextBoolean(), "");
	}
	
	private String getOutputName() {
		return this.myCfg.getUid() + "-" + 
			this.myCfg.getAssociatedDomain().getConfigurationValue(DomainHolderKeys.DOMAIN_ID_KEY) +
			"-" + this.processRank + "-" + this.attribute;
	}
}

/*
/*
List<Page> all;
this.output.open(CROWD_MANAGER_EXPERIMENTS_OUTPUT);

for (DOMAINS domain : this.experiments.getDomains()) {

	ExperimentKey main = new ExperimentKeyMongo(domain.toString(),
			this.experiments.getWebsite(domain), null);

	firstPage = this.pageLoader.getOnePage(this.experiments.getFirstPage(domain), main);

	all = this.pageLoader.getDomainPages(main, PAGES_NUMBER);
	all.add(firstPage);

	for (String attribute : this.experiments.getAttributes(domain)) {
		main = main.buildNewKey(attribute);

		Rule rule = new XPathRule(this.experiments.getGoldenXpath(domain, attribute));
		Map<String, String> url2Value = new HashMap<>();
		for (Page page : all) {
			url2Value.put(page.getTitle(), rule.applyOn(page).getTextContent());
		}
		int occ = this.experiments.getOccurrence(domain, attribute);

		// submit task
		ExperimentCrowdManagerRunner e = new ExperimentCrowdManagerRunner(all, firstPage,
				url2Value, occ, main, 5, WORKER_FUNCTION.EXPONENTIAL, 0.20);
//		String s = e.call();
//		this.output.addLine(CROWD_MANAGER_EXPERIMENTS_OUTPUT, s);
//		this.output.close(CROWD_MANAGER_EXPERIMENTS_OUTPUT);
		comp.submit(e);
//		Thread.sleep(10000000);
	}
}
*/
