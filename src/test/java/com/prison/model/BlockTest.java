package com.prison.model;

import com.prison.exception.*;
import com.prison.test.SimpleUnitTest;

public class BlockTest extends SimpleUnitTest {
    public static void main(String[] args) {
        runTest("testBlockCreation", () -> {
            Block b = new Block("A", 10, Block.BlockType.MINIMUM_SECURITY);
            assertEquals("A", b.getName());
        });

        runTest("testBlockNegativeCells", () -> {
            assertThrows(NegativeNumberException.class, () -> {
                new Block("A", -1, Block.BlockType.MINIMUM_SECURITY);
            });
        });

        runTest("testBlockEmptyName", () -> {
            assertThrows(EmptyStringException.class, () -> {
                new Block("", 10, Block.BlockType.MINIMUM_SECURITY);
            });
        });
    }
}
