package com.prison.model;

import com.prison.exception.*;
import com.prison.test.SimpleUnitTest;
import java.time.LocalDate;

public class ChargesTest extends SimpleUnitTest {
    public static void main(String[] args) {
        runTest("testChargesValidation", () -> {
            Prisoner prisoner = new Prisoner("Test", "Prisoner", 30, "Test", 
                LocalDate.of(2020, 1, 1), 5, "None", "Active");
            CourtCase courtCase = new CourtCase(LocalDate.now().plusMonths(1), CourtCase.CaseStatus.PENDING, "Judge Smith");
            
            assertThrows(EmptyStringException.class, () -> {
                new Charges("", "Section 1", Charges.SeverityLevel.Severe, LocalDate.now(), prisoner, courtCase);
            });
            // lawSection is now optional [0..1], so null/empty is allowed
            Charges validCharge = new Charges("Theft", null, Charges.SeverityLevel.Minor, LocalDate.now(), prisoner, courtCase);
            assertEquals(Charges.SeverityLevel.Minor, validCharge.getSeverityLevel());
            
            Prisoner.clearExtent();
            CourtCase.clearExtent();
            Charges.clearExtent();
        });
    }
}
