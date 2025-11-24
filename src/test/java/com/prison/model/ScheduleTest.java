package com.prison.model;

import com.prison.exception.*;
import com.prison.test.SimpleUnitTest;
import java.time.LocalTime;

public class ScheduleTest extends SimpleUnitTest {
    public static void main(String[] args) {
        runTest("testScheduleEndTimeBeforeStartTime", () -> {
            assertThrows(InvalidDateException.class, () -> {
                new Schedule(LocalTime.of(10, 0), LocalTime.of(9, 0), Schedule.ActivityType.Work);
            });
        });
        
        runTest("testScheduleValid", () -> {
            Schedule s = new Schedule(LocalTime.of(9, 0), LocalTime.of(10, 0), Schedule.ActivityType.Work);
            assertEquals(Schedule.ActivityType.Work, s.getType());
        });
    }
}
