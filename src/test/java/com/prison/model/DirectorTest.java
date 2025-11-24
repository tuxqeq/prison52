package com.prison.model;

import com.prison.exception.*;
import com.prison.test.SimpleUnitTest;

public class DirectorTest extends SimpleUnitTest {
    public static void main(String[] args) {
        runTest("testDirectorCreation", () -> {
            Director d = new Director("Alice", "Boss", 10, "8am-6pm", "555-0200", 
                "boss@prison.com", Director.DirectorRank.GENERAL);
            assertEquals("Alice", d.getName());
            assertEquals(Director.DirectorRank.GENERAL, d.getRank());
        });

        runTest("testDirectorInvalidRank", () -> {
            assertThrows(InvalidReferenceException.class, () -> {
                new Director("Alice", "Boss", 10, "8am-6pm", "555-0200", 
                    "boss@prison.com", null);
            });
        });
    }
}
