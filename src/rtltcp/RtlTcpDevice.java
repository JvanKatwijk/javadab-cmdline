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

import devices.Device;


// tries to implement a Device according to the interface
// however, I did not fully understand the semantics of the Device interface
// and I do not fully understand the rtl_tcp protocol
// so maybe something is wrong here?

public class RtlTcpDevice implements Device {

	private int dataInputStream;
	private int gain;
	private boolean autogain;
	private String host;
	private int port;
	private int frequencyHz;
	
	private final RtlTcpCommandMessageFactory commandFactory;
	private final TcpClient tcpClient;
	private final SampleFIFOBuffer buffer;
		
	public RtlTcpDevice(String host, int port, int frequencyHz, int gain, boolean autogain) {
		System.out.format("init RtlTcpDevice(host=%s, port=%s, frequencyHz=%s, gain=%s, autogain=%s)\n",host,port,frequencyHz,gain,autogain);
		this.frequencyHz = frequencyHz;
		this.gain = gain;
		this.autogain = autogain;
		this.host = host;
		this.port = port;
		this.buffer = new SampleFIFOBuffer();
		this.tcpClient = new TcpClient(host, port,this.buffer);
		this.commandFactory = new RtlTcpCommandMessageFactory(this.tcpClient);
		this.commandFactory.setCenterFrequency(frequencyHz);
		this.commandFactory.setTunerGain(gain);
		this.commandFactory.setAGCmode(autogain); // is this the correct assoziation !?
		//this.buffer = this.connector.getCopyOfBufferedInputstream();
	}
	
	public RtlTcpCommandMessageFactory commandFactory() {
		return this.commandFactory;
	}
	
	@Override
	public void restartReader(int frequencyHz) { // was originally just "int f" and not so sure what it might be
		System.out.format("RtlTcpDevice.restartReader(%s)\n", frequencyHz);
		// not sure what shall be done here ... can only guess
		this.commandFactory.setCenterFrequency(frequencyHz); // guessed
		this.buffer.clear(); // guessed
	}

	@Override
	public void stopReader() {
		System.out.format("RtlTcpDevice.stopReader()\n");
		// WHAT SHOULD BE DO HERE ???? disconnect ???
	}

	@Override
	public void resetBuffer() {
		System.out.format("RtlTcpDevice.resetBuffer()\n");
		this.buffer.clear();
	}

	@Override
	public void setGain(int v) {
		System.out.format("RtlTcpDevice.setGain(%s)\n",v);
		this.commandFactory.setTunerGain(v);
	}

	@Override
	public void autoGain(boolean b) {
		System.out.format("RtlTcpDevice.autoGain(%s)\n",b);
		this.commandFactory.setAGCmode(b);
	}

	@Override
	public synchronized int samples() {
		int count = this.buffer.bufferedFloatCount()/2;
		//System.out.format("RtlTcpDevice.samples() => %s\n",count);  // verbose output here slows the processing alot!!!
		return (count);
	}

	@Override
	public synchronized int getSamples(float[] v, int amount) {
		//System.out.format("RtlTcpDevice.getSamples(float[].length = %s, amount = %s) => \n",v.length,amount); // verbose output here slows the processing alot!!!
		return this.buffer.consumeSamples(v,amount);
	}

	@Override
	public boolean is_nullDevice() {
		System.out.format("RtlTcpDevice.is_nullDevice() = false\n");
		return false;
	}

	@Override
	public boolean is_fileInput() {
		System.out.format("RtlTcpDevice.is_fileInput() = false\n");
		return false;
	}

}
