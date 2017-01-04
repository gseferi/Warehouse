package serobot.pcinterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lejos.pc.comm.NXTInfo;
import serobot.bluetooth.BluetoothRobotSocket;
import serobot.findroute.FindRoute;
import serobot.findroute.GridMap;
import serobot.findroute.GridPoint;
import serobot.jobparser.Item;
import serobot.jobparser.Job;
import serobot.map.Map;
import serobot.routeexec.RouteServer;

/**
 * The data class which holds all the information about the PC Interface
 * @author Aaquib Naved
 *
 */
public class PCInterfaceData {
	private HashMap<String, RobotData> robots;
	private List<RobotData> robotList;
	private FindRoute route_engine;
	private RouteServer route_server;
	private List<Item> items;
 	private List<Job> upcomingJobs;
	private List<Job> completedJobs;
	private List<Job> ongoingJobs; 
	private List<String> errors;
	private GridMap gridMap;
	private Map guiMap;
	private int tabIndex;
	private Job selectedJob;
	private Item selectedItem;
	private String selectedRobot;
	private String jobFilePath;
	private String itemFilePath;
	private String locationsFilePath;
	private boolean update = false;
	private double totalReward = 0;
	private GridPoint dropOffPoint;
	
	/**
	 * Creates the data object.
	 * @param jobs The list of jobs which are parsed from the csv files
	 * @param items The list of items which are parsed from the csv files
	 * @param gridMap The map of the warehouse
	 * @param route_engine The search algorithm to find the path between two locations
	 * @param route_server The server which handles Multi Robot path finding.
	 */
	public PCInterfaceData(List<Job> jobs, List<Item> items, GridMap gridMap, FindRoute route_engine, RouteServer route_server, GridPoint dropOffPoint) {
		this.robots = new HashMap<String, RobotData>();
		this.route_engine = route_engine;
		this.route_server = route_server;
		this.gridMap = gridMap;
		this.upcomingJobs = jobs;
		this.completedJobs = new ArrayList<Job>();
		this.ongoingJobs = new ArrayList<Job>();
		this.items = items;
		this.errors = new ArrayList<String>();
		this.tabIndex = 0;
		this.dropOffPoint = dropOffPoint;
	}
	
	public GridPoint getDropOffPoint() {
		return dropOffPoint;
	}
	
	public void setDropOffPoint(GridPoint point) {
		this.dropOffPoint = point;
	}
	
	public Map getMap() {
		return guiMap;
	}
	
	public void setMap(Map map) {
		this.guiMap = map;
	}
	
	/**
	 * Get the job selected in the table by the user
	 * @return The selected job
	 */
	public Job getSelectedJob() {
		return selectedJob;
	}
	
	/**
	 * Store the job selected in the table by the user
	 * @param job The selected job
	 */
	public void setSelectedJob(Job job) {
		this.selectedJob = job;
	}
	
	/**
	 * Get the item selected in the table by the user
	 * @return The selected item
	 */
	public Item getSelectedItem() {
		return selectedItem;
	}
	
	/**
	 * Store the item selected in the table by the user
	 * @param item The selected item
	 */
	public void setSelectedItem(Item item) {
		this.selectedItem = item;
	}
	
	/**
	 * Get the name of the robot selected in the table by the user
	 * @return The name of the robot
	 */
	public String getSelectedRobot() {
		return selectedRobot;
	}
	
	/**
	 * Store the name of the robot selected in the table by the user
	 * @param robotName The name of the robot
	 */
	public void setSelectedRobot(String robotName) {
		this.selectedRobot = robotName;
	}
	
	/**
	 * Get the collection of robots stored in the model
	 * @return The collection of robots
	 */
	public HashMap<String, RobotData> getAllRobots() {
		return robots;
	}
	
	/**
	 * Get the data associated with a robot using its name
	 * @param robotName The namge of the robot
	 * @return The data associated with the robot.
	 */
	public RobotData getRobot(String robotName) {
		return robots.get(robotName);
	}
	
	/**
	 * Add a robot to the collection
	 * @param robotName The name of the robot (key)
	 * @param data The data associated with the robot (value)
	 */
	public void addRobot(String robotName, RobotData data) {
		robots.put(robotName, data);
	}
	
	/**
	 * Reset the robot variables.
	 * This will set the Robot collection to empty and the Robot list to empty.
	 */
	public void resetRobotDatabase() {
		robots = new HashMap<String, RobotData>();
	}
	
	/**
	 * Get a list of all the robots
	 * @return List of robots
	 */
	public List<RobotData> getRobotList() {
		return this.robotList;
	}
	
	/**
	 * Set the list of robots
	 * @param list The list of robots
	 */
	public void setRobotList(List<RobotData> list) {
		this.robotList = list;
	}
	
	/**
	 * Get the object which represents the search algorithm
	 * @return The search algorithm object
	 */
	public FindRoute getRouteEngine() {
		return route_engine;
	}
	
	/**
	 * Set the object which represents the search algorithm
	 * @param route_engine The search algorithm object
	 */
	public void setRouteEngine(FindRoute route_engine) {
		this.route_engine = route_engine;
	}
	
	/**
	 * Get the server which handles multi-robot path finding
	 * @return server
	 */
	public RouteServer getRouteServer() {
		return route_server;
	}
	
	/**
	 * Set the server which handles multi-robot path finding
	 * @param route_server The server
	 */
	public void setRouteServer(RouteServer route_server) {
		this.route_server = route_server;
	}
	
	/**
	 * Get all the upcoming jobs
	 * @return A list of upcoming jobs
	 */
	public List<Job> getUpcomingJobs() {
		return upcomingJobs;
	}
	
	/**
	 * Set the list of upcoming jobs
	 * @param jobs The list of jobs
	 */
	public void setUpcomingJobs(List<Job> jobs) {
		upcomingJobs = jobs;
	}
	
	/**
	 * Add a job to 
			delay(500);the list of upcoming jobs
	 * @param job The job to be added to the list
	 */
	public void addUpcomingJobs(Job job) {
		upcomingJobs.add(job);
	}
	
	/**
	 * Remove a job from the list of upcoming jobs
	 * @param job The job to be removed
	 */
	public void removeUpcomingJobs(Job job) {
		upcomingJobs.remove(job);
	}
	
	/**
	 * Get all the completed jobs
	 * @return A list of completed jobs
	 */
	public List<Job> getCompletedJobs() {
		return completedJobs;
	}
	
	/**
	 * Set the list of completed jobs
	 * @param jobs The new list of completed jobs
	 */
	public void setCompletedJobs(List<Job> jobs) {
		completedJobs = jobs;
	}
	
	/**
	 * Add a job to the list of completed jobs
	 * @param job The job to be added
	 */
	public void addCompletedJobs(Job job) {
		completedJobs.add(job);
	}
	
	/**
			delay(500);
	 * Remove a job from the list of completed jobs
	 * @param job The job to be removed
	 */
	public void removeCompletedJobs(String job) {
		completedJobs.remove(job);
	}
	
	/**
	 * Get all the stored items
	 * @return the list of stored items
	 */
	public List<Item> getItems() {
		return items;
	}
	
	/**
	 * Set the list of stored items
	 * @param items The new list of items
	 */
	public void setItems(List<Item> items) {
		this.items = items;
	}

	/**
	 * Get the errors
	 * @return The errors
	 */
	public List<String> getErrors() {
		return errors;
	}
	
	/**
	 * Add an error
	 * @param The error to be added
	 */
	public void addErrors(String error) {
		errors.add(error);
	}
	
	/**
	 * Remove an error
	 * @param error The error to be removed
	 */
	public void removeErrors(String error) {
		errors.remove(error);
	}
	
	/**
	 * Get the ongoing jobs
	 * @return A list of ongoing jobs
	 */
	public List<Job> getOngoingJobs() {
		return ongoingJobs;
	}
	
	/**
	 * Set a new list of ongoing jobs
	 * @param jobs The new list of ongoing jobs
	 */
	public void setOngoingJobs(List<Job> jobs) {
		ongoingJobs = jobs;
	}
	
	/**
	 * Get the warehouse map
	 * @return the warehouse map
	 */
	public GridMap getGridMap() {
		return gridMap;
	}
	
	/**
	 * Store the warehouse map
	 * @param newGridMap The new warehouse map
	 */
	public void setGridMap(GridMap newGridMap) {
		gridMap = newGridMap;
	}
	
	/**
	 * Get the index of the tab which has been clicked (in the tabbed pane above the job tables)
	 * @return index of the tab
	 */
	public int getTabIndex() {
		return tabIndex;
	}
	
	/**
	 * Set the tab 
			delay(500);index
	 * @param index The new index
	 */
	public void setTabIndex(int index) {
		tabIndex = index;
	}
	
	/**
	 * Get the file path of the job.csv file
	 * @return a file path in the form of a String
	 */
	public String getJobFilePath() {
		return jobFilePath;
	}
	
	/**
	 * Store the file path of the job.csv file
	 * @param path The new file path in the form of a string.
	 */
	public void setJobFilePath(String path) {
		jobFilePath = path;
	}
	
	/**
	 * Get the file path of the item.csv file
	 * @return a file path in the form of a String
	 */
	public String getItemFilePath() {
		return itemFilePath;
	}
	
	/**
	 * Store the file path of the item.csv file
	 * @param path The new file path in the form of a String
	 */
	public void setItemFilePath(String path) {
		itemFilePath = path;
	}
	
	/**
	 * Get the file path of the locations.csv file
	 * @return a file path in the form of a String
	 */
	public String getLocationsFilePath() {
		return locationsFilePath;
	}
	
	/**
	 * Store the file path of the locations.csv file
	 * @param path The new file path in the form of a String
	 */
	public void setLocationsFilePath(String path) {
		locationsFilePath = path;
	}
	
	public void update() {
		this.update = !update;
	}
	
	public double getTotalReward() {
		return totalReward;
	}
	
	public void setTotalReward(double totalReward) {
		this.totalReward = totalReward;
	}
}
