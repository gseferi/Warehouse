package serobot.jobparser;

import java.util.ArrayList;

/**
 * RobotWarehouse Solution This piece of software runs on the PC and not the NXT
 * 
 * @author bobbydilley
 *
 */
public class Warehouse {
	/**
	 * The main function which instantiates all of the classes.
	 * 
	 * @param args
	 *            Arguments from the command line, currently none.
	 */
	private static ArrayList<ItemReward> itemRewards = new ArrayList<ItemReward>();
	private static ArrayList<ItemPosition> itemPositions = new ArrayList<ItemPosition>();
	private static ArrayList<Item> items = new ArrayList<Item>();
	private static ArrayList<Job> jobs = new ArrayList<Job>();

	public static void main(String[] args) {

		// Parse the rewards file
		CSVFileReader rewardReader = new CSVFileReader("data/items.csv");
		Parser rewardParser = new Parser(rewardReader.read());
		itemRewards = rewardParser.parseReward();

		// Parse the positions file
		// is!! Need someone to help :)
		CSVFileReader positionReader = new CSVFileReader("data/locations.csv");
		Parser positionParser = new Parser(positionReader.read());
		itemPositions = positionParser.parsePosition();

		// Join the reward and position information
		ItemJoiner itemJoiner = new ItemJoiner(itemPositions, itemRewards);
		items = itemJoiner.Join();

		// Parse the jobs file
		CSVFileReader jobReader = new CSVFileReader("data/jobs.csv");
		Parser jobParser = new Parser(jobReader.read());
		jobs = jobParser.parseJobs(items);

		// We can get items by their ID
		Item tempItem = Item.itemByID("a", items);

		// We can get jobs by their ID
		Job tempJob = Job.jobByID("1001", jobs);

		// We can iterate through the items
		for (Item item : items) {
			System.out.println("ID: " + item.getID());
			System.out.println("X: " + item.getXPosition());
			System.out.println("Y: " + item.getYPosition());
			System.out.println("Reward: " + item.getReward());
			System.out.println("Weight: " + item.getWeight());
			System.out.println("-------------------------");
		}

		// We can iterate through the jobs
		for (Job job : jobs) {
			System.out.println("ID: " + job.getID());
			for (JobLine jobLine : job.getLines()) {
				System.out.println(
						"\t JobLine Item ID: " + jobLine.getItem().getID() + " - Count: " + jobLine.getCount());
			}
			System.out.println("-------------------------");
		}

	}
}
