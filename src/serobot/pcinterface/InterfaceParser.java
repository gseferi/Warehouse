package serobot.pcinterface;

import java.util.ArrayList;

import serobot.jobparser.CSVFileReader;
import serobot.jobparser.Item;
import serobot.jobparser.ItemJoiner;
import serobot.jobparser.ItemPosition;
import serobot.jobparser.ItemReward;
import serobot.jobparser.Job;
import serobot.jobparser.Parser;
import serobot.jobselection.JobSorter;

/**
 * Parses the jobs, items and locations csv files.
 * Stores the information in the data model.
 * @author Aaquib Naved
 *
 */
public class InterfaceParser {
	public static void parseFiles(PCInterfaceDataModel model, String jobPath, String itemPath, String locationsPath) {
		ArrayList<ItemReward> itemRewards = new ArrayList<ItemReward>();
		ArrayList<ItemPosition> itemPositions = new ArrayList<ItemPosition>();
		ArrayList<Item> items = new ArrayList<Item>();
		ArrayList<Job> jobs = new ArrayList<Job>();
		
		// Parse the rewards file
		CSVFileReader rewardReader = new CSVFileReader(itemPath);
		Parser rewardParser = new Parser(rewardReader.read());
		itemRewards = rewardParser.parseReward();

		// Parse the positions file
		// is!! Need someone to help :)
		CSVFileReader positionReader = new CSVFileReader(locationsPath);
		Parser positionParser = new Parser(positionReader.read());
		itemPositions = positionParser.parsePosition();

		// Join the reward and position information
		ItemJoiner itemJoiner = new ItemJoiner(itemPositions, itemRewards);
		items = itemJoiner.Join();

		// Parse the jobs file
		CSVFileReader jobReader = new CSVFileReader(jobPath);
		Parser jobParser = new Parser(jobReader.read());
		jobs = jobParser.parseJobs(items);
		
		JobSorter sorter = new JobSorter(jobs);
		ArrayList<Job> sortedJobs = sorter.getCostSortedJobsList();
		
		model.setUpcomingJobs(sortedJobs);
		model.setItems(items);
	}
}
