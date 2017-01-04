package serobot.map;

public class Wall {
	private double x;
	private double y;
	private int length;

	public Wall(double x, double y, int length) {
		this.x = x;
		this.y = y;
		this.length = length;
	}

	public int getLength() {
		return length;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

}
