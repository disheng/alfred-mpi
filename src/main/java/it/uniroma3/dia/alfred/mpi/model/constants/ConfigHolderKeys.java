package it.uniroma3.dia.alfred.mpi.model.constants;

public class ConfigHolderKeys {
	
	// this needs to be the last row in a configuration
	public final static String DOMAINS_KEY = "domains";

	public final static String OUTPUT_FOLDER_KEY = "output_folder";
	public final static String BASELINE_RANDOM_ITERATIONS_KEY = "baseline_random_iterations";
	public final static String BASELINE_CORE_KEY = "baseline_core";
	public final static String BASELINE_CHOOSER_KEY = "baseline_chooser";
	public final static String ALGORITHMS_CORE_KEY = "algorithms_core";
	public final static String ALGORITHMS_CHOOSER_KEY = "algorithms_chooser";
	public final static String MAX_MQ_NUMBER_KEY = "max_mq_number";
	public final static String PROBABILITY_THREASHOLD_KEY = "probability_threashold";
	public final static String ACCURACY_THREASHOLD_KEY = "accuracy_threashold";
	public final static String FAILT_PROBABILITY_KEY = "failt_probability";
	public final static String MAX_EXPRESSIVENESS_KEY = "max_expressiveness";
	public final static String MIN_EXPRESSIVENESS_KEY = "min_expressiveness";
	public final static String LUCKY_THREASHOLD_KEY = "lucky_threashold";
	public final static String DEFAULT_ANSWER_PROBABILITY_KEY = "default_answer_probability";
	public final static String CONCURRENT_EXPERIMENTS_KEY = "concurrent_experiments";
	public final static String TYPES_KEY = "types";
	public final static String TRAINING_SIZE_KEY = "training_dimension";
	public final static String TESTING_SIZE_KEY = "testing_dimension";
	public final static String ITERATIONS_KEY = "iterations";
	public final static String WORKER_SIMULATION_KEY = "worker_simulation";
	public final static String ALGORITHM_KEY = "algorithm";

	public enum WORKER_SIMULATION {
		REAL("Real"),
		EXPONENTIAL("Exponential");
		
		private String reason;
		WORKER_SIMULATION(String reason) {
			this.reason = reason;
		}
		
		public String getReason() {
			return this.reason;
		}
	}
	
	public enum ALGORITHMS_CORE {
		SRM("Srm"),
		Simple("Simple");
		
		private String reason;
		ALGORITHMS_CORE(String reason) {
			this.reason = reason;
		}
		
		public String getReason() {
			return this.reason;
		}
	}
}