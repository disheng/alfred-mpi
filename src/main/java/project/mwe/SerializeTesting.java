package project.mwe;

import it.uniroma3.dia.alfred.mpi.model.XPathHolder;
import it.uniroma3.dia.alfred.mpi.model.serializer.XPathHolderSerializable;

public class SerializeTesting {
	private static final String JSON_FILE = "xpathHolderTest.json";
	public static void runExample() {
		XPathHolderSerializable.toJsonFile(generateXpathHolder(), JSON_FILE);
	}
	
	private static XPathHolder generateXpathHolder() {
		XPathHolder xpathTest = new XPathHolder();
		
		xpathTest.addXpathToAttr("Title", "//*[@itemprop='name']/text()");
		xpathTest.addXpathToAttr("Rating", "//*[@itemprop='ratingValue']/text()");
		xpathTest.addXpathToAttr("number of users Rating", "//*[@itemprop='ratingCount']/text()");
		xpathTest.addXpathToAttr("number of Reviews", "//*[@itemprop='director'][1]/text()[1]");
		xpathTest.addXpathToAttr("Director", "//*[@itemprop='director'][1]/text()[1]");
		xpathTest.addXpathToAttr("Country", "//*[contains(text(),'Country:')]/../A[1]/text()[1]");
		xpathTest.addXpathToAttr("Language", "//*[@itemprop='inLanguage'][1]/text()[1]");
		xpathTest.addXpathToAttr("Release date", "//*[@itemprop='datePublished'][1]/text()[1]");
		xpathTest.addXpathToAttr("Runtime", "//*[@itemprop='duration'][1]/text()[1]");
		xpathTest.addXpathToAttr("Writer", "//*[contains(text(),'Writer:') or contains(text(),'Writers:')]/../A[1]/text()[1]");
		
		return xpathTest;
	}
}
