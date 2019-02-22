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

import java.util.LinkedList;
import java.util.List;

public class SampleFIFOBuffer {
//	not very efficient, but very convenient and simple to
//	use without risk of bugs
//	private final LinkedList<Sample> buffer = new LinkedList<Sample>();

	private final LinkedList<float[]> buffer = new LinkedList<float[]>();
	private Byte rest = null;	
	private int bufferedFloatCount = 0;
	
	public SampleFIFOBuffer () {}
	
	public synchronized void clear () {
	   this. buffer. clear ();
	   System. out. println ("clear ... clear buffer List and set bufferedFloatCount to zero");
	   this. bufferedFloatCount = 0;
	}
	
	public synchronized int bufferedFloatCount () {
//	   System. out. format ("bufferedFloatCount = %s\n",
//	                                  this. bufferedFloatCount);
	   return this.bufferedFloatCount;
	}
	
	private void reduceBufferedFloatCount (int byAmount) {
	   int newAmount = this. bufferedFloatCount - byAmount;
//	   System. out. format ("reduceBufferedFloatCount = %s - byAmount = %s  => %s\n",
//	                      this.bufferedFloatCount,byAmount,newAmount);
	   this. bufferedFloatCount = newAmount;
	}
	
	private void increaseBufferedFloatCount (int byAmount) {
	   int newAmount = this.bufferedFloatCount + byAmount;
//	   System. out. format ("increaseBufferedFloatCount = %s + byAmount = %s  => %s\n",
//	                      this. bufferedFloatCount, byAmount, newAmount);
	   this. bufferedFloatCount = newAmount;
	   if (this. bufferedFloatCount > 10000000) {
	      System. out. format ("Warning: BufferedFloatCount %s MByte\n",
	                             this. bufferedFloatCount / 1000000);
	   }
	}
		
	public synchronized void storeBytes (byte [] input) {
	   input = this. restManage (input);
//	only full values (ignore for odd number of bytes the last byte!)
	   int halfLen = (input.length / 2); 
	   float [] store = new float [halfLen * 2];
	   for (int pos = 0;  pos < halfLen; pos++) {
	      int re = ((int)input [2 * pos    ]) & 0xFF;
	      int im = ((int)input [2 * pos + 1]) & 0xFF;
	      store [2 * pos]     = ((float) (re - 128)) / 128.0f;
	      store [2 * pos + 1] = ((float) (im - 128)) / 128.0f;
	   }
	   buffer. add (store);
//	   this.bufferedFloatCount+=store.length;
	   this. increaseBufferedFloatCount (store.length); 
//	   System. out. format ("addedBytes amount = %s ;  ListLength = %s \n",
//	                                   input. length, this. buffer.size ());
	}
		
	private byte [] restManage (byte [] bytes) {
	   if (rest != null) {
	      byte [] c = new byte [1 + bytes.length];
	      c [0] = rest;
	      System. arraycopy (bytes, 0, c, 1, bytes. length);
	      bytes = c;
	      rest = null;
	   }

	   if (bytes. length % 2 != 0) {
//	save last position // (shall not be used elsewhere!)
	      rest = bytes [bytes. length - 1];
	   }
	   return bytes;
	}
	
	public synchronized int consumeSamples (float[] destination,
	                                        int amountSamples) {		
//	   System. out. format ("consumeSamples(destination.length = %s, amountSamples = %s)\n",
//	                                  destination. length, amountSamples);
	   if ((amountSamples * 2) > destination.length) {
	      amountSamples = destination.length / 2;
	   }

	   int amountFloats = amountSamples * 2;
//	   float [] out = new float [amountFloats];
	   int nextPos = 0;
	   float [] current = null;
	   int stillNeeded = (amountFloats - nextPos);

	   while (nextPos < amountFloats) {
//	      System. out. format ("while (nextpos=%s < amountFloats=%s)\n",
//	                                              nextPos, amountFloats);
//	      stillNeeded = (amountFloats - nextPos);
//	not necessary to update here, as it is done
//	BEFORE the loop and in the end of the loop (see below) 

//	head element in the list, containing a number of float values
	      current = this. buffer. peekFirst();
			
	      if (this. buffer. size() == 0) {
	         return nextPos / 2;
	      }

//	use full amount of current head element 
	      if (current.length <= (stillNeeded)) {
	         System. arraycopy (current, 0,
	                            destination, nextPos, current.length);
//	         this.bufferedFloatCount-=current.length;
	         this. reduceBufferedFloatCount (current.length);
//	first element was used and is fully consumed
	         this. buffer. removeFirst ();
	         nextPos += current. length; // update nextPos
	         stillNeeded = (amountFloats - nextPos); // update stillNeeded	
	      } else {  // use only part of the head element and shorten it
	         System. arraycopy (current, 0,
	                            destination, nextPos, stillNeeded);
	         int remaining = (current. length - stillNeeded);
	         float[] shortened = new float [remaining];
	         System. arraycopy (current, stillNeeded,
	                            shortened, 0, remaining);
//	         bufferedFloatCount=-stillNeeded;
	         this. reduceBufferedFloatCount (stillNeeded); 
	         this. buffer. set (0, shortened); // replace head element
//	update nextPos  ---> should be nextPos == amountFloats
	         nextPos += stillNeeded;
//	update stillNeeded	   ---> should be stillNeeded = 0;
	         stillNeeded = (amountFloats - nextPos);
	      }
	   }

//	   System. out. format ("consumeSamples return sampleAmount = %s, bufferListLen = %s, headItemLen = %s \n",
//	                        amountSamples, this. buffer. size (),
//	                                 this. buffer. peekFirst (). length);
	   return nextPos / 2;
	}
			
/*
	private void addSample (Sample sample) {
	   this. buffer.add(sample);
	   this. notify();
	}
	
	private void addSamples (List<Sample> samples) {
	   this. buffer.addAll(samples);
	   this. notify();
	}
 */
/*
	public synchronized
	LinkedList<Sample> removeSamples (int num) throws InterruptedException {
//	   System. out. format ("removeSamples(%s)  buffer.size = %s\n",
	                          num, this. buffer. size ());
	   while (this. buffer. size() < num) {
//	      System. out. format ("waiting while buffer.size = %s  <  num = %s\n",
//	                            this.buffer.size(),num);
	      this. wait (1000);
	   }

	   LinkedList<Sample> out = new LinkedList<Sample>();
	   int i = 0;
	   while (i < num) {
	      out. add (buffer. removeFirst ());
	      ++i;
	   }
	   return out;
	}
 */

 /*
	public synchronized
	  LinkedList<Float> removeSamplesAsFloatList (int num)
	                                      throws InterruptedException {
	   LinkedList<Sample> samples = this. removeSamples (num);
	   LinkedList<Float> floats = new LinkedList<Float> ();
	   while (!samples.isEmpty()) {
	      Sample sample = samples. removeFirst ();
	      floats. add (sample. first ());
	      floats.add(sample.second());
	   }
	   return floats;
	}
	
	public synchronized float[]
	   removeSamplesAsFloatArray (int num) throws InterruptedException {
	   LinkedList<Sample> samples = this. removeSamples(num);
	   float[] floats = new float [samples.size()];
	   int pos = 0;
	   while (!samples. isEmpty ()) {
	      Sample sample = samples. removeFirst();
	      floats [pos++] = sample. first();
	      floats [pos++] = sample. second();
	   }
//	note: it is expected that pos = num * 2  after this loop!
	   return floats;
	}
	
	public synchronized
	   int removeSamplesAsFloatArray (float [] out, int num)
	                                   throws InterruptedException {
//	   System.out.format ("this.removeSamples(%s) ...\n",num);
	   LinkedList<Sample> samples = this. removeSamples (num);
//	   System. out. format ("... got %s Samples \n", samples. size ());
//	   float [] floats = new float [samples. size ()];
	   int pos = 0;
	   while (!samples. isEmpty()) {
//	      System. out. format ("repack pos=%s\n",pos);
	      Sample sample = samples. removeFirst ();
	      out [pos++] = sample. first ();
	      out [pos++] = sample. second();
	   }

//	   System. out. format ("repacked finished. pos = %s  ; num = %s",
	                                                          pos, num);
//	note: it is expected that pos = num * 2  after this loop!
	   return pos;
	}
 */

}
