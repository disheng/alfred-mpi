package it.uniroma3.dia.alfred.mpi.model;

import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class DomainHolder {
	@JsonProperty("configuration")
	private Map<String,String> configurationMap;
	@JsonProperty("goldenxpath")
	private Map<String,String> goldenXPathMap;
	
	public DomainHolder() {}

	@JsonIgnore
	public String getConfigurationValue(String key) {
		lazyInitConfiguration();
		
		return this.configurationMap.get(key);
	}
	
	@JsonIgnore
	public String setConfigurationValue(String key, String value) {
		lazyInitConfiguration();
		
		return this.configurationMap.put(key, value);
	}
	
	@JsonIgnore
	public String getGoldenXPath(String key) {
		lazyInitGolden();
		
		return this.goldenXPathMap.get(key);
	}
	
	@JsonIgnore
	public String setGoldenXPath(String key, String value) {
		lazyInitGolden();
		
		return this.goldenXPathMap.put(key, value);
	}
	
	@JsonIgnore
	public List<String> getXPathNames() {
		lazyInitGolden();
		
		return Lists.newLinkedList(this.goldenXPathMap.keySet());
	}
	
	private void lazyInitConfiguration() {
		if (this.configurationMap == null) {
			this.configurationMap = Maps.newHashMap();
		}
	}
	
	private void lazyInitGolden() {
		if (this.goldenXPathMap == null) {
			this.goldenXPathMap = Maps.newHashMap();
		}
	}

	@Override
	@JsonIgnore
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((configurationMap == null) ? 0 : configurationMap.hashCode());
		result = prime * result
				+ ((goldenXPathMap == null) ? 0 : goldenXPathMap.hashCode());
		return result;
	}

	@Override
	@JsonIgnore
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DomainHolder other = (DomainHolder) obj;
		if (configurationMap == null) {
			if (other.configurationMap != null)
				return false;
		} else if (!configurationMap.equals(other.configurationMap))
			return false;
		if (goldenXPathMap == null) {
			if (other.goldenXPathMap != null)
				return false;
		} else if (!goldenXPathMap.equals(other.goldenXPathMap))
			return false;
		return true;
	}
}
