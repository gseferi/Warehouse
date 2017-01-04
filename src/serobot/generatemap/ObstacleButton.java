package serobot.generatemap;

import java.awt.Color;
import java.awt.event.*;

import javax.swing.JButton;

public class ObstacleButton extends JButton implements ActionListener{
	
	private int size;
	private boolean obstacle;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7410961093843753953L;

	public ObstacleButton(double x, double y, int size){
		super();
		this.size = size;
		this.obstacle = false;
		setBackground(Color.BLUE);
		this.setSize(size,size);
		this.setLocation((int)x-size/2,(int)y-size/2);
	}
	
	public void setLocation(double x, double y){
		this.setLocation((int)x-size/2,(int)y-size/2);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(!this.obstacle){
			this.obstacle = true;
			setBackground(Color.RED);
		}else{
			this.obstacle = false;
			setText("");
			setBackground(Color.BLUE);
		}
		revalidate();
		repaint();
	}
	
	public boolean isUnreachable(){
		return this.obstacle;
	}
}
