package it.uniroma3.dia.alfredmpi;

import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import com.google.common.collect.Maps;

public class DomainHolder {

	@JsonProperty("configuration")
	private Map<String,String> configurationMap;
	
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

	public Map<String, String> getConfigurationMap() {
		return configurationMap;
	}
	
}
