package it.uniroma3.dia.alfred.mpi.xpath;

import java.io.ByteArrayInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class XPathHandlerJavax {
	
	private static XPathHandlerJavax instance;	
	private static DocumentBuilderFactory documentFactory;
	private static XPathFactory xPathfactory;
	
	public static XPathHandlerJavax getInstance(){
		if (instance==null){
			instance = new XPathHandlerJavax();
			inizializeDocumentFactory();
			inizializeXPathFactory();		
		}
		return instance;		
	}
	
	
	/**
	 * 
	 * Metodo per creare una rappresentazione di un documento da interrogare
	 * con una query XPath
	 * 
	 * @param html - Una stringa che identifica una pagina html
	 * @return Un oggetto documento che rappresenta la pagina
	 */
	public Document buildDocumentFromString(String html){
		Document doc = null;
						
		try {			
	        DocumentBuilder builder = documentFactory.newDocumentBuilder();		
	        doc = builder.parse(new ByteArrayInputStream(html.getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return doc;       
	}
	
	/**
	 * 
	 * Metodo che esegue una query su un documento
	 * 
	 * @param doc - Rappresentazione di un documento
	 * @param xPathQuery - Una Query XPath
	 * @return NodeList del risultato
	 */
	public NodeList executeQuery(Document doc, String xPathQuery){
				
		XPath xpath = xPathfactory.newXPath();
        XPathExpression expr;
        NodeList nodes = null;
        
		try {
			expr = xpath.compile(xPathQuery);
			Object result = expr.evaluate(doc, XPathConstants.NODESET);
			nodes = (NodeList) result;
		} catch (XPathExpressionException e) {
			e.printStackTrace();        
		}        
        
        return nodes;
	}
	
	/**
	 * 
	 * Metodo che esegue una query su un documento stampando il risultato su output
	 * 
	 * @param doc - Rappresentazione di un documento
	 * @param xPathQuery - Una Query XPath
	 */
	public void print_executeQuery(Document doc, String xPathQuery){
		
		XPath xpath = xPathfactory.newXPath();
        XPathExpression expr;
        NodeList nodes = null;
        
		try {
			expr = xpath.compile(xPathQuery);
			Object result = expr.evaluate(doc, XPathConstants.NODESET);
			nodes = (NodeList) result;
		} catch (XPathExpressionException e) {
			e.printStackTrace();        
		}  
		
		if(nodes != null){
	        for(int i = 0; i < nodes.getLength(); ++i) {
		        System.out.println(nodes.item(i).toString());
	        }
		}
		
	}
	
	private static void inizializeDocumentFactory(){		
		if(documentFactory == null)
			documentFactory = DocumentBuilderFactory.newInstance();		
	}
	
	private static void inizializeXPathFactory() {
		if (xPathfactory == null){
			xPathfactory = XPathFactory.newInstance();
		}		
	}
}
