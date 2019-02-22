/*
 *    Copyright (C) 2017
 *    Jan van Katwijk (J.vanKatwijk@gmail.com)
 *    Lazy Chair Computing
 *
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
import	javax.swing.*;
import	devices.*;
import	package_Model.*;
import  rtltcp.RtlTcpDevice;
import	utils.*;
import	java. util. *;
import	java.io.File;
import	java.io.FileInputStream;
import	java.io.FileOutputStream;
import	java.io.IOException;

public class JavaRadio implements modelSignals  {

	static boolean ensembleRecognized	= false;
	static boolean data_notFound		= false;
	static boolean	running			= false;
	static BandHandler	my_bandHandler;
	static int	serviceCount		= 0;
	private final   List<ProgramData> services = new ArrayList<>();

	public static void main (String[] args) {

	   Device theDevice;
	   final String iniFile	=
	                System. getProperty ("user.home") + "/.javaDab.ini";
	   Properties savedValues  = new Properties ();
	   File ff = new File (iniFile);
	   try {
	      savedValues. load (new FileInputStream (ff));
	   } catch (IOException ex) {
		   ex.printStackTrace();
	   }
	   
	   JavaRadio theSystem	= new JavaRadio ();
	   String dabBand	= savedValues. getProperty ("dabBand",
	                                                    "Band III");
	   my_bandHandler	= new BandHandler (dabBand);
	   int dabMode		= Integer.
	                             parseInt (getOption (args, "-m:", "1"));
	   String channel	= getOption (args, "-c:", "8A");
	   int gain		= Integer.
	                             parseInt (getOption (args, "-g:", "50"));
	   String program	= getOption (args, "-p:", "RADIONL Utrecht");
	   String fileName	= getOption (args, "-f:", "no file");
	   String ipaddress	= getOption (args, "-ip:", "no ip");
	   int    ip_port	= Integer.
	                             parseInt (getOption (args, "-port:", "0"));
	   int    waitDab	= Integer.
	                             parseInt (getOption (args, "waitDab:", "10"));
	   int    waitEnsemble	= Integer.
	                             parseInt (getOption (args, "waitEnsemble:","10"));
	   int	Frequency	= my_bandHandler. Frequency (channel);

	   theDevice = null;
	   if ((ipaddress != "no ip") && (ip_port != 0))
	      theDevice = new RtlTcpDevice (ipaddress, ip_port,
	                                    Frequency, gain, true);
	   else
	   if (!fileName. equals ("no file")) {
              try {
                 theDevice = new fileReader (fileName);
	         System. out. println ("trying to process " + fileName);
              } catch (Exception e) {
	         System. out. println ("Sorry, we cannot handle " + fileName);
	         System. exit (2);
	      }
	   }
	   else {
	      theDevice =
	           bindDevice (args,
	                       my_bandHandler. Frequency (channel), gain);
	      if (theDevice. is_nullDevice ()) {
	         System. out. println ("unable to bind to device");
	         System. exit (1);
	      }
	   }
//
	   RadioModel theRadio	= new RadioModel (dabMode, theDevice);
//	start listening to whatever the "model" has to say
	   theRadio. addServiceListener (theSystem);
//	and start the engines
	   theRadio.  selectChannel (my_bandHandler. Frequency (channel));
//
//	Now, the processing is "on", we have to wait until either
//	we get a signal that there is no data or a timeout - assuming
//	all data about services is known

	   while (--waitDab > 0) {
	      try {
	         Thread. sleep (1000);
	      } catch (Exception e) {
	    	  e.printStackTrace();
	      }
	      if (data_notFound) {
	         System. out. println ("no DAB data discovered");
	         System. exit (1);
	      }
	   }
//
//	if we are here, we apparently have data, give it a few
//	more seconds to build up the whole ensemble list
	   while (--waitEnsemble > 0) {
	      try {
	         Thread. sleep (1000);
	      } catch (Exception e) {
	    	  e.printStackTrace();
	      }
	   }

	   if (!ensembleRecognized) {
	      System. out. println ("sorry, no ensemble found");
	      System. exit (1);
	   }

	   if (!(theRadio. is_audioService (program) ||
	         theRadio. is_packetService (program)) ) {
	      System. out. println ("the service " + program + " is not recognized");
	      System. exit (1);
	   }

	   theRadio. setService (program);
	   running	= true;
	   while (running) 
	      try {
	         Thread. sleep (1000);
	      } catch (Exception e) {
	    	  e.printStackTrace();
	      }
	}

	private static Device bindDevice (String [] args,
	                                  int frequency, int gain) {
	   Device tester;
	   
	   
	   try {
	      tester = new airspyDevice (frequency, gain, true);
	      return tester;
	   } catch (Exception e) {}

	   try {
	      tester = new sdrplayDevice (frequency, gain, true);
	      return tester;
 	   } catch (Exception e) {}

	   try {
	      tester = new rtlsdrDevice (frequency, gain, true);
	      return tester;
	   } catch (Exception e) {}

	   if (args. length > 1) {
           }

	   return new nullDevice ();
	}

	static
	private String getOption (String [] args, String key, String defValue) {
	   for (String s: args) {
	      if (s. startsWith (key))
	         return s. substring (key. length (), s. length ());
	   }
	   return defValue;
	}

///////////////////////////////////////////////////////////////////////////
        public void newService  (String s1, ProgramData p) {
           p. channel           = my_bandHandler. channel (21);
           p. serviceName       = s1;
	   System. out. println ("detected service " + s1);
	   serviceCount ++;

           if (p instanceof PacketData) {
              PacketData pd = (PacketData)p;
              services. add (pd);
           }
           else {
              AudioData ad = (AudioData)p;
              services. add (ad);
           }
        }

        public  void    ensembleName    (String s1, int s2) {
	   ensembleRecognized	= true;
	}

        public  void    no_signal_found () {
	   if (serviceCount == 0)
	      data_notFound = true;
	}

        public  void    show_SNR        (int snr) {}

        public  void    show_Sync       (boolean flag) {}

        public  void    show_isStereo   (boolean b) {}

        public  void    show_ficSuccess (int successRate) {}

        public  void    show_freqOffset (int offset) {}

        public  void    show_picture    (byte [] data,
	                                 int subtype, String name) {}

        public  void    show_dynamicLabel       (String s) {
	   System. out. println (s);
	}

        public  void    show_motHandling        (boolean flag) {}

        public  void    show_frameErrors        (int errors) {}
}

