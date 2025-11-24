package com.prison.model;

import com.prison.exception.*;
import com.prison.test.SimpleUnitTest;
import java.time.LocalDate;

public class MedicalExaminationTest extends SimpleUnitTest {
    public static void main(String[] args) {
        runTest("testMedicalExamFutureDate", () -> {
            Prisoner prisoner = new Prisoner("Test", "Prisoner", 30, "Test", 
                LocalDate.of(2020, 1, 1), 5, "None", "Active");
            Doctor doctor = new Doctor("Dr.", "Test", 5, "9am-5pm", "555-0100", 
                "test@hospital.com", "MD123", "555-0101");
            
            assertThrows(InvalidDateException.class, () -> {
                new MedicalExamination(LocalDate.now().plusDays(1), "Checkup", "None", doctor);
            });
            
            Prisoner.clearExtent();
            Doctor.clearDoctorExtent();
        });
    }
}
