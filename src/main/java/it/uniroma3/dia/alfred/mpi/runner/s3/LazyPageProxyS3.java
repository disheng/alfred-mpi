package it.uniroma3.dia.alfred.mpi.runner.s3;

import it.uniroma3.dia.datasource.s3.S3Uploader;

import java.io.StringReader;

import org.w3c.dom.Document;

import experiment.LazyPageProxy;

public class LazyPageProxyS3 extends LazyPageProxy {
	private String bucketName;
	
	public LazyPageProxyS3(String path, String buckName) {
		super(null, path);
		
		this.bucketName = buckName;
	}

	protected Document getDocument(String path) {
		return getDocument(new StringReader(this.getContent()));
	}
	
	public String getContent(){
		return S3Uploader.getInstance().getPageFromBucket(this.bucketName, this.path).getContent();
	}
}