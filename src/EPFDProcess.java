public class EPFDProcess extends Process {
	
	private IFailureDetector detector;

	public EPFDProcess(String name, int pid, int n) {
		super(name, pid, n);
		detector = new EventuallyPerfectFailureDetector(this, n);
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
		String name = args[0];
		int id = Integer.parseInt(args[1]);
		int n = Integer.parseInt(args[2]);
		
		PFDProcess p = new PFDProcess(name, id, n);
		p.registeR();
		p.begin();
	}
	
}
