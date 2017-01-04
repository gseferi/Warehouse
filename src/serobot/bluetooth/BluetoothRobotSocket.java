package serobot.bluetooth;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTInfo;

/**
 * A bluetooth implementation of a socket to facilitate computer-robot
 * communications.
 * 
 * @author Tom Galvin
 */
public class BluetoothRobotSocket implements RobotSocket {
	private NXTInfo info;
	protected NXTComm comm;
	private ArrayList<SocketListener> listeners;
	private DataInputStream inputStream;
	private DataOutputStream outputStream;
	private Thread receiveThread;
	private volatile boolean open;
	
	/**
	 * Creates a new BluetoothSocket.
	 * 
	 * @param info An {@link NXTInfo} representing the identity of the NXT
	 * brick being communicated with.
	 * @param comm An {@link NXTComm} object which manages the underlying
	 * connection.
	 */
	public BluetoothRobotSocket(NXTInfo info, NXTComm comm) {
		this.info = info;
		this.comm = comm;
		this.listeners = new ArrayList<SocketListener>();
	}
	
	@Override
	public void open() throws CommException {
		if(!this.open) {
			try {
				if(this.comm.open(info)) {
					this.inputStream = new DataInputStream(this.comm.getInputStream());
					this.outputStream = new DataOutputStream(this.comm.getOutputStream());
					this.open = true;
					
					(receiveThread = new Thread(new ReceiveThreadBody())).start();
				} else {
					throw new CommException("Could not open the Bluetooth connection.");
				}
			} catch(NXTCommException e) {
				throw new CommException(e);
			}
		}
	}
	
	@Override
	public boolean isOpen() {
		return open;
	}
	
	@Override
	public void close() {
		if(this.open) {
			this.open = false;
			if(this.receiveThread.isAlive()) {
				this.receiveThread.interrupt();
			}
			
			onDisconnect();
		}
	}
	
	@Override
	public void addListener(SocketListener listener) {
		synchronized(listeners) {
			if(!listeners.contains(listener)) {
				listeners.add(listener);
			} else {
				throw new IllegalArgumentException(
						"The given listener is already listening to this socket.");
			}
		}
	}

	@Override
	public void removeListener(SocketListener listener) {
		if(listeners.contains(listener)) {
			synchronized (listeners) {
				listeners.remove(listener);
			}
		} else {
			throw new IllegalArgumentException(
					"The given listener is not listening to this socket."
					);
		}
	}
	

	
	/**
	 * Invoked internally when the socket connects.
	 */
	protected void onConnect() {
		synchronized (listeners) {
			for(SocketListener listener : listeners) {
				listener.onConnect(this);
			}
		}
	}
	
	/**
	 * Invoked internally when a command is received from the remote end
	 * point.
	 * 
	 * @param command The command that was received.
	 */
	protected void onCommandReceived(Command command) {
		synchronized (listeners) {
			for(SocketListener listener : listeners) {
				listener.onCommandReceived(this, command);
			}	
		}
	}
	
	/**
	 * Invoked internally when the socket disconnects.
	 */
	protected void onDisconnect() {
		synchronized (listeners) {
			for(SocketListener listener : listeners) {
				listener.onDisconnect(this);
			}
		}
	}

	@Override
	public void sendCommand(Command command) {
		if(isOpen()) {
			try {
				String commandString = command.toString();
				outputStream.writeUTF(commandString);
				outputStream.flush();
			} catch(IOException e) {
				close();
			}
		} else {
			throw new CommException("Socket not open.");
		}
	}

	@Override
	public String getRobotName() {
		return info.name;
	}

	@Override
	public String getRobotID() {
		return info.deviceAddress;
	}
	
	/**
	 * Controls the thread for asynchronously receiving data.
	 * 
	 * @author Tom Galvin
	 */
	private class ReceiveThreadBody implements Runnable {
		@Override
		public void run() {
			onConnect();
			
			try {
				while(open) {
					String commandString = inputStream.readUTF();
					Command command = Command.fromString(commandString);
					onCommandReceived(command);
				}
			} catch(IOException e) {
				/*
				 * IOException means the socket has been closed, so just
				 * gracefully exit the thread.
				 */
			} finally {
				close();
			}
		}
	}
}
