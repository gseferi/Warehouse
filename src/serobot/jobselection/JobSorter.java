package serobot.jobselection;

import java.util.ArrayList;
import java.util.List;


import serobot.findroute.FindRoute;
import serobot.findroute.GridLink;
import serobot.findroute.GridPoint;
import serobot.findroute.Obstacle;
import serobot.findroute.PointOutOfBoundaryException;
import serobot.findroute.RouteNotFoundException;
import serobot.jobparser.Item;
import serobot.jobparser.Job;
import serobot.jobparser.JobLine;
import serobot.robot.Robot;

public class JobSorter
{

	// data fields
	private ArrayList<Job> jobs = new ArrayList<Job>();
	private ArrayList<Job> jobsTemp = new ArrayList<Job>();
	private ArrayList<JobLine> jobLines = new ArrayList<JobLine>();
	private ArrayList<JobLine> jobLinesTemp = new ArrayList<JobLine>();
	private ArrayList<Job> sortedJobs = new ArrayList<Job>();
	private ArrayList<String> sortedJobsID = new ArrayList<String>();
	private ArrayList<GridLink> links = new ArrayList<GridLink>();
	private ArrayList<GridPoint> points = new ArrayList<GridPoint>();
	private ArrayList<Obstacle> obstacles = new ArrayList<Obstacle>();
	private String bestJobID = null;
//	private int currentXCoord = 0;
//	private int currentYCoord = 0;
	private Robot robot = new Robot();
	private double storedReward;
	private Job bestJob;
	private int bestJobIndex;
	private int width = 10;
	private int height = 12;
	private int sumDistance;
	private int storedSumDistance;
	private int randomCounter=0;
	

	// constructor
	public JobSorter(ArrayList<Job> jobs)
	{
		this.jobs = jobs;
		this.sortedJobs = sortedJobs;
		this.jobsTemp = jobsTemp;
		this.jobLinesTemp = jobLinesTemp;
		this.links = links;
		this.points = points;
		this.obstacles=obstacles;
//		this.currentXCoord = currentXCoord;
//		this.currentYCoord = currentYCoord;
		jobsTemp = jobs;
		jobLinesTemp = jobLines;

	}

	public ArrayList<Job> getCostSortedJobsList()
	{

		// loop through job in ArrayList of Jobs

		while (jobsTemp.size() != 0)
		{
			storedReward = 0;

			for (int i = 0; i < jobsTemp.size(); i++)
			{
				double sumRewards = 0;
				double newReward = 0;

				// Loop through Job Lines
				for (int j = 0; j < jobsTemp.get(i).getLines().size(); j++)
				{
					// Get Item in this jobList
					Item item = jobsTemp.get(i).getLine(j).getItem();
					newReward = item.getReward() * jobsTemp.get(i).getLine(j).getCount();
					sumRewards = sumRewards + newReward;
				}

				if (sumRewards > storedReward)
				{

					storedReward = sumRewards;

					// get JobID for the best job so far
					bestJobID = jobsTemp.get(i).getID();
					bestJob = jobsTemp.get(i);
					bestJobIndex = i;
				}

			}

			// adding best Job to front of new ArrayList
			sortedJobs.add(bestJob);
			// remove it from the list
			jobsTemp.remove(jobsTemp.indexOf(bestJob));

		}
		// reset jobTemp ArrayList back to normal and not null
		jobsTemp = jobs;
		return sortedJobs;
	}

	public ArrayList<String> getCostSortedJobsIDList()
	{

		// loop through job in ArrayList of Jobs

		while (jobsTemp.size() != 0)
		{
			storedReward = 0;

			for (int i = 0; i < jobsTemp.size(); i++)
			{
				double sumRewards = 0;
				double newReward = 0;

				// Loop through Job Lines
				for (int j = 0; j < jobsTemp.get(i).getLines().size(); j++)
				{
					// Get Item in this jobList
					Item item = jobsTemp.get(i).getLine(j).getItem();
					newReward = item.getReward() * jobsTemp.get(i).getLine(j).getCount();
					sumRewards = sumRewards + newReward;
				}

				if (sumRewards > storedReward)
				{

					storedReward = sumRewards;

					// get JobID for the best job so far
					bestJobID = jobsTemp.get(i).getID();
					bestJob = jobsTemp.get(i);
					bestJobIndex = i;
				}

			}

			// adding best Job to front of new ArrayList
			sortedJobsID.add(bestJobID);
			// remove it from the list
			jobsTemp.remove(jobsTemp.indexOf(bestJob));

		}
		// reset jobTemp ArrayList back to normal and not null
		jobsTemp = jobs;
		return sortedJobsID;
	}

	public Job getNextCostSortedJob()
	{

		for (int i = 0; i < jobsTemp.size(); i++)
		{
			double sumRewards = 0;
			double newReward = 0;

			// Loop through Job Lines
			for (int j = 0; j < jobsTemp.get(i).getLines().size(); j++)
			{
				// Get Item in this jobList
				Item item = jobsTemp.get(i).getLine(j).getItem();
				newReward = item.getReward() * jobsTemp.get(i).getLine(j).getCount();
				sumRewards = sumRewards + newReward;
			}

			if (sumRewards > storedReward)
			{

				storedReward = sumRewards;

				// get JobID for the best job so far
				bestJob = jobsTemp.get(i);

			}

		}

		return bestJob;

	}

	public String getNextCostSortedJobID()
	{

		for (int i = 0; i < jobsTemp.size(); i++)
		{
			double sumRewards = 0;
			double newReward = 0;

			// Loop through Job Lines
			for (int j = 0; j < jobsTemp.get(i).getLines().size(); j++)
			{
				// Get Item in this jobList
				Item item = jobsTemp.get(i).getLine(j).getItem();
				newReward = item.getReward() * jobsTemp.get(i).getLine(j).getCount();
				sumRewards = sumRewards + newReward;
			}

			if (sumRewards > storedReward)
			{

				storedReward = sumRewards;

				// get JobID for the best job so far
				bestJob = jobsTemp.get(i);
				bestJobID = bestJob.getID();
			}

		}

		return bestJobID;

	}

	public Job getNextLocationGreedy(ArrayList<Obstacle> obstacles,Robot robot) throws RouteNotFoundException, PointOutOfBoundaryException
	{

		// create new GridMap
		
		serobot.findroute.GridMap map = new serobot.findroute.GridMap(width, height, obstacles);
		storedSumDistance = 13000;
		for (int i = 0; i < jobsTemp.size(); i++)
		{

			sumDistance = 0;

			// Loop through Job Lines
			for (int j = 0; j < jobsTemp.get(i).getLines().size(); j++)
			{
				// Get Item in this jobList
				Item item = jobsTemp.get(i).getLine(j).getItem();
				int itemX = item.getXPosition();
				int itemY = item.getYPosition();
				GridPoint itemGridPoint = new GridPoint(itemX, itemY);
				GridPoint currentGridPoint = new GridPoint(robot.getXCoord(), robot.getYCoord());
				GridPoint dropGridPoint= new GridPoint(0,0);
				FindRoute findRoute = new FindRoute(map);
				int itemDistance = findRoute.getDistance(currentGridPoint, itemGridPoint);
				
				sumDistance = sumDistance + itemDistance;
			}

			if (sumDistance < storedSumDistance)
			{

				storedSumDistance = sumDistance;
				bestJob = jobsTemp.get(i);
			}

		}

		/*
		 * Get bestJob
		 * Loop through items within that Job
		 * Get closest item to me
		 * set that as bestItem
		 * Add that jobLine to the end
		 * Get next bestItem
		 * repeat
		 * When Items are over delete all jobLines prior to first bestItem
		 * 
		 * 
		 */
		
		
		while(randomCounter!=bestJob.getLines().size())
		{
		int storedItemDistance=1110;
		randomCounter++;
		JobLine bestJobLine=bestJob.getLine(0);
		int bestJobLineIndex=0;
		for(int i=0;i<bestJob.getLines().size();i++)
		{
			Item item=bestJob.getLine(i).getItem();
			GridPoint itemGridPoint=new GridPoint(item.getXPosition(),item.getYPosition());
			GridPoint currentGridPoint=new GridPoint(robot.getXCoord(),robot.getYCoord());
			FindRoute findRoute = new FindRoute(map);
			int itemDistance = findRoute.getDistance(currentGridPoint, itemGridPoint);
			
			if(itemDistance<storedItemDistance)
			{
				storedItemDistance=itemDistance;
				bestJobLine=bestJob.getLine(i);
				bestJobLineIndex=i;
			}
			
		}
		bestJob.addLine(bestJobLine);
		bestJob.removeLine(bestJobLineIndex);
		
		}
		
		return bestJob;

	}

	public String getNextLocationGreedyID(ArrayList<Obstacle> obstacles,Robot robot) throws RouteNotFoundException, PointOutOfBoundaryException
	{

		// create new GridMap
		
		serobot.findroute.GridMap map = new serobot.findroute.GridMap(width, height, obstacles);
		storedSumDistance = 13000;
		for (int i = 0; i < jobsTemp.size(); i++)
		{

			sumDistance = 0;

			// Loop through Job Lines
			for (int j = 0; j < jobsTemp.get(i).getLines().size(); j++)
			{
				// Get Item in this jobList
				Item item = jobsTemp.get(i).getLine(j).getItem();
				int itemX = item.getXPosition();
				int itemY = item.getYPosition();
				//System.out.println("My location is "+itemX+","+itemY);
				GridPoint itemGridPoint = new GridPoint(itemX, itemY);
				GridPoint currentGridPoint = new GridPoint(robot.getXCoord(), robot.getYCoord());
				robot.setCoords(0, 5);
				//System.out.println("My location is "+robot.getXCoord()+","+robot.getYCoord());
				FindRoute findRoute = new FindRoute(map);
				int itemDistance = findRoute.getDistance(currentGridPoint, itemGridPoint);
				sumDistance = sumDistance + itemDistance;
			}

			if (sumDistance < storedSumDistance)
			{

				storedSumDistance = sumDistance;
				bestJob = jobsTemp.get(i);
				bestJobID = bestJob.getID();
			}

		}

		return bestJobID;

	}

	
	public Job getNextBestJob()
	{
		
		/*
		 * Loop through jobs
		 * Get location of Job
		 * Get Reward for that Job
		 * Get fitness function for each job
		 * Function=k*Reward-q*Distance
		 * Return Job with best Fitness Function
		 */
		
		
		
		
		return bestJob;
		
	}

	
	
	
	public void checkLocation(Robot robot)
	{
		
		System.out.println("Current X: "+robot.getXCoord()+"   Current Y:  "+robot.getYCoord());

		
	}
}