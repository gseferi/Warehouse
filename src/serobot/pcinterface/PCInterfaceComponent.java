package serobot.pcinterface;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;

import serobot.map.Map;
import serobot.map.Robot;
import serobot.map.Wall;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.PrintStream;

/**
 * Creates the component which holds all the elements of the GUI.
 * @author Aaquib Naved
 *
 */
public class PCInterfaceComponent extends JPanel {

		private PCInterfaceDataModel model;
		
		public PCInterfaceComponent(PCInterfaceDataModel model, BluetoothReceiver receiver, Map map)
		{
			super();
			this.model = model;
			
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
			//Robot robot = new Robot(Map.absoluteToRelativeWidth(2), Map.absoluteToRelativeHeight(5));
			
			//This command makes the map follow that particular robot
			//map.followRobot(robot, 1.5);
			
			//This adds the robot to the map
			//map.addRobot(robot);
			model.setMap(map);
			
			MenuPanel menuPanel = new MenuPanel(model);
			JPanel window = new JPanel();
			window.setLayout(new BorderLayout());
			RobotPanel robotPanel = new RobotPanel(model, receiver);
			OverridePanel overridePanel = new OverridePanel(model);
			JPanel leftPanel = new JPanel();
			JPanel mapAndJobs = new JPanel();
			mapAndJobs.setLayout(new BorderLayout());
			//Map map = new Map();
			//MapPanel mapPanel = new MapPanel();
			JPanel tableAndTabs = new JPanel();
			tableAndTabs.setLayout(new BorderLayout());
			TabPanel tabPanel = new TabPanel(model);
			TablePanel tablePanel = new TablePanel(model);
			//JLabel totalReward = new JLabel("Total Reward: " + model.getTotalReward());
			RewardPanel rewardPanel = new RewardPanel(model);
			
			model.addObserver(robotPanel);
			model.addObserver(rewardPanel);
			model.addObserver(tablePanel);
			
			setLayout(new BorderLayout());
			
			tableAndTabs.setLayout(new BorderLayout());
			tableAndTabs.add(tabPanel, BorderLayout.NORTH);
			tableAndTabs.add(tablePanel, BorderLayout.CENTER);
			mapAndJobs.setLayout(new BorderLayout());
			mapAndJobs.add(map, BorderLayout.CENTER);
			mapAndJobs.add(tableAndTabs, BorderLayout.SOUTH);
			leftPanel.setLayout(new BorderLayout());
			leftPanel.add(robotPanel, BorderLayout.NORTH);
			leftPanel.add(rewardPanel, BorderLayout.CENTER);
			leftPanel.add(overridePanel, BorderLayout.SOUTH);
			window.setLayout(new BorderLayout());
			window.add(leftPanel, BorderLayout.WEST);
			window.add(mapAndJobs, BorderLayout.CENTER);
			menuPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
			add(menuPanel, BorderLayout.NORTH);
			add(window, BorderLayout.CENTER);
		}
}
