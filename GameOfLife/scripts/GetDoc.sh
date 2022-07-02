#!/bin/bash
#Batch script to generate the JavaDoc
cd ../src/main/java
printf "\nGenerating JavaDoc...\n"
javadoc -d ../../../docs *.java
cd ../../../scripts
$SHELL