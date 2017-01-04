package serobot.findroute;

import java.util.*;

public class Obstacle {

	private GridPoint pt1;
	private GridPoint pt2;
	
	/**
	 * Create an obstacle preventing robot from travelling from point 1 to point 2
	 * @param pt1 Point 1
	 * @param pt2 Point 2
	 */
	public Obstacle(GridPoint pt1, GridPoint pt2){
		if((pt1.getX() == pt2.getX()) || pt1.getY() == pt2.getY()){
			this.pt1 = pt1;
			this.pt2 = pt2;
		}else{
			throw new IllegalArgumentException(pt1 + " and " + pt2 + " are not next to each other.");
		}
	}
	
	/**
	 * Get point 1
	 * @return point 1
	 */
	public GridPoint getPt1(){
		return this.pt1;
	}
	
	/**
	 * Get point 2
	 * @return point 2
	 */
	public GridPoint getPt2(){
		return this.pt2;
	}
	
	/**
	 * Generate obstacles with a given unreachable point.
	 * @param point The unreachable point
	 * @return The list of obstacles blocking nearby points to that unreachable point.
	 */
	public static List<Obstacle> generateObstacles(GridPoint point){
		List<Obstacle> obstacles = new ArrayList<Obstacle>(4);
		//get the coordinates
		int x = point.getX();
		int y = point.getY();
		//add the obstacles
		obstacles.add(new Obstacle(new GridPoint(x,y),new GridPoint(x-1,y)));
		obstacles.add(new Obstacle(new GridPoint(x,y),new GridPoint(x+1,y)));
		obstacles.add(new Obstacle(new GridPoint(x,y),new GridPoint(x,y-1)));
		obstacles.add(new Obstacle(new GridPoint(x,y),new GridPoint(x,y+1)));
		return obstacles;
	}
}
