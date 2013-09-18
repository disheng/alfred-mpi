package it.uniroma3.dia.alfredmpi.examples;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import mpi.MPI;
import mpi.MPIException;

public class MPIExample {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Options cmdLineOptions = new Options();
		cmdLineOptions.addOption("p", "rank", true, "run rank example");
		cmdLineOptions.addOption("r", "ring", true, "run ring example");
		
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
				rankExample(args);
			} catch (MPIException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (cmd.hasOption("r")) {
			try {
				ringExample(args);
			} catch (MPIException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			System.out.println("No valid switch");
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp( MPIExample.class.getName(), cmdLineOptions );
		}

	}

	private static void rankExample(String[] args) throws MPIException {
		MPI.Init(args);

		int myrank = MPI.COMM_WORLD.Rank();
		int size = MPI.COMM_WORLD.Size() ;
		System.out.println("Hello world from rank " + myrank + " of " + size);
	 
		MPI.Finalize();
	}
	
	private static void ringExample(String[] args) throws MPIException {
		MPI.Init(args) ;
	      
		int source;  // Rank of sender
		int dest;    // Rank of receiver 
		int tag=50;  // Tag for messages	
		int next;
		int prev;
		int message[] = new int [1];

		int myrank = MPI.COMM_WORLD.Rank() ;
		int size = MPI.COMM_WORLD.Size() ;

		/* Calculate the rank of the next process in the ring.  Use the
		   modulus operator so that the last process "wraps around" to
		   rank zero. */

		next = (myrank + 1) % size;
		prev = (myrank + size - 1) % size;

		/* If we are the "master" process (i.e., MPI_COMM_WORLD rank 0),
		   put the number of times to go around the ring in the
		   message. */

		if (0 == myrank) {
		    message[0] = 10;

		    System.out.println("Process 0 sending " + message[0] + " to rank " + next + " (" + size + " processes in ring)"); 
		    MPI.COMM_WORLD.Send(message, 0, 1, MPI.INT, next, tag); 
		}

		/* Pass the message around the ring.  The exit mechanism works as
		   follows: the message (a positive integer) is passed around the
		   ring.  Each time it passes rank 0, it is decremented.  When
		   each processes receives a message containing a 0 value, it
		   passes the message on to the next process and then quits.  By
		   passing the 0 message first, every process gets the 0 message
		   and can quit normally. */

		while (true) {
		    MPI.COMM_WORLD.Recv(message, 0, 1, MPI.INT, prev, tag);

		    if (0 == myrank) {
			--message[0];
			System.out.println("Process 0 decremented value: " + message[0]);
		    }

		    MPI.COMM_WORLD.Send(message, 0, 1, MPI.INT, next, tag);
		    if (0 == message[0]) {
			System.out.println("Process " + myrank + " exiting");
			break;
		    }
		}

		/* The last process does one extra send to process 0, which needs
		   to be received before the program can exit */

		if (0 == myrank) {
		    MPI.COMM_WORLD.Recv(message, 0, 1, MPI.INT, prev, tag);
		}
	    
		MPI.Finalize();
	}	
}
