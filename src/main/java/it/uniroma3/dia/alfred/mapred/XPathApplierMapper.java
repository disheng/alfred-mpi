package it.uniroma3.dia.alfred.mapred;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.log4j.Logger;

public class XPathApplierMapper 
extends Mapper<Text, Text, Text, Text>{ //TODO

	static final Logger log = Logger.getLogger(XPathApplierMapper.class);
	public enum Counters { DOCS, ERROR, VALID };

	@Override
	protected void setup(Context context)
			throws IOException, InterruptedException {
		//TODO
	}

	/**
	 * Maps the input.
	 *
	 * @param key The row key.
	 * @throws java.io.IOException When mapping the input fails.
	 */
	@Override
	public void map(Text key, Text value, Context context) 
			throws IOException {
		//TODO

		context.getCounter(Counters.DOCS).increment(1);	

		//APPLY ROLE/ROLES THEN
		context.getCounter(Counters.VALID).increment(1);	

	try {
			//WRITE THE OUTPUT
		} catch (Exception e) {
			log.error(e.toString());
			context.getCounter(Counters.ERROR).increment(1);
		}
	}		

}