package serobot.findroute;

import java.util.*;

public class TimeStep extends Thread {
	
	//attributes
	private final List<List<Reservation>> reservations;
	private final int size;
	private int startingindex;
	private final Object lock;
	private final long unittime;
	private boolean running;
	private long startlooptime;
	
	/**
	 * Construct a TimeStep object
	 * @param size number of time step for reservation table
	 */
	public TimeStep(int size, long unittime){
		//initialize the reservations
		this.size = size;
		this.reservations = new ArrayList<List<Reservation>>(this.size);
		this.startingindex = 0;
		this.lock = new Object();
		initializeSublist();
		this.unittime = unittime;
		this.running = false;
	}
	
	public Object getLock(){
		return this.lock;
	}
	
	private void initializeSublist(){
		for(int i = 0; i < this.size; i++){
			this.reservations.add(new LinkedList<Reservation>());
		}
	}
	
	//running method of the thread
	public void run(){
		//change running status to true
		this.running = true;
		
		//sleep for one time unit
		delay(this.unittime);
		
		while(this.running){
			//get current time
			this.startlooptime = System.currentTimeMillis();
			
			//shift time step
			synchronized(this.lock){
				this.reservations.get(this.startingindex++).clear();
				if(this.startingindex >= this.reservations.size()){
					//no out of bounds allowed
					this.startingindex -= this.reservations.size();
				}
			}
			
			//sleep
			sleep();
		}
	}
	
	public void end(){
		this.running = false;
	}
	
	//sleep after each loop
	private void sleep(){
		long sleeptime = this.unittime - (System.currentTimeMillis() - this.startlooptime);
		if(sleeptime <= 0){
			//running longer than expected
			return;
		}
		
		//sleep
		delay(sleeptime);
	}
	
	//delay for a certain time
	private void delay(long time){
		try{
			sleep(time);
		}catch(InterruptedException e){
			//won't happen
		}
	}
	
	public void addReservation(int timestep, GridPoint point, int priority){
		int index = this.startingindex + timestep;
		if(index >= this.size){
			index -= this.size;
		}
		this.reservations.get(index).add(new Reservation(priority, point));
	}
	
	public List<Reservation> getReservationOfTime(int timestep){
		int index = this.startingindex + timestep;
		if(index >= this.size){
			index -= this.size;
		}
		return this.reservations.get(index);
	}
	
	List<List<Reservation>> getReservations(){
		return this.reservations;
	}
	
	public void clearTimeSteps(){
		synchronized(this.lock){
			for(List<Reservation> reservation : this.reservations){
				reservation.clear();
			}
		}
	}
}
