package it.uniroma3.dia.alfred.mpi;

import static org.junit.Assert.*;
import it.uniroma3.dia.alfred.mpi.model.ConfigHolder;
import it.uniroma3.dia.alfred.mpi.model.constants.DomainHolderKeys;

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
		
		ConfigHolder currentConfigHolder = configHoldersList.get(0);
		assertEquals("conf1_movies",
				currentConfigHolder.getUid());
		assertEquals("imdb",
				currentConfigHolder.getAssociatedDomain().getGoldenXPathMap(DomainHolderKeys.SITE_KEY));
		assertEquals("nome_bucket_1",
				currentConfigHolder.getAssociatedDomain().getGoldenXPathMap(DomainHolderKeys.BUCKET_S3_KEY));
		assertEquals("movies",
				currentConfigHolder.getAssociatedDomain().getGoldenXPathMap(DomainHolderKeys.DOMAIN_ID_KEY));
		assertEquals("http://www.imdb.com/sections/dvd/?ref_=nb_mv_8_dvd",
				currentConfigHolder.getAssociatedDomain().getGoldenXPathMap(DomainHolderKeys.FIRST_PAGE_KEY));
		assertEquals("output1",
				currentConfigHolder.getConfigurationValue("output_folder"));
		assertEquals("1",
				currentConfigHolder.getConfigurationValue("baseline_random_iterations"));
		assertEquals("0.7",
				currentConfigHolder.getConfigurationValue("probability_threashold"));
		assertEquals("True",
				currentConfigHolder.getConfigurationValue("concurrent_experiments"));
		assertEquals("1000",
				currentConfigHolder.getConfigurationValue("testing_dimension"));
		assertEquals("ALF#ALFRED",
				currentConfigHolder.getConfigurationValue("algorithm"));
		assertEquals("//*[@itemprop='name']/text()",
				currentConfigHolder.getAssociatedDomain().getConfigurationValue("Title"));
		assertEquals("//*[@itemprop='reviewCount']/text()",
				currentConfigHolder.getAssociatedDomain().getConfigurationValue("number_of_Reviews"));
		
		currentConfigHolder = configHoldersList.get(1);
		assertEquals("conf1_attori",
				currentConfigHolder.getUid());
		assertEquals("imdb",
				currentConfigHolder.getAssociatedDomain().getGoldenXPathMap(DomainHolderKeys.SITE_KEY));
		assertEquals("nome_bucket_2",
				currentConfigHolder.getAssociatedDomain().getGoldenXPathMap(DomainHolderKeys.BUCKET_S3_KEY));
		assertEquals("attori",
				currentConfigHolder.getAssociatedDomain().getGoldenXPathMap(DomainHolderKeys.DOMAIN_ID_KEY));
		assertEquals("http://www.imdb.com/chart/top?ref_=nb_mv_3_chttp",
				currentConfigHolder.getAssociatedDomain().getGoldenXPathMap(DomainHolderKeys.FIRST_PAGE_KEY));
		assertEquals("output1",
				currentConfigHolder.getConfigurationValue("output_folder"));
		assertEquals("1",
				currentConfigHolder.getConfigurationValue("baseline_random_iterations"));
		assertEquals("0.7",
				currentConfigHolder.getConfigurationValue("probability_threashold"));
		assertEquals("True",
				currentConfigHolder.getConfigurationValue("concurrent_experiments"));
		assertEquals("1000",
				currentConfigHolder.getConfigurationValue("testing_dimension"));
		assertEquals("ALF#ALFRED",
				currentConfigHolder.getConfigurationValue("algorithm"));
		assertEquals("//*[@itemprop='name']/text()#NO",
				currentConfigHolder.getAssociatedDomain().getConfigurationValue("Name"));
		assertEquals("//*[@itemprop='birthDate']/A[1]/text()",
				currentConfigHolder.getAssociatedDomain().getConfigurationValue("Birth_date"));
		
		currentConfigHolder = configHoldersList.get(3);
		assertEquals("conf2_albums",
				currentConfigHolder.getUid());
		assertEquals("allmusic",
				currentConfigHolder.getAssociatedDomain().getGoldenXPathMap(DomainHolderKeys.SITE_KEY));
		assertEquals("nome_bucket_3",
				currentConfigHolder.getAssociatedDomain().getGoldenXPathMap(DomainHolderKeys.BUCKET_S3_KEY));
		assertEquals("albums",
				currentConfigHolder.getAssociatedDomain().getGoldenXPathMap(DomainHolderKeys.DOMAIN_ID_KEY));
		assertEquals("http://www.allmusic.com/album/mw000236364",
				currentConfigHolder.getAssociatedDomain().getGoldenXPathMap(DomainHolderKeys.FIRST_PAGE_KEY));
		assertEquals("output2",
				currentConfigHolder.getConfigurationValue("output_folder"));
		assertEquals("Random#Greedy#Lucky",
				currentConfigHolder.getConfigurationValue("algorithms_chooser"));
		assertEquals("1000",
				currentConfigHolder.getConfigurationValue("max_mq_number"));
		assertEquals("1230.4",
				currentConfigHolder.getConfigurationValue("lucky_threashold"));
		assertEquals("81000",
				currentConfigHolder.getConfigurationValue("testing_dimension"));
		assertEquals("ALFRED",
				currentConfigHolder.getConfigurationValue("algorithm"));
		assertEquals("//*[@class='release-date']/text()",
				currentConfigHolder.getAssociatedDomain().getConfigurationValue("Release date"));
		assertEquals("//*[@class='genres']/UL/LI[1]/A/text()",
				currentConfigHolder.getAssociatedDomain().getConfigurationValue("Genres"));
		
		currentConfigHolder = configHoldersList.get(6);
		assertEquals("conf3_movies",
				currentConfigHolder.getUid());
		assertEquals("imdb",
				currentConfigHolder.getAssociatedDomain().getGoldenXPathMap(DomainHolderKeys.SITE_KEY));
		assertEquals("nome_bucket_1",
				currentConfigHolder.getAssociatedDomain().getGoldenXPathMap(DomainHolderKeys.BUCKET_S3_KEY));
		assertEquals("movies",
				currentConfigHolder.getAssociatedDomain().getGoldenXPathMap(DomainHolderKeys.DOMAIN_ID_KEY));
		assertEquals("http://www.imdb.com/sections/dvd/?ref_=nb_mv_8_dvd",
				currentConfigHolder.getAssociatedDomain().getGoldenXPathMap(DomainHolderKeys.FIRST_PAGE_KEY));
		assertEquals("output3",
				currentConfigHolder.getConfigurationValue("output_folder"));
		assertEquals("Simple#Srm",
				currentConfigHolder.getConfigurationValue("algorithms_core"));
		assertEquals("100",
				currentConfigHolder.getConfigurationValue("max_mq_number"));
		assertEquals("0.3",
				currentConfigHolder.getConfigurationValue("lucky_threashold"));
		assertEquals("2000",
				currentConfigHolder.getConfigurationValue("testing_dimension"));
		assertEquals("ALF#ALFRED",
				currentConfigHolder.getConfigurationValue("algorithm"));
		assertEquals("//*[@itemprop='ratingValue']/text()",
				currentConfigHolder.getAssociatedDomain().getConfigurationValue("Rating"));
		assertEquals("//*[@itemprop='reviewCount']/text()",
				currentConfigHolder.getAssociatedDomain().getConfigurationValue("number_of_Reviews"));
		
	}

}
