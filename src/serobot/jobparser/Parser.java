package serobot.jobparser;

import java.util.ArrayList;

/**
 * The Parser class which allows you to take in a text file and convert it into
 * the template classes
 * 
 * @author bobbydilley
 */
public class Parser {
	private String toParse;

	/**
	 * The constructor for the Parser class
	 * 
	 * @param toParse
	 *            A string representing a CSV file with either ItemRewards,
	 *            ItemPositions, or ItemJobs
	 */
	public Parser(String toParse) {
		this.toParse = toParse;
	}

	/**
	 * This function will parse the text file as a ItemReward type file.
	 * 
	 * @return ArrayList of ItemRewards, which contain the ItemID and rewards.
	 */
	public ArrayList<ItemReward> parseReward() {
		// Create a temporary ArrayList to store the items in
		ArrayList<ItemReward> itemRewards = new ArrayList<ItemReward>();

		// First we must split by the new lines
		String[] newLineSplit = toParse.split(System.lineSeparator());

		// Now we must split by commas for each line
		for (String line : newLineSplit) {
			String[] newCommaSplit = line.split(",");
			ItemReward itemReward = new ItemReward(newCommaSplit[0], Double.parseDouble(newCommaSplit[1]),
					Double.parseDouble(newCommaSplit[2]));
			itemRewards.add(itemReward);
		}
		return itemRewards;
	}

	/**
	 * This function will parse the text file as a ItemPosition type file.
	 * 
	 * @return ArrayList of ItemRewards, which contain the ItemID and x,y
	 *         positions.
	 */
	public ArrayList<ItemPosition> parsePosition() {
		// Create a temporary ArrayList to store the items in
		ArrayList<ItemPosition> itemPositions = new ArrayList<ItemPosition>();

		// First we must split by the new lines
		String[] newLineSplit = toParse.split(System.lineSeparator());

		// Now we must split by commas for each line
		for (String line : newLineSplit) {
			String[] newCommaSplit = line.split(",");
			ItemPosition itemPosition = new ItemPosition(Integer.parseInt(newCommaSplit[0]),
					Integer.parseInt(newCommaSplit[1]), newCommaSplit[2]);
			itemPositions.add(itemPosition);
		}
		return itemPositions;
	}

	public ArrayList<Job> parseJobs(ArrayList<Item> items) {
		ArrayList<Job> jobs = new ArrayList<Job>();

		// First we must split by the new lines
		String[] newLineSplit = toParse.split(System.lineSeparator());
		
		for (String line : newLineSplit) {
			String[] newCommaSplit = line.split(",");
			Job job = new Job(newCommaSplit[0]);

			for (int i = 1; i < newCommaSplit.length; i += 2) {
				String itemID = newCommaSplit[i];
				int count = Integer.parseInt(newCommaSplit[i + 1]);
				JobLine jobLine = new JobLine(Item.itemByID(itemID, items), count);
				job.addLine(jobLine);
			}
			jobs.add(job);
		}
		return jobs;
	}

}
