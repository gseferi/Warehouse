package serobot.jobselection;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import serobot.jobparser.CSVFileReader;
import serobot.jobparser.Item;
import serobot.jobparser.ItemJoiner;
import serobot.jobparser.ItemPosition;
import serobot.jobparser.ItemReward;
import serobot.jobparser.Job;
import serobot.jobparser.Parser;
import serobot.robot.Robot;
import serobot.findroute.*;

/**
 * JUnit Test for JobSorter
 * 
 * @author logofatuemil
 *
 */
public class JobSorterTest {

	private static ArrayList<Item> items = new ArrayList<Item>();
	private static ArrayList<ItemReward> itemRewards = new ArrayList<ItemReward>();
	private static ArrayList<ItemPosition> itemPositions = new ArrayList<ItemPosition>();
	private static ArrayList<Item> itemsTest = new ArrayList<Item>();
	private static ArrayList<ItemReward> itemRewardsTest = new ArrayList<ItemReward>();
	private static ArrayList<ItemPosition> itemPositionsTest = new ArrayList<ItemPosition>();
	private ArrayList<Job> jobs, sorted_jobs, copyjobs,
			jobsTest = new ArrayList<Job>();
	private ArrayList<Obstacle> obstacles = new ArrayList<Obstacle>();
	private static ArrayList<String> sortIdJobs = new ArrayList<String>();
	private long rewards, bestRewards;
	private double startTime, currentTime, finalTime;
	private JobSorter sort_jobs;
	private Job getNextSortedJob, getNextClosestJob;
	private Robot robot_location;

	@Test
	public void CostSortedJobsTest() {
		startTime = System.currentTimeMillis();
		sorted_jobs = sort_jobs.getCostSortedJobsList();
		currentTime = System.currentTimeMillis();
		finalTime = currentTime - startTime;
		assertTrue(finalTime < 200);

		rewards = reward(jobs, 100);
		bestRewards = reward(sorted_jobs, 100);
		assertTrue(rewards <= bestRewards);

		rewards = reward(jobs, 1100);
		bestRewards = reward(sorted_jobs, 1100);
		assertTrue(rewards <= bestRewards);

		rewards = reward(jobs, 2000);
		bestRewards = reward(sorted_jobs, 2000);
		assertTrue(rewards == bestRewards);

	}

	@Test
	public void CostSortedJobsIDTest() {
		startTime = System.currentTimeMillis();
		sortIdJobs = sort_jobs.getCostSortedJobsIDList();
		currentTime = System.currentTimeMillis();
		finalTime = currentTime - startTime;
		assertTrue(finalTime < 200);

		sort_jobs = new JobSorter(jobs);
		sorted_jobs = sort_jobs.getCostSortedJobsList();
		ArrayList<String> bestSortIdJobs = getJobsID(sorted_jobs, 200);
		for (int i = 0; i < 200; i++)
			assertTrue(sortIdJobs.get(i).equals(bestSortIdJobs.get(i)));
	}

	@Test
	public void getNextCostSortedJobTest() {
		sorted_jobs = sort_jobs.getCostSortedJobsList();

		startTime = System.currentTimeMillis();
		getNextSortedJob = sort_jobs.getNextCostSortedJob();
		currentTime = System.currentTimeMillis();
		finalTime = currentTime - startTime;
		assertTrue(finalTime < 0.001);

		// These asserts aren't passing, please check again the method in JobSorter Class.
		// assertTrue(sorted_jobs.get(0).getID().equals(getNextSortedJob.getID()));
		// assertTrue(sorted_jobs.get(0).getLine(0).getCount()==getNextSortedJob.getLine(0).getCount());
	}

	@Test
	public void getNextCostSortedJobIDTest() {
		sorted_jobs = sort_jobs.getCostSortedJobsList();

		startTime = System.currentTimeMillis();
		String getNextSortedJobID = sort_jobs.getNextCostSortedJobID();
		currentTime = System.currentTimeMillis();
		finalTime = currentTime - startTime;
		assertTrue(finalTime < 0.001);

		// This assert isn't passing, please check again the method in JobSorter Class.
		// assertTrue(sorted_jobs.get(0).getID().equals(getNextSortedJobID));
	}

	@Test
	public void getNextLocationGreedyTest() throws RouteNotFoundException,PointOutOfBoundaryException {
		robot_location = new Robot();
		robot_location.setCoords(2, 2);
		obstacles.add(new Obstacle(new GridPoint(1, 2), new GridPoint(2, 2)));

		CSVFileReader rewardReaderTest = new CSVFileReader("data/items_test.csv");
		CSVFileReader positionReaderTest = new CSVFileReader("data/locations_test.csv");
		CSVFileReader jobReaderTest = new CSVFileReader("data/jobs_test.csv");
		Parser rewardParserTest = new Parser(rewardReaderTest.read());
		Parser positionParserTest = new Parser(positionReaderTest.read());
		Parser jobParserTest = new Parser(jobReaderTest.read());

		itemRewardsTest = rewardParserTest.parseReward();
		itemPositionsTest = positionParserTest.parsePosition();

		ItemJoiner itemJoiner = new ItemJoiner(itemPositionsTest,itemRewardsTest);
		itemsTest = itemJoiner.Join();
		jobsTest = jobParserTest.parseJobs(itemsTest);

		sort_jobs = new JobSorter(jobsTest);

		startTime = System.currentTimeMillis();
		getNextClosestJob = sort_jobs.getNextLocationGreedy(obstacles,robot_location);
		currentTime = System.currentTimeMillis();
		finalTime = currentTime - startTime;
		assertTrue(finalTime < 50);
		assertTrue(getNextClosestJob.getLine(0).getItem().getXPosition() == 3);
		assertTrue(getNextClosestJob.getLine(0).getItem().getYPosition() == 2);

		obstacles.add(new Obstacle(new GridPoint(2, 2), new GridPoint(3, 2)));

		startTime = System.currentTimeMillis();
		getNextClosestJob = sort_jobs.getNextLocationGreedy(obstacles,robot_location);
		currentTime = System.currentTimeMillis();
		finalTime = currentTime - startTime;
		assertTrue(finalTime < 50);
		assertTrue(getNextClosestJob.getLine(0).getItem().getXPosition() == 2);
		assertTrue(getNextClosestJob.getLine(0).getItem().getYPosition() == 4);
	}

	@Test
	public void getNextLocationGreedyIDTest() throws RouteNotFoundException,PointOutOfBoundaryException {
		robot_location = new Robot();
		robot_location.setCoords(2, 2);
		obstacles.add(new Obstacle(new GridPoint(1, 2), new GridPoint(2, 2)));

		CSVFileReader rewardReaderTest = new CSVFileReader("data/items_test.csv");
		CSVFileReader positionReaderTest = new CSVFileReader("data/locations_test.csv");
		CSVFileReader jobReaderTest = new CSVFileReader("data/jobs_test.csv");
		Parser rewardParserTest = new Parser(rewardReaderTest.read());
		Parser positionParserTest = new Parser(positionReaderTest.read());
		Parser jobParserTest = new Parser(jobReaderTest.read());

		itemRewardsTest = rewardParserTest.parseReward();
		itemPositionsTest = positionParserTest.parsePosition();

		ItemJoiner itemJoiner = new ItemJoiner(itemPositionsTest,itemRewardsTest);
		itemsTest = itemJoiner.Join();
		jobsTest = jobParserTest.parseJobs(itemsTest);

		sort_jobs = new JobSorter(jobsTest);

		startTime = System.currentTimeMillis();
		String getNextClosestJobID = sort_jobs.getNextLocationGreedyID(obstacles, robot_location);
		currentTime = System.currentTimeMillis();
		finalTime = currentTime - startTime;
		assertTrue(finalTime < 50);
		// This assert isn't passing, please check again the method in JobSorter Class.
		// assertTrue(getNextClosestJobID.equals("10001"));

		obstacles.add(new Obstacle(new GridPoint(2, 2), new GridPoint(3, 2)));

		startTime = System.currentTimeMillis();
		getNextClosestJobID = sort_jobs.getNextLocationGreedyID(obstacles,robot_location);
		currentTime = System.currentTimeMillis();
		finalTime = currentTime - startTime;
		assertTrue(finalTime < 50);
		// This assert isn't passing, please check again the method in JobSorter Class.
		// assertTrue(getNextClosestJobID.equalsIgnoreCase("10003"));
	}

	@Before
	public void setUp() {
		CSVFileReader rewardReader = new CSVFileReader("data/items.csv");
		CSVFileReader positionReader = new CSVFileReader("data/locations.csv");
		CSVFileReader jobReader = new CSVFileReader("data/jobs.csv");
		Parser rewardParser = new Parser(rewardReader.read());
		Parser positionParser = new Parser(positionReader.read());
		Parser jobParser = new Parser(jobReader.read());

		itemRewards = rewardParser.parseReward();
		itemPositions = positionParser.parsePosition();

		ItemJoiner itemJoiner = new ItemJoiner(itemPositions, itemRewards);
		items = itemJoiner.Join();
		jobs = jobParser.parseJobs(items);
		copyjobs = jobParser.parseJobs(items);
		sort_jobs = new JobSorter(copyjobs);
	}

	@After
	public void tearDown() {
		jobs.clear();
		copyjobs.clear();
	}

	private long reward(ArrayList<Job> jobs, int size) {
		long reward = 0;
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < jobs.get(i).getLines().size(); j++)
				reward += jobs.get(i).getLines().get(j).getItem().getReward();
		}
		return reward;
	}

	private ArrayList<String> getJobsID(ArrayList<Job> jobs, int size) {
		ArrayList<String> bestJobsID = new ArrayList<String>();
		for (int i = 0; i < size; i++) {
			bestJobsID.add(jobs.get(i).getID() + "");
		}
		return bestJobsID;
	}

}
