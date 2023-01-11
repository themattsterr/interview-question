@echo off
del bin\*.class
javac src\*.java -d bin
cd bin
java MainDriver