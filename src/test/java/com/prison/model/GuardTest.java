package com.prison.model;

import com.prison.exception.*;
import com.prison.test.SimpleUnitTest;

public class GuardTest extends SimpleUnitTest {
    public static void main(String[] args) {
        runTest("testGuardCreation", () -> {
            Guard g = new Guard("Paul", "Blart", 5, "Day Shift", "555-0100", 
                "blart@prison.com", Guard.Rank.SENIOR, "Baton");
            assertEquals("Paul", g.getName());
            assertEquals(Guard.Rank.SENIOR, g.getRank());
        });

        runTest("testGuardInvalidRank", () -> {
            assertThrows(InvalidReferenceException.class, () -> {
                new Guard("Paul", "Blart", 5, "Day Shift", "555-0100", 
                    "blart@prison.com", null, "Baton");
            });
        });
    }
}
