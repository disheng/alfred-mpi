package it.uniroma3.dia.alfred.mapred;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import model.MaterializedRuleSet;
import model.Page;
import model.Rule;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.util.StringUtils;

import rules.xpath.XPathRule;

import com.atlantbh.hadoop.s3.io.S3ObjectSummaryWritable;
import com.atlantbh.hadoop.s3.io.S3ObjectWritable;

public class XPathApplierMapper 
extends Mapper<S3ObjectSummaryWritable,S3ObjectWritable,Text,MapWritable>{

	private MaterializedRuleSet rules;
	private List<List<Rule>> rulesSets;
	private MapWritable rule2null;
	private int contaPagine;		

	public enum Counters { PAGE, RULE_NULL, REPRESENTATIVE };

	@Override
	protected void setup(Context context)
			throws InterruptedException {

		//creo la mappa per i valori nulli
		this.rule2null = new MapWritable();

		//carico le regole dalla DistributedCache
		Path[] cacheFiles = new Path[0];

		try {
			cacheFiles = DistributedCache.getLocalCacheFiles(context.getConfiguration());
		} catch (IOException ioe) {
			System.err.println("Caught exception while getting cached files: " + StringUtils.stringifyException(ioe));
		}

		String cacheFile = cacheFiles[0].toString();
		try {
			loadRules(cacheFile);
		} catch (IOException e) {
			e.printStackTrace();
		}

		//inizializzo il contatore di pagine
		this.contaPagine = 0;		
	}

	@Override
	protected void cleanup(Context context) {

		//aggiungo a rule2null il conteggio del numero di pagine
		this.rule2null.put(new Text("numero_di_pagine_totale"), new IntWritable(this.contaPagine));

		//imposto il contatore con il conteggio del numero di pagine
		context.getCounter(Counters.PAGE).setValue(this.contaPagine);	

		//scrivo la mappa dei valori nulli
		try {
			context.write(new Text(""), this.rule2null);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public void loadRules(String cachePath) throws IOException {
		// note use of regular java.io methods here - this is a local file now
		BufferedReader wordReader = new BufferedReader(new FileReader(cachePath));
		try {
			String line;

			this.rules = new MaterializedRuleSet();
			while ((line = wordReader.readLine()) != null) {
				this.rules.addRule(new XPathRule(line));
				this.rule2null.put(new Text(line), new IntWritable(0));
			}

			this.rulesSets = new LinkedList<List<Rule>>();
			this.rulesSets.add(this.rules.getAllRules());

		} finally { wordReader.close(); }
	}

	@Override
	public void map(S3ObjectSummaryWritable key, S3ObjectWritable value, Context context) throws IOException, InterruptedException {

		Map<Rule, String> result = new HashMap<Rule, String>();

		Page p = new Page(IOUtils.toString(value.getObjectContent(), "UTF-8"));

		for (Rule rule : this.rules.getAllRules()) {
			String estratto = rule.applyOn(p).getTextContent();
			result.put(rule, estratto);

			//se la regola non ha estratto un valore lo inserisco in rule2null
			if (estratto.equals("")) {
				Text regolaToText = new Text(rule.toString());
				IntWritable intValue = (IntWritable)this.rule2null.get(regolaToText);
				intValue.set(intValue.get() + 1);
				context.getCounter(Counters.RULE_NULL).increment(1);
			}
		}

		boolean rapr = addIfRepresentative(result);

		if (rapr) {

			context.getCounter(Counters.REPRESENTATIVE).increment(1);
			MapWritable mappa = new MapWritable();

			for (Rule regola : result.keySet()) {
				String valore = result.get(regola);
				if (valore != null) {
					mappa.put(new Text(regola.toString()), new Text(valore));
				} else {
					mappa.put(new Text(regola.toString()), new Text(""));
				}
			}

			//content_pagina + mappa dei risultati per ogni regola
			context.write(new Text(p.getContent()), mappa);
		}

		this.contaPagine++;

	}


	private boolean addIfRepresentative(Map<Rule, String> map) {
		boolean notSameValues = false;

		List<List<Rule>> newRulesSets = new LinkedList<List<Rule>>();
		for (List<Rule> rules : this.rulesSets) {
			List<String> values = new ArrayList<String>();

			for (Rule r : rules) {
				values.add(map.get(r));
			}

			if (!sameValues(values)) {
				notSameValues = true;
				newRulesSets.addAll(groupForExtractedValues(values, rules));
			} else {
				// if they are same
				newRulesSets.add(rules);
			}

		}

		this.rulesSets = newRulesSets;

		return notSameValues;

	}

	private boolean sameValues(List<String> values) {
		boolean same = true;
		String firstValue = values.get(0);
		for (String otherValues : values) {
			if (!firstValue.equals(otherValues)) {
				same = false;
				break;
			}
		}
		return same;
	}

	private List<List<Rule>> groupForExtractedValues(List<String> values, List<Rule> rules) {
		Map<String, List<Rule>> val2rules = new HashMap<String, List<Rule>>();
		List<List<Rule>> result = new LinkedList<List<Rule>>();
		int i = 0;
		for (Rule r : rules) {
			String val = values.get(i);
			if (val2rules.containsKey(val)) {
				val2rules.get(val).add(r);
			} else {
				List<Rule> newSet = new LinkedList<Rule>();
				newSet.add(r);
				val2rules.put(val, newSet);
			}
			i++;
		}
		for (List<Rule> ruleGroup : val2rules.values()) {
			result.add(ruleGroup);
		}
		return result;
	}
}