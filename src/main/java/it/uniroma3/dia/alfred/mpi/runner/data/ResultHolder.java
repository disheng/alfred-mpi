package it.uniroma3.dia.alfred.mpi.runner.data;

public class ResultHolder {

	private boolean booleanResult;
	private String stringResult;
	
	public ResultHolder(boolean booleanResult, String stringResult) {
		super();
		this.booleanResult = booleanResult;
		this.stringResult = stringResult;
	}

	public boolean getBooleanResult() {
		return booleanResult;
	}

	public String getStringResult() {
		return stringResult;
	}

}
