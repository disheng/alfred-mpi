package it.uniroma3.dia.alfred.mpi.model.serializer;

import it.uniroma3.dia.alfred.mpi.model.ConfigHolder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class ConfigHolderSerializable {
	private ConfigHolderSerializable() {}

	private static ObjectMapper jacksonMapper = new ObjectMapper();
	
	public static String toJson(ConfigHolder configReference) {
		String returnRef = null;
		
		try {
			returnRef = jacksonMapper.writeValueAsString(configReference);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return returnRef;
	}
	
	public static void toJsonFile(ConfigHolder input, String filePath) {
		try {
			jacksonMapper.writeValue(new File(filePath), input);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static ConfigHolder fromJsonFile(String filePath) {
		String jsonString = null;
		
		try {
			jsonString = new Scanner(new File(filePath)).useDelimiter("\\Z").next();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ConfigHolderSerializable.fromJson(jsonString);
	}
	
	public static ConfigHolder fromJson(String jsonString) {
		ConfigHolder returnRef = null;
		
		try {
			returnRef = jacksonMapper.readValue(jsonString, ConfigHolder.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return returnRef; 
	}

}
