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


// the idea is that each sample consists of two float values
// they are packed together in one object and handled together

// this is not very efficient, but helps to keep things sorted

public class Sample {

	private float first;
	private float second;
	
	public Sample(byte firstByte, byte secondByte) {
		int re = ((int)firstByte) & 0xFF;
	    int im = ((int)secondByte) & 0xFF;
	    this.first  = ((float) (re - 128)) / 128.0f;
	    this.second = ((float) (im - 128)) / 128.0f;
	}
	
	public float first() {
		return first;
	}
	
	public float second() {
		return second;
	}
	
	/*
	int re = ((int)dataBuffer [2 * bufferP    ]) & 0xFF;
    int im = ((int)dataBuffer [2 * bufferP + 1]) & 0xFF;
    buffer [2 * i]     = ((float) (re - 128)) / 128.0f;
    buffer [2 * i + 1] = ((float) (im - 128)) / 128.0f;
    bufferP ++;
	*/
}

/*
 * 
 * 
 * public	int	getSamples	(float [] buffer, int amount) {
	   byte lbuf [] = new byte [2 * amount];
	   if (!running)
	      return 0;
	   try {
	      for (int i = 0; i < amount; i ++) {
	         if (bufferP >= 4 * 2048) {
	            long waitingTime = nextTime - System. nanoTime ();
	            if (waitingTime > 0) {
	               try {
	                  Thread. sleep (waitingTime / 1000,
	                                 (int)(waitingTime % 1000));
                       } catch (Exception e) {}
	            }
	            nextTime	= System. nanoTime () + 8000;
	            inStream. read (dataBuffer);
	            bufferP = 0;
	         }
	         int re = ((int)dataBuffer [2 * bufferP    ]) & 0xFF;
	         int im = ((int)dataBuffer [2 * bufferP + 1]) & 0xFF;
	         buffer [2 * i]     = ((float) (re - 128)) / 128.0f;
	         buffer [2 * i + 1] = ((float) (im - 128)) / 128.0f;
	         bufferP ++;
	      }
	   } catch (Exception e) {
	      System. out. println ("help");
	   }
	   return amount;
	}
 * 
 * 
 * 
 */
