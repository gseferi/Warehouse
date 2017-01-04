package serobot.routeexec;

import serobot.findroute.GridPoint;

public class InvalidRouteException extends RuntimeException {

	public InvalidRouteException(RobotControl source, GridPoint pt1, GridPoint pt2){
		super(source + " cannot travel from " + pt1 + " to " + pt2 + ".");
	}
}
