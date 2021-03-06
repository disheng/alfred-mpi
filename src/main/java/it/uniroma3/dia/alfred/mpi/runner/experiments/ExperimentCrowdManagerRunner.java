package it.uniroma3.dia.alfred.mpi.runner.experiments;

import java.util.HashMap;
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
	// Alf parameters
	private boolean expansion;
	private int initialExp;
	private int maxExp;
	private double probt;
	private double acct;
	private int maxMQ;
	String sampleChooser;
	double workeraccuracy;
	

	public ExperimentCrowdManagerRunner(List<Page> all, Page firstPage,
			Map<String, String> url2Value, int occ, ExperimentKey main, int times, WORKER_FUNCTION f, double expo) {
		this.allPages = new LinkedList<Page>(all);
		this.training = Lists.newLinkedList();
		this.firstPage = firstPage;
		this.page2values = new HashMap<String, String>(url2Value);
		this.occ = occ;
		this.key = main;
		this.times = times;
		
		if (f.equals(WORKER_FUNCTION.REAL)){
			this.simul = new RealWorkerSimulation(RealWorkerSimulation.WORKER_DISTRIBUTION);
		}
		
		if (f.equals(WORKER_FUNCTION.EXPONENTIAL)){
			this.simul = new ExponentialWorkerSimulation(expo);
		}
		
		initDefaultALFParams();
	}
	
	private void initDefaultALFParams() {
		this.expansion = false;
		this.initialExp = 5;
		this.maxExp = 5;
		this.probt = 0.999;
		this.acct = 0.9999;
		this.maxMQ = 10;
		this.sampleChooser = "Entropy";
		this.workeraccuracy = 0.9;
	}

	@Override
	public String call() throws Exception {
		// this.allPages.add(firstPage);
		this.training.addAll(this.allPages);

		// this.firstCore = AlfCoreFactory.getSystemFromConfiguration(false, 5, 5, 0.999, 0.9999, 10, "Entropy", 0.9);
		this.firstCore = AlfCoreFactory.getSystemFromConfiguration(this.expansion, this.initialExp, this.maxExp, this.probt, this.acct, this.maxMQ, this.sampleChooser, this.workeraccuracy);
		this.firstCore.setUp("crowd", new MaterializedPageSet(training));
		this.firstCore.firstSample(firstPage.getTitle(), this.page2values.get(firstPage.getTitle()), this.occ);
		
		
		// selects which worker should be simulated
		String res = "";
		for (int i=0; i<this.times ; i++){
			res += this.runCrowdManager() + "\n";
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

	public void setExpansion(boolean expansion) {
		this.expansion = expansion;
	}

	public void setInitialExp(int initialExp) {
		this.initialExp = initialExp;
	}

	public void setMaxExp(int maxExp) {
		this.maxExp = maxExp;
	}

	public void setProbt(double probt) {
		this.probt = probt;
	}

	public void setAcct(double acct) {
		this.acct = acct;
	}

	public void setMaxMQ(int maxMQ) {
		this.maxMQ = maxMQ;
	}

	public void setSampleChooser(String sampleChooser) {
		this.sampleChooser = sampleChooser;
	}

	public void setWorkeraccuracy(double workeraccuracy) {
		this.workeraccuracy = workeraccuracy;
	}
}
