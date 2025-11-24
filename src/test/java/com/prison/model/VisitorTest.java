package com.prison.model;

import com.prison.exception.*;
import com.prison.test.SimpleUnitTest;

public class VisitorTest extends SimpleUnitTest {
    public static void main(String[] args) {
        runTest("testVisitorValidation", () -> {
            assertThrows(EmptyStringException.class, () -> {
                new Visitor("", "Doe", "Friend");
            });
            assertThrows(EmptyStringException.class, () -> {
                new Visitor("John", "", "Friend");
            });
            assertThrows(EmptyStringException.class, () -> {
                new Visitor("John", "Doe", "");
            });
        });
    }
}
