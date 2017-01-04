package serobot.map;

import java.text.DecimalFormat;

public class Robot {
	private double x;
	private double y;
	private double targetx;
	private double targety;
	private int angle;
	private int targetAngle;

	public Robot(double x, double y) {
		this.targetx = x;
		this.targety = y;
		this.x = targetx;
		this.y = targety;
		this.angle = 0;
	}

	public boolean waiting() {
		return x == targetx && y == targety && angle == targetAngle;
	}

	public double getX() {
		return this.x;
	}

	public double getY() {
		return this.y;
	}

	public void setX(double x) {
		this.targetx = x;
	}

	public void setY(double y) {
		this.targety = y;
	}

	public void setAngle(int a) {
		this.targetAngle = a;
	}

	public void tend() {
		x = tendTo(x, targetx, 0.1);
		y = tendTo(y, targety, 0.1);
		angle = tendTo(angle, targetAngle, 1);

	}

	public int getAngle() {
		return this.angle;
	}

	private double tendTo(double at, double target, double amount) {
		if (at < target) {
			at += amount * (target - at);
		} else if (at > target) {
			at -= amount * (at - target);
		}
		DecimalFormat df = new DecimalFormat("#.###");
		double toReturn = Double.valueOf(df.format(at));
		return toReturn;
	}

	private int tendTo(int at, int target, int amount) {
		if (at < target) {
			at += amount;
		} else if (at > target) {
			at -= amount;
		}
		return at;
	}

}
