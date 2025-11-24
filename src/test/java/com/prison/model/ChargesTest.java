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
                new Charges("", "Section 1", "High", LocalDate.now(), prisoner, courtCase);
            });
            assertThrows(EmptyStringException.class, () -> {
                new Charges("Theft", "", "High", LocalDate.now(), prisoner, courtCase);
            });
            
            Prisoner.clearExtent();
            CourtCase.clearExtent();
            Charges.clearExtent();
        });
    }
}
