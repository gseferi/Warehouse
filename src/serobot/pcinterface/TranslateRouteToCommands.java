package serobot.pcinterface;

import java.util.ArrayList;
import java.util.List;

import serobot.findroute.GridPoint;

public class TranslateRouteToCommands {
	
	public static final int UP = 1;
	public static final int RIGHT = 2;
	public static final int DOWN = 3;
	public static final int LEFT = 4;
	public static final int WAIT = 0;
	
	public static List<String> translateRouteToCommands(List<GridPoint> route) {
		List<String> commands = new ArrayList<String>();
		GridPoint previousRoute = new GridPoint(0, 0);
		while (route.size() != 0) {
			int dx;
			int dy;
			if (route.size() != 1) {
				dx = route.get(0).getX() - route.get(1).getX();
				dy = route.get(0).getY() - route.get(1).getY();
			}
			else {
				dx = previousRoute.getX() - route.get(0).getX();
				dy = previousRoute.getY() - route.get(0).getY();
			}
			if (dx < 0 && dy == 0) {
				commands.add("E");
			}
			else if (dx > 0 && dy == 0) {
				commands.add("W");
			}
			else if (dx == 0 && dy < 0) {
				commands.add("N");
			}
			else if (dx == 0 && dy > 0) {
				commands.add("S");
			}
			else {
				commands.add("wait");
			}
			previousRoute = route.get(0);
			route.remove(0);
		}
		return commands;
	}
	
	public static int translateNextPoint(RobotData robot, GridPoint currentpt, GridPoint nextpt, int direction) {
		int absoluteDirection;
		int dx = currentpt.getX() - nextpt.getX();
		int dy = currentpt.getY() - nextpt.getY();
		if (dx < 0 && dy == 0) {
			absoluteDirection = RIGHT;
		}
		else if (dx > 0 && dy == 0) {
			absoluteDirection = LEFT;
		}
		else if (dx == 0 && dy < 0) {
			absoluteDirection = UP;
		}
		else if (dx == 0 && dy > 0) {
			absoluteDirection = DOWN;
		}
		else {
			absoluteDirection = WAIT;
		}
		switch(direction) {
		case UP:
			switch(absoluteDirection) {
			case UP:
				robot.setDirection(UP);
				return UP;
			case RIGHT:
				robot.setDirection(RIGHT);
				return RIGHT;
			case DOWN:
				robot.setDirection(DOWN);
				return DOWN;
			case LEFT:
				robot.setDirection(LEFT);
				return LEFT;
			}
		case RIGHT:
			switch(absoluteDirection) {
			case UP:
				robot.setDirection(UP);
				return LEFT;
			case RIGHT:
				robot.setDirection(RIGHT);
				return UP;
			case DOWN:
				robot.setDirection(DOWN);
				return RIGHT;
			case LEFT:
				robot.setDirection(LEFT);
				return DOWN;
			}
		case DOWN:
			switch(absoluteDirection) {
			case UP:
				robot.setDirection(UP);
				return DOWN;
			case RIGHT:
				robot.setDirection(RIGHT);
				return LEFT;
			case DOWN:
				robot.setDirection(DOWN);
				return UP;
			case LEFT:
				robot.setDirection(LEFT);
				return RIGHT;
			}
		case LEFT:
			switch(absoluteDirection) {
			case UP:
				robot.setDirection(UP);
				return RIGHT;
			case RIGHT:
				robot.setDirection(RIGHT);
				return DOWN;
			case DOWN:
				robot.setDirection(DOWN);
				return LEFT;
			case LEFT:
				robot.setDirection(LEFT);
				return UP;
			}
		}
		return WAIT;
	}
	
	public static String intToString(int direction) {
		if (direction == 1) {
			return "F";
		}
		else if (direction == 2) {
			return "R";
		}
		else if (direction == 3) {
			return "B";
		}
		else if (direction == 4) {
			return "L";
		}
		else {
			return "WAIT";
		}
	}
}
