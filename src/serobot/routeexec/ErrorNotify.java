package serobot.routeexec;

/**
 * This will be notified when problem detected. The methods implemented should be non-blocking.
 * @author kh_tsang
 *
 */
public interface ErrorNotify {
	
	//error id
	public static final int collision = 1;
	
	/**
	 * This method will be called when there is an error on the route server
	 * @param robot The Robot Controller
	 * @param error The error code, refer to the public static final contants
	 */
	public void notifyError(RobotControl robot, int error);

}
