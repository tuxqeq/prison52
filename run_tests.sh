#!/bin/bash
# Compile all source and test files
echo "Compiling..."
javac -d out src/main/java/com/prison/exception/*.java src/main/java/com/prison/model/*.java src/test/java/com/prison/test/SimpleUnitTest.java src/test/java/com/prison/model/*.java

if [ $? -eq 0 ]; then
    echo "Compilation successful."
    echo "Running Tests..."
    
    for testFile in src/test/java/com/prison/model/*Test.java; do
        className=$(basename "$testFile" .java)
        echo "--------------------------------------------------"
        echo "Running $className..."
        java -cp out "com.prison.model.$className"
    done
else
    echo "Compilation failed."
fi
