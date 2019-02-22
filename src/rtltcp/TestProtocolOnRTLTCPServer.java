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


// this has it's own main method and can directly be run
// you must include the correct IP address of your rtl_tcp server in the sourcecode
// it tests the different commands and you can crosscheck on the rtl_tcp output if command occur as expected
// 
public class TestProtocolOnRTLTCPServer {

	
	private SampleFIFOBuffer buffer = new SampleFIFOBuffer();
	private TcpClient tcpClient = new TcpClient("192.168.123.123", 1234, buffer);
	private RtlTcpCommandMessageFactory factory = new RtlTcpCommandMessageFactory(tcpClient);
	
	public TestProtocolOnRTLTCPServer() {
		
	}
	
	public void runtest() throws InterruptedException {
		RtlTcpCommandMessageFactory factory = new RtlTcpCommandMessageFactory();
		this.sendGenericCommand(0x01, 200000000);
		this.sendGenericCommand(0x02, 200000000);
		this.sendGenericCommand(0x03, 1);
		this.sendGenericCommand(0x04, 1);
		this.sendGenericCommand(0x05, 1);
		this.sendGenericCommand(0x06, 1);
		this.sendGenericCommand(0x07, 1);
		this.sendGenericCommand(0x08, 1);
		this.sendGenericCommand(0x09, 1);
		this.sendGenericCommand(0x0a, 1);
		this.sendGenericCommand(0x0b, 1);
		this.sendGenericCommand(0x0c, 1);
		this.sendGenericCommand(0x0d, 1);
		this.sendGenericCommand(0x0e, 1);
		this.sendGenericCommand(0x0f, 1);
		this.sendGenericCommand(0x10, 1);
		this.sendGenericCommand(0x11, 1);
		this.sendGenericCommand(0x12, 1);
		this.sendGenericCommand(0x13, 1);
		this.sendGenericCommand(0x14, 1);
		this.sendGenericCommand(0x15, 1);
		this.sendGenericCommand(0x16, 1);
		this.sendGenericCommand(0x17, 1);
		this.sendGenericCommand(0x18, 1);
		this.sendGenericCommand(0x19, 1);
		this.sendGenericCommand(0x1a, 1);
		this.sendGenericCommand(0x1b, 1);
		this.sendGenericCommand(0x1c, 1);
		this.sendGenericCommand(0x1d, 1);
		this.sendGenericCommand(0x1e, 1);
		this.sendGenericCommand(0x1f, 1);
		this.sendGenericCommand(0xf0, 1);
		this.sendGenericCommand(0xf1, 1);
		this.sendGenericCommand(0xf2, 1);
		this.sendGenericCommand(0xf3, 1);
		this.sendGenericCommand(0xff, 1);
		/*
		this.switchFrequencyMHz(50);
		this.switchFrequencyMHz(100);
		this.switchFrequencyMHz(150);
		this.switchFrequencyMHz(200);
		this.switchFrequencyMHz(250);
		this.switchFrequencyMHz(300);
		this.switchFrequencyMHz(350);
		this.switchFrequencyMHz(400);
		this.switchFrequencyMHz(450);
		this.switchFrequencyMHz(500);
		*/
	}

	
	
	public void sendGenericCommand(int command, int value) throws InterruptedException {
		System.out.format("Sending generic command %s value %s n",command, value);
		this.factory.genericCommand(command, value);
		System.out.format("Waiting 5 Seconds\n");
		Thread.sleep(5000);
	}
	
	public static void main(String[] args) throws InterruptedException {
		new TestProtocolOnRTLTCPServer().runtest();

	}

	

}
