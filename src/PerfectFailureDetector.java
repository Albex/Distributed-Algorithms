import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class PerfectFailureDetector implements IFailureDetector {
	
	Process p;
	ArrayList<Integer> suspects;
	Timer t;
	ArrayList<Long> times;
	
	static final int Delta = 1000; /* 1sec */
	static final int Timeout = Delta + 2*Utils.DELAY;
	
	class PeriodicTask extends TimerTask {
		public void run() {
			p.broadcast("heartbeat", "null");
		}
	}
	
	class PeriodicCheck extends TimerTask {
		public void run() {
			Long currentTime = System.currentTimeMillis();
			for (int i = 0; i < times.size(); i++) {
				if (currentTime - times.get(i) > Timeout) {
					suspects.add(i);
				}
				else {
					suspects.remove((Integer) i);
				}
			}
		}
	}

	public PerfectFailureDetector(Process p, int n) {
		this.p = p;
		t = new Timer();
		suspects = new ArrayList<Integer>();
		times = new ArrayList<Long>(n);
		for (int i = 0; i < n; i++) {
			times.set(i, System.currentTimeMillis());
		}
	}

	@Override
	public void begin() {
		t.schedule(new PeriodicTask(), 0, Delta);
		t.schedule(new PeriodicCheck(), 0, Timeout);
	}
	
	@Override
	public void receive(Message m) {
		Utils.out(p.pid, m.toString());
		
		/* reset the times */
		times.set(m.getSource()-1, System.currentTimeMillis());
	}

	@Override
	public boolean isSuspect(Integer pid) {
		return suspects.contains(pid);
	}

	@Override
	public int getLeader() {
		return -1;
	}

	@Override
	public void isSuspected(Integer process) {
		return ;
	}
	
}
