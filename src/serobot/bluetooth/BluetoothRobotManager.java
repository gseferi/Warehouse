package serobot.bluetooth;

import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;

import java.io.IOException;
import java.util.ArrayList;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;

/**
 * Allows you to find and initiate connections to robots from the computer.
 * 
 * @author Tom Galvin
 */
public class BluetoothRobotManager {
	public static final NXTInfo ULTRON = new NXTInfo(NXTCommFactory.BLUETOOTH, "Ultron","0016531B594D");
	public static final NXTInfo ROBBIE = new NXTInfo(NXTCommFactory.BLUETOOTH, "Robbie","001653156386");
	public static final NXTInfo ANTMAR = new NXTInfo(NXTCommFactory.BLUETOOTH, "AntMar","0016530A61AF");
	
	/**
	 * Create a new bluetooth robot manager.
	 */
	public BluetoothRobotManager() {
	}
	
	/**
	 * Creates an {@link NXTComm} object for use with Bluetooth communication.
	 * 
	 * @return A new NXTComm object, set to use Bluetooth as the protocol.
	 */
	private NXTComm createNXTComm() throws CommException {
		try {
            return NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
		} catch(NXTCommException e) {
			throw new CommException(e.getMessage(), e);
		}
	}

	/**
	 * Gets the {@link NXTInfo} corresponding to an NXT with a known name and
	 * MAC address.
	 * 
	 * @param name The name of the NXT (eg. {@code "Ultron"}).
	 * @param mac The MAC address of the NXT (eg. {@code "00:16:53:1B:59:4D"} or
	 * {@code "0016531b594d"}).
	 * @return The {@link NXTInfo} corresponding to the robot with the given
	 *         name and MAC address.
	 */
	public NXTInfo getNXT(String name, String mac) {
		return new NXTInfo(NXTCommFactory.BLUETOOTH, name, mac.replace(":", "").toUpperCase().trim());
	}

	/**
	 * Get all the NXTs in the area whose name contain the string {@code name}.
	 * 
	 * @param name A filter with which to search for NXTs.
	 * @return A list of all of the NXTs in the local vicinity.
	 * @throws CommException
	 *             When something goes wrong with the NXT connection.
	 */
	public NXTInfo[] getAllNXTs(String name) throws CommException {
		try {
			NXTComm comm = createNXTComm();
			NXTInfo[] nxts = comm.search(null);

			ArrayList<NXTInfo> names = new ArrayList<NXTInfo>();
			for (int i = 0; i < nxts.length; i++) {
				if (nxts[i].name.contains(name)) {
					names.add(nxts[i]);
				}
			}
			comm.close();
			return names.toArray(new NXTInfo[0]);
		} catch (NXTCommException e) {
			throw new CommException(e);
		} catch(IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * Get all the NXTs in the area.
	 * 
	 * @return A list of all of the NXTs in the local vicinity.
	 * @throws CommException
	 *             When something goes wrong with the NXT connection.
	 */
	public NXTInfo[] getAllNXTs() throws CommException {
		return getAllNXTs("");
	}

	/**
	 * Create a connection to the robot with the given name.
	 * 
	 * @param info
	 *            The identity of the robot to connect to.
	 * @return A socket with which to communicate with the robot.
	 * @throws CommException
	 *             When something goes wrong during initiation of the
	 *             connection.
	 */
	public RobotSocket createConnection(NXTInfo info) {
		NXTComm comm = createNXTComm();
		BluetoothRobotSocket socket = new BluetoothRobotSocket(info, comm);
		return socket;
	}
}
