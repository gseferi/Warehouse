package serobot.routeexec;

public class DepartFailureException extends RuntimeException {
	
	public DepartFailureException(RobotControl robot){
		super(robot + " departure failure. Cannot find location lock. Please notify developer.");
	}

}
