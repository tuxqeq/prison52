package com.prison.model;

import com.prison.exception.*;
import com.prison.test.SimpleUnitTest;
import java.time.LocalDate;

public class PunishmentTest extends SimpleUnitTest {
    public static void main(String[] args) {
        runTest("testPunishmentValidation", () -> {
            Prisoner prisoner = new Prisoner("Test", "Prisoner", 30, "Test", 
                LocalDate.of(2020, 1, 1), 5, "None", "Active");
            IncidentReport incident = new IncidentReport(LocalDate.now(), "Fight", IncidentReport.Status.OPEN);
            
            assertThrows(EmptyStringException.class, () -> {
                new Punishment("", "Description", LocalDate.now(), 5, "Active");
            });
            assertThrows(NegativeNumberException.class, () -> {
                new Punishment("Solitary", "Description", LocalDate.now(), -1, "Active");
            });
            
            Prisoner.clearExtent();
            IncidentReport.clearExtent();
        });
    }
}
