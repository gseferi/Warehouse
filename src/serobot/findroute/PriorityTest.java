package serobot.findroute;

import java.util.*;

public class PriorityTest {

	public static void main(String[] args) throws RouteNotFoundException, PointOutOfBoundaryException {
		List<Obstacle> obstacles = new ArrayList<Obstacle>();
		GridMap map = new GridMap(8,9,obstacles);
		
		//One robot with priority 10 at (3,8)
		map.getLink(new GridPoint(3,8), new GridPoint(3,7)).get().setOccupiedPriority(10);
		map.getLink(new GridPoint(2,8), new GridPoint(3,8)).get().setOccupiedPriority(10);
		map.getLink(new GridPoint(3,8), new GridPoint(4,8)).get().setOccupiedPriority(10);
		
		//One robot with priority 20 at (3,0)
		map.getLink(new GridPoint(3,0), new GridPoint(3,1)).get().setOccupiedPriority(20);
		map.getLink(new GridPoint(2,0), new GridPoint(3,0)).get().setOccupiedPriority(20);
		map.getLink(new GridPoint(3,0), new GridPoint(4,0)).get().setOccupiedPriority(20);
		
		FindRoute route_engine = new FindRoute(map);
		
		//Get route as priority 5, 15 and 25 and print the map
		System.out.println(map);
		System.out.println("Priority  5: " + route_engine.getRoute(0,0,7,8,5));
		System.out.println("Priority 15: " + route_engine.getRoute(0,0,7,8,15));
		System.out.println("Priority 25: " + route_engine.getRoute(0,0,7,8,25));

	}

}
