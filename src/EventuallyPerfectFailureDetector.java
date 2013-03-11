import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class EventuallyPerfectFailureDetector implements IFailureDetector {

	Process p;
	ArrayList<Integer> suspects;
	Timer t;
	ArrayList<Long> times;
	
	static Long delay;
	static final int Delta = 1000; /* 1sec */
	static Long Timeout = Delta + 2*delay;
	
	class PeriodicTask extends TimerTask {
		public void run() {
			p.broadcast("heartbeat", String.format("%d", System.currentTimeMillis()));
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

	public EventuallyPerfectFailureDetector(Process p, int n) {
		this.p = p;
		t = new Timer();
		suspects = new ArrayList<Integer>();
		times = new ArrayList<Long>(n);
		for (int i = 0; i < times.size(); i++) {
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
		
		/* delay upadte */
		delay = (System.currentTimeMillis() - Long.parseLong(m.getPayload()));
		/* reset the times */
		times.set(m.getSource(), System.currentTimeMillis());
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