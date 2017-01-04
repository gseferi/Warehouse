package serobot.findroute;

public class Reservation {
	
	private int priority;
	private GridPoint point;
	
	public Reservation(int priority, GridPoint point){
		this.priority = priority;
		this.point = point;
	}
	
	public int getPriority(){
		return this.priority;
	}
	
	public GridPoint getPoint(){
		return this.point;
	}
	
	public String toString(){
		return "" + this.priority + "," + this.point;
	}

}
