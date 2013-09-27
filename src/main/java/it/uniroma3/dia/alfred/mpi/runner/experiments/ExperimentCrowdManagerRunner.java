package it.uniroma3.dia.alfred.mpi.runner.experiments;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import model.MaterializedPageSet;
import model.Page;
import model.Rule;
import alfcore.AlfCoreFacade;
import alfcore.AlfCoreFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import crowd.CrowdManager;
import crowd.WorkerTask;
import experiment.AlfCoreSimulation;
import experiment.key.ExperimentKey;
import experiment.responder.AutomaticResponderFile;
import experiment.workerSimulation.ExponentialWorkerSimulation;
import experiment.workerSimulation.RealWorkerSimulation;
import experiment.workerSimulation.WorkerSimulation;

public class ExperimentCrowdManagerRunner implements Callable<String> {

	@SuppressWarnings("unused")
	private static final int MAX_NUMBER_WORKERS = 5;
	private static final double QUALITY_TERMINATION_THREASHOLD = 0.9;

	public static enum WORKER_FUNCTION {EXPONENTIAL, REAL};

	private ExperimentKey key;
	private int occ;
	private Map<String, String> page2values;
	private Page firstPage;
	private List<Page> allPages;
	private AlfCoreFacade firstCore;
	private LinkedList<Page> training;
	private int times;
	private WorkerSimulation simul;

	public ExperimentCrowdManagerRunner(List<Page> all, Page firstPage,
			Map<String, String> url2Value, int occ, ExperimentKey main, int times, WORKER_FUNCTION f, double expo) {
		this.allPages = Lists.newLinkedList(all);
		this.firstPage = firstPage;
		this.page2values = Maps.newHashMap(url2Value);
		this.occ = occ;
		this.key = main;
		this.times = times;
		
		if (f.equals(WORKER_FUNCTION.REAL)){
			this.simul = new RealWorkerSimulation(RealWorkerSimulation.WORKER_DISTRIBUTION);
		}
		
		if (f.equals(WORKER_FUNCTION.EXPONENTIAL)){
			this.simul = new ExponentialWorkerSimulation(expo);
		}
			
	}

	@Override
	public String call() throws Exception {
		this.allPages.add(firstPage);

		this.training.addAll(this.allPages);

		this.firstCore = AlfCoreFactory.getSystemFromConfiguration(false, 5, 5, 0.999, 0.9999, 10,"Entropy", 0.9);
		this.firstCore.setUp("crowd", new MaterializedPageSet(training));
		this.firstCore.firstSample(firstPage.getTitle(), this.page2values.get(firstPage.getTitle()), this.occ);
		
		
		// selects which worker should be simulated
		String res = "";
		for (int i=0; i<this.times ; i++){
			res += this.runCrowdManager();
		}
		
		return res;
	}

	private String runCrowdManager() {

		double[] accuracies = simul.getWorkersAccuracy(10);

		String res = executeAlfred(false, false, "estimation", accuracies);
		
		return res;
	}

	private String executeAlfred(boolean probabilities, boolean real, String label,
			double[] accuracies) {

		AlfCoreSimulation firstTask = this.getNewAlfSimulation(accuracies[0]);
		AlfCoreSimulation secondTask = this.getNewAlfSimulation(accuracies[1]);
		List<WorkerTask> tasks = Lists.newLinkedList();

		if (real) {
			tasks.add(firstTask.getObservedWorkerTask());
			tasks.add(secondTask.getObservedWorkerTask());
		} else {
			tasks.add(firstTask.getWorkerTask());
			tasks.add(secondTask.getWorkerTask());
		}

		CrowdManager crowd = null;

		if (label.equals("estimation"))
			if (probabilities) {
				// TODO to move threshold in configuration
				crowd = new CrowdManager(true, true, firstCore, tasks, QUALITY_TERMINATION_THREASHOLD);
			} else {
				crowd = new CrowdManager(true, firstCore, tasks, QUALITY_TERMINATION_THREASHOLD);
			}
		if (label.equals("no estimation"))
			if (probabilities) {
				crowd = new CrowdManager(true, false, firstCore, tasks, QUALITY_TERMINATION_THREASHOLD);
			} else {
				crowd = new CrowdManager(false, firstCore, tasks, QUALITY_TERMINATION_THREASHOLD);
			}
		int i = 2;

		while (!crowd.isTerminated()) {		
			AlfCoreSimulation newSim = null;

			if (i >= accuracies.length)
				return null;
			else
				newSim = this.getNewAlfSimulation(accuracies[i]);

			if (real) {
				crowd.addTask(newSim.getObservedWorkerTask());
			} else {
				crowd.addTask(newSim.getWorkerTask());
			}
			
			
		}
		
	
		String res = crowd.getMostLikelyVector().getRule().toString();
		System.out.println(res);
		return res;
	}

	private AlfCoreSimulation getNewAlfSimulation(double accuracy) {
		AlfCoreFacade alf = AlfCoreFactory.getSystemFromConfiguration(false, 5, 5, 0.9999, 0.9999, 10,
				"Entropy", 0.9);

		AlfCoreSimulation simul = new AlfCoreSimulation(page2values, key, accuracy, alf, firstPage, occ);
		simul.simulate(new MaterializedPageSet(this.training));
		
		return simul;	
		}

	@SuppressWarnings("unused")
	private double getObservedAccuracy(Rule rule, List<Page> testPages) {
		AutomaticResponderFile resp = new AutomaticResponderFile(page2values, key);
		resp.computeCorrectness(testPages, rule);
		return resp.getAccuracy();
	}
}
