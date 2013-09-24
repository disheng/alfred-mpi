package it.uniroma3.dia.alfred.mpi.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class XPathHolder {
	@JsonProperty("xpaths")
	private Map<String, Set<String>> xPaths;
	
	public XPathHolder() {}
	
	@JsonIgnore
	public List<String> getAttributesList() {
		lazyInit();
		
		Set<String> pathList = this.xPaths.keySet();
		if (pathList == null) {
			return null;
		}
		
		return Lists.newArrayList(pathList);
	}
	
	@JsonIgnore
	public List<String> getXpathsFromAttribute(String key) {
		lazyInit();
		
		Set<String> pathList = this.xPaths.get(key);
		if (pathList == null) {
			return null;
		}
		
		return Lists.newArrayList(pathList);
	}
	
	@JsonIgnore
	public void addXpathToAttr(String key, String xpath) {
		lazyInit();
		
		Set<String> pathList = this.xPaths.get(key);
		if (pathList == null) {
			pathList = Sets.newHashSet();
		}
		pathList.add(xpath);
		this.xPaths.put(key, pathList);
	}
	
	@JsonIgnore
	private synchronized void lazyInit() {
		if(this.xPaths == null) {
			this.xPaths = Maps.newHashMap();
		}
	}

	@Override
	@JsonIgnore
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((xPaths == null) ? 0 : xPaths.hashCode());
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
		XPathHolder other = (XPathHolder) obj;
		if (xPaths == null) {
			if (other.xPaths != null)
				return false;
		} else if (!xPaths.equals(other.xPaths))
			return false;
		return true;
	}
}
