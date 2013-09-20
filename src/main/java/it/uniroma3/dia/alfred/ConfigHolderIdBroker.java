package it.uniroma3.dia.alfredmpi;

public class ConfigHolderIdBroker {

	private static String name;
	
	public static void setName(String name) {
		ConfigHolderIdBroker.name = name;
	}

	public static String getId(String domainName) {
		return name+"_"+domainName;
	}

}
