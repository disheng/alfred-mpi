package it.uniroma3.dia.alfred.mpi.model.serializer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import it.uniroma3.dia.alfred.mpi.model.XPathHolder;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

public class XPathHolderSerializableTest {
	private static final int MAX_VALUES = 100;
	private Random randomGen;
	private XPathHolder xPathData;

	@Before
	public void setUp() throws Exception {
		randomGen = new Random(System.nanoTime());
		xPathData = new XPathHolder();
		
		List<String> attributes;
		List<String> xpaths;
		
		attributes = randomStringList(randomGen.nextInt(MAX_VALUES));
		for(String currAttr: attributes) {
			xpaths = randomStringList(randomGen.nextInt(MAX_VALUES));
			for(String currXpath: xpaths) {
				xPathData.addXpathToAttr(currAttr, currXpath);
			}
		}
	}
	
	@Test
	public void checkIfSerializedIsNotNull() {
		assertNotNull(XPathHolderSerializable.toJson(xPathData));
	}
	
	@Test
	public void checkIfSerializedIsNotEmpty() {
		assertTrue(XPathHolderSerializable.toJson(xPathData).length() > 0);
	}
	
	@Test
	public void checkIfDeserializedIsNotNull() {
		assertNotNull( XPathHolderSerializable.fromJson(XPathHolderSerializable.toJson(xPathData)) );
	}
	
	@Test
	public void checkIfDeserializedIsSame() {
		assertEquals(xPathData, XPathHolderSerializable.fromJson(XPathHolderSerializable.toJson(xPathData)) );
	}

	private static List<String> randomStringList(int qty) {
		List<String> lstOutput = Lists.newArrayList();
		
		for (int i = 0; i < qty; ++i) {
			lstOutput.add(UUID.randomUUID().toString());
		}
			
		return lstOutput;
	}
}
