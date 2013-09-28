package it.uniroma3.dia.alfred.mpi.runner;

import it.uniroma3.dia.alfred.mpi.ConfiguratorParser;
import it.uniroma3.dia.alfred.mpi.model.ConfigHolder;
import it.uniroma3.dia.alfred.mpi.runner.MPIConstants.AbortReason;

import java.util.List;

import mpi.MPI;
import mpi.MPIException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class MainRunner {
	private static int RANK_MASTER = 0;
	
	private static Options cmdLineOptions;
	static {
		cmdLineOptions = new Options();
		cmdLineOptions.addOption("c", "config-file", true, "config file path");
		cmdLineOptions.addOption("d", "domain-file", true, "domain file path");
		cmdLineOptions.addOption("h", "help", false, "print help");
	}
	/**
	 * @param args
	 * @throws MPIException 
	 */
	public static void main(String[] args) throws MPIException {
		MPI.Init(args);

		int myrank = MPI.COMM_WORLD.Rank();
		List<ConfigHolder> parsedConfigurations = null;
		
		if (myrank == RANK_MASTER) {
			CommandLineParser parser = new GnuParser();
			CommandLine cmd;
			
			try {
				cmd = parser.parse( cmdLineOptions, args);
			} catch (ParseException e) {
				RunAlfred.abort(AbortReason.CMD_LINE_ABORT);
				return;
			}
			
			if (cmd.hasOption("h")) {
				printHelp();
				RunAlfred.abort(AbortReason.HELP_ABORT);
				return;
			}
			
			boolean canRun = cmd.hasOption("c") && cmd.hasOption("d");
			String configPath = cmd.getOptionValue("c");
			String domainPath = cmd.getOptionValue("d");
			
			canRun = canRun && (configPath != null) && (configPath.length() > 0);
			canRun = canRun && (domainPath != null) && (domainPath.length() > 0);
			
			if (!canRun) {
				printHelp("No valid arguments");
				RunAlfred.abort(AbortReason.CONFIG_FAIL);
				return;
			}
			
			parsedConfigurations = ConfiguratorParser.readConfig(configPath, domainPath);
			
			canRun = (parsedConfigurations != null) && (parsedConfigurations.size() > 0);
			
			if (!canRun) {
				printHelp("No valid configurations");
				RunAlfred.abort(AbortReason.CONFIG_FAIL);
				return;
			}
		}
			
		RunAlfred.run(args, parsedConfigurations);
	 
		MPI.Finalize();
	}
	
	private static void printHelp() {
		printHelp(null);
	}
	
	private static void printHelp(String incipit) {
		if (incipit != null) {
			System.out.println(incipit);
		}
		
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp( MainRunner.class.getName(), cmdLineOptions );
	}

}
