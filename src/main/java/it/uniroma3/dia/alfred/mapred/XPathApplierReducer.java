package it.uniroma3.dia.alfred.mapred;

import java.io.IOException;

import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Reducer;

public class XPathApplierReducer 
extends Reducer<Text,MapWritable,Text,MapWritable> {
	
	private MapWritable result;
	
	@Override
	protected void setup(Context context) {
		result = new MapWritable();
	}

	@Override
	public void reduce(Text key, Iterable<MapWritable> mappe, Context context) throws IOException, InterruptedException {

		for (MapWritable mappa : mappe) {	    	
			for (Writable rule : mappa.keySet()) {
				Text regola = (Text)rule;
				Text valore = (Text)mappa.get(rule);
				result.put(new Text(regola.toString()), new Text(valore.toString()));
			}
		}	
		context.write(key,result);       
	}
}
