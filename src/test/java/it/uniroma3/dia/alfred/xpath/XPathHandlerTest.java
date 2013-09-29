package it.uniroma3.dia.alfred.xpath;

import static org.junit.Assert.assertEquals;
import model.Page;

import org.junit.Test;

public class XPathHandlerTest {
	public static final String PAGE_TITLE = "Titolo di prova";
	public static final String PAGE_CONTENT = "<root><a>#BBB#</a><a>#CCC#</a><b><a>#DDD#</a></b></root>";
	
	@Test
	public void test_create_page_from_string() {
		Page page = new Page(PAGE_CONTENT);		
		assertEquals(PAGE_CONTENT, page.getContent());
		page.setTitle(PAGE_TITLE);
		assertEquals(PAGE_TITLE, page.getTitle());
	}
	
	@Test
	public void test_apply_xpath_to_page() {	
		Page page = new Page(PAGE_CONTENT);	
		page.setTitle(PAGE_TITLE);
		assertEquals(6, XPathHandler.executeQuery(page, "//*").getNumberOfNodesValues());
	}

}
