package serobot.findroute;

/**
 * This kind of exception is thrown when route not found.
 * @author TSANG KWONG HEI
 *
 */
public class RouteNotFoundException extends Exception{

	public RouteNotFoundException(GridPoint initial_point, GridPoint destination_point){
		super("Destination unreacheable from " + initial_point + " to " + destination_point + ".");
	}
}
