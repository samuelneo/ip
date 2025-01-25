#!/usr/bin/env bash

# create bin directory if it doesn't exist
if [ ! -d "../bin" ]
then
    mkdir ../bin
fi

# delete output from previous run
if [ -e "./ACTUAL.txt" ]
then
    rm ACTUAL.txt
fi

# compile the code into the bin folder, terminates if error occurred
if ! javac -cp ../src/main/java -Xlint:none -d ../bin ../src/main/java/carbon/*.java
then
    echo "********** BUILD FAILURE **********"
    exit 1
fi

# run the program, feed commands from input.txt file and redirect the output to the ACTUAL.txt
java -classpath ../bin carbon.Carbon < input.txt > ACTUAL.txt

# convert to UNIX format
cp EXPECTED.txt EXPECTED-UNIX.txt
dos2unix ACTUAL.txt EXPECTED-UNIX.txt

# compare the output to the expected output
diff ACTUAL.txt EXPECTED-UNIX.txt
if [ $? -eq 0 ]
then
    echo "Test result: PASSED"
    exit 0
else
    echo "Test result: FAILED"
    exit 1
fi