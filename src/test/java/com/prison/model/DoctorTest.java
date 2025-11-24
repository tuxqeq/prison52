package com.prison.model;

import com.prison.exception.*;
import com.prison.test.SimpleUnitTest;

public class DoctorTest extends SimpleUnitTest {
    public static void main(String[] args) {
        runTest("testDoctorCreation", () -> {
            Doctor d = new Doctor("Dr", "House", 15, "9am-5pm", "555-0100", 
                "house@hospital.com", "LIC-123", "555-0101");
            assertEquals("LIC-123", d.getLicenseNumber());
        });

        runTest("testDoctorInvalidLicense", () -> {
            assertThrows(EmptyStringException.class, () -> {
                new Doctor("Dr", "House", 15, "9am-5pm", "555-0100", 
                    "house@hospital.com", "", "555-0101");
            });
        });

        runTest("testDoctorSpecialization", () -> {
            Doctor d = new Doctor("Dr", "House", 15, "9am-5pm", "555-0100", 
                "house@hospital.com", "LIC-123", "555-0101");
            d.addSpecialization("Diagnostic");
            assertEquals(1, d.getSpecializations().size());
            assertThrows(EmptyStringException.class, () -> {
                d.addSpecialization(null);
            });
        });
    }
}
