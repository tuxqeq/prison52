package com.prison.model;

import com.prison.exception.*;
import com.prison.test.SimpleUnitTest;
import java.time.LocalDate;

public class IncidentReportTest extends SimpleUnitTest {
    public static void main(String[] args) {
        runTest("testIncidentReportValidation", () -> {
            Prisoner prisoner = new Prisoner("Test", "Prisoner", 30, "Test", 
                LocalDate.of(2020, 1, 1), 5, "None", "Active");
            
            assertThrows(EmptyStringException.class, () -> {
                new IncidentReport(LocalDate.now(), "", "Pending");
            });
            
            // Valid incident report
            IncidentReport report = new IncidentReport(LocalDate.now(), "Fight in cafeteria", "Open");
            report.addPersonInvolved("Prisoner A");
            assertEquals("Open", report.getStatus());
            
            Prisoner.clearExtent();
            IncidentReport.clearExtent();
        });
    }
}
