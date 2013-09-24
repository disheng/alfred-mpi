package it.uniroma3.dia.alfred.mapred;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.CombineFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import com.atlantbh.hadoop.s3.io.S3ObjectInputFormat;

public class XPathApplierDriver extends Configured implements Tool {

	public static final String NAME = "MapredJobDriver";	

	static String S3_BUCKET_NAME = "s3.bucket.name"; //il nome del bucket S3
	static String S3_KEY_PREFIX = "s3.key.prefix"; //per filtrare i file del bucket si può impostare un prefisso

	/**
	 * Number of files to get from S3 in single request. Default value is 100
	 */
	static String S3_MAX_KEYS = "s3.max.keys";
	static String S3_NUM_OF_KEYS_PER_MAPPER = "s3.input.numOfKeys"; // numero di file che sarà raggruppato e inviato ad ogni mapper
	static String S3_NUM_OF_MAPPERS = "s3.input.numOfMappers";

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

		long start = System.currentTimeMillis();
		
		/* ARGS CHECK */
		if (args.length < 4 && !(args.length >= 6)) {
			System.err.println("Usage "+NAME+" <pageBucketName> <xPathRulesPathS3>" +
					" <outputPathS3> <numberOfKeys> <optionalKeyPrefix> \n");
			ToolRunner.printGenericCommandUsage(System.err);
			return -1;
		}

		String nomeBucketPagine = args[0];
		//laciato così per il momento
		String inputPath = "s3n://"+nomeBucketPagine;
		String inputRulesPathS3 = args[1];
		String outputPathS3 = args[2];

		int numeroChiavi = Integer.parseInt(args[3]);

		String prefisso = "";
		
		if(args[4] != null){				
			prefisso = args[4];
		}

		Configuration conf = new Configuration();

		// Add resources
		conf.addResource("hdfs-default.xml");
		conf.addResource("hdfs-site.xml");
		conf.addResource("mapred-default.xml");
		conf.addResource("mapred-site.xml");

		conf.set(S3_BUCKET_NAME, nomeBucketPagine);
		conf.set(S3_KEY_PREFIX, prefisso);
		conf.setInt(S3_NUM_OF_KEYS_PER_MAPPER, numeroChiavi);

		Job job = new Job(conf,NAME+" : Apply Xpath on page set and save result");	

		// Carico nella distr. il file di configurazione contenente le regole xPath
		DistributedCache.addCacheFile(new Path(inputRulesPathS3).toUri(), job.getConfiguration());

		job.setMapOutputKeyClass(Text.class); //NOTA: S3ObjectWritable è usabile solo come input
		job.setMapOutputValueClass(MapWritable.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(MapWritable.class);	

		job.setMapperClass(XPathApplierMapper.class);
		job.setReducerClass(XPathApplierReducer.class);

		job.setInputFormatClass(S3ObjectInputFormat.class);
		
		//File map<pagina, list<xpath,res>> su S3
		job.setOutputFormatClass(SequenceFileOutputFormat.class);

		// Set the input path
		CombineFileInputFormat.setInputPaths(job, inputPath);
		// Set the output path
		SequenceFileOutputFormat.setOutputPath(job, new Path(outputPathS3));

		// Set the jar file to run
		job.setJarByClass(XPathApplierDriver.class);

		// Set the number of Reducers
		job.setNumReduceTasks(1);

		boolean res = job.waitForCompletion(true);

		long end = System.currentTimeMillis();		
		System.out.println("Time taken: "+ (end - start) + " msec");

		return res ? 0 : 1;
	}
}
