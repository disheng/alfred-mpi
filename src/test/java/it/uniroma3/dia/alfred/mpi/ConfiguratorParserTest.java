package it.uniroma3.dia.alfred.mpi;

import static org.junit.Assert.*;
import it.uniroma3.dia.alfred.mpi.model.ConfigHolder;

import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ConfiguratorParserTest {

	private static List<ConfigHolder> configHoldersList;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		configHoldersList = null;
	}

	@Test
	public void testReadConfigStringString() {
		
	}
	
	@Test
	public void testReadConfigNullNull() {
		test(null, null);
	}
	
	public void test(String filePathConfigurations, String filePathDomains){
		configHoldersList = ConfiguratorParser.readConfig();
		assertEquals(7, configHoldersList.size());
		assertEquals(expected, actual);
	}

}
