package serobot.jobparser;
/**
 * The ItemPosition class represents the items position.
 * @author bobbydilley
 */
public class ItemPosition {
	private int x;
	private int y;
	private String id;
	
	/**
	 * The constructor for the ItemPosition class
	 * @param x The X Position of the item
	 * @param y The Y Position of the item
	 * @param id The particular Items ID
	 */
	public ItemPosition(int x, int y, String id) {
		this.x = x;
		this.y = y;
		this.id = id;
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
}
