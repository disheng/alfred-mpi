package it.uniroma3.dia.alfred.mapred;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.util.StringUtils;

import model.MaterializedRuleSet;
import model.Rule;
import rules.xpath.XPathRule;

public class XPathApplierReducer 
extends Reducer<Text,MapWritable,Text,MapWritable> {
	
	private MaterializedRuleSet rules;
	private List<List<Rule>> rulesSets;
	private Text textVuoto; //mi identifica le mappe di valori nulli in ingresso
	private Text textRegola; //identifica la mappa con il rulesSets in uscita
	private Text textNull; //identifica la mappa dei valori nulli in uscita
	private MapWritable rule2null;
	private MapWritable mapVuoto;
	
	
	@Override
	protected void setup(Context context) {
		
		//carico le regole dalla DistributedCache
		Path[] cacheFiles = new Path[0];
		
		try {
			cacheFiles = DistributedCache.getLocalCacheFiles(context.getConfiguration());
		} catch (IOException ioe) {
			System.err.println("caught exception while getting cached files: " + StringUtils.stringifyException(ioe));
		}

		String cacheFile = cacheFiles[0].toString();
		try {
			loadRules(cacheFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//inizializzo i textXxx
		this.textVuoto = new Text("");
		this.textRegola = new Text("Regola");
		this.textNull = new Text("Null");
		
		this.mapVuoto = new MapWritable();
				
	}
	
	
	@Override
	protected void cleanup(Context context) {
		
		//scrivo la mappa dei valori nulli
		try {			
			
			context.write(this.textNull, this.rule2null);	
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//scrivo il rulesSets sotto forma di mappa
		try {
			
			MapWritable mapRulesSets = new MapWritable();
			
			for (List<Rule> lista : this.rulesSets) {
				MapWritable mapList = new MapWritable();
				for (Rule regola : lista) {
					mapList.put(new Text(regola.encode()), this.textVuoto);
				}
				mapRulesSets.put(mapList, this.textVuoto);
			}

			context.write(this.textRegola, mapRulesSets);
			
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
		    }
			
			this.rulesSets = new LinkedList<List<Rule>>();
			this.rulesSets.add(this.rules.getAllRules());
		    
		} finally { wordReader.close(); }
	}
	
	
	@Override
	public void reduce(Text key, Iterable<MapWritable> mappe, Context context) throws IOException, InterruptedException {
			
		if (!key.equals(this.textVuoto)) {
			
			Map<Rule, String> result = new HashMap<Rule, String>();
	    	
			for (MapWritable mappa : mappe) {	    	
		    	for (Writable rule : mappa.keySet()) {
		    		Text regola = (Text)rule;
		    		Text valore = (Text)mappa.get(rule);
		    		
		    		result.put(new XPathRule(regola.toString()), valore.toString());
		    	}
			}
			
			boolean rapr = addIfRepresentative(result);
			
	        if (rapr) context.write(key, this.mapVuoto);
	        
		} else {
			
			//aggrego i null			
			for (MapWritable mappa : mappe) {
				if (this.rule2null == null) {
					//inizializzo rule2null con la prima mappa ricevuta dai mapper
					this.rule2null = new MapWritable(mappa);
				} else {
					for (Writable regola : mappa.keySet()) {
						IntWritable newValue = (IntWritable)mappa.get((Text)regola);
						IntWritable oldValue = (IntWritable)this.rule2null.get((Text)regola);
						oldValue.set(oldValue.get() + newValue.get());
					}
				}
			}
			
		}
        
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
