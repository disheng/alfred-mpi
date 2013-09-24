package it.uniroma3.dia.alfred.mpi.model.serializer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import it.uniroma3.dia.alfred.mpi.model.ConfigHolder;
import it.uniroma3.dia.alfred.mpi.model.DomainHolder;

import java.util.Random;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

public class ConfigHolderSerializableTest {
	private static final int MAX_VALUES = 100;
	private ConfigHolder cfgHolder;

	@Before
	public void setUp() throws Exception {
		cfgHolder = new ConfigHolder();
		
		fillConfigHolder(cfgHolder);
	}
	
	private static void fillConfigHolder(ConfigHolder cfgHolder) {
		Random randomGen = new Random(System.nanoTime());
		
		cfgHolder.setUid(UUID.randomUUID().toString());
		for(int i = 0; i < randomGen.nextInt(MAX_VALUES); ++i) {
			cfgHolder.setConfigurationValue(UUID.randomUUID().toString(), UUID.randomUUID().toString());
		}
		
		DomainHolder dhStuff = new DomainHolder();
		for(int i = 0; i < randomGen.nextInt(MAX_VALUES); ++i) {
			dhStuff.setConfigurationValue(UUID.randomUUID().toString(), UUID.randomUUID().toString());
		}
		for(int i = 0; i < randomGen.nextInt(MAX_VALUES); ++i) {
			dhStuff.setGoldenXPathMap(UUID.randomUUID().toString(), UUID.randomUUID().toString());
		}
		
		cfgHolder.setAssociatedDomain(dhStuff);
	}

	@Test
	public void checkIfSerializedIsNotNull() {
		assertNotNull(ConfigHolderSerializable.toJson(cfgHolder));
	}
	
	@Test
	public void checkIfSerializedIsNotEmpty() {
		assertTrue(ConfigHolderSerializable.toJson(cfgHolder).length() > 0);
	}
	
	@Test
	public void checkIfDeserializedIsNotNull() {
		assertNotNull( ConfigHolderSerializable.fromJson(ConfigHolderSerializable.toJson(cfgHolder)) );
	}
	
	@Test
	public void checkIfDeserializedIsSame() {
		assertEquals(cfgHolder, ConfigHolderSerializable.fromJson(ConfigHolderSerializable.toJson(cfgHolder)) );
	}
}
