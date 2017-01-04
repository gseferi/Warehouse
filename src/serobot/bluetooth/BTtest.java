package serobot.bluetooth;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;

public class BTtest implements Runnable{
	
	private DataInputStream m_dis;
	private DataOutputStream m_dos;
	private final int m_start;
	private final int m_count;
	private final NXTInfo m_nxt;
	private final static Object m_lock = new Object();

	
	public BTtest(NXTInfo _nxt, int _start, int _count) {
		m_start = _start;
		m_count = _count; 
		m_nxt = _nxt;
	}

	public boolean connect(NXTComm _comm) throws NXTCommException {
		if (_comm.open(m_nxt)) {

			m_dis = new DataInputStream(_comm.getInputStream());
			m_dos = new DataOutputStream(_comm.getOutputStream());
		}
		return isConnected();
	}

	public boolean isConnected() {
		return m_dos != null;
	}

	@Override
	public void run() {

		try {

				synchronized (m_lock) {
					m_dos.writeUTF("a string for testing");
					m_dos.flush();
				}

				synchronized (m_lock) {
					String answer = m_dis.readUTF();
					System.out.println(answer);
				}
			
			
			System.out.println(m_nxt.name + " checked out ok");
			
			return;

		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(m_nxt.name + " was a problem");

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			NXTComm nxtComm = NXTCommFactory
					.createNXTComm(NXTCommFactory.BLUETOOTH);

			NXTInfo[] nxts = {
					new NXTInfo(NXTCommFactory.BLUETOOTH, "Ultron","0016531B594D")};

			Random rand = new Random();

			ArrayList<BTtest> connections = new ArrayList(
					nxts.length);
			for (NXTInfo nxt : nxts) {
				connections.add(new BTtest(nxt, rand.nextInt(), 20));
			}

			for (BTtest connection : connections) {
				connection.connect(nxtComm);
			}

			ArrayList<Thread> threads = new ArrayList(nxts.length);

			for (BTtest connection : connections) {
				threads.add(new Thread(connection));
			}

			for (Thread thread : threads) {
				thread.start();
			}

			for (Thread thread : threads) {
				try {
					thread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		} catch (NXTCommException e) {
			e.printStackTrace();
		}

	}

}
