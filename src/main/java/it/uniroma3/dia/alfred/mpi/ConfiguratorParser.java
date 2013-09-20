package it.uniroma3.dia.alfred.mpi;

import it.uniroma3.dia.alfred.ConfigHolderIdBroker;
import it.uniroma3.dia.alfred.DomainHolder;
import it.uniroma3.dia.alfred.mpi.model.ConfigHolder;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ConfiguratorParser {
	
	// File that contains the all the configurations
	private final static String MAIN_CONF_FILE = "configurations.properties";
	private final static String DOMAIN_KEY_NAME = "domains"; 
	
	// File that contains all the description about domains
	private final static String MAIN_DOMAIN_FILE = "domains.properties";
	
	// Delimiter used in the properties file
	private final static String DELIMITER = "=";
	private final static String SECOND_DELIMITER = ",";
	
	/**
	 * This map will have for each domain (represented by a String)
	 * a class {@link DomainHolder} containing all the parameters
	 */
	private static Map<String, DomainHolder> name2domain;
	
	/**
	 * Get a long config file and split it in many ConfigHolder
	 * @return
	 */
	public static List<ConfigHolder> readConfig() {
		return readConfig(null, null);
	}
	
	public static List<ConfigHolder> readConfig(String filePathConfigurations, String filePathDomains) {
	
		readDomains(filePathDomains);
		
		BufferedReader reader = null;
		
		if (filePathConfigurations == null){
			reader = new BufferedReader(new InputStreamReader(ConfiguratorParser.class.getResourceAsStream(MAIN_CONF_FILE)));
		} else {
			try {
				reader = new BufferedReader(new FileReader(filePathConfigurations));
			} catch (FileNotFoundException e) {
				System.err.println("Error in opening " + filePathConfigurations);
				e.printStackTrace();
			}
		}
		
		LinkedList<ConfigHolder> configList = new LinkedList<ConfigHolder>();
		
		String currentLine = null;
		ConfigHolder currentConfigHolder = null;
		Map<String, String> key2value = new HashMap<String, String>();
		String key = null;	
		String value = null;
		
		try {
			while( (currentLine = reader.readLine()) != null) {
				
				// if the line is empty
				if (currentLine.matches("\\s*")) {
					continue;
				}
				
				// if contains a new definition
				if (currentLine.matches("\\[.*\\]")) {
					ConfigHolderIdBroker.setName(currentLine.substring(1, currentLine.length()-1));
					continue;
				}
				
				key = currentLine.split(DELIMITER)[0];
				value = currentLine.split(DELIMITER)[1];
				
				if (key.equals(DOMAIN_KEY_NAME)) {
					for (String domainName : value.split(SECOND_DELIMITER)){
						currentConfigHolder = new ConfigHolder();
						currentConfigHolder.setConfigurationValue("id", ConfigHolderIdBroker.getId(domainName));
						currentConfigHolder.getConfigurationMap().putAll(key2value);
						currentConfigHolder.getConfigurationMap().putAll(name2domain.get(domainName).getConfigurationMap());
						configList.add(currentConfigHolder);
					}
					key2value.clear();
					continue;
				}
				
				key2value.put(key, value);
				
			}
		} catch (IOException e) {
			System.err.println("Error in reading configuratios file");
			e.printStackTrace();
		}
		
		// ConfiguratorParser.class.getResourceAsStream(MAIN_CONF_FILE).close();
		
		if (filePathConfigurations != null) {
			try {
				reader.close();
			} catch (IOException e) {
				System.err.println("Error in closing properties file");
				e.printStackTrace();
			}			
		}
		
		return configList;
		
	}
	
	private static void readDomains(String filePathDomains){
		
		name2domain = new HashMap<String, DomainHolder>();
		
		BufferedReader reader = null;
		
		// open the file
		if (filePathDomains == null){
			reader = new BufferedReader(new InputStreamReader(ConfiguratorParser.class.getResourceAsStream(MAIN_CONF_FILE)));
		} else {
			try {
				reader = new BufferedReader(new FileReader(filePathDomains));
			} catch (FileNotFoundException e) {
				System.err.println("Error in opening " + filePathDomains);
				e.printStackTrace();
				return;
			}
		}
		
		String currentLine = null;
		DomainHolder currentDomainHolder = null;
		
		try {
			while( (currentLine = reader.readLine()) != null) {
				
				// if the line is empty
				if (currentLine.matches("\\s*")) {
					continue;
				}
				
				// if contains a new definition
				if (currentLine.matches("\\[.*\\]")) {
					currentDomainHolder = new DomainHolder();
					name2domain.put(currentLine.substring(1, currentLine.length()-1), currentDomainHolder);
					currentDomainHolder.setConfigurationValue("domain_name", currentLine.substring(1, currentLine.length()-1));
					continue;
				}
				
				currentDomainHolder.setConfigurationValue(currentLine.split(DELIMITER)[0], currentLine.split(DELIMITER)[1]);
				
			}
		} catch (IOException e) {
			System.err.println("Error in reading domains file");
			e.printStackTrace();
		}
		
		if (filePathDomains != null) {
			try {
				reader.close();
			} catch (IOException e) {
				System.err.println("Error in closing domains file");
				e.printStackTrace();
			}
		}
		
	}
	
}
