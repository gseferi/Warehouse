package serobot.routeexec;

import java.util.*;
import serobot.findroute.*;

/**
 * The thread controlling the route of the robots
 * @author TSANG KWONG HEI
 *
 */
public class RouteServer extends Thread {
	
	//constants
	public static final long sleep_time = 100;
	
	//attributes
	private List<RobotControl> robots;
	private boolean running;
	private Object running_status_lock;
	private List<ErrorNotify> error_notify;
	private List<LocationLock> locks;
	private Object location_locks_lock;
	private FindRoute route_engine;
	
	/**
	 * Construct the RouteServer
	 * @param route_engine The FindRoute object being used
	 */
	public RouteServer(FindRoute route_engine){
		this.robots = new ArrayList<RobotControl>();
		this.running = false;
		this.running_status_lock = new Object();
		this.error_notify = new ArrayList<ErrorNotify>();
		this.locks = new ArrayList<LocationLock>();
		this.location_locks_lock = new Object();
		this.route_engine = route_engine;
	}
	
	/**
	 * Add a robot to be processed.
	 * @param robot The robot to be processed.
	 * @throws CannotAddRobotException 
	 */
	public void addRobot(RobotControl robot) throws CannotAddRobotException{
		synchronized(this.running_status_lock){
			if(!this.running){
				this.robots.add(robot);
				depart(robot,robot.getLocation());
			}else{
				throw new CannotAddRobotException();
			}
		}
	}
	
	public void run(){
		
		//initialize the runing status
		synchronized(this.running_status_lock){
			this.running = true;
		}
		
		//checking the route
		while(this.running){
			
			//Check next locations
			for(int i = 0; i < this.robots.size()-1; i++){
				for(int j = i+1; j < this.robots.size(); j++){
					GridPoint i_location = this.robots.get(i).getLocation();
					Optional<GridPoint> i_next_location = this.robots.get(i).getNextLocation();
					GridPoint j_location = this.robots.get(j).getLocation();
					Optional<GridPoint> j_next_location = this.robots.get(j).getNextLocation();
					if(i_next_location.isPresent() && j_next_location.isPresent() && i_location.samePoint(j_next_location.get()) && j_location.samePoint(i_next_location.get())){
						errorNotify(this.robots.get(i),ErrorNotify.collision);
						errorNotify(this.robots.get(j),ErrorNotify.collision);
					}
				}
			}
			
			//End of a task
			delay(sleep_time);
		}
		
		//Stop the robots
		stopRobots();
		
		synchronized(this.running_status_lock){
			this.running = false;
		}
	}
	
	/**
	 * To stop this server and the robots.
	 */
	public void stopServer(){
		this.running = false;
	}
	
	private void delay(long time){
		try{
			sleep(time);
		}catch(InterruptedException e){
		}
	}
	
	private void stopRobots(){
		for(RobotControl robot : this.robots){
			robot.shutdown();
		}
	}
	
	/**
	 * Add an ErrorNotify object to be notified when this route server gets problem.
	 * @param to_be_notified The ErrorNotify object to be added
	 */
	public void addErrorNotify(ErrorNotify to_be_notified){
		this.error_notify.add(to_be_notified);
	}
	
	/**
	 * Remove an ErrorNotify object to be notified when this route server gets problem.
	 * @param to_be_notified The ErrorNotify object to be removed
	 */
	public void removeErrorNotify(ErrorNotify to_be_notified){
		this.error_notify.remove(to_be_notified);
	}
	
	private void errorNotify(RobotControl robot, int error_code){
		for(ErrorNotify to_be_notified : error_notify){
			to_be_notified.notifyError(robot,error_code);
		}
	}
	
	/**
	 * Robot departure
	 * @param source The robot
	 * @param pt The point the robot is going to
	 */
	public void depart(RobotControl source, GridPoint pt){
		
		//apply time step
		List<GridPoint> route = source.getRoute();
		TimeStep step = this.route_engine.getTimeStep();
		synchronized(step.getLock()){
			for(int i = 0; i < route.size(); i++){
				step.addReservation(i+1, route.get(i), source.getRobotPriority());
			}
		}
		
		synchronized(this.location_locks_lock){
			int index = -1;
			for(int i = 0; i < this.locks.size(); i++){
				if(this.locks.get(i).match_robot(source)){
					index = i;
					break;
				}
			}
			if(index != -1){
				GridPoint original_location = this.locks.get(index).getPoint();
				this.locks.remove(index);
				
				//release priority lock
				int old_x = original_location.getX();
				int old_y = original_location.getY();
				List<Optional<GridLink>> links_to_release_lock = new ArrayList<Optional<GridLink>>(4);
				//links to release priority
				links_to_release_lock.add(getLink(new GridPoint(old_x-1,old_y),new GridPoint(old_x,old_y)));
				links_to_release_lock.add(getLink(new GridPoint(old_x+1,old_y),new GridPoint(old_x,old_y)));
				links_to_release_lock.add(getLink(new GridPoint(old_x,old_y-1),new GridPoint(old_x,old_y)));
				links_to_release_lock.add(getLink(new GridPoint(old_x,old_y+1),new GridPoint(old_x,old_y)));
				for(Optional<GridLink> link : links_to_release_lock){
					if(link.isPresent()){
						//System.out.println(link.get().getPt1() + "-" + link.get().getPt2() + " released.");
						link.get().disablePriority();
					}
				}
			}
			
			this.locks.add(new LocationLock(source,pt));
			
			//Add priority lock
			int new_x = pt.getX();
			int new_y = pt.getY();
			List<Optional<GridLink>> links_to_apply_priority = new ArrayList<Optional<GridLink>>(4);
			links_to_apply_priority.add(getLink(new GridPoint(new_x-1,new_y),new GridPoint(new_x,new_y)));
			links_to_apply_priority.add(getLink(new GridPoint(new_x+1,new_y),new GridPoint(new_x,new_y)));
			links_to_apply_priority.add(getLink(new GridPoint(new_x,new_y-1),new GridPoint(new_x,new_y)));
			links_to_apply_priority.add(getLink(new GridPoint(new_x,new_y+1),new GridPoint(new_x,new_y)));
			for(Optional<GridLink> link : links_to_apply_priority){
				if(link.isPresent()){
					//System.out.println(link.get().getPt1() + "-" + link.get().getPt2() + " locked.");
					link.get().setOccupiedPriority(source.getRobotPriority());
				}
			}
		}
	}
	
	/**
	 * Ask for going to a point
	 * @param source The robot.
	 * @param pt1 The initial point.
	 * @param pt2 The next point.
	 * @return whether authorized to go
	 */
	public boolean goTo(RobotControl source, GridPoint pt1, GridPoint pt2){
		if(!(pt1.getX() == pt2.getX() || pt1.getY() == pt2.getY())){
			throw new InvalidRouteException(source, pt1,pt2);
		}
		
		synchronized(this.location_locks_lock){
			for(LocationLock lock : this.locks){
				if(lock.getPoint().samePoint(pt2)){
					return false;
				}
			}
		}
		return true;
	}
	
	private Optional<GridLink> getLink(GridPoint pt1, GridPoint pt2){
		for(GridLink link : this.route_engine.getLinks()){
			if(link.getPt1().samePoint(pt1) && link.getPt2().samePoint(pt2)){
				return Optional.of(link);
			}else if(link.getPt2().samePoint(pt1) && link.getPt1().samePoint(pt2)){
				return Optional.of(link);
			}
		}
		return Optional.empty();
	}
	
	/*
	 * Arrive at a point
	 * @param source The robot
	 * @param pt The point arrived
	 */
	/*public void arrive(RobotControl source, GridPoint pt){
		synchronized(this.location_locks_lock){
			this.locks.add(new LocationLock(source,pt));
		}
	}*/
}
