package it.uniroma3.dia.alfred.xpath;

import rules.xpath.XPathRule;
import model.ExtractedValue;
import model.Page;
import model.Rule;

public class XPathHandler {
	private XPathHandler() {}
	
	/**
	 * 
	 * Metodo che esegue una query su una pagina
	 * 
	 * @param page - Rappresentazione di una pagina
	 * @param xPathQuery - Una Rule query
	 * @return ExtractedValue - oggetto risultato della query
	 */
	public static ExtractedValue executeQuery(Page page, Rule xPathQuery) {
		return xPathQuery.applyOn(page);		
	}
	
	/**
	 * 
	 * Metodo che esegue una query su una pagina
	 * 
	 * @param page - Rappresentazione di una pagina
	 * @param xPathQuery - Una Query XPath
	 * @return ExtractedValue - oggetto risultato della query
	 */
	public static ExtractedValue executeQuery(Page page, String xPathQuery) {
		return executeQuery(page, new XPathRule(xPathQuery));		
	}
	
	/**
	 * 
	 * Metodo che esegue una query su una pagina e ritorna il risultato come stringa
	 * 
	 * @param page - Rappresentazione di una pagina
	 * @param xPathQuery - Una Rule query
	 * @return String - oggetto risultato della query
	 */
	public static String executeQueryAsText(Page page, Rule xPathQuery) {
		return executeQuery(page, xPathQuery).getTextContent();		
	}
	
	/**
	 * 
	 * Metodo che esegue una query su una pagina e ritorna il risultato come stringa
	 * 
	 * @param page - Rappresentazione di una pagina
	 * @param xPathQuery - Una Query XPath
	 * @return String - oggetto risultato della query
	 */
	public static String executeQueryAsText(Page page, String xPathQuery) {
		return executeQuery(page, xPathQuery).getTextContent();		
	}
	
	/**
	 * 
	 * Metodo che esegue una query su una pagina stampando il risultato su output
	 * 
	 * @param page - Rappresentazione di una pagina
	 * @param xPathQuery - Una Query XPath
	 */
	public static void print_executeQuery(Page page, String xPathQuery) {
		ExtractedValue value = executeQuery(page, xPathQuery);	
		System.out.println(value.toString());	
		System.out.println("Number of nodes values : "+value.getNumberOfNodesValues());
	}
}
