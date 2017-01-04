package serobot.findroute;

public class Cost {
	
	public static final int unit_path_cost = 1;
	public static final int rotation_cost = 1;
	
	private int x_destination;
	private int y_destination;
	private int x_currentpoint;
	private int y_currentpoint;
	private int current_cost;
	private GridPoint destination_point;
	private GridPoint current_point;
	
	public Cost(int current_cost,GridPoint current_point, GridPoint destination_point){
		this.x_destination = destination_point.getX();
		this.y_destination = destination_point.getY();
		this.x_currentpoint= current_point.getX();
		this.y_currentpoint= current_point.getY();;
		this.destination_point = destination_point;
		this.current_point = current_point;
		this.current_cost = current_cost;
	}
	
	public int remainingCost(){
		int x_cost = x_destination - x_currentpoint;
		if(x_cost < 0){
			x_cost = -x_cost;
		}
		int y_cost = y_destination - y_currentpoint;
		if(y_cost < 0){
			y_cost = -y_cost;
		}
		return x_cost + y_cost;
	}
	
	public int getCurrentCost(){
		return this.current_cost;
	}
	
	public int totalCost(){
		return remainingCost() + getCurrentCost();
	}
	
	public boolean isGoal(){
		return this.current_point.samePoint(this.destination_point);
	}
}
