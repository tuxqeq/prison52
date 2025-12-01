package com.prison.model;

import com.prison.exception.*;
import com.prison.test.SimpleUnitTest;

public class VisitorTest extends SimpleUnitTest {
    public static void main(String[] args) {
        runTest("testVisitorValidation", () -> {
            assertThrows(EmptyStringException.class, () -> {
                new Visitor("", "Doe", "555-0000", "Friend");
            });
            assertThrows(EmptyStringException.class, () -> {
                new Visitor("John", "", "555-0000", "Friend");
            });
            assertThrows(EmptyStringException.class, () -> {
                new Visitor("John", "Doe", "", "Friend");
            });
            assertThrows(EmptyStringException.class, () -> {
                new Visitor("John", "Doe", "555-0000", "");
            });
        });
        
        runTest("testVisitorMaxVisits", () -> {
            assertEquals(2, Visitor.getMaxAmountOfVisitPerMonth());
        });
    }
}
