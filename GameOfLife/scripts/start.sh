#!/bin/bash
#Bash script to compile and run the GameOfLife Application
cd ../src/main/java
javac -d ../../../bin *.java
printf "\nRunning Program...\n"
java -cp ../../../bin main.java.Main
cd ../../../scripts
$SHELL