package serobot.jobparser;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import serobot.jobselection.JobSorter;

public class jobparserTest {
	private static ArrayList<ItemReward> itemRewards;
	private static ArrayList<ItemPosition> itemPositions;
	private static ArrayList<Item> items;
	private static ArrayList<Job> jobs;

	
	@Before
	public void setUp() {
		itemRewards = new ArrayList<ItemReward>();
		itemPositions = new ArrayList<ItemPosition>();
		items = new ArrayList<Item>();
		jobs = new ArrayList<Job>();
	}

	@After
	public void tearDown() {
		itemRewards.clear();
		itemPositions.clear();
		items.clear();
		jobs.clear();
	}
	
	@Test
	public void parsePositionTest() {
		// Parse the positions file
		double startTime = System.currentTimeMillis();
		CSVFileReader positionReader = new CSVFileReader("data/locations.csv");
		Parser positionParser = new Parser(positionReader.read());
		itemPositions = positionParser.parsePosition();
		double currentTime = System.currentTimeMillis();
		double finalTime = currentTime - startTime;
		assertTrue(finalTime < 200);
		for(ItemPosition itemPosition : itemPositions) {
			assertTrue(itemPosition.getID() != null);
		}
	}
	
	
	@Test
	public void parseRewardsTest() {
		// Parse the rewards file
		double startTime = System.currentTimeMillis();
		CSVFileReader rewardReader = new CSVFileReader("data/items.csv");
		Parser rewardParser = new Parser(rewardReader.read());
		itemRewards = rewardParser.parseReward();
		double currentTime = System.currentTimeMillis();
		double finalTime = currentTime - startTime;
		assertTrue(finalTime < 200);
		for(ItemReward itemReward : itemRewards) {
			assertTrue(itemReward.getID() != null);
		}
	}
	
	@Test
	public void joinTest() {
		double startTime = System.currentTimeMillis();
		// Join the reward and position information
		ItemJoiner itemJoiner = new ItemJoiner(itemPositions, itemRewards);
		items = itemJoiner.Join();
		double currentTime = System.currentTimeMillis();
		double finalTime = currentTime - startTime;
		assertTrue(finalTime < 200);
		for(Item item : items) {
			assertTrue(item.getID() != null);
		}
		
	}
	
	@Test
	public void parseJobsTest() {
		// Parse the jobs file
		double startTime = System.currentTimeMillis();
		CSVFileReader jobReader = new CSVFileReader("data/jobs.csv");
		Parser jobParser = new Parser(jobReader.read());
		jobs = jobParser.parseJobs(items);
		double currentTime = System.currentTimeMillis();
		double finalTime = currentTime - startTime;
		assertTrue(finalTime < 200);
		for(Job job : jobs) {
			assertTrue(job.getID() != null);
		}
	}
}


