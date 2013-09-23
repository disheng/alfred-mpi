package it.uniroma3.dia.alfred.mpi.runner;

import java.util.Random;
import java.util.concurrent.Callable;

import it.uniroma3.dia.alfred.mpi.model.ConfigHolder;

//see http://www.vogella.com/articles/JavaConcurrency/article.html#threadpools
class SlaveMPIThread implements Callable<Boolean> {
	
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
	
		
		
		return new Random(System.nanoTime()).nextBoolean();
	}
}

// TODO:
// From config read pages List<Page>

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
//				String s = e.call();
//				this.output.addLine(CROWD_MANAGER_EXPERIMENTS_OUTPUT, s);
//				this.output.close(CROWD_MANAGER_EXPERIMENTS_OUTPUT);
				comp.submit(e);
//				Thread.sleep(10000000);
			}
		}

*/