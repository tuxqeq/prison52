package com.prison.model;

import com.prison.exception.*;
import com.prison.test.SimpleUnitTest;
import java.time.LocalDate;
import java.time.LocalTime;

public class VisitTest extends SimpleUnitTest {
    public static void main(String[] args) {
        runTest("testVisitPastDate", () -> {
            Prisoner prisoner = new Prisoner("Test", "Prisoner", 30, "Test", 
                LocalDate.of(2020, 1, 1), 5, "None", "Active");
            Visitor visitor = new Visitor("Test", "Visitor", "Friend");
            
            assertThrows(InvalidDateException.class, () -> {
                new Visit(LocalDate.now().minusDays(1), LocalTime.now(), visitor);
            });
            
            Prisoner.clearExtent();
            Visitor.clearExtent();
        });

        runTest("testVisitRejectionReason", () -> {
            Prisoner prisoner = new Prisoner("Test", "Prisoner", 30, "Test", 
                LocalDate.of(2020, 1, 1), 5, "None", "Active");
            Visitor visitor = new Visitor("Test", "Visitor", "Friend");
            Visit v = new Visit(LocalDate.now().plusDays(1), LocalTime.now(), visitor);
            v.setApprovalStatus(Visit.ApprovalStatus.REJECTED);
            
            // Should throw if reason is null/empty when rejected
            assertThrows(EmptyStringException.class, () -> {
                v.setRejectionReason(null);
            });
            
            v.setRejectionReason("Security risk");
            assertEquals("Security risk", v.getRejectionReason());
            
            Prisoner.clearExtent();
            Visitor.clearExtent();
        });
    }
}
