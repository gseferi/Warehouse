package serobot.findroute;

public class Frontier {
	
	private GridPoint point;
	private Cost cost;
	private GridPoint destination_point;
	private Frontier parent;
	private int timestep;
	
	public Frontier(int current_cost, Frontier parent, GridPoint point, GridPoint destination_point, int timestep){
		this.point = point;
		this.parent = parent;
		this.destination_point = destination_point;
		this.cost = new Cost(current_cost,point,destination_point);
		this.timestep = timestep;
	}
	
	public GridPoint getGridPoint(){
		return this.point;
	}
	
	public Cost getCost(){
		return this.cost;
	}
	
	public boolean isGoal(){
		return this.point.samePoint(this.destination_point);
	}
	
	public Frontier getParent(){
		return this.parent;
	}
	
	public int getTimeStep(){
		return this.timestep;
	}
}
