import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class EventuallyPerfectFailureDetector implements IFailureDetector {

	Process p;
	ArrayList<Integer> suspects;
	Timer t;
	ArrayList<Long> times;
	
	static final int Delta = 1000; /* 1sec */
	
	class PeriodicTask extends TimerTask {
		public void run() {
			p.broadcast("heartbeat", String.format("%d", System.currentTimeMillis()));
		}
	}
	
	class PeriodicCheck extends TimerTask {
		public void run() {
			//TODO
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
		//TODO PeriodicCheck()
	}

	@Override
	public void receive(Message m) {
		Utils.out(p.pid, m.toString());

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