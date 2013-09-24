package it.uniroma3.dia.alfred.mpi.model.serializer;

import it.uniroma3.dia.alfred.mpi.model.XPathHolder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Scanner;

import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class XPathHolderSerializable {
	private XPathHolderSerializable() {}
	private static ObjectMapper jacksonMapper = new ObjectMapper();
	
	public static String toJson(XPathHolder xpathReference) {
		String returnRef = null;
		
		try {
			returnRef = jacksonMapper.writeValueAsString(xpathReference);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return returnRef;
	}
	
	public static void toJsonFile(XPathHolder input, String filePath) {
		try {
			jacksonMapper.writeValue(new File(filePath), input);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static XPathHolder fromJsonFile(String filePath) {
		String jsonString = null;
		
		try {
			jsonString = new Scanner(new File(filePath)).useDelimiter("\\Z").next();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return XPathHolderSerializable.fromJson(jsonString);
	}
	
	public static XPathHolder fromJsonStream(InputStream isData) {
		StringWriter writer = new StringWriter();
		String jsonString = null;
		
		try {
			IOUtils.copy(isData, writer);
			jsonString = writer.toString();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return XPathHolderSerializable.fromJson(jsonString);
	}	
	
	public static XPathHolder fromJson(String jsonString) {
		XPathHolder returnRef = null;
		
		try {
			returnRef = jacksonMapper.readValue(jsonString, XPathHolder.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return returnRef; 
	}
}
