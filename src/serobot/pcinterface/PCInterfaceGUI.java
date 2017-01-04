package serobot.pcinterface;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import serobot.findroute.FindRoute;
import serobot.findroute.GridMap;
import serobot.findroute.GridPoint;
import serobot.findroute.Obstacle;
import serobot.jobparser.CSVFileReader;
import serobot.jobparser.Item;
import serobot.jobparser.ItemJoiner;
import serobot.jobparser.ItemPosition;
import serobot.jobparser.ItemReward;
import serobot.jobparser.Job;
import serobot.jobparser.Parser;
import serobot.jobselection.JobSorter;
import serobot.map.Map;
import serobot.routeexec.RouteExecTest.ErrorNotifyTest;
import serobot.routeexec.RouteServer;

/**
 * Initialises the PC Interface GUI.
 * Creates the warehouse map
 * Creates the objects for Multi Robot path finding
 * Creates a data object to store all the information.
 * Parses the job files and stores in the data model.
 * Displays the frame.
 * @author Aaquib Naved
 *
 */
public class PCInterfaceGUI {
	
	public static void main(String []args) {
		ArrayList<ItemReward> itemRewards = new ArrayList<ItemReward>();
		ArrayList<ItemPosition> itemPositions = new ArrayList<ItemPosition>();
		ArrayList<Item> items = new ArrayList<Item>();
		ArrayList<Job> jobs = new ArrayList<Job>();
		
		JFrame.setDefaultLookAndFeelDecorated(true);
		JFrame frame = new JFrame();
		frame.setSize(900, 600);
		frame.setLocationRelativeTo(null);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		List<Obstacle> obstacles = new ArrayList<Obstacle>();
		GridMap map = new GridMap(12,8,obstacles);
		FindRoute route_engine = new FindRoute(map);
		RouteServer route_server = new RouteServer(route_engine);
		ErrorNotifyTest error_notify = new ErrorNotifyTest();
		route_server.addErrorNotify(error_notify);
		
		Map guiMap = new Map();
		
		PCInterfaceData data = new PCInterfaceData(new ArrayList<Job>(), items, map, route_engine, route_server, new GridPoint(1, 2));
		PCInterfaceDataModel model = new PCInterfaceDataModel(data);
		InterfaceParser.parseFiles(model, "data/jobs.csv", "data/items.csv", "data/locations.csv");
		BluetoothReceiver receiver = new BluetoothReceiver(model);
		PCInterfaceComponent comp = new PCInterfaceComponent(model, receiver, guiMap);	
		frame.add(comp);
		//frame.pack();		
		frame.setVisible(true);
	}
}
