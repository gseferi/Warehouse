package serobot.pcinterface;

import java.util.HashMap;
import java.util.List;
import java.util.Observable;

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
 * The model which is based off the PC Interface Data class
 * @author Aaquib Naved
 *
 */
public class PCInterfaceDataModel extends Observable {

	private PCInterfaceData data;
	
	/**
	 * Creates the model based on the data
	 * @param data
	 */
	public PCInterfaceDataModel(PCInterfaceData data) {
		this.data = data;
	}
	
	public GridPoint getDropOffPoint() {
		return data.getDropOffPoint();
	}
	
	public void setDropOffPoint(GridPoint point) {
		data.setDropOffPoint(point);
	}
	
	public Map getMap() {
		return data.getMap();
	}
	
	public void setMap(Map map) {
		data.setMap(map);
	}
	
	/**
	 * Get the selected job
	 * @return The selected job
	 */
	public Job getSelectedJob() {
		return data.getSelectedJob();
	}
	
	/**
	 * Store the job selected in the table by the user
	 * @param job The selected job
	 */
	public void setSelectedJob(Job job) {
		data.setSelectedJob(job);
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Get the item selected in the table by the user
	 * @return The selected item
	 */
	public Item getSelectedItem() {
		return data.getSelectedItem();
	}
	
	/**
	 * Store the item selected in the table by the user
	 * @param item The selected item
	 */
	public void setSelectedItem(Item item) {
		data.setSelectedItem(item);
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Get the name of the robot selected in the table by the user
	 * @return The name of the robot
	 */
	public String getSelectedRobot() {
		return data.getSelectedRobot();
	}
	
	/**
	 * Store the name of the robot selected in the table by the user
	 * @param robotName The name of the robot
	 */
	public void setSelectedRobot(String robotName) {
		data.setSelectedRobot(robotName);
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Get the collection of robots stored in the model
	 * @return The collection of robots
	 */
	public HashMap<String, RobotData> getAllRobots() {
		return data.getAllRobots();
	}
	
	/**
	 * Get the data associated with a robot using its name
	 * @param robotName The namge of the robot
	 * @return The data associated with the robot.
	 */
	public RobotData getRobot(String robotName) {
		return data.getRobot(robotName);
	}
	
	/**
	 * Add a robot to the collection
	 * @param robotName The name of the robot (key)
	 * @param data The data associated with the robot (value)
	 */
	public void addRobot(String robotName, RobotData robotData) {
		data.addRobot(robotName, robotData);
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Reset the robot variables.
	 * This will set the Robot collection to empty and the Robot list to empty.
	 */
	public void resetRobotDatabase() {
		data.resetRobotDatabase();
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Get a list of all the robots
	 * @return List of robots
	 */
	public List<RobotData> getRobotList() {
		return data.getRobotList();
	}
	
	/**
	 * Set the list of robots
	 * @param list The list of robots
	 */
	public void setRobotList(List<RobotData> list) {
		data.setRobotList(list);
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Get the object which represents the search algorithm
	 * @return The search algorithm object
	 */
	public FindRoute getRouteEngine() {
		return data.getRouteEngine();
	}
	
	/**
	 * Set the object which represents the search algorithm
	 * @param route_engine The search algorithm object
	 */
	public void setRouteEngine(FindRoute route_engine) {
		data.setRouteEngine(route_engine);
	}
	
	/**
	 * Get the server which handles multi-robot path finding
	 * @return server
	 */
	public RouteServer getRouteServer() {
		return data.getRouteServer();
	}
	
	/**
	 * Set the server which handles multi-robot path finding
	 * @param route_server The server
	 */
	public void setRouteServer(RouteServer route_server) {
		data.setRouteServer(route_server);
	}
	
	/**
	 * Get all the upcoming jobs
	 * @return A list of upcoming jobs
	 */
	public List<Job> getUpcomingJobs() {
		return data.getUpcomingJobs();
	}
	
	/**
	 * Set the list of upcoming jobs
	 * @param jobs The list of jobs
	 */
	public void setUpcomingJobs(List<Job> jobs) {
		data.setUpcomingJobs(jobs);
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Add a job to the list of upcoming jobs
	 * @param job The job to be added to the list
	 */
	public void addUpcomingJobs(Job job) {
		data.addUpcomingJobs(job);
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Remove a job from the list of upcoming jobs
	 * @param job The job to be removed
	 */
	public void removeUpcomingJobs(Job job) {
		data.removeUpcomingJobs(job);
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Get all the completed jobs
	 * @return A list of completed jobs
	 */
	public List<Job> getCompletedJobs() {
		return data.getCompletedJobs();
	}
	
	/**
	 * Set the list of completed jobs
	 * @param jobs The new list of completed jobs
	 */
	public void setCompletedJobs(List<Job> jobs) {
		data.setCompletedJobs(jobs);
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Add a job to the list of completed jobs
	 * @param job The job to be added
	 */
	public void addCompletedJobs(Job job) {
		data.addCompletedJobs(job);
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Remove a job from the list of completed jobs
	 * @param job The job to be removed
	 */
	public void removeCompletedJobs(String job) {
		data.removeCompletedJobs(job);
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Get all the stored items
	 * @return the list of stored items
	 */
	public List<Item> getItems() {
		return data.getItems();
	}
	
	/**
	 * Set the list of stored items
	 * @param items The new list of items
	 */
	public void setItems(List<Item> items) {
		data.setItems(items);
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Get the errors
	 * @return The errors
	 */
	public List<String> getErrors() {
		return data.getErrors();
	}
	
	/**
	 * Add an error
	 * @param The error to be added
	 */
	public void addErrors(String error) {
		data.addErrors(error);
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Remove an error
	 * @param error The error to be removed
	 */
	public void removeErrors(String error) {
		data.removeErrors(error);
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Get the ongoing jobs
	 * @return A list of ongoing jobs
	 */
	public List<Job> getOngoingJobs() {
		return data.getOngoingJobs();
	}
	
	/**
	 * Set a new list of ongoing jobs
	 * @param jobs The new list of ongoing jobs
	 */
	public void setOngoingJobs(List<Job> jobs) {
		data.setOngoingJobs(jobs);
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Get the warehouse map
	 * @return the warehouse map
	 */
	public GridMap getGridMap() {
		return data.getGridMap();
	}
	
	/**
	 * Store the warehouse map
	 * @param newGridMap The new warehouse map
	 */
	public void setGridMap(GridMap newGridMap) {
		data.setGridMap(newGridMap);
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Get the index of the tab which has been clicked (in the tabbed pane above the job tables)
	 * @return index of the tab
	 */
	public int getTabIndex() {
		return data.getTabIndex();
	}
	
	/**
	 * Set the tab index
	 * @param index The new index
	 */
	public void setTabIndex(int index) {
		data.setTabIndex(index);
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Get the file path of the job.csv file
	 * @return a file path in the form of a String
	 */
	public String getJobFilePath() {
		return data.getJobFilePath();
	}
	
	/**
	 * Store the file path of the job.csv file
	 * @param path The new file path in the form of a string.
	 */
	public void setJobFilePath(String path) {
		data.setJobFilePath(path);
		InterfaceParser.parseFiles(this, data.getJobFilePath(), data.getItemFilePath(), data.getLocationsFilePath());
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Get the file path of the item.csv file
	 * @return a file path in the form of a String
	 */
	public String getItemFilePath() {
		return data.getJobFilePath();
	}
	
	/**
	 * Store the file path of the item.csv file
	 * @param path The new file path in the form of a String
	 */
	public void setItemFilePath(String path) {
		data.setItemFilePath(path);
		InterfaceParser.parseFiles(this, data.getJobFilePath(), data.getItemFilePath(), data.getLocationsFilePath());
		setChanged();
		notifyObservers();
	}
	
	/**
	 * Get the file path of the locations.csv file
	 * @return a file path in the form of a String
	 */
	public String getLocationsFilePath() {
		return data.getLocationsFilePath();
	}
	
	/**
	 * Store the file path of the locations.csv file
	 * @param path The new file path in the form of a String
	 */
	public void setLocationsFilePath(String path) {
		data.setLocationsFilePath(path);
		InterfaceParser.parseFiles(this, data.getJobFilePath(), data.getItemFilePath(), data.getLocationsFilePath());
		setChanged();
		notifyObservers();
	}
	
	public void update() {
		data.update();
		setChanged();
		notifyObservers();
	}
	
	public double getTotalReward() {
		return data.getTotalReward();
	}
	
	public void setTotalReward(double totalReward) {
		data.setTotalReward(totalReward);
	}
}
