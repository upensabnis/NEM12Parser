# NEM12Parser

Overview
---------
This project implements parser for NEM12 file format in java.

Some of the questions I came across for which I thought of:
- How big generally this file is? Are there any server resource constraints? If it is a huge file, then reading whoele file in memory might consume lot of memory.
- What should be done when un-expected recrord type is found? File does not start with 100 OR recordtype 300 is found instead of 200?
- What if NMI is not 10 char long?
- Once parsed, how will we use this data? Accordingly we can choose data structure.

I have implemented my code taking care of all above constraints.

How to build:
-------------
This is a maven based project. So standard maven command like ``mvn clean verify`` should work.

System Requirements:
---------------------
1. Java 1.8
2. Maven 3.0
