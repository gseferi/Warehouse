package serobot.findroute;


import java.util.*;

/**
 * Expand a node
 * @author TSANG KWONG HEI
 *
 */
public class ExpandNode {
	
	private List<GridLink> links;
	private TimeStep step;
	
	public ExpandNode(List<GridLink> links,TimeStep step){
		this.links = links;
		this.step = step;
	}
	
	/**
	 * Expand a node.
	 * @param current_pt The current point.
	 * @param destination_pt The destination point.
	 * @return The frontiers.
	 */
	public List<Frontier> expand(List<Frontier> frontiers, Frontier frontier, GridPoint destination_pt, int priority){
		frontiers.remove(frontier);
		GridPoint current_pt = frontier.getGridPoint();
		//int current_cost = frontier.getCost().getCurrentCost();
		
		for(GridLink link : links){
			GridPoint pt1 = link.getPt1();
			GridPoint pt2 = link.getPt2();
			
			if(!link.isEnabled(priority)){
			}else if((pt1.getX() == current_pt.getX()) && (pt1.getY() == current_pt.getY())){
				addFrontier(frontiers,frontier,pt2,destination_pt,priority);
				//frontiers.add(new Frontier(current_cost+Cost.unit_path_cost,frontier,pt2,destination_pt));
			}else if((pt2.getX() == current_pt.getX()) && (pt2.getY() == current_pt.getY())){
				addFrontier(frontiers,frontier,pt1,destination_pt,priority);
				//frontiers.add(new Frontier(current_cost+Cost.unit_path_cost,frontier,pt1,destination_pt));
			}
		}
		
		return frontiers;
	}
	
	private void addFrontier(List<Frontier> frontiers, Frontier frontier, GridPoint nextPoint,GridPoint destination_pt, int priority){
		int current_cost = frontier.getCost().getCurrentCost();
		int new_cost = current_cost + Cost.unit_path_cost;
		boolean rotation = false;
		
		//check the need for rotation
		if(frontier.getParent() != null){
			if(frontier.getParent().getGridPoint().getX() == frontier.getGridPoint().getX()){
				if(frontier.getGridPoint().getX() != nextPoint.getX()){
					//rotation needed
					rotation = true;
				}
			}else if(frontier.getParent().getGridPoint().getY() == frontier.getGridPoint().getY()){
				if(frontier.getGridPoint().getY() != nextPoint.getY()){
					//rotation needed
					rotation = true;
				}
			}
		}
		
		if(rotation){
			new_cost += Cost.rotation_cost;
		}
		
		//check whether point is reserved
		boolean add = true;
		for(Reservation reservation : this.step.getReservationOfTime(frontier.getTimeStep()+1)){
			GridPoint point = reservation.getPoint();
			int reservationpriority = reservation.getPriority();
			if(reservationpriority <= priority && nextPoint.samePoint(point)){
				//point is allocated, not add this point
				add = false;
				break;
			}
		}
		
		if(add){
			frontiers.add(new Frontier(new_cost,frontier,nextPoint,destination_pt,frontier.getTimeStep()+1));
		}
	}
}
