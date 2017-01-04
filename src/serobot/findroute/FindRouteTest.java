package serobot.findroute;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class FindRouteTest {

	@Parameters
	public static Collection<Object[]> data(){
		return Arrays.asList(new Object[][]{
			{new RouteJob(new GridPoint(0,0), new GridPoint(7,8), 5),"[(0,1), (0,2), (0,3), (0,4), (0,5), (0,6), (0,7), (0,8), (1,8), (2,8), (3,8), (4,8), (5,8), (6,8), (7,8)]"},
			{new RouteJob(new GridPoint(0,0), new GridPoint(7,8),15),"[(1,0), (2,0), (3,0), (4,0), (5,0), (6,0), (7,0), (7,1), (7,2), (7,3), (7,4), (7,5), (7,6), (7,7), (7,8)]"},
			{new RouteJob(new GridPoint(0,0), new GridPoint(7,8),25),"[(0,1), (0,2), (0,3), (0,4), (0,5), (0,6), (0,7), (1,7), (2,7), (3,7), (4,7), (5,7), (6,7), (7,7), (7,8)]"},
			{new RouteJob(new GridPoint(0,0), new GridPoint(9,9),3),"PointOutOfBoundaryException"},
			{new RouteJob(new GridPoint(12,12), new GridPoint(9,9),3),"PointOutOfBoundaryException"},
			{new RouteJob(new GridPoint(0,0), new GridPoint(7,0),25),"RouteNotFoundException"},
			{new RouteJob(new GridPoint(0,0), new GridPoint(7,0),25),"[(1,0), (2,0), (2,1), (3,1), (4,1), (5,1), (6,1), (7,1), (7,0)]"}
		});
	}
	
	private GridPoint[] fInput;
	private String fExpected;
	private FindRoute route_engine;
	private GridMap map;
	private int priority;
	private static int test_count = 0;
	
	public FindRouteTest(RouteJob job, String expected){
		this.fInput = new GridPoint[2];
		this.fInput[0] = job.getStart();
		this.fInput[1] = job.getEnd();
		this.fExpected = expected;
		List<Obstacle> obstacles = new ArrayList<Obstacle>();
		if(test_count == 5){
			obstacles.add(new Obstacle(new GridPoint(7,0),new GridPoint(7,1)));
			obstacles.add(new Obstacle(new GridPoint(6,0),new GridPoint(7,0)));
		}
		this.map = new GridMap(8,9,obstacles);
		
		//One robot with priority 10 at (3,8)
		this.map.getLink(new GridPoint(3,8), new GridPoint(3,7)).get().setOccupiedPriority(10);
		this.map.getLink(new GridPoint(2,8), new GridPoint(3,8)).get().setOccupiedPriority(10);
		this.map.getLink(new GridPoint(3,8), new GridPoint(4,8)).get().setOccupiedPriority(10);
		
		//One robot with priority 20 at (3,0)
		this.map.getLink(new GridPoint(3,0), new GridPoint(3,1)).get().setOccupiedPriority(20);
		this.map.getLink(new GridPoint(2,0), new GridPoint(3,0)).get().setOccupiedPriority(20);
		this.map.getLink(new GridPoint(3,0), new GridPoint(4,0)).get().setOccupiedPriority(20);
		
		this.route_engine = new FindRoute(this.map);
		
		this.priority = job.getPriority();
	}
	
	@Test
	public void test() throws RouteNotFoundException, PointOutOfBoundaryException{
		try{
			assertEquals(fExpected,this.route_engine.getRoute(this.fInput[0], this.fInput[1],priority).toString());
		}catch(PointOutOfBoundaryException e){
			if(test_count == 3 || test_count == 4){
				assertTrue(true);
			}else{
				assertTrue(false);
			}
		}catch(RouteNotFoundException e){
			if(test_count == 5){
				assertTrue(true);
			}else{
				assertTrue(false);
			}
		}
	}
	
	@After
	public void after(){
		test_count++;
	}
	
	private static class RouteJob{
		private GridPoint start;
		private GridPoint end;
		private int priority;
		
		public RouteJob(GridPoint start, GridPoint end, int priority){
			this.start = start;
			this.end = end;
			this.priority = priority;
		}
		
		public GridPoint getStart(){
			return this.start;
		}
		
		public GridPoint getEnd(){
			return this.end;
		}
		
		public int getPriority(){
			return this.priority;
		}
	}

}
