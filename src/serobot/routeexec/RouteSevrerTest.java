package serobot.routeexec;

import static org.junit.Assert.*;

import org.junit.Test;

import serobot.findroute.*;

public class RouteSevrerTest {

	@Test
	public void test() {
		long start_time = System.currentTimeMillis();
		Thread test = new Thread(){
			public void run(){
				RouteExecTest.main(new String[]{});
			}
		};
		test.start();
		delay(1000);
		boolean testing = true;
		RouteExecTest.RobotTest robot_A = RouteExecTest.getRobotA();
		RouteExecTest.RobotTest robot_B = RouteExecTest.getRobotB();
		RouteExecTest.RobotTest robot_C = RouteExecTest.getRobotC();
		RouteExecTest.ErrorNotifyTest error_notify = RouteExecTest.getErrorNotifyTest();
		while(testing){
			GridPoint A = robot_A.getLocation();
			GridPoint B = robot_B.getLocation();
			GridPoint C = robot_C.getLocation();
			assertFalse(A.samePoint(B)|| B.samePoint(C) || A.samePoint(C));
			
			long time = System.currentTimeMillis() - start_time;
			if(time/1000 < 20){
				assertTrue(error_notify.getCount() == 0);
			}
			
			delay(100);
			
			if(!test.isAlive()){
				testing = false;
			}
		}
		
		assertTrue(error_notify.getCount() == 2);
	}
	
	private static void delay(long time){
		try{
			Thread.sleep(time);
		}catch(InterruptedException e){
		}
	}

}
