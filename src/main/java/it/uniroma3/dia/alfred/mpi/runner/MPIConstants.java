package it.uniroma3.dia.alfred.mpi.runner;

public class MPIConstants {
	private MPIConstants() {}

	public static final int MASTER = 0;
	
	
	public enum TagValue {
		TAG_SIZE_CONF(0),
		TAG_CONF_LEN(1),
		TAG_CONF_DATA(2);
		
		private int value;
		TagValue(int tag) {
			this.value = tag;
		}
		
		int getValue() {
			return this.value;
		}
	}
	
	public enum AbortReason {
		WORK_SEND(1),
		WORK_SEND_ACK(2),
		WORK_SIZE_MISMATCH(3);
		
		private int reason;
		AbortReason(int reason) {
			this.reason = reason;
		}
		
		int getReason() {
			return this.reason;
		}
	}
}
