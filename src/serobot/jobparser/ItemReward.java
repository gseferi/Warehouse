package serobot.jobparser;
/**
 * The ItemReward class represents the amount of reward you get for collecting the item.
 * @author bobbydilley
 */
public class ItemReward {
	private double reward;
	private double weight;
	private String id;
	/**
	 * The constructor for the ItemReward class.
	 * @param id The particular Items ID
	 * @param reward The reward you will get from successfully transporting the item.
	 */
	public ItemReward(String id, double reward, double weight) {
		this.reward = reward;
		this.weight = weight;
		this.id = id;
	}

	/**
	 * Gets the reward value of the item in question.
	 * @return The reward value
	 */
	public double getReward() {
		return this.reward;
	}
	
	/**
	 * Gets the weight value of the item in question
	 * @return The weight value
	 */
	public double getWeight() {
		return this.weight;
	}

	/**
	 * Gets the ID of the item in question.
	 * @return The ID
	 */
	public String getID() {
		return this.id;
	}
}
