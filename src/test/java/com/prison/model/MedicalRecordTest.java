package com.prison.model;

import com.prison.exception.*;
import com.prison.test.SimpleUnitTest;
import java.time.LocalDate;

public class MedicalRecordTest extends SimpleUnitTest {
    public static void main(String[] args) {
        runTest("testMedicalRecordAllergy", () -> {
            MedicalRecord mr = new MedicalRecord("O+", LocalDate.now(), "Healthy");
            mr.addAllergy("Peanuts");
            assertThrows(EmptyStringException.class, () -> {
                mr.addAllergy("");
            });
        });
    }
}
