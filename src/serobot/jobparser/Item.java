package serobot.jobparser;
import java.util.ArrayList;

/**
 * The ItemPosition class represents the items position.
 * @author bobbydilley
 */
public class Item {
	private int x;
	private int y;
	private String id;
	private double reward;
	private double weight;
	
	public static Item itemByID(String id, ArrayList<Item> items)
	{
		for(Item item : items) {
			if(item.getID().equals(id)) {
				return item;
			}
		}
		return null;
	}
	/**
	 * The constructor for the ItemPosition class
	 * @param weight The weight of the item
	 * @param reward The reward you will get from successfully transporting the item.
	 * @param x The X Position of the item
	 * @param y The Y Position of the item
	 * @param id The particular Items ID
	 */
	public Item(double weight, double reward, int x, int y, String id) {
		this.x = x;
		this.y = y;
		this.id = id;
		this.reward = reward;
		this.weight = weight;
	}

	/**
	 * Gets the X Position of the item in question.
	 * @return The X Position.
	 */
	public int getXPosition() {
		return this.x;
	}

	/**
	 * Gets the Y Position of the item in question.
	 * @return The Y Position.
	 */
	public int getYPosition() {
		return this.y;
	}

	/**
	 * Gets the ID of the item in question.
	 * @return The ID
	 */
	public String getID() {
		return this.id;
	}
	
	/**
	 * Gets the reward value of the item in question.
	 * @return The reward value
	 */
	public double getReward() {
		return this.reward;
	}
	
	/**
	 * Gets the weight value of the item in question.
	 * @return The weight value
	 */
	public double getWeight() {
		return this.weight;
	}
}
