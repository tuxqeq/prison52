package com.prison.model;

import com.prison.exception.*;
import com.prison.test.SimpleUnitTest;
import java.time.LocalDate;

public class CourtCaseTest extends SimpleUnitTest {
    public static void main(String[] args) {
        runTest("testCourtCaseValidation", () -> {
            assertThrows(InvalidReferenceException.class, () -> {
                new CourtCase(null, CourtCase.CaseStatus.PENDING, "Judge Judy");
            });
            assertThrows(EmptyStringException.class, () -> {
                new CourtCase(LocalDate.now(), CourtCase.CaseStatus.PENDING, "");
            });
        });
    }
}
