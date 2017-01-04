package serobot.findroute;

import java.util.*;

public class GridMap {

	private final List<GridLink> links;
	private final List<GridPoint> points;
	private final List<Obstacle> obstacles;
	private final int height;
	private final int width;
	
	/**
	 * Construct a map
	 * @param width The width of the map
	 * @param height The height of the map
	 * @param obstacles The list of obstacles
	 */
	public GridMap(int width, int height, List<Obstacle> obstacles){
		//apply variables
		this.obstacles = obstacles;
		this.height = height;
		this.width = width;
		
		//generate points
		this.points = new ArrayList<GridPoint>(width*height);
		for(int i = 0; i < width; i++) for(int j = 0; j < height; j++){
			this.points.add(new GridPoint(i,j));
		}
		
		//generate links
		this.links = new ArrayList<GridLink>((height-1)*width + (width-1)*height);
		
		//create vertical links
		for(int i = 0; i < width; i++) for(int j = 0; j < height-1; j++){
			int index = i*height+j;
			GridPoint pt1 = this.points.get(index);
			GridPoint pt2 = this.points.get(index+1);
			assert(pt1.getX() == i && pt1.getY() == j);
			assert(pt2.getX() == i && pt2.getY() == j+1);
			GridLink new_link = new GridLink(pt1,pt2);
			if(checkObstacle(new_link)){
				new_link.setEnabled(false);
			}
			this.links.add(new_link);
		}
		
		//create horizontal links
		for(int y = 0; y < height; y++) for(int x = 0; x < width-1; x++){
			int index = x*height+y;
			GridPoint pt1 = this.points.get(index);
			GridPoint pt2 = this.points.get(index+height);
			assert(pt1.getX() == x && pt1.getY() == y);
			assert(pt2.getX() == x+1 && pt2.getY() == y);
			GridLink new_link = new GridLink(pt1,pt2);
			if(checkObstacle(new_link)){
				new_link.setEnabled(false);
			}
			this.links.add(new_link);
		}
	}
	
	public List<GridLink> getLinks(){
		return this.links;
	}
	
	public List<GridPoint> getPoints(){
		return this.points;
	}
	
	public List<Obstacle> getObstacles(){
		return this.obstacles;
	}
	
	private boolean checkObstacle(GridLink link){
		for(Obstacle obstacle : this.obstacles){
			if(link.getPt1().samePoint(obstacle.getPt1()) && link.getPt2().samePoint(obstacle.getPt2())){
				return true;
			}else if(link.getPt1().samePoint(obstacle.getPt2()) && link.getPt2().samePoint(obstacle.getPt1())){
				return true;
			}
		}
		return false;
	}
	
	public String toString(){
		String result = "";
		for(int y = this.height-1; y >= 0; y--){
			//one line
			for(int x = 0; x < width; x++){
				result += " # ";
				//horizontal links
				if(x != width-1){
					Optional<GridLink> link = getLink(new GridPoint(x,y),new GridPoint(x+1,y));
					assert(link.isPresent());
					if(link.get().isEnabled(0)){
						result += " - ";
					}else{
						result += "   ";
					}
				}
			}
			
			//start a new line
			result += "\n";
			
			if(y != 0){
				//vertical links
				for(int x = 0; x < width; x++){
					Optional<GridLink> link = getLink(new GridPoint(x,y),new GridPoint(x,y-1));
					assert(link.isPresent());
					if(link.get().isEnabled(0)){
						result += " |    ";
					}else{
						result += "      ";
					}
				}
				
				//start a new line
				result += "\n";
			}
			
			
		}
		return result;
	}
	
	public Optional<GridLink> getLink(GridPoint pt1, GridPoint pt2){
		for(GridLink link : this.links){
			if(link.getPt1().samePoint(pt1) && link.getPt2().samePoint(pt2)){
				return Optional.of(link);
			}else if(link.getPt2().samePoint(pt1) && link.getPt1().samePoint(pt2)){
				return Optional.of(link);
			}
		}
		return Optional.empty();
	}
	
	
	public static void main(String[] args) throws RouteNotFoundException, PointOutOfBoundaryException{
		List<Obstacle> obstacles = new ArrayList<Obstacle>();
		obstacles.add(new Obstacle(new GridPoint(0,0), new GridPoint(0,1)));
		obstacles.add(new Obstacle(new GridPoint(7,7), new GridPoint(7,8)));
		obstacles.add(new Obstacle(new GridPoint(6,6), new GridPoint(6,7)));
		obstacles.add(new Obstacle(new GridPoint(6,7), new GridPoint(7,7)));
		obstacles.add(new Obstacle(new GridPoint(7,1), new GridPoint(6,1)));
		obstacles.add(new Obstacle(new GridPoint(5,0), new GridPoint(6,0)));
		obstacles.add(new Obstacle(new GridPoint(6,1), new GridPoint(6,0)));
		obstacles.add(new Obstacle(new GridPoint(7,1), new GridPoint(7,0)));
		GridMap map = new GridMap(8,9,obstacles);
		
		System.out.println("Print the map: ");
		System.out.println(map);
		
		FindRoute route_engine = new FindRoute(map);
		System.out.println("Try finding route from (0,0) to (7,8):");
		List<GridPoint> route = route_engine.getRoute(0,0,7,8);
		System.out.println("Print the route:");
		System.out.println(route);
		
		System.out.println("Try finding route from (7,7) to (7,8):");
		List<GridPoint> route2 = route_engine.getRoute(7,7,7,8);
		System.out.println("Print the route:");
		System.out.println(route2);
		
		System.out.println("Try finding route from (7,0) to (7,8):");
		try{
			route_engine.getRoute(7,0,7,8);
			assert(false);
		}catch(RouteNotFoundException e){
			System.out.println("Unrecheable.");
		}
		
		//Make sure assertion is enabled
		try{
			assert(false);
			System.out.println("Assertion not enabled.");
		}catch(AssertionError e){
			System.out.println("Test passed.");
		}
	}
}
