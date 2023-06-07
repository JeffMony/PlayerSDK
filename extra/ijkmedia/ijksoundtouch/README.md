
SoundTouch audio processing library v1.9.2

SoundTouch library Copyright © Olli Parviainen 2001-2015

1. Introduction

SoundTouch is an open-source audio processing library that allows changing the sound tempo, pitch and playback rate parameters independently from each other, i.e.:

Sound tempo can be increased or decreased while maintaining the original pitch
Sound pitch can be increased or decreased while maintaining the original tempo
Change playback rate that affects both tempo and pitch at the same time
Choose any combination of tempo/pitch/rate
1.1 Contact information

Author email: oparviai 'at' iki.fi

SoundTouch WWW page: http://soundtouch.surina.net

2. Compiling SoundTouch

Before compiling, notice that you can choose the sample data format if it's desirable to use floating point sample data instead of 16bit integers. See section "sample data format" for more information.

Also notice that SoundTouch can use OpenMP instructions for parallel computation to accelerate the runtime processing speed in multi-core systems, however, these improvements need to be separately enabled before compiling. See OpenMP notes in Chapter 3 below.

2.1. Building in Microsoft Windows

Project files for Microsoft Visual C++ are supplied with the source code package. Go to Microsoft WWW page to download Microsoft Visual Studio Express version for free.

To build the binaries with Visual C++ compiler, either run "make-win.bat" script, or open the appropriate project files in source code directories with Visual Studio. The final executable will appear under the "SoundTouch\bin" directory. If using the Visual Studio IDE instead of the make-win.bat script, directories bin and lib may need to be created manually to the SoundTouch package root for the final executables. The make-win.bat script creates these directories automatically.

OpenMP NOTE: If activating the OpenMP parallel computing in the compilation, the target program will require additional vcomp dll library to properly run. In Visual C++ 9.0 these libraries can be found in the following folders.

x86 32bit: C:\Program Files (x86)\Microsoft Visual Studio 9.0\VC\redist\x86\Microsoft.VC90.OPENMP\vcomp90.dll
x64 64bit: C:\Program Files (x86)\Microsoft Visual Studio 9.0\VC\redist\amd64\Microsoft.VC90.OPENMP\vcomp90.dll
In Visual Studio 2008, a SP1 version may be required for these libraries. In other VC++ versions the required library will be expectedly found in similar "redist" location.

Notice that as minor demonstration of a "dll hell" phenomenon both the 32-bit and 64-bit version of vcomp90.dll have the same filename but different contents, thus choose the proper version to allow the program start.

2.2. Building in Gnu platforms

The SoundTouch library compiles in practically any platform supporting GNU compiler (GCC) tools. SoundTouch requires GCC version 4.3 or later.

To build and install the binaries, run the following commands in /soundtouch directory:

./bootstrap  -
Creates "configure" file with local autoconf/automake toolset.
./configure  -
Configures the SoundTouch package for the local environment. Notice that "configure" file is not available before running the "./bootstrap" command as above.
make         -
Builds the SoundTouch library & SoundStretch utility. You can optionally add "-j" switch after "make" to speed up the compilation in multi-core systems.

make install -
Installs the SoundTouch & BPM libraries to /usr/local/lib and SoundStretch utility to /usr/local/bin. Please notice that 'root' privileges may be required to install the binaries to the destination locations.

2.2.1 Required GNU tools 

Bash shell, GNU C++ compiler, libtool, autoconf and automake tools are required for compiling the SoundTouch library. These are usually included with the GNU/Linux distribution, but if not, install these packages first. For example, Ubuntu Linux can acquire and install these with the following command:

sudo apt-get install automake autoconf libtool build-essential
2.2.2 Problems with GCC compiler compatibility

At the release time the SoundTouch package has been tested to compile in GNU/Linux platform. However, If you have problems getting the SoundTouch library compiled, try disabling optimizations that are specific for x86 processors by running ./configure script with switch

--enable-x86-optimizations=no
Alternatively, if you don't use GNU Configure system, edit file "include/STTypes.h" directly and remove the following definition:
#define SOUNDTOUCH_ALLOW_X86_OPTIMIZATIONS 1
2.2.3 Compiling Shared Library / DLL version in Cygwin

The GNU compilation does not automatically create a shared-library version of SoundTouch (.so or .dll). If such is desired, then you can create it as follows after running the usual compilation:

g++ -shared -static -DDLL_EXPORTS -I../../include -o SoundTouch.dll \
     SoundTouchDLL.cpp ../SoundTouch/.libs/libSoundTouch.a
sstrip SoundTouch.dll
2.1. Building in Android

Android compilation instructions are within the source code package, see file "source/Android-lib/README-SoundTouch-Android.html" in the source code package.

The Android compilation automatically builds separate .so library binaries for ARM, X86 and MIPS processor architectures. For optimal device support, include all these .so library binaries into the Android .apk application package, so the target Android device can automatically choose the proper library binary version to use.

The source/Android-lib folder includes also an Android example application that processes WAV audio files using SoundTouch library in Android devices.

3. About implementation & Usage tips

3.1. Supported sample data formats

The sample data format can be chosen between 16bit signed integer and 32bit floating point values. The default is 32bit floating point format, which will also provide slightly better sound quality over the integer format.

In Windows environment, the sample data format is chosen in file "STTypes.h" by choosing one of the following defines:

#define SOUNDTOUCH_INTEGER_SAMPLES for 16bit signed integer
#define SOUNDTOUCH_FLOAT_SAMPLES for 32bit floating point
In GNU environment, the floating sample format is used by default, but integer sample format can be chosen by giving the following switch to the configure script:

./configure --enable-integer-samples
The sample data can have either single (mono) or double (stereo) audio channel. Stereo data is interleaved so that every other data value is for left channel and every second for right channel. Notice that while it'd be possible in theory to process stereo sound as two separate mono channels, this isn't recommended because processing the channels separately would result in losing the phase coherency between the channels, which consequently would ruin the stereo effect.

Sample rates between 8000-48000H are supported.

3.2. Processing latency

The processing and latency constraints of the SoundTouch library are:

Input/output processing latency for the SoundTouch processor is around 100 ms. This is when time-stretching is used. If the rate transposing effect alone is used, the latency requirement is much shorter, see section 'About algorithms'.
Processing CD-quality sound (16bit stereo sound with 44100H sample rate) in real-time or faster is possible starting from processors equivalent to Intel Pentium 133Mh or better, if using the "quick" processing algorithm. If not using the "quick" mode or if floating point sample data are being used, several times more CPU power is typically required.
3.3. About algorithms

SoundTouch provides three seemingly independent effects: tempo, pitch and playback rate control. These three controls are implemented as combination of two primary effects, sample rate transposing and time-stretching.

Sample rate transposing affects both the audio stream duration and pitch. It's implemented simply by converting the original audio sample stream to the  desired duration by interpolating from the original audio samples. In SoundTouch, linear interpolation with anti-alias filtering is used. Theoretically a higher-order interpolation provide better result than 1st order linear interpolation, but in audio application linear interpolation together with anti-alias filtering performs subjectively about as well as higher-order filtering would.

Time-stretching means changing the audio stream duration without affecting it's pitch. SoundTouch uses WSOLA-like time-stretching routines that operate in the time domain. Compared to sample rate transposing, time-stretching is a much heavier operation and also requires a longer processing "window" of sound samples used by the processing algorithm, thus increasing the algorithm input/output latency. Typical i/o latency for the SoundTouch time-stretch algorithm is around 100 ms.

Sample rate transposing and time-stretching are then used together to produce the tempo, pitch and rate controls:

'Tempo' control is implemented purely by time-stretching.
'Rate' control is implemented purely by sample rate transposing.
'Pitch' control is implemented as a combination of time-stretching and sample rate transposing. For example, to increase pitch the audio stream is first time-stretched to longer duration (without affecting pitch) and then transposed back to original duration by sample rate transposing, which simultaneously reduces duration and increases pitch. The result is original duration but increased pitch.
3.4 Tuning the algorithm parameters

The time-stretch algorithm has few parameters that can be tuned to optimize sound quality for certain application. The current default parameters have been chosen by iterative if-then analysis (read: "trial and error") to obtain best subjective sound quality in pop/rock music processing, but in applications processing different kind of sound the default parameter set may result into a sub-optimal result.

The time-stretch algorithm default parameter values are set by the following #defines in file "TDStretch.h":

#define DEFAULT_SEQUENCE_MS     AUTOMATIC
#define DEFAULT_SEEKWINDOW_MS   AUTOMATIC
#define DEFAULT_OVERLAP_MS      8
These parameters affect to the time-stretch algorithm as follows:

DEFAULT_SEQUENCE_MS: This is the default length of a single processing sequence in milliseconds which determines the how the original sound is chopped in the time-stretch algorithm. Larger values mean fewer sequences are used in processing. In principle a larger value sounds better when slowing down the tempo, but worse when increasing the tempo and vice versa. 

By default, this setting value is calculated automatically according to tempo value.
DEFAULT_SEEKWINDOW_MS: The seeking window default length in milliseconds is for the algorithm that seeks the best possible overlapping location. This determines from how wide a sample "window" the algorithm can use to find an optimal mixing location when the sound sequences are to be linked back together. 

The bigger this window setting is, the higher the possibility to find a better mixing position becomes, but at the same time large values may cause a "drifting" sound artifact because neighboring sequences can be chosen at more uneven intervals. If there's a disturbing artifact that sounds as if a constant frequency was drifting around, try reducing this setting.

By default, this setting value is calculated automatically according to tempo value.
DEFAULT_OVERLAP_MS: Overlap length in milliseconds. When the sound sequences are mixed back together to form again a continuous sound stream, this parameter defines how much the ends of the consecutive sequences will overlap with each other.

This shouldn't be that critical parameter. If you reduce the DEFAULT_SEQUENCE_MS setting by a large amount, you might wish to try a smaller value on this.
Notice that these parameters can also be set during execution time with functions "TDStretch::setParameters()" and "SoundTouch::setSetting()".

The table below summaries how the parameters can be adjusted for different applications:

Parameter name	Default value magnitude	Larger value affects...	Smaller value affects...	Effect to CPU burden
SEQUENCE_MS
Default value is relatively large, chosen for slowing down music tempo	Larger value is usually better for slowing down tempo. Growing the value decelerates the "echoing" artifact when slowing down the tempo.	Smaller value might be better for speeding up tempo. Reducing the value accelerates the "echoing" artifact when slowing down the tempo	Increasing the parameter value reduces computation burden
SEEKWINDOW_MS
Default value is relatively large, chosen for slowing down music tempo	Larger value eases finding a good mixing position, but may cause a "drifting" artifact	Smaller reduce possibility to find a good mixing position, but reduce the "drifting" artifact.	Increasing the parameter value increases computation burden
OVERLAP_MS
Default value is relatively large, chosen to suit with above parameters.	 	If you reduce the "sequence ms" setting, you might wish to try a smaller value.	Increasing the parameter value increases computation burden
3.5 Performance Optimizations

General optimizations:

The time-stretch routine has a 'quick' mode that substantially speeds up the algorithm but may slightly compromise the sound quality. This mode is activated by calling SoundTouch::setSetting() function with parameter  id of SETTING_USE_QUICKSEEK and value "1", i.e.

setSetting(SETTING_USE_QUICKSEEK, 1);

CPU-specific optimizations:

Intel x86 specific SIMD optimizations are implemented using compiler intrinsics, providing about a 3x processing speedup for x86 compatible processors vs. non-SIMD implementation:

Intel MMX optimized routines are used with x86 CPUs when 16bit integer sample type is used
Intel SSE optimized routines are used with x86 CPUs when 32bit floating point sample type is used
3.5 OpenMP parallel computation

SoundTouch 1.9 onwards support running the algorithms parallel in several CPU cores. Based on benchmark the experienced multi-core processing speed-up gain ranges between +30% (on a high-spec dual-core x86 Windows PC) to 215% (on a moderately low-spec quad-core ARM of Raspberry Pi2).

The parallel computing support is implemented using OpenMP spec 3.0 instructions. These instructions are supported by Visual C++ 2008 and later, and GCC v4.2 and later. Compilers that do not supporting OpenMP will ignore these optimizations and routines will still work properly. Possible warnings about unknown #pragmas are related to OpenMP support and can be safely ignored.

The OpenMP improvements are disabled by default, and need to be enabled by developer during compile-time. Reason for this is that parallel processing adds moderate runtime overhead in managing the multi-threading, so it may not be necessary nor desirable in all applications. For example real-time processing that is not constrained by CPU power will not benefit of speed-up provided by the parallel processing, in the contrary it may increase power consumption due to the increased overhead.

However, applications that run on low-spec multi-core CPUs and may otherwise have possibly constrained performance will benefit of the OpenMP improvements. This include for example multi-core embedded devices.

OpenMP parallel computation can be enabled before compiling SoundTouch library as follows:

Visual Studio: Open properties for the SoundTouch sub-project, browse to C/C++ and Language settings. Set there "OpenMP support" to "Yes". Alternatively add /openmp switch to command-line parameters
GNU: Run the configure script with "./configure --enable-openmp" switch, then run make as usually
Android: Add "-fopenmp" switches to compiler & linker options, see README-SoundTouch-Android.html in the source code package for more detailed instructions.
4. SoundStretch audio processing utility

SoundStretch audio processing utility
Copyright (c) Olli Parviainen 2002-2015

SoundStretch is a simple command-line application that can change tempo, pitch and playback rates of WAV sound files. This program is intended primarily to demonstrate how the "SoundTouch" library can be used to process sound in your own program, but it can as well be used for processing sound files.

4.1. SoundStretch Usage Instructions

SoundStretch Usage syntax:

soundstretch infilename outfilename [switches]
Where:

"infilename"
Name of the input sound data file (in .WAV audio file format). Give "stdin" as filename to use standard input pipe.
"outfilename"
Name of the output sound file where the resulting sound is saved (in .WAV audio file format). This parameter may be omitted if you  don't want to save the output (e.g. when only calculating BPM rate with '-bpm' switch). Give "stdout" as filename to use standard output pipe.
 [switches]
Are one or more control switches.
Available control switches are:

-tempo=n 
Change the sound tempo by n percents (n = -95.0 .. +5000.0 %)
-pitch=n
Change the sound pitch by n semitones (n = -60.0 .. + 60.0 semitones)
-rate=n
Change the sound playback rate by n percents (n = -95.0 .. +5000.0 %)
-bpm=n
Detect the Beats-Per-Minute (BPM) rate of the sound and adjust the tempo to meet 'n' BPMs. When this switch is applied, the "-tempo" switch is ignored. If "=n" is omitted, i.e. switch "-bpm" is used alone, then the BPM rate is estimated and displayed, but tempo not adjusted according to the BPM value.
-quick
Use quicker tempo change algorithm. Gains speed but loses sound quality.
-naa
Don't use anti-alias filtering in sample rate transposing. Gains speed but loses sound quality.
-license
Displays the program license text (LGPL)
Notes:

To use standard input/output pipes for processing, give "stdin" and "stdout" as input/output filenames correspondingly. The standard input/output pipes will still carry the audio data in .wav audio file format.
The numerical switches allow both integer (e.g. "-tempo=123") and decimal (e.g. "-tempo=123.45") numbers.
The "-naa" and/or "-quick" switches can be used to reduce CPU usage while compromising some sound quality
The BPM detection algorithm works by detecting repeating bass or drum patterns at low frequencies of <250Hz. A lower-than-expected BPM figure may be reported for music with uneven or complex bass patterns.
4.2. SoundStretch usage examples

Example 1

The following command increases tempo of the sound file "originalfile.wav" by 12.5% and stores result to file "destinationfile.wav":

soundstretch originalfile.wav destinationfile.wav -tempo=12.5
Example 2

The following command decreases the sound pitch (key) of the sound file "orig.wav" by two semitones and stores the result to file "dest.wav":

soundstretch orig.wav dest.wav -pitch=-2
Example 3

The following command processes the file "orig.wav" by decreasing the sound tempo by 25.3% and increasing the sound pitch (key) by 1.5 semitones. Resulting .wav audio data is directed to standard output pipe:

soundstretch orig.wav stdout -tempo=-25.3 -pitch=1.5
Example 4

The following command detects the BPM rate of the file "orig.wav" and adjusts the tempo to match 100 beats per minute. Result is stored to file "dest.wav":

soundstretch orig.wav dest.wav -bpm=100
Example 5

The following command reads .wav sound data from standard input pipe and estimates the BPM rate:

soundstretch stdin -bpm
Example 6

The following command tunes song from original 440Hz tuning to 432Hz tuning: this corresponds to lowering the pitch by -0.318 semitones:

soundstretch original.wav output.wav -pitch=-0.318
5. Change History

5.1. SoundTouch library Change History

1.9.2:

Fix in GNU package configuration
1.9.1:

Improved SoundTouch::flush() function so that it returns precisely the desired amount of samples for exact output duration control
Redesigned quickseek algorithm for improved sound quality when using the quickseek mode. The new quickseek algorithm can find 99% as good results as the default full-scan mode, while the quickseek algorithm is remarkable less CPU intensive.
Added adaptive integer divider scaling for improved sound quality when using integer processing algorithm
1.9:

Added support for parallel computation support via OpenMP primitives for better performance in multicore systems. Benchmarks show that achieved parallel processing speedup improvement typically range from +30% (x86 dual-core) to +180% (ARM quad-core). The OpenMP optimizations are disabled by default, see OpenMP notes above in this readme file how to enabled these optimizations.
Android: Added support for Android devices featuring X86 and MIPS CPUs, in addition to ARM CPUs.
Android: More versatile Android example application that processes WAV audio files with SoundTouch library
Replaced Windows-like 'BOOL' types with native 'bool'
Changed documentation token to "dist_doc_DATA" in Makefile.am file
Miscellaneous small fixes and improvements
1.8.0:

Added support for multi-channel audio processing
Added support for cubic and shannon interpolation for rate and pitch shift effects besides the original linear interpolation, to reduce aliasing at high frequencies due to interpolation. Cubic interpolation is used as default for floating point processing, and linear interpolation for integer processing.
Fixed bug in anti-alias filtering that limited stop-band attenuation to -10 dB instead of <-50dB, and increased filter length from 32 to 64 taps to further reduce aliasing due to frequency folding.
Performance improvements in cross-correlation algorithm
Other bug and compatibility fixes
1.7.1:

Added files for Android compilation
1.7.0:

Sound quality improvements/li>
Improved flush() to adjust output sound stream duration to match better with ideal duration
Rewrote x86 cpu feature check to resolve compatibility problems
Configure script automatically checks if CPU supports mmx & sse compatibility for GNU platform, and the script support now "--enable-x86-optimizations" switch to allow disabling x86-specific optimizations.
Revised #define conditions for 32bit/64bit compatibility
gnu autoconf/automake script compatibility fixes
Tuned beat-per-minute detection algorithm
1.6.0:

Added automatic cutoff threshold adaptation to beat detection routine to better adapt BPM calculation to different types of music
Retired 3DNow! optimization support as 3DNow! is nowadays obsoleted and assembler code is nuisance to maintain
Retired "configure" file from source code package due to autoconf/automake versio conflicts, so that it is from now on to be generated by invoking "boostrap" script that uses locally available toolchain version for generating the "configure" file
Resolved namespace/label naming conflicts with other libraries by replacing global labels such as INTEGER_SAMPLES with more specific SOUNDTOUCH_INTEGER_SAMPLES etc.
Updated windows build scripts & project files for Visual Studio 2008 support
Updated SoundTouch.dll API for .NET compatibility
Added API for querying nominal processing input & output sample batch sizes
1.5.0:

Added normalization to correlation calculation and improvement automatic seek/sequence parameter calculation to improve sound quality
Bugfixes: 
Fixed negative array indexing in quick seek algorithm
FIR autoalias filter running too far in processing buffer
Check against zero sample count in rate transposing
Fix for x86-64 support: Removed pop/push instructions from the cpu detection algorithm. 
Check against empty buffers in FIFOSampleBuffer
Other minor fixes & code cleanup
Fixes in compilation scripts for non-Intel platforms
Added Dynamic-Link-Library (DLL) version of SoundTouch library build, provided with Delphi/Pascal wrapper for calling the dll routines
Added #define PREVENT_CLICK_AT_RATE_CROSSOVER that prevents a click artifact when crossing the nominal pitch from either positive to negative side or vice versa
1.4.1:

Fixed a buffer overflow bug in BPM detect algorithm routines if processing more than 2048 samples at one call 
1.4.0:

Improved sound quality by automatic calculation of time stretch algorithm processing parameters according to tempo setting
Moved BPM detection routines from SoundStretch application into SoundTouch library
Bugfixes: Usage of uninitialied variables, GNU build scripts, compiler errors due to 'const' keyword mismatch.
Source code cleanup
1.3.1:

Changed static class declaration to GCC 4.x compiler compatible syntax.
Enabled MMX/SSE-optimized routines also for GCC compilers. Earlier the MMX/SSE-optimized routines were written in compiler-specific inline assembler, now these routines are migrated to use compiler intrinsic syntax which allows compiling the same MMX/SSE-optimized source code with both Visual C++ and GCC compilers.
Set floating point as the default sample format and added switch to the GNU configure script for selecting the other sample format.
1.3.0:

Fixed tempo routine output duration inaccuracy due to rounding error
Implemented separate processing routines for integer and floating arithmetic to allow improvements to floating point routines (earlier used algorithms mostly optimized for integer arithmetic also for floating point samples)
Fixed a bug that distorts sound if sample rate changes during the sound stream
Fixed a memory leak that appeared in MMX/SSE/3DNow! optimized routines
Reduced redundant code pieces in MMX/SSE/3DNow! optimized routines vs. the standard C routines.
MMX routine incompatibility with new gcc compiler versions
Other miscellaneous bug fixes
1.2.1:

Added automake/autoconf scripts for GNU platforms (in courtesy of David Durham)
Fixed SCALE overflow bug in rate transposer routine.
Fixed 64bit address space bugs.
Created a 'soundtouch' namespace for SAMPLETYPE definitions.
1.2.0:

Added support for 32bit floating point sample data type with SSE/3DNow! optimizations for Win32 platform (SSE/3DNow! optimizations currently not supported in GCC environment)
Replaced 'make-gcc' script for GNU environment by master Makefile
Added time-stretch routine configurability to SoundTouch main class
Bugfixes
1.1.1:

Moved SoundTouch under lesser GPL license (LGPL). This allows using SoundTouch library in programs that aren't released under GPL license.
Changed MMX routine organiation so that MMX optimized routines are now implemented in classes that are derived from the basic classes having the standard non-mmx routines.
MMX routines to support gcc version 3.
Replaced windows makefiles by script using the .dsw files
1.0.1:

"mmx_gcc.cpp": Added "using namespace std" and removed "return 0" from a function with void return value to fix compiler errors when compiling the library in Solaris environment.
Moved file "FIFOSampleBuffer.h" to "include" directory to allow accessing the FIFOSampleBuffer class from external files.
1.0:

Initial release
 

5.2. SoundStretch application Change History

1.9:

Added support for WAV file 'fact' information chunk.
1.7.0:

Bugfixes in Wavfile: exception string formatting, avoid getLengthMs() integer precision overflow, support WAV files using 24/32bit sample format.
1.5.0:

Added "-speech" switch to activate algorithm parameters more suitable for speech processing than the default parameters tuned for music processing.
1.4.0:

Moved BPM detection routines from SoundStretch application into SoundTouch library
Allow using standard input/output pipes as audio processing input/output streams
1.3.0:

Simplified accessing WAV files with floating point sample format.
1.2.1:

Fixed 64bit address space bugs.
1.2.0:

Added support for 32bit floating point sample data type
Restructured the BPM routines into separate library
Fixed big-endian conversion bugs in WAV file routines (hopefully :)
1.1.1:

Fixed bugs in WAV file reading & added byte-order conversion for big-endian processors.
Moved SoundStretch source code under 'example' directory to highlight difference from SoundTouch stuff.
Replaced windows makefiles by script using the .dsw files
Output file name isn't required if output isn't desired (e.g. if using the switch '-bpm' in plain format only)
1.1:

Fixed "Release" settings in Microsoft Visual C++ project file (.dsp)
Added beats-per-minute (BPM) detection routine and command-line switch "-bpm"
1.01:

Initial release
6. Acknowledgements

Kudos for these people who have contributed to development or submitted bugfixes:

Arthur A
Richard Ash
Stanislav Brabec
Christian Budde
Chris Bryan
Jacek Caban
Brian Cameron
Jason Champion
David Clark
Patrick Colis
Miquel Colon
Jim Credland
Sandro Cumerlato
Justin Frankel
Masa H.
Jason Garland
Takashi Iwai
Thomas Klausner
Mathias Möhl
Yuval Naveh
Paulo Pizarro
Blaise Potard
Michael Pruett
Rajeev Puran
RJ Ryan
John Sheehy
Tim Shuttleworth
Albert Sirvent
John Stumpo
Katja Vetter
Moral greetings to all other contributors and users also!

7. LICENSE

SoundTouch audio processing library
Copyright (c) Olli Parviainen

This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License version 2.1 as published by the Free Software Foundation.

This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

README.html file updated in Sep-2015