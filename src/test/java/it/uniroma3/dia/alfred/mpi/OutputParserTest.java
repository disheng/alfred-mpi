package it.uniroma3.dia.alfred.mpi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

public class OutputParserTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		setup();
		OutputParser.parse(
				ConfiguratorParser.readConfig("examples/configuration.properties", "examples/domains.properties"),
				"examples/output_mpi/",
				"examples/testOutputParser"
			);
	}
	
	private static void setup() {
		if (new File("examples/testOutputParser/attori").exists()){
			new File("examples/testOutputParser/attori").delete();
		}
		if (new File("examples/testOutputParser/movies").exists()){
			new File("examples/testOutputParser/movies").delete();
		}
	}

	@Test
	public void testParse() {
		assertTrue(new File("examples/testOutputParser").exists());
		assertTrue(new File("examples/testOutputParser").isDirectory());
		assertNotNull(new File("examples/testOutputParser").listFiles());
		assertTrue(new File("examples/testOutputParser/attori").exists());
		assertTrue(new File("examples/testOutputParser/movies").exists());
		
		BufferedReader reader = null;
		
		try {
			reader = new BufferedReader(new FileReader("examples/testOutputParser/attori"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			fail();
		}
		
		String line = null;
		
		try {
			line = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
		
		String expected = "{\"xpaths\":{\"Name\":[\"//*[@itemprop='name']/text()\",\"/HTML/BODY[1]/DIV[3]/DIV[1]/DIV[4]/DIV[3]/DIV[2]/DIV[1]/TABLE[1]/TBODY[1]/TR[1]/TD[2]/DIV[1]/DIV[1]/H1[1]/text()[1]\"],\"AltName\":[\"//*[@itemprop='name']/text()\",\"//H1[@class='header'][1]/text()[1]\"]}}";
		
		assertNotNull(line);
		assertEquals(expected, line);
		
		try {
			line = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
		
		assertNull(line);
		
		try {
			reader = new BufferedReader(new FileReader("examples/testOutputParser/movies"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			fail();
		}
		
		try {
			line = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
		
		expected = "{\"xpaths\":{\"Rating\":[\"//*[@itemprop='ratingValue']/text()\",\"/HTML/BODY[1]/DIV[3]/DIV[1]/DIV[6]/DIV[3]/DIV[2]/DIV[1]/DIV[1]/TABLE[1]/TBODY[1]/TR[1]/TD[2]/DIV[3]/DIV[1]/text()[1]\"],\"Title\":[\"/HTML/BODY[1]/DIV[3]/DIV[1]/DIV[6]/DIV[3]/DIV[2]/DIV[1]/DIV[1]/TABLE[1]/TBODY[1]/TR[1]/TD[2]/H1[1]/text()[1]\",\"//*[@itemprop='name']/text()\"]}}";
		
		assertNotNull(line);
		assertEquals(expected, line);
		
		try {
			line = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
		
		assertNull(line);
	}

}
