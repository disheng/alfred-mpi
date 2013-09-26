package it.uniroma3.dia.alfred.mapred.model;

import java.io.IOException;
import java.util.Map;

import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.google.common.collect.Maps;

public class MapWritableConverter {
	private MapWritableConverter() {}
	private static ObjectMapper jacksonMapper = new ObjectMapper();	
	
	public static String toJsonText(MapWritable input) {
		String returnRef = null;
		
		try {
			returnRef = jacksonMapper.writeValueAsString(convertToMap(input));
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return returnRef;
	}

	private static Map<String, Map<String,String>> convertToMap(MapWritable inputMap) {
		Map<String, Map<String,String>> mapResult = Maps.newHashMap();
		
		for (Writable attributeText : inputMap.keySet()) {
			MapWritable partialInsideMap = (MapWritable) inputMap.get(attributeText);
			Map<String,String> partialOutputMap = Maps.newHashMap();
			
			for (Writable rule : partialInsideMap.keySet()) {
				Text regola = (Text) rule;
				Text valore = (Text) partialInsideMap.get(rule);
				
				partialOutputMap.put(regola.toString(), valore.toString());
			}
			
			mapResult.put(((Text)attributeText).toString(), partialOutputMap);
		}
		
		return mapResult;
	}
}
