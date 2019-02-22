/*
 *    Copyright (C) 2019
 *    Mathias.Kuefner@bmt-online.de
 *    Bayerische Medien Technik GmbH
 *
 *    This file is part of the javadab-cmdline program
 *    javadab-cmdline is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 2 of the License, or
 *    (at your option) any later version.
 *
 *    javadab-cmdline is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with javadab-cmdline; if not, write to the Free Software
 *    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package rtltcp;


import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.Instant;
import java.util.Arrays;


// this does the tcp connection to the rtl_tcp server
// it uses an inputStream it repacks in a dataStream (for simple float reading) and handles this in its own thread
// the output stream is used to send commands
// the buffer is used to store samples untill they are needed

public class TcpClient  {

	private String sourcehost;
	private int sourceport;
	private Socket socket;
	private InputStream inputStream;
	//private DataInputStream dataInputStream;
	private OutputStream outputStream;
	private Thread readThread;
	private boolean ending = false;
	private volatile boolean initfinished = false;
	private final Object initfinishedtrigger = new Object();
	private final SampleFIFOBuffer buffer;
	
	

	public TcpClient(String sourcehost, int sourceport, SampleFIFOBuffer buffer)  {
		System.out.format("TcpClient initialized to  %s %s \n",sourcehost,sourceport);
		this.sourcehost = sourcehost;
		this.sourceport = sourceport;
		this.buffer = buffer;

		
		this.startLifeCycleLoopInNewThread();
		
		// wait for other thread to start and do init
		synchronized(this.initfinishedtrigger) {
			while (this.initfinished==false) {
				try {
					System.out.println("mainthread waiting for initfinish");
					this.initfinishedtrigger.wait(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.out.println("mainthread continuing - inifished -");
		}
		
	}
	
	private void startLifeCycleLoopInNewThread() {
		Thread readThread = new Thread(new Runnable() {
			public void run() {
						lifecycleLoop();
						}
		});
		readThread.start();
	}
	
	
	private boolean initSocketAndStream() {

			System.out.println("initSocketAndStream ... ");
			try {
				this.socket = new Socket(this.sourcehost, this.sourceport);
				this.inputStream = this.socket.getInputStream();
				//this.dataInputStream = new DataInputStream(this.inputStream);
				
				
				System.out.println("interactive connection with outputStream");
				this.outputStream = this.socket.getOutputStream();
				System.out.format("outputStream: %s\n",this.outputStream);
				
				System.out.println(" ... ok");
				
				// inform waiting main thread
				synchronized(this.initfinishedtrigger) {
					this.initfinished = true;
					this.initfinishedtrigger.notify();
				}
				return true;
				
			} catch (UnknownHostException e1) {
				e1.printStackTrace();
				synchronized(this.initfinishedtrigger) {
					this.initfinished = true;
					this.initfinishedtrigger.notify();
				}
				return false;
			} catch (IOException e1) {
				e1.printStackTrace();
				synchronized(this.initfinishedtrigger) {
					this.initfinished = true;
					this.initfinishedtrigger.notify();
				}
				return false;
			}

	}
	
	private void closeSocket() {
		if (this.socket!=null) {
			if (!this.socket.isClosed()) {
				try {
					this.socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	// is called by the new thread and runs the readLoop
	// if something fails this loop is able to re-establish the tcp connection
	private void lifecycleLoop() {
		
		while(!this.ending) {
			this.initSocketAndStream();
			this.readLoop();
			this.closeSocket();
			try {
				Thread.sleep(250); // against too high retry rates if something fails (e.g. server temp down)
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
		
	private void readLoop() {
		//System.out.println("readLoop head");
		int amountread = 0;
	
		
		while((!this.socket.isInputShutdown()) && (amountread>-1) && (!this.ending)) {
			//System.out.println("readLoop whileloop head");	
			
			byte[] bytes;
			try {
					int available = this.inputStream.available();
					if (available >= 0) {
						bytes = new byte[available];
						int actuallyRead = this.inputStream.read(bytes);
						if (actuallyRead == -1) {
							System.out.println("END OF STREAM (-1) \n");
							return;
						}
						else if (actuallyRead == 0) {
							Thread.sleep(10);
							// nothing to store; avoid creating an empty list item in buffer!
						}
						else if (actuallyRead < available) {
							byte[] shorted = new byte[actuallyRead];
							System.arraycopy(bytes, 0, shorted, 0, actuallyRead);
							this.buffer.storeBytes(shorted);
						} else {
							this.buffer.storeBytes(bytes);
						}
					} else {
						Thread.sleep(10);
					}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("readLoop whileloop EXITED WITH IOEXCEPTION ---------------------- !!!!!!!!!!!!!!!!! ----------------");
				return;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("readLoop whileloop EXITED WITH InterruptedException ---------------------- !!!!!!!!!!!!!!!!! ----------------");
				return;
			}
			
			
		}
	}
	
	// for sending pre-encoded commands to the rtl_tcp server
	public void write(byte[] data) {
		try {
			this.outputStream.write(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void end() {
		this.ending = true;
	}
	
	

		

}
