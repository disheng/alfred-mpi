package it.uniroma3.dia.alfred.mpi.runner.s3;

import it.uniroma3.dia.alfred.mpi.model.DomainHolder;
import it.uniroma3.dia.alfred.mpi.model.constants.DomainHolderKeys;
import it.uniroma3.dia.datasource.IncrementalIdRetriever;
import it.uniroma3.dia.datasource.s3.S3Uploader;

import java.util.Collections;
import java.util.List;

import model.Page;

import com.google.common.collect.Lists;

public class GenerateLazyPagesFromDomain {
	private GenerateLazyPagesFromDomain() {}
	
	public static Page getGoldenPage(DomainHolder domainConf) {
		String firstPageKey = domainConf.getConfigurationValue(DomainHolderKeys.FIRST_PAGE_KEY);
		String bucketValue = domainConf.getConfigurationValue(DomainHolderKeys.BUCKET_S3_KEY);
		
		List<Page> firstPage = convertUriToPage(Lists.newArrayList(firstPageKey), bucketValue);
		return firstPage.get(0);
	}
	
	public static List<Page> getPages(DomainHolder domainConf) {
		return getPages(domainConf, Integer.MIN_VALUE);
	}

	public static List<Page> getPages(DomainHolder domainConf, int size) {
		List<String> uriToRetrieve = Lists.newArrayList();
		
		IncrementalIdRetriever iidFake = new IncrementalIdRetriever();
		String siteValue = domainConf.getConfigurationValue(DomainHolderKeys.SITE_KEY);
		String domainValue = domainConf.getConfigurationValue(DomainHolderKeys.DOMAIN_ID_KEY);
		String bucketValue = domainConf.getConfigurationValue(DomainHolderKeys.BUCKET_S3_KEY);
		
		if ( siteValue.equalsIgnoreCase(DomainHolderKeys.KnownSites.IMDB.getKey()) && 
				( domainValue.equalsIgnoreCase(DomainHolderKeys.KnownDomains.FILM.getKey()) || 
						domainValue.equalsIgnoreCase(DomainHolderKeys.KnownDomains.ACTORS.getKey()) )) {
			// IMDB
			if ( domainValue.equalsIgnoreCase(DomainHolderKeys.KnownDomains.FILM.getKey()) ) {
				uriToRetrieve.addAll( iidFake.getAllMoviesURL() );
			} else {
				uriToRetrieve.addAll( iidFake.getAllActorsURL() );
			}
		} else if ( siteValue.equalsIgnoreCase(DomainHolderKeys.KnownSites.ALLMUSIC.getKey()) && 
				( domainValue.equalsIgnoreCase(DomainHolderKeys.KnownDomains.ALBUMS.getKey()) || 
						domainValue.equalsIgnoreCase(DomainHolderKeys.KnownDomains.ARTIST.getKey()) )) {
			// AllMusic
			if ( domainValue.equalsIgnoreCase(DomainHolderKeys.KnownDomains.ALBUMS.getKey()) ) {
				uriToRetrieve.addAll( iidFake.getAllAlbumsURL() );
			} else {
				uriToRetrieve.addAll( iidFake.getAllArtistsURL() );
			}
		} else {
			System.out.println("Fetching list from S3 bucket " + bucketValue);
			List<String> tempUris = null;
			try {
				tempUris = S3Uploader.getInstance().getObjectUrls(bucketValue);
			} catch(Exception e) {
				System.out.println("ERROR fetching list from S3 bucket " + bucketValue);
				tempUris = null;
			}
			uriToRetrieve.addAll(tempUris);
		}
		
		// System.out.println(uriToRetrieve);
		Collections.shuffle(uriToRetrieve);
		if (size != Integer.MIN_VALUE) {
			if (size <= uriToRetrieve.size() ) {
				uriToRetrieve = uriToRetrieve.subList(0, size);
			}
		}

		return convertUriToPage(uriToRetrieve, domainConf.getConfigurationValue(DomainHolderKeys.BUCKET_S3_KEY));
	}
	
	private static List<Page> convertUriToPage(List<String> uriList, String bucketName) {
		List<Page> pagesReturn = Lists.newLinkedList();
		
		for(String uri: uriList) {
			pagesReturn.add(new LazyPageProxyS3(uri, bucketName));
		}
		
		return pagesReturn;
	}
}
