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
	@JsonProperty("domain")
	private DomainHolder associatedDomain;
	
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

	@JsonIgnore
	public DomainHolder getAssociatedDomain() {
		return associatedDomain;
	}
	
	@JsonIgnore
	public void setAssociatedDomain(DomainHolder associatedDomain) {
		this.associatedDomain = associatedDomain;
	}
	
	@Override
	@JsonIgnore
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((associatedDomain == null) ? 0 : associatedDomain.hashCode());
		result = prime
				* result
				+ ((configurationMap == null) ? 0 : configurationMap.hashCode());
		result = prime * result + ((uid == null) ? 0 : uid.hashCode());
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
		ConfigHolder other = (ConfigHolder) obj;
		if (associatedDomain == null) {
			if (other.associatedDomain != null)
				return false;
		} else if (!associatedDomain.equals(other.associatedDomain))
			return false;
		if (configurationMap == null) {
			if (other.configurationMap != null)
				return false;
		} else if (!configurationMap.equals(other.configurationMap))
			return false;
		if (uid == null) {
			if (other.uid != null)
				return false;
		} else if (!uid.equals(other.uid))
			return false;
		return true;
	}
}
