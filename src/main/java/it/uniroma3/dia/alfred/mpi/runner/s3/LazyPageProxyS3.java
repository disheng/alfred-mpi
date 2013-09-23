package it.uniroma3.dia.alfred.mpi.runner.s3;

import it.uniroma3.dia.datasource.s3.S3Uploader;

import java.io.StringReader;

import org.w3c.dom.Document;

import experiment.LazyPageProxy;

public class LazyPageProxyS3 extends LazyPageProxy {
	private String bucketName;
	private String content;
	private boolean contentLoaded;
	
	public LazyPageProxyS3(String path, String buckName) {
		super(null, path);
		
		this.bucketName = buckName;
		this.contentLoaded = false;
		this.content = null;
	}

	protected Document getDocument(String path) {
		return getDocument(new StringReader(this.getContent()));
	}
	
	public synchronized String getContent(){
		if (this.contentLoaded) {
			return this.content;
		}
		
		this.content = S3Uploader.getInstance().getPageFromBucket(this.bucketName, this.path).getContent();
		this.contentLoaded = true;
		return this.content;
	}
}