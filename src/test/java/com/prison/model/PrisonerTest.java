package com.prison.model;

import com.prison.exception.*;
import com.prison.test.SimpleUnitTest;
import java.time.LocalDate;
import java.io.File;

public class PrisonerTest extends SimpleUnitTest {

    public static void main(String[] args) {
        System.out.println("Running PrisonerTest...");
        
        runTest("testMandatoryAttributes", () -> {
            Prisoner.clearExtent();
            Prisoner p = new Prisoner("John", "Doe", 30, "Theft", 
                LocalDate.of(2020, 1, 1), 5, "None", "Active");
            assertEquals("John", p.getName());
            assertEquals("Doe", p.getSurname());
            assertEquals("Theft", p.getCrime());
        });

        runTest("testInvalidName", () -> {
            assertThrows(EmptyStringException.class, () -> {
                new Prisoner("", "Doe", 30, "Theft", 
                    LocalDate.of(2020, 1, 1), 5, "None", "Active");
            });
            assertThrows(EmptyStringException.class, () -> {
                new Prisoner(null, "Doe", 30, "Theft", 
                    LocalDate.of(2020, 1, 1), 5, "None", "Active");
            });
        });

        runTest("testInvalidAge", () -> {
            assertThrows(NegativeNumberException.class, () -> {
                new Prisoner("John", "Doe", -1, "Theft", 
                    LocalDate.of(2020, 1, 1), 5, "None", "Active");
            });
        });

        runTest("testAgeAttribute", () -> {
            Prisoner p = new Prisoner("John", "Doe", 25, "Theft", 
                LocalDate.of(2020, 1, 1), 5, "None", "Active");
            assertEquals(25, p.getAge());
        });

        runTest("testExtentAddition", () -> {
            Prisoner.clearExtent();
            new Prisoner("P1", "S1", 30, "C1", 
                LocalDate.of(2020, 1, 1), 5, "None", "Active");
            new Prisoner("P2", "S2", 35, "C2", 
                LocalDate.of(2020, 1, 1), 5, "None", "Active");
            assertEquals(2, Prisoner.getExtent().size());
        });

        runTest("testPersistence", () -> {
            try {
                Prisoner.clearExtent();
                String filename = "test_prisoners.ser";
                
                // Create and Save
                new Prisoner("SaveMe", "Please", 40, "Testing", 
                    LocalDate.of(2020, 1, 1), 5, "None", "Active");
                Prisoner.saveExtent(filename);
                
                // Clear
                Prisoner.clearExtent();
                assertEquals(0, Prisoner.getExtent().size());
                
                // Load
                Prisoner.loadExtent(filename);
                assertEquals(1, Prisoner.getExtent().size());
                assertEquals("SaveMe", Prisoner.getExtent().get(0).getName());
                
                // Cleanup
                new File(filename).delete();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
