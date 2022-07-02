@ECHO OFF
:: Batch Script to compile and run the custom test class
cd ../src/test/java
javac -cp ../../../bin -d ../../../bin GridTest.java
java -cp ../../../bin test.java.GridTest
cd ../../../scripts
PAUSE