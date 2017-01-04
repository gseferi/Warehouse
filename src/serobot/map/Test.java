package serobot.map;

import java.awt.BorderLayout;

import javax.swing.JFrame;

public class Test {
	public static void main(String[] args) {
		JFrame frame = new JFrame("Map Demo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Firstly you can create a new map
		Map map = new Map();
		
		//Then you can create some walls (these are the correct walls)
		Wall wall1 = new Wall(Map.absoluteToRelativeWidth(1), Map.absoluteToRelativeHeight(2), 5);
		Wall wall2 = new Wall(Map.absoluteToRelativeWidth(4), Map.absoluteToRelativeHeight(2), 5);
		Wall wall3 = new Wall(Map.absoluteToRelativeWidth(7), Map.absoluteToRelativeHeight(2), 5);
		Wall wall4 = new Wall(Map.absoluteToRelativeWidth(10), Map.absoluteToRelativeHeight(2), 5);

		//Then you can add the walls onto the map
		map.addWall(wall1);
		map.addWall(wall2);
		map.addWall(wall3);
		map.addWall(wall4);
		
		//Then you can add the robot
		Robot robot = new Robot(Map.absoluteToRelativeWidth(2), Map.absoluteToRelativeHeight(6));
		
		//This command makes the map follow that particular robot
		map.followRobot(robot, 1.5);
		
		//This adds the robot to the map
		//map.addRobot(robot);
		
		frame.getContentPane().add(map, BorderLayout.CENTER);
		frame.setSize(800, 600);
		frame.setVisible(true);
	}
}
