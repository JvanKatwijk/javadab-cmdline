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

import java.nio.ByteBuffer;


//	these tool collection is now only used for encoding
//	the command messages that are sent to the rtl_tcp server

public class Tools {

//	just a collection of static methods
//	(that can be used without instantiating the class)
	
	public static byte [] integerToBytes (Integer in) {
	   byte[] bytes = ByteBuffer. allocate (4). putInt (in). array();
	   return bytes;
	}
	
/*
 * not needed as DataInputStream readFloat() should exactly do this for us
 * 
	public static float bytesToFloat(byte[] bytes) {
	float f = Float.intBitsToFloat(
		    (bytes[0] & 0xff)
		    | ((bytes[1] & 0xff) << 8)
		    | ((bytes[2] & 0xff) << 16)
		    | ((bytes[3] & 0xff) << 24));
	return f;
}
*/

public static byte[] shortenTail(byte[] a, int amount) {
	byte[] c = new byte[a.length - amount];
	System.arraycopy(a, 0, c, 0, a.length-amount);
	return c;
	}
	
	public static byte[] concat(byte[] a, byte[] b) {
		byte[] c = new byte[a.length + b.length];
		System.arraycopy(a, 0, c, 0, a.length);
		System.arraycopy(b, 0, c, a.length, b.length);
		return c;
	}
	
	public static byte[] concat(byte a, byte[] b) {
		byte[] c = new byte[1 + b.length];
		c[0] = a;
		System.arraycopy(b, 0, c, 1, b.length);
		return c;
	}
	
	public static byte[] concat(byte[] a, byte b) {
		byte[] c = new byte[a.length + 1];
		System.arraycopy(a, 0, c, 0, a.length);
		c[c.length-1] = b;
		return c;
	}
	
}
