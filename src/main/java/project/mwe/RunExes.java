package project.mwe;

import mpi.MPIException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class RunExes {

	public static void main(String[] args) {
		Options cmdLineOptions = new Options();
		cmdLineOptions.addOption("p", "rank", false, "run rank example");
		cmdLineOptions.addOption("r", "ring", false, "run ring example");
		cmdLineOptions.addOption("a", "alfred-test", false, "run alfred example");
		cmdLineOptions.addOption("s", "serialize-test", false, "serialize a xpath random class");
		
		CommandLineParser parser = new GnuParser();
		CommandLine cmd;
		
		try {
			cmd = parser.parse( cmdLineOptions, args);
		} catch (ParseException e) {
			e.printStackTrace();
			return;
		}
		
		if (cmd.hasOption("p")) {
			try {
				MPIExamples.rankExample(args);
			} catch (MPIException e) {
				e.printStackTrace();
			}
		} else if (cmd.hasOption("r")) {
			try {
				MPIExamples.ringExample(args);
			} catch (MPIException e) {
				e.printStackTrace();
			}
		} else if (cmd.hasOption("a")) {
			try {
				MPIAlfred.main(args);
			} catch (MPIException e) {
				e.printStackTrace();
			}
		} else if (cmd.hasOption("s")) {
			SerializeTesting.runExample();
		} else {
			System.out.println("No valid switch");
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp( MPIExamples.class.getName(), cmdLineOptions );
		}
	}

}
