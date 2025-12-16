package com.prison;

import com.prison.model.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;


public class Main {
    public static void main(String[] args) {
        System.out.println("=== Prison Management System - Complete Integration Test ===\n");

        try {
            // 1. Create Prisoners
            System.out.println("1. CREATING PRISONERS");
            System.out.println("-".repeat(50));
            Prisoner p1 = new Prisoner("John", "Doe", 35, "Theft", 
                LocalDate.of(2020, 1, 15), 10, "None", "Active");
            Prisoner p2 = new Prisoner("Jane", "Smith", 40, "Fraud", 
                LocalDate.of(2019, 6, 10), 15, "Supervised", "Active");
            p1.addPossession("Book");
            p1.addAllergyInfo("None");
            p2.addPossession("Photos");
            p2.addAllergyInfo("Peanuts");
            
            System.out.println("✓ Prisoner 1: " + p1);
            System.out.println("✓ Prisoner 2: " + p2);
            System.out.println("✓ Total Prisoners in Extent: " + Prisoner.getExtent().size());

            // 2. Create Staff (Doctor, Guard, Director)
            System.out.println("\n2. CREATING STAFF");
            System.out.println("-".repeat(50));
            Doctor d1 = new Doctor("Gregory", "House", 15, "9am-5pm", 
                "555-0100", "house@hospital.com", "MED-12345", "555-0101");
            d1.addSpecialisation("Diagnostician");
            d1.addSpecialisation("Nephrology");
            
            Guard g1 = new Guard("Paul", "Blart", 5, "Day Shift", "555-0200", 
                "blart@prison.com", Guard.Rank.SENIOR, "Baton");
            
            Director dir1 = new Director("Warden", "Norton", 20, "8am-6pm", "555-0300", 
                "norton@prison.com", Director.DirectorRank.GENERAL);
            
            System.out.println("✓ Doctor: " + d1.getName() + " " + d1.getSurname() + ", License: " + d1.getLicenseNumber());
            System.out.println("✓ Guard: " + g1.getName() + " " + g1.getSurname() + ", Rank: " + g1.getRank());
            System.out.println("✓ Director: " + dir1.getName() + " " + dir1.getSurname() + ", Rank: " + dir1.getRank());
            System.out.println("✓ Total Staff: " + Staff.getExtent().size());

            // 3. Create Block and Cells (Aggregation)
            System.out.println("\n3. TESTING AGGREGATION: Block-Cell");
            System.out.println("-".repeat(50));
            Block block1 = new Block("Block A", 50, Block.BlockType.MAXIMUM_SECURITY);
            Cell cell1 = new Cell(101, "Solitary", 1, Cell.SecurityLevel.HIGH);
            Cell cell2 = new Cell(102, "Shared", 4, Cell.SecurityLevel.MEDIUM);
            
            block1.addCell(cell1);
            block1.addCell(cell2);
            
            System.out.println("✓ Block: " + block1.getName() + ", Max Capacity: " + block1.getMaxCapacity());
            System.out.println("✓ Cell 1: #" + cell1.getCellNumber() + " in Block: " + (cell1.getBlock() != null ? cell1.getBlock().getName() : "None"));
            System.out.println("✓ Cell 2: #" + cell2.getCellNumber() + " in Block: " + (cell2.getBlock() != null ? cell2.getBlock().getName() : "None"));
            System.out.println("✓ Cells in Block A: " + block1.getCells().size());

            // 4. Test Basic Association: Prisoner-Cell (unidirectional)
            System.out.println("\n4. TESTING BASIC ASSOCIATION: Prisoner→Cell");
            System.out.println("-".repeat(50));
            p1.assignToCell(cell1);
            p2.assignToCell(cell2);
            System.out.println("✓ " + p1.getName() + " assigned to Cell #" + p1.getCurrentCell().getCellNumber());
            System.out.println("✓ " + p2.getName() + " assigned to Cell #" + p2.getCurrentCell().getCellNumber());

            // 5. Test Qualified Association: Visitor-Visit by Date
            System.out.println("\n5. TESTING QUALIFIED ASSOCIATION: Visitor-Visit by Date");
            System.out.println("-".repeat(50));
            Visitor visitor1 = new Visitor("Mary", "Doe", "555-1234", "Sister");
            LocalDate visitDate1 = LocalDate.now().plusDays(1);
            LocalDate visitDate2 = LocalDate.now().plusDays(3);
            
            Visit visit1 = new Visit(visitDate1, 60, Visit.VisitType.FAMILY, visitor1, p1);
            Visit visit2 = new Visit(visitDate2, 45, Visit.VisitType.FAMILY, visitor1, p1);
            
            System.out.println("✓ Visitor: " + visitor1.getName() + " " + visitor1.getSurname());
            System.out.println("✓ Visit 1 on " + visitDate1 + ": " + visit1.getDuration() + " minutes");
            System.out.println("✓ Visit 2 on " + visitDate2 + ": " + visit2.getDuration() + " minutes");
            System.out.println("✓ Total visits by date: " + visitor1.getVisitsByDate().size());
            System.out.println("✓ Can retrieve visit by date: " + (visitor1.getVisitByDate(visitDate1) != null));

            // 6. Test Composition: MedicalRecord-MedicalReport with Cascade Delete
            System.out.println("\n6. TESTING COMPOSITION: MedicalRecord→MedicalReport");
            System.out.println("-".repeat(50));
            MedicalRecord mr1 = new MedicalRecord(LocalDate.now(), "History of asthma");
            mr1.addHistory("Initial examination on admission");
            
            MedicalReport report1 = new MedicalReport(LocalDate.now(), "Routine checkup", 
                "Room-101", 30.0, "Low", d1, mr1);
            MedicalReport report2 = new MedicalReport(LocalDate.now().minusDays(1), "X-ray scan", 
                "Room-102", 15.0, "Medium", d1, mr1);
            
            System.out.println("✓ Medical Record created with " + mr1.getMedicalReports().size() + " reports");
            System.out.println("✓ Report 1: " + report1.getDescription() + ", Severity: " + report1.getSeverityLevel());
            System.out.println("✓ Report 2: " + report2.getDescription() + ", Severity: " + report2.getSeverityLevel());
            
            int reportsBefore = MedicalReport.getExtent().size();
            mr1.delete(); // Cascade delete
            int reportsAfter = MedicalReport.getExtent().size();
            System.out.println("✓ Cascade delete: Reports before=" + reportsBefore + ", after=" + reportsAfter);

            // 7. Test Reflex Association: IncidentReport-IncidentReport
            System.out.println("\n7. TESTING REFLEX ASSOCIATION: IncidentReport↔IncidentReport");
            System.out.println("-".repeat(50));
            IncidentReport incident1 = new IncidentReport(LocalDate.now(), "Fight in cafeteria", IncidentReport.Status.OPEN);
            IncidentReport incident2 = new IncidentReport(LocalDate.now(), "Follow-up investigation", IncidentReport.Status.INREVIEW);
            incident1.setReportingGuard(g1);
            incident1.setReviewingDirector(dir1);
            incident2.setReportingGuard(g1);
            incident2.setReviewingDirector(dir1);
            
            incident2.setRelatedIncident(incident1);
            System.out.println("✓ Incident 1: " + incident1.getDescription());
            System.out.println("✓ Incident 2: " + incident2.getDescription());
            System.out.println("✓ Incident 2 related to Incident 1: " + (incident2.getRelatedIncident() != null));

            // 8. Test Association with Attribute: MealDelivery
            System.out.println("\n8. TESTING ASSOCIATION WITH ATTRIBUTE: MealDelivery");
            System.out.println("-".repeat(50));
            Meal meal1 = new Meal("Vegetarian breakfast", Meal.DietPlan.VEGETARIAN, 500.0, Meal.MealType.Breakfast);
            meal1.addAllergen("Gluten");
            
            MealDelivery delivery1 = new MealDelivery(LocalDateTime.now().withHour(8).withMinute(0), p1, meal1);
            delivery1.setStatus(MealDelivery.DeliveryStatus.DELIVERED);
            MealDelivery delivery2 = new MealDelivery(LocalDateTime.now().withHour(8).withMinute(15), p2, meal1);
            
            System.out.println("✓ Meal: " + meal1.getMealType() + ", Calories: " + meal1.getCalories());
            System.out.println("✓ Delivery 1 to " + p1.getName() + ", Status: " + delivery1.getStatus());
            System.out.println("✓ Delivery 2 to " + p2.getName() + ", Status: " + delivery2.getStatus());
            System.out.println("✓ Total deliveries: " + MealDelivery.getExtent().size());

            // 9. Test Many-to-Many: Schedule-Prisoner-Staff
            System.out.println("\n9. TESTING MANY-TO-MANY: Schedule↔Prisoner↔Staff");
            System.out.println("-".repeat(50));
            Schedule schedule1 = new Schedule(LocalTime.of(9, 0), LocalTime.of(11, 0), 
                Schedule.ActivityType.Exercise, block1);
            
            schedule1.addPrisoner(p1);
            schedule1.addStaff(g1);
            
            System.out.println("✓ Schedule Type: " + schedule1.getType());
            System.out.println("✓ Time: " + schedule1.getStartTime() + " - " + schedule1.getEndTime());
            System.out.println("✓ Prisoners in schedule: " + schedule1.getPrisoners().size());
            System.out.println("✓ Staff in schedule: " + schedule1.getStaffMembers().size());
            System.out.println("✓ " + p1.getName() + " has " + p1.getSchedules().size() + " schedule(s)");
            System.out.println("✓ " + g1.getName() + " has " + g1.getSchedules().size() + " schedule(s)");

            // 10. Test Additional Features
            System.out.println("\n10. TESTING ADDITIONAL FEATURES");
            System.out.println("-".repeat(50));
            
            // Court Case
            CourtCase courtCase = new CourtCase(LocalDate.now().plusMonths(1), 
                CourtCase.CaseStatus.PENDING, "Judge Smith");
            Charges charges = new Charges("Robbery", "Armed robbery of convenience store", 
                Charges.SeverityLevel.Severe, LocalDate.now(), p1, courtCase);
            System.out.println("✓ Court Case: " + courtCase.getJudgeName() + ", Status: " + courtCase.getStatus());
            System.out.println("✓ Charges: " + charges.getDescription() + ", Severity: " + charges.getSeverityLevel());
            
            // Punishment
            Punishment punishment = new Punishment("SOLITARY_CONFINEMENT", 
                "2 weeks solitary confinement", LocalDate.now(), 14, "Active");
            punishment.addPrisoner(p1);
            System.out.println("✓ Punishment: " + punishment.getDescription() + ", Duration: " + punishment.getDuration() + " days");
            
            // Medical Examination
            MedicalExamination exam = new MedicalExamination(LocalDate.now().minusDays(2), 
                MedicalExamination.ReasonForVisit.Routine, d1);
            exam.addPrescription("Ibuprofen");
            System.out.println("✓ Medical Examination by Dr. " + d1.getSurname() + ", Reason: " + exam.getReasonForVisit());

            // 11. Extent Statistics
            System.out.println("\n11. SYSTEM STATISTICS");
            System.out.println("-".repeat(50));
            System.out.println("✓ Total Prisoners: " + Prisoner.getExtent().size());
            System.out.println("✓ Total Staff: " + Staff.getExtent().size());
            System.out.println("✓ Total Guards: " + Guard.getGuardExtent().size());
            System.out.println("✓ Total Doctors: " + Doctor.getDoctorExtent().size());
            System.out.println("✓ Total Directors: " + Director.getDirectorExtent().size());
            System.out.println("✓ Total Blocks: " + Block.getExtent().size());
            System.out.println("✓ Total Cells: " + Cell.getExtent().size());
            System.out.println("✓ Total Visits: " + Visit.getExtent().size());
            System.out.println("✓ Total Schedules: " + Schedule.getExtent().size());
            System.out.println("✓ Total Meals: " + Meal.getExtent().size());
            System.out.println("✓ Total Meal Deliveries: " + MealDelivery.getExtent().size());
            System.out.println("✓ Total Court Cases: " + CourtCase.getExtent().size());
            System.out.println("✓ Total Punishments: " + Punishment.getExtent().size());
            System.out.println("✓ Total Incident Reports: " + IncidentReport.getExtent().size());

            // 12. Persistence Demo
            System.out.println("\n12. PERSISTENCE TEST");
            System.out.println("-".repeat(50));
            String filename = "prisoners.ser";
            
            System.out.println("Saving prisoners to " + filename + "...");
            Prisoner.saveExtent(filename);
            int savedCount = Prisoner.getExtent().size();
            
            System.out.println("Clearing memory...");
            Prisoner.clearExtent();
            System.out.println("✓ Prisoners after clear: " + Prisoner.getExtent().size());
            
            System.out.println("Loading prisoners from " + filename + "...");
            Prisoner.loadExtent(filename);
            int loadedCount = Prisoner.getExtent().size();
            System.out.println("✓ Prisoners after load: " + loadedCount);
            System.out.println("✓ Persistence verified: " + (savedCount == loadedCount ? "SUCCESS" : "FAILED"));
            
            System.out.println("\n" + "=".repeat(50));
            System.out.println("ALL TESTS COMPLETED SUCCESSFULLY!");
            System.out.println("=".repeat(50));

        } catch (Exception e) {
            System.err.println("\n❌ ERROR OCCURRED:");
            e.printStackTrace();
        }
    }
}
