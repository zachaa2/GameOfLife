#!/bin/bash
#Bash script to run the Java Test Class
cd ../src/test/java
javac -cp ../../../bin -d ../../../bin GridTest.java
java -cp ../../../bin test.java.GridTest
cd ../../../scripts
$SHELL