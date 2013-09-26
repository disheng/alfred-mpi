package it.uniroma3.dia.alfred.mpi.runner.main;

import it.uniroma3.dia.alfred.mpi.ConfiguratorParser;
import it.uniroma3.dia.alfred.mpi.model.ConfigHolder;
import it.uniroma3.dia.alfred.mpi.runner.RunAlfred;

import java.util.List;

import mpi.MPIException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class MainRunner {

	private static Options cmdLineOptions;
	static {
		cmdLineOptions = new Options();
		cmdLineOptions.addOption("c", "config-file", true, "config file path");
		cmdLineOptions.addOption("d", "domain-file", true, "domain file path");
		cmdLineOptions.addOption("h", "help", false, "print help");
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		cmdLineOptions.addOption("c", "config-file", true, "config file path");
		cmdLineOptions.addOption("d", "domain-file", true, "domain file path");
		
		CommandLineParser parser = new GnuParser();
		CommandLine cmd;
		
		try {
			cmd = parser.parse( cmdLineOptions, args);
		} catch (ParseException e) {
			e.printStackTrace();
			return;
		}
		
		if (cmd.hasOption("h")) {
			printHelp();
			return;
		}
		
		boolean canRun = cmd.hasOption("c") && cmd.hasOption("d");
		String configPath = cmd.getOptionValue("c");
		String domainPath = cmd.getOptionValue("d");
		
		canRun = canRun && (configPath != null) && (configPath.length() > 0);
		canRun = canRun && (domainPath != null) && (domainPath.length() > 0);
		
		if (!canRun) {
			printHelp("No valid arguments");
			return;
		}
		
		List<ConfigHolder> parsedConfigurations = 
		ConfiguratorParser.readConfig(configPath, domainPath);
		
		canRun = (parsedConfigurations != null) && (parsedConfigurations.size() > 0);
		
		if (canRun) {
			try {
				RunAlfred.run(args, parsedConfigurations);
			} catch (MPIException e) {
				e.printStackTrace();
			}
		} else {
			printHelp("No valid configurations");
		}

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
