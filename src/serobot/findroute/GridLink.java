package serobot.findroute;

/**
 * Represent a link between two points
 * @author TSANG KWONG HEI
 *
 */
public class GridLink{
	private GridPoint pt1;
	private GridPoint pt2;
	private boolean enabled;
	private int priority;
	private boolean priority_enabled;
	
	public GridLink(GridPoint pt1, GridPoint pt2){
		this.pt1 = pt1;
		this.pt2 = pt2;
		this.enabled = true;
		this.priority_enabled = false;
		this.priority = 0;
	}
	
	public GridPoint getPt1(){
		return this.pt1;
	}
	
	public GridPoint getPt2(){
		return this.pt2;
	}
	
	public boolean isEnabled(int priority){
		if(!this.priority_enabled){
			return this.enabled;
		}else{
			if(priority > this.priority){
				return false;
			}else{
				return this.enabled;
			}
		}
	}
	
	public void setEnabled(boolean enabled){
		this.enabled = enabled;
	}
	
	public synchronized void setOccupiedPriority(int priority){
		if(!(priority > 0)){
			throw new IllegalArgumentException("Priority must be larger than 0.");
		}
		this.priority = priority;
		this.priority_enabled = true;
	}
	
	public synchronized void disablePriority(){
		this.priority_enabled = false;
	}
}
