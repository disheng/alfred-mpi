package it.uniroma3.dia.alfred.mpi.runner.s3;

import it.uniroma3.dia.datasource.s3.S3Uploader;

import java.io.StringReader;

import org.w3c.dom.Document;

import experiment.LazyPageProxy;

public class LazyPageProxyS3Cache extends LazyPageProxy {
	private String bucketName;
	private String content;
	private boolean contentLoaded;
	
	public LazyPageProxyS3Cache(String path, String buckName) {
		super(null, path);
		
		this.bucketName = buckName;
		this.contentLoaded = false;
		this.content = null;
	}

	protected Document getDocument(String path) {
		return getDocument(new StringReader(this.getContent()));
	}
	
	public String getContent(){
		if (this.contentLoaded) {
			return this.content;
		}
		
		this.content = S3Uploader.getInstance().getPageFromBucket(this.bucketName, this.path).getContent();
		if (this.content == null) {
			this.contentLoaded = true;
		}
		
		return this.content;
	}
}