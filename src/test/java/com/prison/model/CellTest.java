package com.prison.model;

import com.prison.exception.*;
import com.prison.test.SimpleUnitTest;

public class CellTest extends SimpleUnitTest {
    public static void main(String[] args) {
        runTest("testCellCreation", () -> {
            Cell c = new Cell(101, "Shared", 2, Cell.SecurityLevel.MEDIUM);
            assertEquals(101, c.getCellNumber());
            assertEquals(2, c.getCapasity());
        });

        runTest("testCellInvalidCapacity", () -> {
            assertThrows(NegativeNumberException.class, () -> {
                new Cell(101, "Shared", 0, Cell.SecurityLevel.MEDIUM);
            });
        });

        runTest("testCellInvalidSecurityLevel", () -> {
            assertThrows(InvalidReferenceException.class, () -> {
                new Cell(101, "Shared", 2, null);
            });
        });
    }
}
