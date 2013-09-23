package it.uniroma3.dia.alfred.mpi.model.constants;

public class DomainHolderKeys {
	
	public final static String DOMAIN_ID_KEY = "domain_name";
	public final static String FIRST_PAGE_KEY = "first_page";
	public final static String BUCKET_S3_KEY = "bucket_S3";
	public final static String SITE_KEY = "site";
	
	public enum KnownSites {
		ALLMUSIC("allmusic"),
		IMDB("imdb");
		
		private String key;
		KnownSites(String k) {
			this.setKey(k);
		}
		public String getKey() {
			return key;
		}
		private void setKey(String key) {
			this.key = key;
		}
	}

	public enum KnownDomains {
		FILM("movies"),
		ACTORS("actors"),
		ALBUMS("album"),
		ARTIST("artists");
		
		private String key;
		KnownDomains(String k) {
			this.setKey(k);
		}
		public String getKey() {
			return key;
		}
		private void setKey(String key) {
			this.key = key;
		}
	}
}
