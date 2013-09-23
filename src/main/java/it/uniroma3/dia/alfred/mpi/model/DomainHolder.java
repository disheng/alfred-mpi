package it.uniroma3.dia.alfred.mpi.model;

import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import com.google.common.collect.Maps;

public class DomainHolder {
	@JsonProperty("configuration")
	private Map<String,String> configurationMap;
	@JsonProperty("goldenxpath")
	private Map<String,String> goldenXPathMap;
	
	public DomainHolder() {}

	@JsonIgnore
	public String getConfigurationValue(String key) {
		if (this.configurationMap == null) {
			this.configurationMap = Maps.newHashMap();
		}
		
		return this.configurationMap.get(key);
	}
	
	@JsonIgnore
	public String setConfigurationValue(String key, String value) {
		if (this.configurationMap == null) {
			this.configurationMap = Maps.newHashMap();
		}
		
		return this.configurationMap.put(key, value);
	}

	@JsonIgnore
	public Map<String, String> getConfigurationMap() {
		return this.configurationMap;
	}
	
	@JsonIgnore
	public String getGoldenXPathMap(String key) {
		if (this.goldenXPathMap == null) {
			this.goldenXPathMap = Maps.newHashMap();
		}
		
		return this.goldenXPathMap.get(key);
	}
	
	@JsonIgnore
	public String setGoldenXPathMap(String key, String value) {
		if (this.goldenXPathMap == null) {
			this.goldenXPathMap = Maps.newHashMap();
		}
		
		return this.goldenXPathMap.put(key, value);
	}
	
	@JsonIgnore
	public Map<String, String> getGoldenXPathMap() {
		return this.goldenXPathMap;
	}
}
