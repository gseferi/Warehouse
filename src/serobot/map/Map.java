package serobot.map;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;

import org.junit.Before;
import org.junit.Test;

import serobot.pcinterface.RobotData;

public class Map extends JPanel implements MouseListener {
	private ArrayList<RobotData> robots = new ArrayList<RobotData>();
	private ArrayList<Wall> walls = new ArrayList<Wall>();

	// Grid variables
	private static int gridWidth = 11;
	private static int gridHeight = 7;

	// Movable screen variables
	private double screenPerc = 0.9;
	private double targetScreenPerc = 0.9;
	private int screenAngle;
	private int targetScreenAngle = 0;

	// Screen variables
	private int screenWidth;
	private int screenHeight;
	private int leftBuffer;
	private int topBuffer;

	// Robot following
	private Robot followrobot;
	boolean follow;
	private int leftOffset;
	private int topOffset;

	public Map() {
		// Start running the main thread which deals with animation.
		new Thread(() -> threadBody()).start();
		this.addMouseListener(this);
		follow = false;
	}

	public void addRobot(RobotData robot) {
		robots.add(robot);
	}

	public void addWall(Wall wall) {
		walls.add(wall);
	}

	public void followRobot(Robot robot, double zoom) {
		this.followrobot = robot;
		follow = true;
		targetScreenPerc = zoom;
	}

	public void followRobot(Robot robot) {
		this.followrobot = robot;
		follow = true;
	}

	public void unFollowRobot() {
		follow = false;
		followrobot = null;
		targetScreenPerc = 0.9;
		targetScreenAngle = 0;
	}

	private void threadBody() {
		while (true) {
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				System.err.println("Interupted Exception");
			}
			screenPerc = tendTo(screenPerc, targetScreenPerc, 0.01);
			screenAngle = tendTo(screenAngle, targetScreenAngle, 1);

			if (followrobot != null) {
				int x = (int) (followrobot.getX() * screenWidth) - screenWidth / 2;
				int y = (int) (followrobot.getY() * screenHeight) - screenHeight / 2;
				leftOffset = (int) tendTo(leftOffset, -x, 0.1);
				topOffset = (int) tendTo(topOffset, -y, 0.1);
			}
			// leftOffset = tendTo(leftOffset, )

			// Make the robots move closer to their target positions
			for (RobotData robot : robots) {
				robot.tend();
			}

			if (this.follow) {
				targetScreenAngle = 360 - this.followrobot.getAngle();
			}
			// Make sure it repaints at the end
			repaint();
		}
	}

	
	//JUnit test for the tendTo Function
	@Test
	public void tendToTest() {
		assertTrue(tendTo(0, 1, 0.1) == 0.1);
		assertTrue(tendTo(0.0, 0.5, 0.1) == 0.05);
	}

	private double tendTo(double at, double target, double amount) {
		if (at < target) {
			at += amount * (target - at);
		} else if (at > target) {
			at -= amount * (at - target);
		}

		DecimalFormat df = new DecimalFormat("#.####");
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

	public static double absoluteToRelativeWidth(int xpos) {
		int gridSections = gridWidth;
		double x = 1.0 / (double) gridSections;
		return xpos * x;
	}

	public static double absoluteToRelativeHeight(int ypos) {
		int gridSections = gridHeight;
		double x = 1.0 / (double) gridSections;
		return ypos * x;
	}

	protected void paintComponent(Graphics e) {
		super.paintComponent(e);
		Graphics2D g = (Graphics2D) e;
		g.rotate(Math.toRadians(screenAngle), this.getWidth() / 2, this.getHeight() / 2);
		g.setColor(Color.gray);

		drawGrid(gridWidth, gridHeight, g);

		// Setup the margins at the side
		screenWidth = (int) (this.getWidth() * screenPerc);
		screenHeight = (int) (this.getHeight() * screenPerc);
		leftBuffer = (int) ((this.getWidth() * (1 - screenPerc)) / 2) + leftOffset;
		topBuffer = (int) ((this.getHeight() * (1 - screenPerc)) / 2) + topOffset;

		g.setFont(new Font("San Francisco", Font.PLAIN, 14));

		// Theos idea
		int textx = leftBuffer < 10 ? 10 : leftBuffer;
		int texty = topBuffer < 20 ? 20 : topBuffer;

		g.drawString("Size: " + screenPerc, textx, texty);
		g.drawPolygon(RotatableShape.drawRectangle(leftBuffer, topBuffer, screenWidth, screenHeight, g));

		drawRobots(g);
		drawWalls(g);
	}

	private void drawRobots(Graphics g) {
		for (RobotData robot : robots) {
			int x = (int) (robot.getX() * screenWidth) + leftBuffer;
			int y = (int) (robot.getY() * screenHeight) + topBuffer;
			int width = (int) ((double) (1.0 / (double) gridWidth) * screenWidth) / 4;
			int height = (int) ((double) (1.0 / (double) gridHeight) * screenHeight) / 2;
			x -= width / 2;
			y -= height / 2;

			g.drawPolygon(RotatableShape.drawRectangle(x, y, width, height, g, robot.getAngle()));
			g.drawString("Robot", x, y);
		}
	}

	private void drawGrid(int lines, int columns, Graphics g) {
		for (int i = 0; i < lines; i++) {
			g.drawLine((int) ((double) screenWidth * (double) i / lines) + leftBuffer, topBuffer,
					(int) ((double) screenWidth * (double) i / lines) + leftBuffer, screenHeight + topBuffer);
		}

		for (int i = 0; i < columns; i++) {
			g.drawLine(leftBuffer, (int) ((double) screenHeight * (double) i / columns) + topBuffer,
					screenWidth + leftBuffer, (int) ((double) screenHeight * (double) i / columns) + topBuffer);
		}
	}

	private void drawWalls(Graphics g) {
		g.setColor(Color.black);
		for (Wall wall : walls) {
			int x = (int) (wall.getX() * screenWidth) + leftBuffer
					- ((int) ((double) (1.0 / (double) gridWidth) * screenWidth)) / 2;
			int y = (int) (wall.getY() * screenHeight) + topBuffer
					- ((int) ((double) (1.0 / (double) gridHeight) * screenHeight)) / 2;
			int width = (int) ((double) (1.0 / (double) gridWidth) * screenWidth);
			int height = (int) ((double) (1.0 / (double) gridHeight) * screenHeight) * wall.getLength();
			g.fillRect(x, y, width, height);
		}
	}

	private Random r = new Random();

	@Override
	public void mouseClicked(MouseEvent e) {
		//robots.get(0).setAngle(90 * r.nextInt(5));
		//robots.get(0).setY(absoluteToRelativeHeight(r.nextInt(gridHeight)));
		//robots.get(0).setX(absoluteToRelativeWidth(r.nextInt(gridWidth)));

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}
}
