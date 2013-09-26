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
	public void reduce(Text key, Iterable<MapWritable> listOfMaps, Context context) throws IOException, InterruptedException {

		for (MapWritable partialResultMap : listOfMaps) {
			for (Writable attributeText : partialResultMap.keySet()) {
				MapWritable partialInsideMap = (MapWritable) partialResultMap.get(attributeText);
				MapWritable partialOutputMap = new MapWritable();
				
				for (Writable rule : partialInsideMap.keySet()) {
					Text regola = (Text) rule;
					Text valore = (Text) partialInsideMap.get(rule);
					
					partialOutputMap.put(new Text(regola.toString()), new Text(valore.toString()));
				}
				
				result.put((Text)attributeText, partialOutputMap);
			}
		}
			
		context.write(key,result);       
	}
}
