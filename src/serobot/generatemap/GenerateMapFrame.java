package serobot.generatemap;

import java.awt.*;
import java.util.*;

import javax.swing.*;

import serobot.findroute.*;

/**
 * The Frame for the map generation
 */
public class GenerateMapFrame extends JFrame{
	
	/**
	 * Serial ID preventing from warnings
	 */
	private static final long serialVersionUID = -4531257704112805611L;
	private final JButton confirm;
	private SelectUnreachablePoints map;
	private ObstacleButton[][] unreachablepoints;

	/**
	 * Construct a generate map frame
	 */
	public GenerateMapFrame(){
		//set the layout
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(800,600);
		setLocationRelativeTo(null);
		setTitle("Warehouse");
		setLayout(new BorderLayout());
		
		//create the top panel
		JPanel toppanel = new JPanel();
		toppanel.setLayout(new BorderLayout());
		add(toppanel,BorderLayout.NORTH);
		JLabel sizelabel = new JLabel("Size(in width,height format):");
		JTextField size = new JTextField("");
		JButton sizebutton = new JButton("Next step");
		sizebutton.addActionListener(e -> createMap(size.getText()));
		toppanel.add(sizelabel,BorderLayout.WEST);
		toppanel.add(size, BorderLayout.CENTER);
		toppanel.add(sizebutton, BorderLayout.EAST);
		
		//create bottom panel
		JPanel bottompanel = new JPanel();
		//bottompanel.setLayout(new BorderLayout());
		add(bottompanel,BorderLayout.SOUTH);
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(e -> dispose());
		confirm = new JButton("Create map");
		confirm.addActionListener(e -> generateMap());
		confirm.setEnabled(false);
		bottompanel.add(confirm);
		bottompanel.add(cancel);
		
		//initialize null for map
		this.map = null;
		this.unreachablepoints = null;
	}
	
	/**
	 * Create a new map
	 */
	public void createMap(String size){
		//parse the input
		String[] dimension = size.split(",");
		int width = 0;
		int height = 0;
		try{
			width = Integer.parseInt(dimension[0]);
			height = Integer.parseInt(dimension[1]);
		}catch(Exception e){
			//wrong input format
			//create frame
			JFrame frame = new JFrame();
			frame.setSize(300, 200);
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.setLocationRelativeTo(null);
			frame.setTitle("Warehouse");
			frame.setLayout(new BorderLayout());
			frame.add(new JLabel("Wrong input format"),BorderLayout.CENTER);
			//create close button
			JButton close = new JButton("Close");
			close.addActionListener(e2 -> frame.dispose());
			frame.add(close,BorderLayout.SOUTH);
			frame.setVisible(true);
			return;
		}
		
		//create the map for obstacles selection
		if(this.map != null){
			this.remove(this.map);
		}
		this.unreachablepoints = new ObstacleButton[height][width];
		this.map = new SelectUnreachablePoints(width,height,this.unreachablepoints);
		add(this.map,BorderLayout.CENTER);
		
		//enable confirm button
		this.confirm.setEnabled(true);
		
		//repaint
		this.revalidate();
		this.repaint();
	}

	public void generateMap(){
		java.util.List<Obstacle> obstacles = new ArrayList<Obstacle>();
		int height = this.unreachablepoints.length;
		int width = this.unreachablepoints[0].length;
		for(int i = 0; i < height; i++) for(int j = 0; j < width; j++){
			ObstacleButton button = this.unreachablepoints[i][j];
			if(button.isUnreachable()){
				obstacles.addAll(Obstacle.generateObstacles(new GridPoint(j,height-1-i)));
			}
		}
		GridMap map = new GridMap(this.unreachablepoints[0].length,this.unreachablepoints.length,obstacles);
		System.out.println(map);
	}
}
