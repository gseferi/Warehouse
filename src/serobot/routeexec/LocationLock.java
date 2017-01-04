package serobot.routeexec;

import serobot.findroute.*;

public class LocationLock {
	
	private final RobotControl robot;
	private final GridPoint point;
	
	public LocationLock(RobotControl robot, GridPoint point){
		this.robot = robot;
		this.point = point;
	}
	
	public RobotControl getRobot(){
		return this.robot;
	}
	
	public GridPoint getPoint(){
		return this.point;
	}
	
	public boolean match_robot(RobotControl robot){
		return this.robot == robot;
	}

}
