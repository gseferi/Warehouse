package serobot.pcinterface;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTInfo;
import serobot.bluetooth.BluetoothRobotManager;
import serobot.bluetooth.Command;
import serobot.bluetooth.CommException;
import serobot.bluetooth.RobotSocket;
import serobot.findroute.GridPoint;
import serobot.jobparser.Job;
import serobot.routeexec.CannotAddRobotException;
import serobot.routeexec.RouteExecTest.ErrorNotifyTest;
import serobot.routeexec.RouteServer;

/**
 * A panel of the PC GUI which contains the Table of Robots, the connect, disconnet, start and stop buttons.
 * The "Find Robots" button displays a list of surrounding robots in a table
 * The "Connect" button opens a socket to the robot the user has selected in the table.
 * The "Disconnect" button closes the socket to the robot the user has selected in the table.
 * The "Start" button starts the route execution with the connected robots.
 * The "Stop" button stops the route execution and resets all the variables.
 * @author Aaquib Naved
 *
 */
public class RobotPanel extends JPanel implements Observer {

	private PCInterfaceDataModel model;
	private DefaultTableModel robotsModel;
	private JTable robots;
	private BluetoothReceiver receiver;
	public String selectedRobot;
	public Collection<String> robotNames = new ArrayList<String>();
	
	public RobotPanel(PCInterfaceDataModel model, BluetoothReceiver receiver) {
		super();
		this.model = model;
		
		robotsModel = new NonEditableTableModel();
		
		robots = new JTable(robotsModel);
		robotsModel.addColumn("Robots");
		robotsModel.addColumn("Connected");
		
		for (String robotName : model.getAllRobots().keySet()) {
			robotsModel.addRow(new Object[]{"Name: " + robotName, model.getRobot(robotName).isConnected()});
		}
		robots.setPreferredScrollableViewportSize(new Dimension(200, 100));
		JScrollPane jobListPane = new JScrollPane(robots);
		
		JButton findRobots = new JButton("Find Robots");
		findRobots.addActionListener(a -> {
			try {
				BluetoothRobotManager manager = new BluetoothRobotManager();
				ArrayList<RobotData> robots = new ArrayList<RobotData>();
				model.resetRobotDatabase();
				
				NXTInfo bot = manager.ULTRON;
				NXTInfo bot1 = manager.ROBBIE;
				
				System.out.println("Found robot.");
				System.out.println(bot.name);
				System.out.println(bot.deviceAddress);

				RobotData robotData = new RobotData(new GridPoint(0, 1), model.getRouteEngine(), model.getRouteServer(), bot.name, bot, false);
				RobotData robotData1 = new RobotData(new GridPoint(8, 1), model.getRouteEngine(), model.getRouteServer(), bot1.name, bot1, false);
				robots.add(robotData);
				robots.add(robotData1);
				model.addRobot(bot.name, robotData);
				model.addRobot(bot1.name, robotData1);
				model.setRobotList(robots);
			} catch(CommException e) {
				e.printStackTrace();
			}
		});
		
		JButton connect = new JButton("Connect");
		connect.addActionListener(a -> {
			try {
				if (selectedRobot != null) {
					BluetoothRobotManager manager = new BluetoothRobotManager();
					RobotData data = model.getRobot(selectedRobot);
					RobotSocket s = manager.createConnection(data.getRobot());
					s.addListener(receiver);
					s.open();
					data.setSocket(s);
					data.setConnected(true);
					model.update();
					model.getMap().addRobot(data);
					System.out.println(selectedRobot + " connected.");
				}
			} catch(CommException e) {
				e.printStackTrace();
			}
		});
		
		JButton disconnect = new JButton("Disconnect");
		disconnect.addActionListener(a -> {
			if (selectedRobot != null) {
				RobotData data = model.getRobot(selectedRobot);
				data.getSocket().close();
				data.setSocket(null);
				data.setConnected(false);
				model.update();
				data.shutdown();
				data.interrupt();
				System.out.println(selectedRobot + " disconnected.");
				
			}
		});
		
		JButton start = new JButton("Start");
		JButton stop = new JButton("Stop");
		stop.setEnabled(false);
		
		start.addActionListener(a -> {
			boolean anyConnected = false;
			if (model.getAllRobots().size() != 0) {
				for (int i = 0; i < model.getRobotList().size(); i++) {
					if (model.getRobotList().get(i).isConnected()) {
						anyConnected = true;
					}
				} 
			}
			if (model.getAllRobots().size() != 0 && anyConnected) {
				for(int i = 0; i < model.getRobotList().size(); i++) {
					model.getRobotList().get(i).setRobotPriority(10 + 10*i);
					model.getRobotList().get(i).start();
					try {
						model.getRouteServer().addRobot(model.getRobotList().get(i));
					} catch (CannotAddRobotException e) {
					}
				}
				model.getRouteServer().start();
				//Job nextJob = model.getUpcomingJobs().get(0);
				//ArrayList<Job> newOngoingJobs = new ArrayList<Job>();
				//newOngoingJobs.add(nextJob);
				//model.setOngoingJobs(newOngoingJobs);
				//model.getUpcomingJobs().remove(0);
				delay(2000);
				
				for(int i = 0; i<model.getAllRobots().size(); i++) {
					if (model.getRobotList().get(i).isConnected()) {
						RobotData robot = model.getRobotList().get(i);
						
						Job newJob1 = new Job(model.getUpcomingJobs().get(0).getID());
						for (int x = 0; x<model.getUpcomingJobs().get(0).getLines().size(); x++) {
							newJob1.addLine(model.getUpcomingJobs().get(0).getLine(x));
						}
						
						robot.setCurrentJob(model.getUpcomingJobs().get(0));
						robot.setJobWithItems(newJob1);
						System.out.println("New Job Lines: " + robot.getCurrentJob().getLines().size());
						
						model.getOngoingJobs().add(model.getUpcomingJobs().get(0));
						model.getUpcomingJobs().remove(0);
						
						System.out.println("ID: " + model.getOngoingJobs().get(0).getLine(0).getItem().getID());
						System.out
								.println("X: " + model.getOngoingJobs().get(0).getLine(0).getItem().getXPosition());
						System.out
								.println("Y: " + model.getOngoingJobs().get(0).getLine(0).getItem().getYPosition());
						robot.setDestination(
								new GridPoint(robot.getCurrentJob().getLine(0).getItem().getXPosition(),
										robot.getCurrentJob().getLine(0).getItem().getYPosition()));
						int direction = TranslateRouteToCommands.translateNextPoint(robot, robot.getLocation(),
								robot.getNextLocation().get(), robot.getDirection());

						Command nextItemRoute = new Command("nextItemRoute");
						nextItemRoute.setString("Job ID",robot.getCurrentJob().getID());
						nextItemRoute.setString("Item ID",
								robot.getCurrentJob().getLine(0).getItem().getID());
						nextItemRoute.setInteger("Item Count", robot.getCurrentJob().getLine(0).getCount());
						nextItemRoute.setString("direction", TranslateRouteToCommands.intToString(direction));
						nextItemRoute.setInteger("Route Length", robot.getRoute().size());
						robot.getSocket().sendCommand(nextItemRoute);
						//robot.setMoveCompleted(true);
						System.out.println("Current Location: " + robot.getLocation());
						System.out.println("Next Location: " + robot.getNextLocation());
						System.out.println(TranslateRouteToCommands.intToString(direction));
						delay(15000);
					}
				}
				start.setEnabled(false);
				stop.setEnabled(true);
				model.update();
			}
		});
		
		stop.addActionListener(a -> {
			if (model.getAllRobots().size() != 0) {
				for (int i = 0; i < model.getRobotList().size(); i++) {
					model.getRobotList().get(i).interrupt();
					model.getRobotList().get(i).shutdown();
					model.getRobotList().get(i).getSocket().close();
					model.getRobotList().get(i).setSocket(null);
					model.getRobotList().get(i).setConnected(false);
				}
				model.getRouteServer().interrupt();
				model.setRouteServer(new RouteServer(model.getRouteEngine()));
				model.getRouteServer().addErrorNotify(new ErrorNotifyTest());
			}
			start.setEnabled(true);
			stop.setEnabled(false);
		});
		
		setLayout(new BorderLayout());
		JPanel connectPanel = new JPanel();
		JPanel connectionButtons = new JPanel();
		connectPanel.setLayout(new BorderLayout());
		connectionButtons.add(connect);
		connectionButtons.add(disconnect);
		connectPanel.add(connectionButtons, BorderLayout.NORTH);
		connectPanel.add(start, BorderLayout.CENTER);
		connectPanel.add(stop, BorderLayout.SOUTH);
		add(findRobots, BorderLayout.NORTH);
		add(jobListPane, BorderLayout.CENTER);
		add(connectPanel, BorderLayout.SOUTH);
		
		robots.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		robots.getSelectionModel().addListSelectionListener(e -> {
			if (robots.getSelectionModel().getMinSelectionIndex() >= 0) {
				selectedRobot = model.getRobotList().get(robots.getSelectionModel().getMinSelectionIndex()).getRobot().name;
				model.setSelectedRobot(selectedRobot);
			}
			});
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if (robotNames != null) {
			int rowCount = robotsModel.getRowCount();
			//Remove rows one by one from the end of the table
			for (int i = rowCount - 1; i >= 0; i--) {
				robotsModel.removeRow(i);
			}
			for (String robotName : model.getAllRobots().keySet()) {
				robotsModel.addRow(new Object[] { "Name: " + robotName, model.getRobot(robotName).isConnected() });
			}
		}
		repaint();

	}
	
	private void delay(long time){
		try{
			Thread.sleep(time);
		}catch(InterruptedException e){
		}
	}

}
