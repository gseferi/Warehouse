package serobot.findroute;

import java.util.*;

/**
 * Find the route to the target
 * 
 * @author TSANG KWONG HEI
 *
 */
public class FindRoute
{

	private List<GridLink> links;
	private List<GridPoint> points;
	private TimeStep step;

	/**
	 * Construct the FindRoute Object.
	 * 
	 * @param links
	 *            The links between GridPoints.
	 */
	public FindRoute(List<GridLink> links, List<GridPoint> points)
	{
		this.links = links;
		this.points = points;
		this.step = new TimeStep(30,2000);
		this.step.setDaemon(true);
		this.step.start();
	}

	/**
	 * Construct the FindRoute object with a specified map
	 * 
	 * @param map
	 *            The map.
	 */
	public FindRoute(GridMap map)
	{
		this.links = map.getLinks();
		this.points = map.getPoints();
		this.step = new TimeStep(30,2000);
		this.step.setDaemon(true);
		this.step.start();
	}
	
	/**
	 * Construct the FindRoute object with a specified map
	 * 
	 * @param map
	 *            The map.
	 * @param sizeoftimestep Number of time unit to be stored.
	 * @param unittime The actual time for one time unit in milliseconds
	 */
	public FindRoute(GridMap map, int sizeoftimestep, long unittime)
	{
		this.links = map.getLinks();
		this.points = map.getPoints();
		this.step = new TimeStep(sizeoftimestep,unittime);
		this.step.setDaemon(true);
		this.step.start();
	}

	/**
	 * TO find the route
	 * 
	 * @param x1
	 *            x1
	 * @param y1
	 *            y1
	 * @param x2
	 *            x2
	 * @param y2
	 *            y2
	 * @return The route
	 * @throws RouteNotFoundException
	 * @throws PointOutOfBoundaryException
	 */
	public List<GridPoint> getRoute(int x1, int y1, int x2, int y2)
			throws RouteNotFoundException, PointOutOfBoundaryException
	{
		return getRoute(new GridPoint(x1, y1), new GridPoint(x2, y2));
	}

	/**
	 * TO find the route
	 * 
	 * @param x1
	 *            x1
	 * @param y1
	 *            y1
	 * @param x2
	 *            x2
	 * @param y2
	 *            y2
	 * @param priority
	 *            The priority of the robot.
	 * @return The route
	 * @throws RouteNotFoundException
	 * @throws PointOutOfBoundaryException
	 */
	public List<GridPoint> getRoute(int x1, int y1, int x2, int y2, int priority)
			throws RouteNotFoundException, PointOutOfBoundaryException
	{
		return getRoute(new GridPoint(x1, y1), new GridPoint(x2, y2), priority);
	}

	/**
	 * To find the route. Please use of GridPoint object as in the lists of
	 * GridLink objects.
	 * 
	 * @param initial_pt
	 *            The current point.
	 * @param destination_pt
	 *            The destination point.
	 * @return The route to destination.
	 * @throws RouteNotFoundException
	 *             It is thrown when the destination is unreachable.
	 * @throws PointOutOfBoundaryException
	 *             When point is out of boundary.
	 */
	public List<GridPoint> getRoute(GridPoint initial_pt, GridPoint destination_pt)
			throws RouteNotFoundException, PointOutOfBoundaryException
	{
		return getRoute(initial_pt, destination_pt, 10);
	}
	
	/**
	 * To find the route. Please use of GridPoint object as in the lists of
	 * GridLink objects.
	 * 
	 * @param initial_pt
	 *            The current point.
	 * @param destination_pt
	 *            The destination point.
	 * @param priority
	 *            The priority of the robot, lower value has higher priority.
	 * @return The route to destination.
	 * @throws RouteNotFoundException
	 *             It is thrown when the destination is unreachable.
	 * @throws PointOutOfBoundaryException
	 *             When point is out of boundary.
	 */
	public List<GridPoint> getRoute(GridPoint initial_pt, GridPoint destination_pt, int priority)
			throws RouteNotFoundException, PointOutOfBoundaryException{
		synchronized(this.step.getLock()){
			return getRouteOperation(initial_pt, destination_pt, priority);
		}
	}

	private List<GridPoint> getRouteOperation(GridPoint initial_pt, GridPoint destination_pt, int priority)
			throws RouteNotFoundException, PointOutOfBoundaryException
	{
		// check start
		boolean valid_start = false;
		boolean valid_end = false;
		for (GridPoint pt : this.points)
		{
			valid_start |= pt.samePoint(initial_pt);
			valid_end |= pt.samePoint(destination_pt);

		}
		if (valid_start == false)
		{
			//System.out.println("\n" + "Not valid Start");

		}
		if (valid_end == false)
		{
			//System.out.println("Not valid end");
		}

		if (!(valid_start && valid_end))
		{
			throw new PointOutOfBoundaryException();
		}

		Frontier current_pt = new Frontier(0, null, initial_pt, destination_pt,0);
		// List<Frontier> route = new ArrayList<Frontier>();
		// route.add(current_pt);
		ExpandNode expand_engine = new ExpandNode(this.links,this.step);
		List<GridPoint> explored = new ArrayList<GridPoint>();
		List<Frontier> frontiers = new ArrayList<Frontier>();
		// for final use after search
		Frontier status = null;

		boolean searching = true;
		while (searching)
		{
			frontiers = expand_engine.expand(frontiers, current_pt, destination_pt, priority);
			Frontier nextPoint = null;
			int total_cost = 0;
			for (Frontier frontier : frontiers)
			{
				if (!explored.contains(frontier.getGridPoint()))
				{
					if (nextPoint == null)
					{
						nextPoint = frontier;
						total_cost = nextPoint.getCost().totalCost();
					} else
					{
						int total_cost_of_a_point = frontier.getCost().totalCost();
						if (total_cost_of_a_point < total_cost)
						{
							nextPoint = frontier;
							total_cost = total_cost_of_a_point;
						}
					}
				}
			}

			explored.add(current_pt.getGridPoint());

			if (nextPoint == null)
			{
				/*
				 * route.remove(current_pt); //System.out.println("Removed: " +
				 * current_pt); if(route.size() != 0){ current_pt =
				 * route.get(route.size()-1); } if(route.size() == 0){ throw new
				 * RouteNotFoundException(initial_pt, destination_pt); }
				 */
				throw new RouteNotFoundException(initial_pt, destination_pt);
			} else
			{
				// route.add(nextPoint);
				// System.out.println("Added: " + nextPoint.getGridPoint());
				current_pt = nextPoint;
				// Check if this is the goal
				if (nextPoint.isGoal())
				{
					searching = false;
					status = nextPoint;
				}
			}
		}
		// route.remove(0);

		List<GridPoint> result = new LinkedList<GridPoint>();
		while (status.getParent() != null)
		{
			result.add(0, status.getGridPoint());
			status = status.getParent();
		}

		return result;
	}

	/**
	 * Get the distance need to be traveled.
	 * 
	 * @param initial_pt
	 *            The current point.
	 * @param destination_pt
	 *            The destination point.
	 * @return The distance need to be traveled.
	 * @throws RouteNotFoundException
	 *             When route is not found.
	 * @throws PointOutOfBoundaryException
	 *             When point is out of boundary.
	 */
	public int getDistance(GridPoint initial_pt, GridPoint destination_pt)
			throws RouteNotFoundException, PointOutOfBoundaryException
	{
		return getRoute(initial_pt, destination_pt).size();
	}
	
	/**
	 * Get the distance need to be traveled.
	 * 
	 * @param initial_pt
	 *            The current point.
	 * @param destination_pt
	 *            The destination point.
	 * @param priority The priority of the robot.
	 * @return The distance need to be traveled.
	 * @throws RouteNotFoundException
	 *             When route is not found.
	 * @throws PointOutOfBoundaryException
	 *             When point is out of boundary.
	 */
	public int getDistance(GridPoint initial_pt, GridPoint destination_pt, int priority)
			throws RouteNotFoundException, PointOutOfBoundaryException
	{
		return getRoute(initial_pt, destination_pt,priority).size();
	}

	public List<GridLink> getLinks()
	{
		return this.links;
	}

	public static void main(String[] args) throws RouteNotFoundException, PointOutOfBoundaryException
	{

		// Test map
		List<GridPoint> points = new ArrayList<GridPoint>(9);
		List<GridLink> links = new ArrayList<GridLink>();
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
			{
				points.add(new GridPoint(i, j));
			}
		links.add(new GridLink(points.get(0), points.get(1)));
		links.add(new GridLink(points.get(1), points.get(2)));
		links.add(new GridLink(points.get(3), points.get(4)));
		links.add(new GridLink(points.get(4), points.get(5)));
		links.add(new GridLink(points.get(6), points.get(7)));
		links.add(new GridLink(points.get(7), points.get(8)));
		links.add(new GridLink(points.get(0), points.get(3)));
		links.add(new GridLink(points.get(3), points.get(6)));
		links.add(new GridLink(points.get(1), points.get(4)));
		links.add(new GridLink(points.get(4), points.get(7)));
		links.add(new GridLink(points.get(2), points.get(5)));
		// links.add(new GridLink(points.get(5),points.get(8)));

		// Test route finding
		FindRoute find_route = new FindRoute(links, points);
		int point1 = 0;
		int point2 = 8;
		System.out.println("Finding route from " + points.get(point1) + " to " + points.get(point2) + ".");
		List<GridPoint> route = find_route.getRoute(points.get(point1), points.get(point2));
		System.out.println("Distance travelled: " + find_route.getDistance(points.get(point1), points.get(point2)));
		System.out.println(route);

		//sleep
		try{
			Thread.sleep(2000);
		}catch(InterruptedException e){
		}
		
		//Test apply reservation
		System.out.println("Testing reservations");
		synchronized(find_route.getTimeStep().getLock()){
			for(int i = 0; i < route.size(); i++){
				find_route.getTimeStep().addReservation(i+1, route.get(i), 10);
			}
			System.out.println(find_route.getTimeStep().getReservations());
			System.out.println(find_route.getTimeStep().getReservationOfTime(0));
		}
		
		//sleep
		try{
			Thread.sleep(2000);
		}catch(InterruptedException e){
		}
		
		//sleep print reservations again
		synchronized(find_route.getTimeStep().getLock()){
			System.out.println(find_route.getTimeStep().getReservations());
			System.out.println(find_route.getTimeStep().getReservationOfTime(0));
		}
		
		//sleep
		try{
			Thread.sleep(2000);
		}catch(InterruptedException e){
		}
				
		//sleep print reservations again
		synchronized(find_route.getTimeStep().getLock()){
			System.out.println(find_route.getTimeStep().getReservations());
			System.out.println(find_route.getTimeStep().getReservationOfTime(0));
		}
		
		//Test effect
		System.out.println("Testing effect of time step");
		System.out.println(find_route.getRoute(1,1,2,1));
		System.out.println(find_route.getRoute(1,1,2,1,5));
	}
	
	public TimeStep getTimeStep(){
		return this.step;
	}
	
	/**
	 * Clear all the time steps reservations
	 */
	public void clearTimeSteps(){
		this.step.clearTimeSteps();
		for(GridLink links : links){
			links.disablePriority();
		}
	}

}
