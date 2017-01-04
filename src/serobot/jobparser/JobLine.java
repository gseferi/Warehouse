package serobot.jobparser;

public class JobLine {
	private Item item;
	private int count;
	
	public JobLine(Item item, int count)
	{
		this.item = item;
		this.count = count;
	}
	
	public Item getItem()
	{
		return this.item;
	}
	
	public int getCount()
	{
		return this.count;
	}
}
