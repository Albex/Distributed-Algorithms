import java.util.ArrayList;


public class EventuallyLeaderElector extends EventuallyPerfectFailureDetector {

	ArrayList<Integer> trusts;
	
	public EventuallyLeaderElector(Process p, int n) {
		super(p, n);
		trusts = new ArrayList<Integer>();
	}

	@Override
	public int getLeader() {
		
		return super.getLeader();
	}

}
