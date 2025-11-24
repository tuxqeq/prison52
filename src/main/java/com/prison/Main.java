package com.prison;

import com.prison.model.*;
import java.time.LocalDate;


public class Main {
    public static void main(String[] args) {
        System.out.println("--- Prison Management System Demo ---");

        try {
            // 1. Create Prisoners
            System.out.println("\nCreating Prisoners...");
            Prisoner p1 = new Prisoner("John", "Doe", 35, "Theft", 
                LocalDate.of(2020, 1, 15), 10, "None", "Active");
            Prisoner p2 = new Prisoner("Jane", "Smith", 40, "Fraud", 
                LocalDate.of(2019, 6, 10), 15, "Supervised", "Active");
            p1.addPossession("Book");
            p1.addAllergyInfo("None");
            p2.addPossession("Photos");
            p2.addAllergyInfo("Peanuts");
            
            System.out.println("Prisoner 1: " + p1);
            System.out.println("Prisoner 2: " + p2);
            System.out.println("Total Prisoners in Extent: " + Prisoner.getExtent().size());

            // 2. Create Staff
            System.out.println("\nCreating Staff...");
            Doctor d1 = new Doctor("Gregory", "House", 15, "9am-5pm", 
                "555-0100", "house@hospital.com", "MED-12345", "555-0101");
            d1.addSpecialization("Diagnostician");
            d1.addSpecialization("Nephrology");
            
            System.out.println("Doctor: " + d1.getName() + " " + d1.getSurname() + ", Lic: " + d1.getLicenseNumber());
            System.out.println("Total Doctors in Extent: " + Doctor.getDoctorExtent().size());

            // 3. Create Cells
            System.out.println("\nCreating Cells...");
            new Cell(101, "Solitary", 1, Cell.SecurityLevel.HIGH);
            new Cell(102, "Shared", 4, Cell.SecurityLevel.MEDIUM);
            System.out.println("Total Cells in Extent: " + Cell.getExtent().size());

            // 5. Create Guards and Directors
            System.out.println("\nCreating Guards and Directors...");
            new Guard("Paul", "Blart", 5, "Day Shift", "555-0200", 
                "blart@prison.com", Guard.Rank.SENIOR, "Baton");
            new Director("Warden", "Norton", 20, "8am-6pm", "555-0300", 
                "norton@prison.com", Director.DirectorRank.GENERAL);
            System.out.println("Total Guards: " + Guard.getGuardExtent().size());
            System.out.println("Total Directors: " + Director.getDirectorExtent().size());

            // 6. Create Block
            System.out.println("\nCreating Blocks...");
            new Block("Block A", 50, Block.BlockType.MAXIMUM_SECURITY);
            System.out.println("Total Blocks: " + Block.getExtent().size());

            // 7. Create Visit
            System.out.println("\nCreating Visits...");
            Visitor visitor = new Visitor("Mary", "Doe", "Sister");
            new Visit(LocalDate.now().plusDays(1), java.time.LocalTime.of(14, 0), visitor);
            System.out.println("Total Visits: " + Visit.getExtent().size());

            // 8. Create Medical Record
            System.out.println("\nCreating Medical Records...");
            MedicalRecord mr = new MedicalRecord("O+", LocalDate.now(), "History of asthma");
            mr.addAllergy("Peanuts");
            mr.addHistory("Initial examination");
            System.out.println("Total Medical Records: " + MedicalRecord.getExtent().size());

            // 9. Persistence Demo (Updated)
            System.out.println("\n--- Persistence Demo (Updated) ---");
            String filename = "prisoners.ser";
            
            // Save
            System.out.println("Saving prisoners to " + filename + "...");
            Prisoner.saveExtent(filename);
            
            // Clear memory
            System.out.println("Clearing memory...");
            Prisoner.clearExtent();
            System.out.println("Total Prisoners after clear: " + Prisoner.getExtent().size());
            
            // Load
            System.out.println("Loading prisoners from " + filename + "...");
            Prisoner.loadExtent(filename);
            System.out.println("Total Prisoners after load: " + Prisoner.getExtent().size());
            
            for (Prisoner p : Prisoner.getExtent()) {
                System.out.println("Loaded: " + p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
