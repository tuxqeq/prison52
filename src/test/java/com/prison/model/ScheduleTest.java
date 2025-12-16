package com.prison.model;

import com.prison.exception.*;
import com.prison.test.SimpleUnitTest;
import java.time.LocalTime;

public class ScheduleTest extends SimpleUnitTest {
    public static void main(String[] args) {
        runTest("testScheduleEndTimeBeforeStartTime", () -> {
            Block block = new Block("Test Block", 10, Block.BlockType.MINIMUM_SECURITY);
            assertThrows(InvalidDateException.class, () -> {
                new Schedule(LocalTime.of(10, 0), LocalTime.of(9, 0), Schedule.ActivityType.Work, block);
            });
        });
        
        runTest("testScheduleValid", () -> {
            Block block = new Block("Test Block", 10, Block.BlockType.MINIMUM_SECURITY);
            Schedule s = new Schedule(LocalTime.of(9, 0), LocalTime.of(10, 0), Schedule.ActivityType.Work, block);
            assertEquals(Schedule.ActivityType.Work, s.getType());
            assertEquals(block, s.getBlock());
        });
    }
}
