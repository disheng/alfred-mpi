package it.uniroma3.dia.alfredmpi.mpirunner;

public class MPIConstants {
	private MPIConstants() {}

	public static final int MASTER = 0;
	
	public static final int TAG_SIZE_CONF = 0;
	
	
	public enum AbortReason {
		WORK_SEND(1),
		WORK_SEND_ACK(2);
		
		private int reason;
		AbortReason(int reason) {
			this.reason = reason;
		}
		
		int getReason() {
			return this.reason;
		}
	}
}
