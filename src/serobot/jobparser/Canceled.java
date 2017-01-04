package serobot.jobparser;

public class Canceled {
	private Job job;
	private boolean canceled;
	
	public Canceled(Job job, boolean canceled) {
		this.job = job;
		this.canceled = canceled;
	}
	
	public boolean isCanceled() {
		return this.canceled;
	}
	public Job getJob(){
		return this.job;
	}
	
	
}
