package serobot.findroute;

public class GridPoint {

	private int x;
	private int y;
	
	public GridPoint(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public int getX(){
		return this.x;
	}
	
	public int getY(){
		return this.y;
	}
	
	public String toString(){
		return "(" + this.x + "," + this.y + ")";
	}
	
	public boolean samePoint(GridPoint pt){
		return (pt.x == this.x && pt.y == this.y);
	}
}
