package serobot.generatemap;

import java.awt.*;
import java.awt.geom.*;

import javax.swing.*;

public class SelectUnreachablePoints extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 887453409876548290L;
	
	private final int width;
	private final int height;
	private final ObstacleButton[][] buttons;
	
	public SelectUnreachablePoints(int width, int height, ObstacleButton[][] buttons){
		this.setLayout(null);
		this.width = width;
		this.height = height;
		this.buttons = buttons;
		
		//add buttons
		for(int i = 0; i < this.height; i++) for(int j = 0; j < this.width; j++){
			this.buttons[i][j] = new ObstacleButton((0.1+0.8*((double)j/(width-1)))*getWidth(),(0.1+0.8*((double)i/(height-1)))*getHeight(),20);
			this.buttons[i][j].addActionListener(this.buttons[i][j]);
			add(this.buttons[i][j]);
		}
	}
	
	
	protected void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D)g;
		g2.clearRect(0, 0, getWidth(), getHeight());
		
		//set buttons
		for(int i = 0; i < this.height; i++) for(int j = 0; j < this.width; j++){
			this.buttons[i][j].setLocation((0.1+0.8*((double)j/(width-1)))*getWidth(),(0.1+0.8*((double)i/(height-1)))*getHeight());
		}
		
		//call the super method to paint the buttons
		super.paintComponent(g);
		
		//draw the lines
		//print the grid lines
		int pheight = getHeight();
		int pwidth = getWidth();
		for(int i = 0; i < this.height; i++){
			g2.draw(new Line2D.Double(0.1*pwidth,0.1*pheight+i*(0.8*pheight/(this.height-1)),0.9*pwidth,0.1*pheight+i*(0.8*pheight/(this.height-1))));
		}
		for(int i = 0; i < this.width; i++){
			g2.draw(new Line2D.Double(0.1*pwidth+i*(0.8*pwidth/(this.width-1)),0.1*pheight,0.1*pwidth+i*(0.8*pwidth/(this.width-1)),0.9*pheight));
		}
	}
}
