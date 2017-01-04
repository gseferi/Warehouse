package serobot.routeexec;

import java.util.*;

import serobot.findroute.*;

/**
 * Abstract class for the robot control.
 * NOTE: Use same instances for the same point.
 * @author TSANG KWONG HEI
 *
 */
public interface RobotControl {
	
	//constants
	public static final int UP = 1;
	public static final int DOWN = 2;
	public static final int LEFT = 3;
	public static final int RIGHT = 4;
	
	//attributes
	
	//methods
	
	/**
	 * Get the direction of the robot which is facing.
	 * UP for upwards.
	 * DOWN for downwards.
	 * LEFT for facing left.
	 * RIGHT for facing right.
	 * @return The direction of the robot.
	 */
	public int getDirection();
	
	/**
	 * Get the location of the robot.
	 * NOTE: Use same instances for the same point as in the find route part.
	 * @return The location of the robot.
	 */
	public GridPoint getLocation();
	
	/**
	 * Get the next location of the robot.
	 * NOTE: Use same instances for the same point as in the find route part.
	 * @return The location of the robot.
	 */
	public Optional<GridPoint> getNextLocation();
	
	/**
	 * Get the final destination of the robot.
	 * @return The final destination of the robot.
	 */
	public GridPoint getDestination();
	
	/**
	 * Stop the robot temporary.
	 */
	public void stop_temp();
	
	/**
	 * Stop the robot permanently. e.g. for shutting down the system and also stopping the route server.
	 */
	public void shutdown();
	
	/**
	 * To resume moving of the robot.
	 */
	public void move();
	
	/**
	 * Get the priority of the robot. The lower value has a higher priority.
	 * @return The priority
	 */
	public int getRobotPriority();

	/**
	 * Get route
	 * @return Get the route holding by the robot.
	 */
	public List<GridPoint> getRoute();
}
