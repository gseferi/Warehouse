package serobot.pcinterface;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import lejos.pc.comm.NXTInfo;
import serobot.bluetooth.BluetoothRobotSocket;
import serobot.bluetooth.RobotSocket;
import serobot.bluetooth.Socket;
import serobot.findroute.FindRoute;
import serobot.findroute.GridPoint;
import serobot.findroute.PointOutOfBoundaryException;
import serobot.findroute.RouteNotFoundException;
import serobot.jobparser.Job;
import serobot.routeexec.RobotControl;
import serobot.routeexec.RouteServer;

public class RobotData extends Thread implements RobotControl{
	private int priority;
	private GridPoint location;
	private List<GridPoint> route;
	private FindRoute route_engine;
	private RouteServer route_server;
	private boolean isMoving;
	private boolean isRunning;
	private boolean suspended;
	private String name;
	private static final long sleep_time = 1000;
	private Object route_lock;
	private NXTInfo robot;
	private RobotSocket socket;
	private boolean isConnected;
	private boolean moveCompleted;
	private int direction = UP;
	private boolean finishedRouteIteration = false;
	private Job currentJob;
	private Job jobWithItems;
	
	
	public RobotData(GridPoint location, FindRoute route_engine,RouteServer route_server,String name, NXTInfo robot, boolean isConnected){
		this.location = location;
		this.route_engine = route_engine;
		this.route_server = route_server;
		this.isMoving = true;
		this.isRunning = true;
		this.route = new LinkedList<GridPoint>();
		this.name = name;
		this.route_lock = new Object();
		this.robot = robot;
		this.isConnected = isConnected;
		this.moveCompleted = false;
	}
	
	public Job getCurrentJob() {
		return currentJob;
	}
	
	public void setCurrentJob(Job job) {
		this.currentJob = job;
	}
	
	public Job getJobWithItems() {
		return jobWithItems;
	}
	
	public void setJobWithItems(Job job) {
		this.jobWithItems = job;
	}
	 
	public boolean getFinishedRouteIteration() {
		return finishedRouteIteration;
	}
	
	public void setFinishedRouteIteration(boolean bool) {
		this.finishedRouteIteration = bool;
	}
	
	public void setMoveCompleted(boolean bool) {
		moveCompleted = bool;
	}
	
	public NXTInfo getRobot() {
		return robot;
	}

	public void setRobot(NXTInfo robot) {
		this.robot = robot;
	}

	public RobotSocket getSocket() {
		return socket;
	}

	public void setSocket(RobotSocket s) {
		this.socket = s;
	}

	public boolean isConnected() {
		return isConnected;
	}

	public void setConnected(boolean isConnected) {
		this.isConnected = isConnected;
	}
	
	public int getDirection() {
		return direction;
	}
	
	public void setDirection(int direction) {
		this.direction = direction;
	}
	
	public GridPoint getLocation() {
		return this.location;
	}
	public Optional<GridPoint> getNextLocation() {
		synchronized(route_lock){
		if(this.route.size() != 0){
			return Optional.of(this.route.get(0));
		}else{
			return Optional.empty();
		}
		}
		
	}
	public GridPoint getDestination() {
		synchronized(route_lock){
			return this.route.get(this.route.size()-1);
		}
	}
	public void stop_temp() {
		this.suspended = true;
		this.isMoving = false;
		this.interrupt();
	}
	public void shutdown() {
		this.suspended = false;
		this.isRunning = false;
		this.isMoving = false;
	}
	public void move() {
		this.isMoving = true;
		this.suspended = false;
	}
	public int getRobotPriority() {
		return this.priority;
	}
	public void setRobotPriority(int priority){
		this.priority = priority;
	}
	public void run(){
		while(this.isRunning){
			//pending route
			while(this.route.size() == 0){
				//System.out.println(this.name + " at " + this.location + " has no destination to go.");
				delay(sleep_time);
			}
			
			//moving
			try{
				boolean printed_cannot_go = false;
				while(this.isMoving){
					if(this.route.size() != 0){
						GridPoint next_point;
						synchronized(route_lock){
							next_point = this.route.get(0);
							//System.out.println("RobotData(next point): " + this.route.get(0));
						}
						boolean can_go = this.route_server.goTo(this, this.location, next_point);
						if(can_go){
							this.route_server.depart(this,next_point);
							this.moveCompleted = false;
							
							while (moveCompleted == false) {
								if (this.getFinishedRouteIteration() == false) {
									setFinishedRouteIteration(true);
								}
								delay(100);
							}
							setFinishedRouteIteration(false);
							
							
							this.location = next_point;
							setFinishedRouteIteration(false);
							synchronized(route_lock){
								this.route.remove(0);
							}
							System.out.println("RobotData: " + this.name + " is now at " + this.location);
							printed_cannot_go = false;
						}else{
							if(!printed_cannot_go){
								System.out.println("RobotData: Another robot at " + next_point + ", " + this.name + " at " + this.location + " cannot go.");
								printed_cannot_go = true;
							}
						}
					}else{
						break;
					}
					sleep(sleep_time);
				}
			}catch(InterruptedException e){
			}
			
			//waiting
			while(this.suspended){
				delay(sleep_time);
			}
			
			//end of one loop
			delay(sleep_time);
		}
	}
	private void delay(long time){
		try{
			sleep(time);
		}catch(InterruptedException e){
		}
	}
	public void setDestination(GridPoint destination){
		synchronized(route_lock){
		try {
			this.route = this.route_engine.getRoute(this.location, destination,this.priority);
			System.out.println(this.name + " at " + this.location + ": " + this.route);
		} catch (RouteNotFoundException e) {
			System.out.println(this.name + " cannot find a route to the destination.");
			setDestination(destination);
		} catch (PointOutOfBoundaryException e) {
			System.out.println(this.name + " received a invalid destination.");
		}
		}
	}
	
	public List<GridPoint> getRoute(){
		return this.route;
	}
	
	
	public boolean waiting() {
		return location.getX() == route.get(0).getX() && location.getY() == route.get(0).getY();
	}

	public double getX() {
		return location.getX();
	}

	public double getY() {
		return location.getY();
	}

	public void setX(double x) {
	}

	public void setY(double y) {
	}

	public void setAngle(int a) {
	}

	public void tend() {
		//int x = (int) tendTo(location.getX(), route.get(0).getX(), 0.1);
		//int y = (int) tendTo(location.getY(), route.get(0).getY(), 0.1);
		int x = (int) tendTo(location.getX(), location.getX(), 0.1);
		int y = (int) tendTo(location.getY(), location.getY(), 0.1);
		location = new GridPoint(x, y);
	}

	public int getAngle() {
		return 0;
	}

	private double tendTo(double at, double target, double amount) {
		if (at < target) {
			at += amount * (target - at);
		} else if (at > target) {
			at -= amount * (at - target);
		}
		DecimalFormat df = new DecimalFormat("#.###");
		double toReturn = Double.valueOf(df.format(at));
		return toReturn;
	}

	private int tendTo(int at, int target, int amount) {
		if (at < target) {
			at += amount;
		} else if (at > target) {
			at -= amount;
		}
		return at;
	}
	
}
