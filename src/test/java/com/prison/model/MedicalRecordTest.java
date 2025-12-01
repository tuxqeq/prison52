package com.prison.model;

import com.prison.exception.*;
import com.prison.test.SimpleUnitTest;
import java.time.LocalDate;

public class MedicalRecordTest extends SimpleUnitTest {
    public static void main(String[] args) {
        runTest("testMedicalRecordHistory", () -> {
            MedicalRecord mr = new MedicalRecord(LocalDate.now(), "Healthy");
            mr.addHistory("Initial checkup");
            assertThrows(EmptyStringException.class, () -> {
                mr.addHistory("");
            });
        });
    }
}
