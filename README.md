**javaDab-cmdline**


------------------------------------------------------------------------------
Introduction
------------------------------------------------------------------------------

Javadab-cmdline is an implementation of a DAB decoder, written (almost
completely) in Java. The program is command-line driven and
supports, next to SDRplay, AIRspy, rt2832 based DABsticks and a file
reader for 8 bit I/Q samples, an interface to the rtl_tcp server.

On start up of the program, it will first try to detect some form of
DAB signal. If not, the program will report that and halt.
If some DAB signal can be detected, the program will wait (some time)
for detecting the services in the ensemble.
If no ensemble can be detected (within a given period of time), the
program will report that and quit.

If an ensemble is found, the program will start the service - the name
of which was passed as a parameter. If the service cannot be detected
(or insufficient information is found to decode the contents of the
service), the program will report so and quit.

Otherwise, decoding of the service starts, and continues until
the user quits the program.


----------------------------------------------------------------------------
Commandline parameters
----------------------------------------------------------------------------

The general structure for parameter handling is "-x:y".

	-c:xx	where xx is the name of the channel, default is "11C";
	-p:xx	where xx is the name of the service to be selected;
	-g:xx	where xx is the gain (range 1 .. 100), default is "50";
	-f:xx	where xx is a filename, selecting this implies running a filereader as input device
	-ip:xx	where xx is a string denoting an ip address, selecting this together with a port number, implies running a client for the rtl_tcp server;
	-port:xx where xx is a port number (see "-ip:")
	-waitab:xx where xx denotes the number of seconds within a DAB signal is to be recognized;
	-waitEnsemble:xx where xx denoted the number of seconds within which an ensemble is to be recognized.
	
----------------------------------------------------------------------------
Installation
----------------------------------------------------------------------------
One needs to have installed 
* a. jdk-8*  for running (compiling) the program
* b. libfaad, the faad library as well as the include files;
* e. libmirsdrapi-rsp, the library for the SDRplay, and/or librtlsdr, the
     library for the dabsticks, and/or libairspy, the library for the
     airspy.

-----------------------------------------------------------------------------
The use of native code
-----------------------------------------------------------------------------

In order to run, one does need some parts that are encoded as "native",
wrappers around the existing libraries mainly.

* a. wrappers for the device handlers;
* b. a wrapper around the faad library;
* c. a small library for handling MP2 frames;

The C(++) code for the wrappers is in the different subdirectories in the
directory "libraries".
These (sub)directories contain - obviously - the code, as well as a script
to generate the wrapper under Ubuntu and Stretch. However, one might have
to adapt the wrapper to point to the right include directories of the 
installed jdk.

Installation of the wrappers is in "/usr/local/lib".

------------------------------------------------------------------------
Compiling and running the program
-------------------------------------------------------------------------

The git repository is made from the Netbeans project directory. It contains
the sources of the program in "src".
If/when everything is in place, "cd" to the directory where the java sources
are stored.

Compilation is simply by "javac JavaRadio.java"

Running is simply by "java JavaRadio"

Running DAB then is simply by "java JavaRadio".

------------------------------------------------------------------------------
Selecting a band
-------------------------------------------------------------------------------

While most transmissions are in Band III, in some countries the Band (and
the Mode) differ. This can be set in an "ini" file. Such an ini file
is stored in the home directory, and is named ".javaDab.ini"

Adding a line "dabBand=LBand" will cause the tuner to
be set to the L-Band, and setting "dabMode=2" will set the software to
recognize Mode 2.

-------------------------------------------------------------------------------
Copyright
------------------------------------------------------------------------------


        Copyright (C)  2017 on the DAB processing
        Jan van Katwijk (J.vanKatwijk@gmail.com)
        Lazy Chair Computing
	
        Copyright (C) 2019 on the IP handling
        Mathias.Kuefner@bmt-online.de
        Bayerische Medien Technik GmbH


        The javadab-cmdline software is made available under the GPL-2.0.
        The SDR-J software, of which the javaProject software is a part,
        is distributed in the hope that it will be useful,
        but WITHOUT ANY WARRANTY; without even the implied warranty of
        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
        GNU General Public License for more details.


