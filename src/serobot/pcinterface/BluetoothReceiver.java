package serobot.pcinterface;

import java.util.ArrayList;
import java.util.List;

import serobot.bluetooth.Command;
import serobot.bluetooth.RobotSocket;
import serobot.bluetooth.Socket;
import serobot.bluetooth.SocketListener;
import serobot.findroute.FindRoute;
import serobot.findroute.GridPoint;
import serobot.findroute.PointOutOfBoundaryException;
import serobot.findroute.RouteNotFoundException;
import serobot.jobparser.Job;

public class BluetoothReceiver implements SocketListener {
	
	PCInterfaceDataModel model;
	boolean goingDropOff = false;
	
	public BluetoothReceiver(PCInterfaceDataModel model) {
		this.model = model;
	}

	@Override
	public void onCommandReceived(Socket socket, Command command) {
		RobotSocket robotSocket = (RobotSocket)socket;
		String robotName = robotSocket.getRobotName();
		RobotData robot = model.getRobot(robotName);
		int direction;
		Command nextItemRoute;
		Command goDropOff;
		//System.out.println(model.getDropOffPoint());
		//System.out.println(goingDropOff);
		
		switch(command.getCommandName()) {
		case "position":
			if (!goingDropOff) {
				int x = command.getInteger("x");
				int y = command.getInteger("y");
				robot.setMoveCompleted(true);
				while (robot.getFinishedRouteIteration() == false) {
					delay(100);
				}
				delay(500);
				System.out.println("Current Location: " + robot.getLocation());
				System.out.println("Next Location: " + robot.getNextLocation());
				direction = TranslateRouteToCommands.translateNextPoint(robot, robot.getLocation(),
						robot.getNextLocation().get(), robot.getDirection());
				nextItemRoute = new Command("nextItemRoute");
				nextItemRoute.setString("Job ID", robot.getCurrentJob().getID());
				nextItemRoute.setString("Item ID", robot.getCurrentJob().getLine(0).getItem().getID());
				nextItemRoute.setInteger("Item Count", robot.getCurrentJob().getLine(0).getCount());
				nextItemRoute.setString("direction", TranslateRouteToCommands.intToString(direction));
				nextItemRoute.setInteger("Route Length", robot.getRoute().size());
				robot.getSocket().sendCommand(nextItemRoute);
				System.out.println(TranslateRouteToCommands.intToString(direction));
			}
			else {
				int x = command.getInteger("x");
				int y = command.getInteger("y");
				robot.setMoveCompleted(true);
				while (robot.getFinishedRouteIteration() == false) {
					delay(100);
				}
				delay(500);
				
				direction = TranslateRouteToCommands.translateNextPoint(robot, robot.getLocation(),
						robot.getNextLocation().get(), robot.getDirection());
				goDropOff = new Command("goDropOff");
				goDropOff.setString("direction", TranslateRouteToCommands.intToString(direction));
				goDropOff.setInteger("Route Length", robot.getRoute().size());
				robot.getSocket().sendCommand(goDropOff);
			}
			break;
		case "itemPicked":
			robot.setMoveCompleted(true);
			while (robot.getFinishedRouteIteration() == false) {
				delay(100);
			}
			
			for (int i = 0; i<model.getOngoingJobs().size(); i++) {
				if (model.getOngoingJobs().get(i).getID().equals(robot.getCurrentJob().getID())) {
					model.getOngoingJobs().get(i).removeLine(0);
					
					//System.out.println("Robot Current Job Lines: " + robot.getCurrentJob().getLines().size());
					
					if (model.getOngoingJobs().get(i).getLines().size() == 0) {
						//System.out.println("Robot Current Job Lines: " + robot.getCurrentJob().getLines().size());
						goingDropOff = true;
						model.getOngoingJobs().remove(i);
						
					}
					model.update();
				}
			}
			
			//System.out.println(model.getOngoingJobs().get(0).getLines().size());
			delay(500);
			if (!goingDropOff) {
				robot.setDestination(new GridPoint(robot.getCurrentJob().getLine(0).getItem().getXPosition(),
						robot.getCurrentJob().getLine(0).getItem().getYPosition()));
				direction = TranslateRouteToCommands.translateNextPoint(robot, robot.getLocation(),
						robot.getNextLocation().get(), robot.getDirection());
				nextItemRoute = new Command("nextItemRoute");
				nextItemRoute.setString("Job ID", robot.getCurrentJob().getID());
				nextItemRoute.setString("Item ID", robot.getCurrentJob().getLine(0).getItem().getID());
				nextItemRoute.setInteger("Item Count", robot.getCurrentJob().getLine(0).getCount());
				nextItemRoute.setString("direction", TranslateRouteToCommands.intToString(direction));
				nextItemRoute.setInteger("Route Length", robot.getRoute().size());
				robot.getSocket().sendCommand(nextItemRoute);
				System.out.println(TranslateRouteToCommands.intToString(direction));
			}
			else {
				System.out.println("Before Set Drop Off Destination");
				robot.setDestination(new GridPoint(model.getDropOffPoint().getX(), model.getDropOffPoint().getY()));
				direction = TranslateRouteToCommands.translateNextPoint(robot, robot.getLocation(),
						robot.getNextLocation().get(), robot.getDirection());
				goDropOff = new Command("goDropOff");
				goDropOff.setString("direction", TranslateRouteToCommands.intToString(direction));
				goDropOff.setInteger("Route Length", robot.getRoute().size());
				robot.getSocket().sendCommand(goDropOff);
				
			}
			break;
		
		case "atDropOff":
			robot.setMoveCompleted(true);
			while (robot.getFinishedRouteIteration() == false) {
				delay(100);
			}
			delay(500);
			
			System.out.println("At Drop Off");
			model.getCompletedJobs().add(robot.getJobWithItems());
			model.getOngoingJobs().add(model.getUpcomingJobs().get(0)); 
			
			Job newJob1 = new Job(model.getUpcomingJobs().get(0).getID());
			for (int a = 0; a<model.getUpcomingJobs().get(0).getLines().size(); a++) {
				newJob1.addLine(model.getUpcomingJobs().get(0).getLine(a));
			}
			
			robot.setJobWithItems(newJob1);
			robot.setCurrentJob(model.getUpcomingJobs().get(0));
			model.getUpcomingJobs().remove(0);
			System.out.println("Job Completed");
			goingDropOff = false;
			
			robot.setDestination(new GridPoint(robot.getCurrentJob().getLine(0).getItem().getXPosition(),
					robot.getCurrentJob().getLine(0).getItem().getYPosition()));
			direction = TranslateRouteToCommands.translateNextPoint(robot, robot.getLocation(),
					robot.getNextLocation().get(), robot.getDirection());
			nextItemRoute = new Command("nextItemRoute");
			nextItemRoute.setString("Job ID", robot.getCurrentJob().getID());
			nextItemRoute.setString("Item ID", robot.getCurrentJob().getLine(0).getItem().getID());
			nextItemRoute.setInteger("Item Count", robot.getCurrentJob().getLine(0).getCount());
			nextItemRoute.setString("direction", TranslateRouteToCommands.intToString(direction));
			nextItemRoute.setInteger("Route Length", robot.getRoute().size());
			robot.getSocket().sendCommand(nextItemRoute);
			System.out.println(TranslateRouteToCommands.intToString(direction));
			break;
		}
	}

	@Override
	public void onConnect(Socket socket) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDisconnect(Socket socket) {
		// TODO Auto-generated method stub
		
	}
	
	private void delay(long time){
		try{
			Thread.sleep(time);
		}catch(InterruptedException e){
		}
	}
}
