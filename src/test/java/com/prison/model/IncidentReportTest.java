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
                new IncidentReport(LocalDate.now(), "", IncidentReport.Status.OPEN);
            });
            
            // Valid incident report
            IncidentReport report = new IncidentReport(LocalDate.now(), "Fight in cafeteria", IncidentReport.Status.OPEN);
            report.addPersonInvolved("Prisoner A");
            assertEquals(IncidentReport.Status.OPEN, report.getStatus());
            
            Prisoner.clearExtent();
            IncidentReport.clearExtent();
        });
    }
}
