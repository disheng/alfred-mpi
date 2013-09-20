package it.uniroma3.dia.alfred.mpi.model;

import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import com.google.common.collect.Maps;

public class ConfigHolder {
	@JsonProperty("id")
	private String uid;
	@JsonProperty("configuration")
	private Map<String,String> configurationMap;
	
	public ConfigHolder() {}
	public ConfigHolder(String id) { this.uid = id; }
	
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
	public String getUid() {
		return uid;
	}

	@JsonIgnore
	public void setUid(String uid) {
		this.uid = uid;
	}
}
