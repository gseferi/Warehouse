package serobot.map;

import static org.junit.Assert.assertTrue;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;

import org.junit.Test;

public class RotatableShape {

	public static Polygon drawRectangle(int x, int y, int width, int height, Graphics g) {
		return drawRectangle(x, y, width, height, g, 0);
	}
	
	//JUnit test for the drawRectangle Function
	@Test
	public void drawRectangleTest() {
		Polygon polygon = drawRectangle(0, 0, 10, 10, null, 0);
		assertTrue(polygon.xpoints.length == 4 + 1);
		assertTrue(polygon.ypoints.length == 4 + 1);
		assertTrue(polygon.xpoints[0] == 0);
		assertTrue(polygon.ypoints[0] == 10);
		assertTrue(polygon.xpoints[1] == 10);
		assertTrue(polygon.ypoints[1] == 10);
		
	}

	public static Polygon drawRectangle(int x, int y, int Width, int Height, Graphics g, int a) {
		double A = Math.toRadians(a);
		int UL = (int) (x + (Width / 2) * Math.cos(A) - (Height / 2) * Math.sin(A)) + (Width / 2);
		int ULY = (int) (y + (Height / 2) * Math.cos(A) + (Width / 2) * Math.sin(A)) + (Height / 2);
		int UR = (int) (x - (Width / 2) * Math.cos(A) - (Height / 2) * Math.sin(A)) + (Width / 2);
		int URY = (int) (y + (Height / 2) * Math.cos(A) - (Width / 2) * Math.sin(A)) + (Height / 2);
		int BL = (int) (x + (Width / 2) * Math.cos(A) + (Height / 2) * Math.sin(A)) + (Width / 2);
		int BLY = (int) (y - (Height / 2) * Math.cos(A) + (Width / 2) * Math.sin(A)) + (Height / 2);
		int BR = (int) (x - (Width / 2) * Math.cos(A) + (Height / 2) * Math.sin(A)) + (Width / 2);
		int BRY = (int) (y - (Height / 2) * Math.cos(A) - (Width / 2) * Math.sin(A)) + (Height / 2);

		int xPoly[] = { UR, UL, BL, BR, UR };
		int ypoly[] = { URY, ULY, BLY, BRY, URY };
		Polygon shape = new Polygon(xPoly, ypoly, xPoly.length);

		return shape;
	}
}
