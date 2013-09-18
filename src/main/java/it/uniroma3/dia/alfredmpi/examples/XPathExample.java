package it.uniroma3.dia.alfredmpi.examples;

import it.uniroma3.dia.alfredmpi.XPathHandler;

import java.io.ByteArrayInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import net.sf.saxon.lib.NamespaceConstant;

import org.apache.hadoop.mapred.gethistory_jsp;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class XPathExample {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	    // System.setProperty(
	    // "javax.xml.xpath.XPathFactory", 
	    // "net.sf.saxon.xpath.XPathFactoryImpl");
		
		String xml="<root><a>#BBB#</a><a>#CCC#</a><b><a>#DDD#</a></b></root>";
		String xPathQuery = "//a[matches(.,'#...#')]";
		
		/* ESEMPIO SENZA HANDLER
	    try{	    	
	        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder builder = factory.newDocumentBuilder();
	        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes()));
	        
	        XPathFactory xPathfactory = XPathFactory.newInstance(NamespaceConstant.OBJECT_MODEL_SAXON);
	        XPath xpath = xPathfactory.newXPath();
	        XPathExpression expr = xpath.compile(xPathQuery);

	        Object result = expr.evaluate(doc, XPathConstants.NODESET);
	        NodeList nodes = (NodeList) result;
	        
	        for(int i = 0; i < nodes.getLength(); ++i) {
		        System.out.println(nodes.item(i).toString());
	        }
	    }
	    catch(Exception e){
	        e.printStackTrace();
	    }
	    */	    
	    
	    /* ESEMPIO CON L'HANDLER COSTRUITO */
	    XPathHandler handler = XPathHandler.getInstance();
		
		Document doc = handler.buildDocumentFromString(xml);
		handler.print_executeQuery(doc, xPathQuery);

	}

}
