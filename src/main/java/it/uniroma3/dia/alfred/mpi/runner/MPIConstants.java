package it.uniroma3.dia.alfred.mpi.runner;

class MPIConstants {
	private MPIConstants() {}

	public static final int MASTER = 0;
	
	
	public enum TagValue {
		TAG_SIZE_CONF(0),
		TAG_CONF_LEN(1),
		TAG_CONF_DATA(2),
		TAG_CONF_RESULTS_SIZE(3),
		TAG_CONF_RESULTS(4);
		
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
		WORK_SIZE_MISMATCH(3),
		WORK_SEND_DATA(4),
		WORK_RECV_DATA(5);
		
		private int reason;
		AbortReason(int reason) {
			this.reason = reason;
		}
		
		int getReason() {
			return this.reason;
		}
	}
}
