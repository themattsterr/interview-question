JDK 8 or higher required to build and run.

REQUIRED SOURCE FILES
    ConcurrentEquationGenerator.java
    MainDriver.java
    PartitionedInput.java

BUILD PROCEDURES
    cd <path to source files>
    javac *.java -d bin

RUN PROCEDURES
    cd <path to source files>\bin
    java MainDriver

RUN AND BULD BATCH FILE
    Alternatively, the text file "rename_me.txt" can be renamed to "build_and_run.bat" and double-clicked to build and run.
    The .bat file must be located in the same directlory as all the required source files

Problem Statement
Write a program that takes as input a string of numbers (ex, 5913168) and a target result (ex, 32), and figures out where within the string of numbers to insert the operators +, -, * and / in order to come up with an equation that equals the target result.  Each operator can be used multiple times or not at all, and the order of operations should go from left to right, ignoring the standard order of operations.  For the above example inputs, the solution would be 59 + 1 / 3 -16 * 8 = 32 and would be solved as

59 + 1 = 60
60 / 3 = 20
20 – 16 = 4
4 * 8 = 32

Finding the correct solution is the primary goal, but candidates should also consider various ways to optimize the program.  If multiple solutions are possible, only a single solution needs to be found.  Any assumptions made about the inputs, results, or solutions should be clearly documented.
Examples
Input		    Result		Solution
22114313	    1878		22 / 11 + 4 * 313
5913168	        32		    59 + 1 / 3 – 16 * 8
5913168	        369		    5 * 9 * 1 + 316 + 8
942319409933	4		    94231 – 94099 / 33

Deliverables
You should submit all of your source code and instructions on how to build and run.  3rd party libraries may be used for utility purposes, but you must implement the solution on your own.  If any 3rd party libraries are used, you may either include them with your source if they are relatively small, or provide download instructions for them.
How input is read in and results are output is entirely up to you.  Ideally, you should be able to run it multiple times for multiple inputs without having to restart each time.  C++ is preferred, but C#, Java, or Python may also be used.
