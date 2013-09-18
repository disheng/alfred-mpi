package it.uniroma3.dia.alfredmpi.xpath;

import rules.xpath.XPathRule;
import model.ExtractedValue;
import model.Page;

public class XPathHandler {
	
	private static XPathHandler instance;	
	
	public static XPathHandler getInstance(){
		if (instance==null){
			instance = new XPathHandler();		
		}
		return instance;		
	}	
	
	/**
	 * 
	 * Metodo che esegue una query su una pagina
	 * 
	 * @param page - Rappresentazione di una pagina
	 * @param xPathQuery - Una Query XPath
	 * @return ExtractedValue - oggetto risultato della query
	 */
	public ExtractedValue executeQuery(Page page, String xPathQuery){		
		XPathRule rule = new XPathRule(xPathQuery);
		return rule.applyOn(page);		
	}
	
	/**
	 * 
	 * Metodo che esegue una query su una pagina stampando il risultato su output
	 * 
	 * @param page - Rappresentazione di una pagina
	 * @param xPathQuery - Una Query XPath
	 */
	public void print_executeQuery(Page page, String xPathQuery){		
		XPathRule rule = new XPathRule(xPathQuery);
		ExtractedValue value = rule.applyOn(page);	
		System.out.println(value.toString());	
		System.out.println("Number of nodes values : "+value.getNumberOfNodesValues());
	}	
	
	/**
	 * 
	 * Costruisce un oggetto Page, rappresentazione di una pagina,
	 * a partire dalla sua rappresentazione in stringa.
	 * 
	 * @param content - contenuto della pagina
	 * @return Page - oggetto che rappresenta la pagina 
	 */
	public Page createPage(String content){
		Page page = new Page(content);				
		return page;
	}
}
