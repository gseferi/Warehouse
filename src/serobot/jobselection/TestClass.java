package serobot.jobselection;

import java.awt.Robot;
import java.util.ArrayList;

import serobot.findroute.PointOutOfBoundaryException;
import serobot.findroute.RouteNotFoundException;
import serobot.jobparser.CSVFileReader;
import serobot.jobparser.Item;
import serobot.jobparser.ItemJoiner;
import serobot.jobparser.ItemPosition;
import serobot.jobparser.ItemReward;
import serobot.jobparser.Job;
import serobot.jobparser.Parser;

public class TestClass
{
	private static ArrayList<ItemReward> itemRewards = new ArrayList<ItemReward>();
	private static ArrayList<ItemPosition> itemPositions = new ArrayList<ItemPosition>();
	private static ArrayList<Item> items = new ArrayList<Item>();
	private static ArrayList<Job> jobs = new ArrayList<Job>();
	
	
	
	public static void main(String[] args) throws RouteNotFoundException, PointOutOfBoundaryException
	{
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
				
				JobSorter sorter= new JobSorter(jobs);
//				System.out.println(sorter.getCostSortedJobsIDList());
//				System.out.println(sorter.getLocationSortedJobs(0, 0));
//				System.out.println("Closest Job to 0,0");
//				System.out.println(sorter.getNextLocationGreedyID());
//				
				serobot.robot.Robot robot=new serobot.robot.Robot();
				
				//robot.setCoords(5, 5);
				//System.out.println("Current Location:");
			//	System.out.println("X-Coord:"+robot.getXCoord()+"\nY-Coord:"+robot.getYCoord());
            //  System.out.println("Closest Job to 5,5");
				/*
				 * Found error. Error is that I set a new location to robot and it still returns 0,0 as its position
				 */
				
				//System.out.println(sorter.getNextLocationGreedyID());
				
				
				
				for(int i=0;i<3;i++)
				{
					for(int j=0;j<3;j++)
					{
						System.out.println("Set X: "+i+" Set Y:"+j);
						robot.setCoords(i, j);
						sorter.checkLocation(robot);
						System.out.println("\n");
					}
				}
		
	}

}
