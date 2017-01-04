package serobot.jobparser;
import java.util.ArrayList;

public class Job {
	private String id;
	ArrayList<JobLine> jobLines = new ArrayList<JobLine>();

	public static Job jobByID(String id, ArrayList<Job> jobs) {
		for (Job job : jobs) {
			if (job.getID().equals(id)) {
				return job;
			}
		}
		return null;
	}

	public Job(String id) {
		this.id = id;
	}

	public String getID() {
		return this.id;
	}

	public void addLine(JobLine jobLine) {
		jobLines.add(jobLine);
	}
	
	public void removeLine(int index) {
		jobLines.remove(index);
	}

	public JobLine getLine(int index) {
		return jobLines.get(index);
	}

	public ArrayList<JobLine> getLines() {
		return jobLines;
	}
}
