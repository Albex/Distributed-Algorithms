public class PFDProcess extends Process {
	
	private IFailureDetector detector;

	public PFDProcess(String name, int pid, int n) {
		super(name, pid, n);
		detector = new PerfectFailureDetector(this, n);
	}
	
	public void begin() {
		detector.begin();
	}
	
	public synchronized void receive (Message m) {
		String type = m.getType();
		if (type.equals("heartbeat")) {
			detector.receive(m);
		}
	}
	
	public static void main(String [] args) {
		PFDProcess p = new PFDProcess("P1", 1, 2);
		p.registeR();
		p.begin();
	}
	
}
