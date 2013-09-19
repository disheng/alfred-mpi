package it.uniroma3.dia.alfredmapred;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class XPathApplierDriver extends Configured implements Tool {
	
	public static final String NAME = "MapredJobDriver";	

	/**
	 * Main entry point.
	 *
	 * @param args The command line parameters.
	 * @throws Exception When running the job fails.
	 */
	public static void main(String[] args) throws Exception {
		ToolRunner.run(new XPathApplierDriver(), args);
	}

	public int run(String[] args) throws Exception {	

		/* FUTURE ARGS CHECK
		if (args.length < 1) {
			System.err.println("Usage: "+NAME+" .. \n");
			ToolRunner.printGenericCommandUsage(System.err);
			return -1;
		}
		*/
		
		long start = System.currentTimeMillis();

		Configuration conf = new Configuration();		
		Job job = new Job(conf,NAME+"Apply Xpath on page set and save result");
		job.setJarByClass(XPathApplierDriver.class);	
		
		//TODO (input file from s3, file output on s3?, map/reducer schema?)		

		
		boolean res = job.waitForCompletion(true);
		
		long end = System.currentTimeMillis();		
		System.out.println("Time taken: "+ (end - start) + " msec");

		return res ? 0 : 1;
	}
}
