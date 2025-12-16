package com.prison.model;

import com.prison.exception.*;
import com.prison.test.SimpleUnitTest;
import java.time.LocalDate;

public class VisitTest extends SimpleUnitTest {
    public static void main(String[] args) {
        runTest("testVisitPastDate", () -> {
            Prisoner prisoner = new Prisoner("Test", "Prisoner", 30, "Test", 
                LocalDate.of(2020, 1, 1), 5, "None", "Active");
            Visitor visitor = new Visitor("Test", "Visitor", "555-0000", "Friend");
            
            assertThrows(InvalidDateException.class, () -> {
                new Visit(LocalDate.now().minusDays(1), 60, Visit.VisitType.FAMILY, "VID_TEST1", visitor, prisoner);
            });
            
            Prisoner.clearExtent();
            Visitor.clearExtent();
        });

        runTest("testVisitDuration", () -> {
            Prisoner prisoner = new Prisoner("Test", "Prisoner", 30, "Test", 
                LocalDate.of(2020, 1, 1), 5, "None", "Active");
            Visitor visitor = new Visitor("Test", "Visitor", "555-0000", "Friend");
            Visit v = new Visit(LocalDate.now().plusDays(1), 30, Visit.VisitType.LAWYER, "VID_TEST2", visitor, prisoner);
            
            assertEquals(30, v.getDuration());
            
            // Duration must be positive
            assertThrows(NegativeNumberException.class, () -> {
                v.setDuration(-10);
            });
            
            Prisoner.clearExtent();
            Visitor.clearExtent();
        });
    }
}
