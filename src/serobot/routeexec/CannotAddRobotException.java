package serobot.routeexec;

/**
 * This kind of exception is thrown when attempting to add a robot when the route server is running..
 * @author TSANG KWONG HEI
 *
 */
public class CannotAddRobotException extends Exception {
	
	public CannotAddRobotException(){
		super("Cannot add robot necause the route server is running.");
	}

}
