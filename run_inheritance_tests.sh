#!/bin/bash
# Run Inheritance Implementation Tests

echo "========================================="
echo "Compiling Inheritance Tests..."
echo "========================================="

javac -d bin -cp ".:bin" \
    src/main/java/com/prison/exception/*.java \
    src/main/java/com/prison/model/*.java \
    src/test/java/com/prison/test/SimpleUnitTest.java \
    src/test/java/com/prison/model/InheritanceImplementationTest.java

if [ $? -eq 0 ]; then
    echo ""
    echo "========================================="
    echo "Running Inheritance Tests..."
    echo "========================================="
    echo ""
    java -cp bin com.prison.model.InheritanceImplementationTest
else
    echo "Compilation failed!"
    exit 1
fi
