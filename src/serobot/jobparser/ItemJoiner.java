package serobot.jobparser;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

/**
 * This class joins the ItemPosition and ItemReward files
 * @author bobbydilley
 *
 */
public class ItemJoiner {
	private ArrayList<ItemPosition> itemPositions;
	private ArrayList<ItemReward> itemRewards;
	
	public ItemJoiner(ArrayList<ItemPosition> itemPositions, ArrayList<ItemReward> itemRewards) {
		this.itemPositions = itemPositions;
		this.itemRewards = itemRewards;
	}
	
	public ArrayList<Item> Join()
	{
		ArrayList<Item> items = new ArrayList<Item>();
		
		for(ItemPosition itemPosition : itemPositions) {
			//Check if the itemPosition and itemRewards ItemID's are the same
			for(ItemReward itemReward : itemRewards) {
				if(itemPosition.getID().equals(itemReward.getID())) {
					Item item = new Item(itemReward.getWeight(), itemReward.getReward(), itemPosition.getXPosition(), itemPosition.getYPosition(), itemPosition.getID());
					items.add(item);
				}
			}
		}
		return items;
	}
}
