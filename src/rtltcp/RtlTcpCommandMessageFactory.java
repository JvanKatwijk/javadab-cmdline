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


// includes the logic of the rtl_tcp stuff
// the messages to be sent to the rtl_tcp seem to be rather simple
// but I am still not totally sure I did all correctly
// However, the rtl_tcp server shows on its console the parameters that I would expect to appear there

public class RtlTcpCommandMessageFactory {

	private TcpClient attachedTcpClient;
	
	public RtlTcpCommandMessageFactory(TcpClient attachedTcpClient) {
		this.attachedTcpClient = attachedTcpClient;
	}
	
	public RtlTcpCommandMessageFactory() {
		this.attachedTcpClient = null;
	}
	
	public void setAttachment(TcpClient attachedConnector) {
		this.attachedTcpClient = attachedTcpClient;
	}
	
	private void autosendIfAttached(byte[] commandMessage) {
		if (commandMessage == null) { return; }
		if (this.attachedTcpClient == null) { return; }
		this.attachedTcpClient.write(commandMessage);
	}
	
	
	// format for command messages (as understood from a python source code)
	// ! 	network (= big-endian) 	standard 	none
	// B 	unsigned char 	integer 	1 bytes   (command)
	// I 	unsigned int 	integer 	4 bytes   (value)
		
	
	// attention ... unclear situation with signed/usigned .. use only 0 and positive low values to avoid problems!
	private byte[] encode(byte command, int value) {
		return Tools.concat( command, Tools.integerToBytes(value));								
	}
	
	// attention ... unclear situation with signed/usigned .. use only 0 and positive low values to avoid problems!
	private byte[] encode(int command, int value) {
		return encode((byte)command,value);								
	}
		
    public byte[] setCenterFrequency(int frequencyHz) { // 0x01
        System.out.format("set center frequency %s Hz\n",frequencyHz);
       	byte[] commandMessage = encode(0x1, frequencyHz); // not sure 
        this.autosendIfAttached(commandMessage);
        return commandMessage;
    }

    public byte[] setSampleRate(int frequencyHz) { // 0x02
       System.out.format("set sample rate %s Hz\n",frequencyHz);
       byte[] commandMessage = encode(0x2, frequencyHz); // not sure if Hz is expected here
       this.autosendIfAttached(commandMessage);
       return commandMessage;
    }
    
    
    public byte[] setTunerGainMode(boolean use) { // 0x03
    	return this.setTunerGainMode(use ? 1 : 0);
    }
    
    public byte[] setTunerGainMode(int mode) { // 0x03
        System.out.format("set tuner gain mode %s\n",mode);
       byte[] commandMessage = encode(0x3, mode);
        this.autosendIfAttached(commandMessage);
        return commandMessage;
    }
    
    
    
    public byte[] setTunerGain(int gain) { // 0x04
        System.out.format("set gain mode %s\n",gain);
       byte[] commandMessage = encode(0x4, gain);
        this.autosendIfAttached(commandMessage);
        return commandMessage;
    }
    
    public byte[] setFrequencyCorrection(int correction) { // 0x05
        System.out.format("set frequency correction %s\n",correction);
       byte[] commandMessage = encode(0x5, correction);
        this.autosendIfAttached(commandMessage);
        return commandMessage;
    }
    
    public byte[] setTunerIfGain(int gain) { // 0x06
        System.out.format("set tuner if gain %s\n",gain);
       byte[] commandMessage = encode(0x6, gain);
        this.autosendIfAttached(commandMessage);
        return commandMessage;
    }
    
    
    public byte[] setTestmode(boolean use) { // 0x07
    	return this.setTestmode(use ? 1 : 0);
    }
    
    public byte[] setTestmode(int val) { // 0x07
        System.out.format("set test mode %s\n",val);
       byte[] commandMessage = encode(0x7, val);
        this.autosendIfAttached(commandMessage);
        return commandMessage;
    }
    
    
    public byte[] setAGCmode(boolean use) { // 0x08
    	return this.setAGCmode(use ? 1 : 0);
    }
    
    public byte[] setAGCmode(int val) { // 0x08
        System.out.format("set agc mode %s",val);
       byte[] commandMessage = encode(0x8, val);
        this.autosendIfAttached(commandMessage);
        return commandMessage;
    }
    
    
    public byte[] setDirectSampling(boolean use) { // 0x09
    	return this.setDirectSampling(use ? 1 : 0);
    }
    
    public byte[] setDirectSampling(int val) { // 0x09
        System.out.format("set direct sampling %s\n",val);
       byte[] commandMessage = encode(0x9, val);
        this.autosendIfAttached(commandMessage);
        return commandMessage;
    }
    
    
    
    public byte[] setOffsetTuning(int val) { // 0x0a
        System.out.format("set offset tuning %s\n",val);
       byte[] commandMessage = encode(0xa, val);
        this.autosendIfAttached(commandMessage);
        return commandMessage;
    }
        
    
    public byte[] setXtalFreq(int val) { // 0x0b
        System.out.format("set xtal freq %s\n",val);
       byte[] commandMessage = encode(0xb, val);
        this.autosendIfAttached(commandMessage);
        return commandMessage;
    }
    
    public byte[] setTunerXtal(int val) { // 0x0c
        System.out.format("set tuner xtal %s\n",val);
       byte[] commandMessage = encode(0xc, val);
        this.autosendIfAttached(commandMessage);
        return commandMessage;
    }
    
    public byte[] setTunerGainByIndex(int val) { // 0x0d
        System.out.format("set tuner gain by index %s\n",val);
       byte[] commandMessage = encode(0x0d, val);
        this.autosendIfAttached(commandMessage);
        return commandMessage;
    }
    
    // attention ... unclear situation with signed/usigned .. use only 0 and positive low values to avoid problems!
    public byte[] genericCommand(byte command, int val) {
        System.out.format("generic command %s and value %s\n ",val);
       byte[] commandMessage = encode(command, val);
        this.autosendIfAttached(commandMessage);
        return commandMessage;
    }
    
    public byte[] genericCommand(int command, int val) {
        return this.genericCommand((byte) command, val); 
    }
    
}
