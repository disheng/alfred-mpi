package it.uniroma3.dia.alfred.mpi;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.BeforeClass;
import org.junit.Test;

public class OutputParserTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		OutputParser.parse(
				ConfiguratorParser.readConfig("examples/configuration.properties", "examples/configuration.properties"),
				"examples/testOutputParser");
	}

	@Test
	public void testParse() {
		assertTrue(new File("examples/testOutputParser").exists());
		assertTrue(new File("examples/testOutputParser").isDirectory());
		assertNotNull(new File("examples/testOutputParser").listFiles());
		fail("Not yet implemented");
	}

}
