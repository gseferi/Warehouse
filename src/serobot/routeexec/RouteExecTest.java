package serobot.routeexec;

import java.util.*;

import serobot.findroute.*;

public class RouteExecTest {

	private static RobotTest robot_A;
	private static RobotTest robot_B;
	private static RobotTest robot_C;
	private static RouteServer route_server;
	private static ErrorNotifyTest error_notify;
	
	public static void main(String[] args) {
		List<Obstacle> obstacles = new ArrayList<Obstacle>();
		GridMap map = new GridMap(10,8,obstacles);
		FindRoute route_engine = new FindRoute(map);
		route_server = new RouteServer(route_engine);
		error_notify = new ErrorNotifyTest();
		route_server.addErrorNotify(error_notify);
		
		robot_A = new RobotTest(new GridPoint(0,0),route_engine,route_server, "Robot A");
		robot_B = new RobotTest(new GridPoint(3,3),route_engine,route_server, "Robot B");
		robot_C = new RobotTest(new GridPoint(6,4),route_engine,route_server, "Robot C");
		robot_A.setRobotPriority(20);
		robot_B.setRobotPriority(30);
		robot_C.setRobotPriority(40);
		robot_A.start();
		robot_B.start();
		robot_C.start();
		try {
			route_server.addRobot(robot_A);
			route_server.addRobot(robot_B);
			route_server.addRobot(robot_C);
		} catch (CannotAddRobotException e) {
		}
		route_server.start();
		
		delay(2000);
		robot_A.setDestination(new GridPoint(3,3));
		
		delay(15000);
		robot_B.setDestination(new GridPoint(1,0));
		
		delay(15000);
		robot_C.setDestination(new GridPoint(2,4));
		robot_A.setDestination(new GridPoint(3,4));
		
		delay(2000);
		robot_A.setDestination(new GridPoint(5,4));
		
		delay(30000);
		robot_C.shutdown();
		robot_A.shutdown();
		robot_B.shutdown();
	}
	
	public static RobotTest getRobotA(){
		return robot_A;
	}
	
	public static RobotTest getRobotB(){
		return robot_B;
	}
	
	public static RobotTest getRobotC(){
		return robot_C;
	}
	
	public static ErrorNotifyTest getErrorNotifyTest(){
		return error_notify;
	}
	
	public static class RobotTest extends Thread implements RobotControl{
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
		public RobotTest(GridPoint location, FindRoute route_engine,RouteServer route_server,String name){
			this.location = location;
			this.route_engine = route_engine;
			this.route_server = route_server;
			this.isMoving = true;
			this.isRunning = true;
			this.route = new LinkedList<GridPoint>();
			this.name = name;
			this.route_lock = new Object();
		}
		public int getDirection() {
			return 0;
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
							}
							boolean can_go = this.route_server.goTo(this, this.location, next_point);
							if(can_go){
								this.route_server.depart(this,next_point);
								delay(sleep_time);
								this.location = next_point;
								synchronized(route_lock){
									this.route.remove(0);
								}
								System.out.println(this.name + " is now at " + this.location);
								printed_cannot_go = false;
							}else{
								if(!printed_cannot_go){
									System.out.println("Another robot at " + next_point + ", " + this.name + " at " + this.location + " cannot go.");
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
	}
	
	public static class ErrorNotifyTest implements ErrorNotify{
		private int called_count;
		public ErrorNotifyTest(){
			this.called_count = 0;
		}
		@Override
		public synchronized void notifyError(RobotControl robot, int error) {
			this.called_count++;
			List<GridPoint> route = ((RobotTest)robot).getRoute();
			((RobotTest)robot).setDestination(route.get(route.size()-1));
		}
		public synchronized int getCount(){
			return this.called_count;
		}
	}

	private static void delay(long time){
		try{
			Thread.sleep(time);
		}catch(InterruptedException e){
		}
	}
}
