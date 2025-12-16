package com.prison.model;

import com.prison.exception.*;
import com.prison.test.SimpleUnitTest;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

/**
 * Comprehensive tests for all association types:
 * 1. Basic Association (with 1..* multiplicity)
 * 2. Composition
 * 3. Aggregation
 * 4. Reflex Association
 * 5. Qualified Association
 * 6. Association with Attribute (Association Class)
 */
public class AssociationTest extends SimpleUnitTest {
    public static void main(String[] args) {
        testBasicAssociation();
        testComposition();
        testAggregation();
        testReflexAssociation();
        testQualifiedAssociation();
        testAssociationWithAttribute();
    }

    /**
     * Test Basic Association: Prisoner[1..*] to Cell[1]
     * - Unidirectional from Prisoner to Cell
     * - Prisoner must always have a cell (multiplicity 1..1)
     */
    private static void testBasicAssociation() {
        System.out.println("\n=== BASIC ASSOCIATION TESTS ===");
        
        runTest("testBasicAssociation_Creation", () -> {
            Cell cell = new Cell(101, "Standard", 2, Cell.SecurityLevel.MEDIUM);
            Prisoner prisoner = new Prisoner("John", "Doe", 30, "Theft",
                LocalDate.now().minusYears(1), 5, "None", "Active");
            
            // Assign prisoner to cell
            prisoner.assignToCell(cell);
            assertEquals(cell, prisoner.getCurrentCell());
            
            Cell.clearExtent();
            Prisoner.clearExtent();
        });

        runTest("testBasicAssociation_ModifyReference", () -> {
            Cell cell1 = new Cell(101, "Standard", 2, Cell.SecurityLevel.MEDIUM);
            Cell cell2 = new Cell(102, "Standard", 2, Cell.SecurityLevel.HIGH);
            Prisoner prisoner = new Prisoner("Jane", "Smith", 25, "Fraud",
                LocalDate.now().minusYears(1), 3, "None", "Active");
            
            // Assign to first cell
            prisoner.assignToCell(cell1);
            assertEquals(cell1, prisoner.getCurrentCell());
            
            // Move to second cell
            prisoner.assignToCell(cell2);
            assertEquals(cell2, prisoner.getCurrentCell());
            
            Cell.clearExtent();
            Prisoner.clearExtent();
        });

        runTest("testBasicAssociation_NullValidation", () -> {
            Prisoner prisoner = new Prisoner("Bob", "Jones", 35, "Assault",
                LocalDate.now().minusYears(1), 7, "None", "Active");
            
            // Cannot assign null cell (violates 1..1 multiplicity)
            assertThrows(InvalidReferenceException.class, () -> {
                prisoner.assignToCell(null);
            });
            
            Prisoner.clearExtent();
        });

        runTest("testBasicAssociation_CannotRemove", () -> {
            Prisoner prisoner = new Prisoner("Alice", "Brown", 28, "Burglary",
                LocalDate.now().minusYears(1), 4, "None", "Active");
            
            // Cannot remove prisoner from cell (violates 1..1 multiplicity)
            assertThrows(ValidationException.class, () -> {
                prisoner.removePrisonerFromCell();
            });
            
            Prisoner.clearExtent();
        });
    }

    /**
     * Test Composition: MedicalReport[0..*] to MedicalRecord[1..1]
     * - Reports owned by record
     * - Reports cannot be shared
     * - Delete cascades
     */
    private static void testComposition() {
        System.out.println("\n=== COMPOSITION TESTS ===");
        
        runTest("testComposition_Creation", () -> {
            MedicalRecord record = new MedicalRecord(LocalDate.now(), "Diabetes management");
            Doctor doctor = new Doctor("Dr", "Smith", 10, "9-5", "555-0000", "dr@test.com", 
                "MD12345", "Emergency: 555-1111");
            
            MedicalReport report = new MedicalReport(LocalDate.now(), "Regular checkup", 
                "Room 101", 30.0, "Low", doctor, record);
            
            assertEquals(record, report.getMedicalRecord());
            assertTrue(record.getMedicalReports().contains(report));
            
            MedicalRecord.clearExtent();
            MedicalReport.clearExtent();
            Staff.clearExtent();
        });

        runTest("testComposition_CannotSharePart", () -> {
            MedicalRecord record1 = new MedicalRecord(LocalDate.now(), "Record 1");
            MedicalRecord record2 = new MedicalRecord(LocalDate.now(), "Record 2");
            Doctor doctor = new Doctor("Dr", "Jones", 10, "9-5", "555-0000", "dr@test.com",
                "MD12346", "Emergency: 555-1111");
            
            MedicalReport report = new MedicalReport(LocalDate.now(), "Checkup", 
                "Room 101", 30.0, "Low", doctor, record1);
            
            // Cannot reassign report to another record (composition violation)
            assertThrows(ValidationException.class, () -> {
                report.setMedicalRecord(record2);
            });
            
            MedicalRecord.clearExtent();
            MedicalReport.clearExtent();
            Staff.clearExtent();
        });

        runTest("testComposition_CascadeDelete", () -> {
            MedicalRecord record = new MedicalRecord(LocalDate.now(), "Test record");
            Doctor doctor = new Doctor("Dr", "Wilson", 10, "9-5", "555-0000", "dr@test.com",
                "MD12347", "Emergency: 555-1111");
            
            MedicalReport report1 = new MedicalReport(LocalDate.now(), "Report 1",
                "Room 101", 30.0, "Low", doctor, record);
            MedicalReport report2 = new MedicalReport(LocalDate.now(), "Report 2",
                "Room 102", 45.0, "Medium", doctor, record);
            
            int extentSizeBefore = MedicalReport.getExtent().size();
            assertTrue(extentSizeBefore >= 2);
            
            // Delete record - should cascade to reports
            record.delete();
            
            // Reports should be deleted from extent
            int extentSizeAfter = MedicalReport.getExtent().size();
            assertTrue(extentSizeAfter < extentSizeBefore);
            
            MedicalRecord.clearExtent();
            MedicalReport.clearExtent();
            Staff.clearExtent();
        });

        runTest("testComposition_PartRequiresWhole", () -> {
            Doctor doctor = new Doctor("Dr", "Taylor", 10, "9-5", "555-0000", "dr@test.com",
                "MD12348", "Emergency: 555-1111");
            
            // Cannot create report without medical record
            assertThrows(InvalidReferenceException.class, () -> {
                new MedicalReport(LocalDate.now(), "Invalid", "Room 103", 20.0, "Low", doctor, null);
            });
            
            Staff.clearExtent();
        });
    }

    /**
     * Test Aggregation: Cell[0..*] to Block[1]
     * - Cell can exist without Block
     * - Weaker relationship than composition
     */
    private static void testAggregation() {
        System.out.println("\n=== AGGREGATION TESTS ===");
        
        runTest("testAggregation_Creation", () -> {
            Block block = new Block("Block A", 50, Block.BlockType.MEDIUM_SECURITY);
            Cell cell = new Cell(101, "Standard", 2, Cell.SecurityLevel.MEDIUM);
            
            // Add cell to block
            cell.setBlock(block);
            assertEquals(block, cell.getBlock());
            assertTrue(block.getCells().contains(cell));
            
            Block.clearExtent();
            Cell.clearExtent();
        });

        runTest("testAggregation_BidirectionalConnection", () -> {
            Block block = new Block("Block B", 50, Block.BlockType.MAXIMUM_SECURITY);
            Cell cell = new Cell(201, "Isolation", 1, Cell.SecurityLevel.HIGH);
            
            // Test reverse connection
            block.addCell(cell);
            assertEquals(block, cell.getBlock());
            assertTrue(block.getCells().contains(cell));
            
            Block.clearExtent();
            Cell.clearExtent();
        });

        runTest("testAggregation_RemoveConnection", () -> {
            Block block = new Block("Block C", 50, Block.BlockType.MINIMUM_SECURITY);
            Cell cell = new Cell(301, "Open", 4, Cell.SecurityLevel.LOW);
            
            block.addCell(cell);
            assertEquals(block, cell.getBlock());
            
            // Remove cell from block - cell still exists
            block.removeCell(cell);
            assertEquals(null, cell.getBlock());
            assertTrue(!block.getCells().contains(cell));
            
            Block.clearExtent();
            Cell.clearExtent();
        });

        runTest("testAggregation_CellCanExistWithoutBlock", () -> {
            // Cell can be created without a block
            Cell cell = new Cell(401, "Temporary", 2, Cell.SecurityLevel.MEDIUM);
            assertEquals(null, cell.getBlock());
            
            Cell.clearExtent();
        });
    }

    /**
     * Test Reflex Association: IncidentReport[0..1] to IncidentReport[0..1]
     * - Self-referencing association
     * - Bidirectional
     */
    private static void testReflexAssociation() {
        System.out.println("\n=== REFLEX ASSOCIATION TESTS ===");
        
        runTest("testReflexAssociation_Creation", () -> {
            IncidentReport report1 = new IncidentReport(LocalDate.now(), "Fight in cafeteria", 
                IncidentReport.Status.OPEN);
            IncidentReport report2 = new IncidentReport(LocalDate.now(), "Follow-up investigation",
                IncidentReport.Status.INREVIEW);
            
            // Link reports
            report1.setRelatedIncident(report2);
            assertEquals(report2, report1.getRelatedIncident());
            assertEquals(report1, report2.getRelatedIncident());
            
            IncidentReport.clearExtent();
        });

        runTest("testReflexAssociation_ModifyReference", () -> {
            IncidentReport report1 = new IncidentReport(LocalDate.now(), "Incident 1",
                IncidentReport.Status.OPEN);
            IncidentReport report2 = new IncidentReport(LocalDate.now(), "Incident 2",
                IncidentReport.Status.INREVIEW);
            IncidentReport report3 = new IncidentReport(LocalDate.now(), "Incident 3",
                IncidentReport.Status.RESOLVED);
            
            // Link report1 to report2
            report1.setRelatedIncident(report2);
            assertEquals(report2, report1.getRelatedIncident());
            
            // Change to report3
            report1.setRelatedIncident(report3);
            assertEquals(report3, report1.getRelatedIncident());
            assertEquals(null, report2.getRelatedIncident());
            assertEquals(report1, report3.getRelatedIncident());
            
            IncidentReport.clearExtent();
        });

        runTest("testReflexAssociation_CannotReferenceSelf", () -> {
            IncidentReport report = new IncidentReport(LocalDate.now(), "Test incident",
                IncidentReport.Status.OPEN);
            
            // Cannot link to itself
            assertThrows(ValidationException.class, () -> {
                report.setRelatedIncident(report);
            });
            
            IncidentReport.clearExtent();
        });

        runTest("testReflexAssociation_RemoveConnection", () -> {
            IncidentReport report1 = new IncidentReport(LocalDate.now(), "Report A",
                IncidentReport.Status.OPEN);
            IncidentReport report2 = new IncidentReport(LocalDate.now(), "Report B",
                IncidentReport.Status.OPEN);
            
            report1.setRelatedIncident(report2);
            assertEquals(report2, report1.getRelatedIncident());
            
            // Remove connection
            report1.removeRelatedIncident();
            assertEquals(null, report1.getRelatedIncident());
            assertEquals(null, report2.getRelatedIncident());
            
            IncidentReport.clearExtent();
        });
    }

    /**
     * Test Qualified Association: Visit[0..*] to Visitor (qualified by date)
     * - Uses Dictionary/Map with qualifier as key
     * - Prevents duplicates based on qualifier
     */
    private static void testQualifiedAssociation() {
        System.out.println("\n=== QUALIFIED ASSOCIATION TESTS ===");
        
        runTest("testQualifiedAssociation_Creation", () -> {
            Visitor visitor = new Visitor("John", "Family", "555-0000", "Brother");
            Prisoner prisoner = new Prisoner("Test", "Prisoner", 30, "Theft",
                LocalDate.now().minusYears(1), 5, "None", "Active");
            
            LocalDate visitDate = LocalDate.now().plusDays(1);
            Visit visit = new Visit(visitDate, 60, Visit.VisitType.FAMILY, visitor, prisoner);
            
            // Access by qualifier (date)
            assertEquals(visit, visitor.getVisitByDate(visitDate));
            
            Visitor.clearExtent();
            Visit.clearExtent();
            Prisoner.clearExtent();
        });

        runTest("testQualifiedAssociation_PreventDuplicates", () -> {
            Visitor visitor = new Visitor("Mary", "Smith", "555-1111", "Mother");
            Prisoner prisoner = new Prisoner("Test", "Prisoner", 30, "Fraud",
                LocalDate.now().minusYears(1), 5, "None", "Active");
            
            LocalDate visitDate = LocalDate.now().plusDays(2);
            new Visit(visitDate, 60, Visit.VisitType.FAMILY, visitor, prisoner);
            
            // Cannot add another visit for the same date
            assertThrows(ValidationException.class, () -> {
                visitor.addVisitByDate(visitDate, 
                    new Visit(visitDate, 30, Visit.VisitType.FAMILY, visitor, prisoner));
            });
            
            Visitor.clearExtent();
            Visit.clearExtent();
            Prisoner.clearExtent();
        });

        runTest("testQualifiedAssociation_RemoveByQualifier", () -> {
            Visitor visitor = new Visitor("Bob", "Johnson", "555-2222", "Father");
            Prisoner prisoner = new Prisoner("Test", "Prisoner", 30, "Assault",
                LocalDate.now().minusYears(1), 5, "None", "Active");
            
            LocalDate visitDate = LocalDate.now().plusDays(3);
            Visit visit = new Visit(visitDate, 60, Visit.VisitType.FAMILY, visitor, prisoner);
            
            assertEquals(visit, visitor.getVisitByDate(visitDate));
            
            // Remove by qualifier
            visitor.removeVisitByDate(visitDate);
            assertEquals(null, visitor.getVisitByDate(visitDate));
            
            Visitor.clearExtent();
            Visit.clearExtent();
            Prisoner.clearExtent();
        });

        runTest("testQualifiedAssociation_UpdateQualifier", () -> {
            Visitor visitor = new Visitor("Alice", "Williams", "555-3333", "Sister");
            Prisoner prisoner = new Prisoner("Test", "Prisoner", 30, "Burglary",
                LocalDate.now().minusYears(1), 5, "None", "Active");
            
            LocalDate oldDate = LocalDate.now().plusDays(4);
            LocalDate newDate = LocalDate.now().plusDays(5);
            Visit visit = new Visit(oldDate, 60, Visit.VisitType.FAMILY, visitor, prisoner);
            
            // Update qualifier
            visitor.updateVisitDate(oldDate, newDate, visit);
            assertEquals(null, visitor.getVisitByDate(oldDate));
            assertEquals(visit, visitor.getVisitByDate(newDate));
            
            Visitor.clearExtent();
            Visit.clearExtent();
            Prisoner.clearExtent();
        });
    }

    /**
     * Test Association with Attribute: MealDelivery (Association Class)
     * - Connects Meal and Prisoner
     * - Has its own attributes (deliveryTime, status)
     */
    private static void testAssociationWithAttribute() {
        System.out.println("\n=== ASSOCIATION WITH ATTRIBUTE TESTS ===");
        
        runTest("testAssociationClass_Creation", () -> {
            Meal meal = new Meal("Lunch special", Meal.DietPlan.STANDARD, 650.0, Meal.MealType.Lunch);
            meal.addAllergen("Gluten");
            
            Prisoner prisoner = new Prisoner("John", "Doe", 30, "Theft",
                LocalDate.now().minusYears(1), 5, "None", "Active");
            
            LocalDateTime deliveryTime = LocalDateTime.now().plusHours(2);
            MealDelivery delivery = new MealDelivery(deliveryTime, prisoner, meal);
            
            // Verify attributes
            assertEquals(deliveryTime, delivery.getDeliveryTime());
            assertEquals(MealDelivery.DeliveryStatus.SCHEDULED, delivery.getStatus());
            assertEquals(prisoner, delivery.getPrisoner());
            assertEquals(meal, delivery.getMeal());
            
            // Verify reverse connections
            assertTrue(prisoner.getMealDeliveries().contains(delivery));
            assertTrue(meal.getDeliveries().contains(delivery));
            
            Meal.clearExtent();
            Prisoner.clearExtent();
            MealDelivery.clearExtent();
        });

        runTest("testAssociationClass_MultipleConnections", () -> {
            Meal breakfast = new Meal("Breakfast", Meal.DietPlan.VEGETARIAN, 400.0, Meal.MealType.Breakfast);
            breakfast.addAllergen("Soy");
            Meal lunch = new Meal("Lunch", Meal.DietPlan.STANDARD, 700.0, Meal.MealType.Lunch);
            lunch.addAllergen("Nuts");
            
            Prisoner prisoner = new Prisoner("Jane", "Smith", 25, "Fraud",
                LocalDate.now().minusYears(1), 3, "None", "Active");
            
            // Same prisoner can have multiple meal deliveries
            MealDelivery delivery1 = new MealDelivery(LocalDateTime.now().plusHours(1), prisoner, breakfast);
            MealDelivery delivery2 = new MealDelivery(LocalDateTime.now().plusHours(6), prisoner, lunch);
            
            assertEquals(2, prisoner.getMealDeliveries().size());
            assertTrue(prisoner.getMealDeliveries().contains(delivery1));
            assertTrue(prisoner.getMealDeliveries().contains(delivery2));
            
            Meal.clearExtent();
            Prisoner.clearExtent();
            MealDelivery.clearExtent();
        });

        runTest("testAssociationClass_AttributeModification", () -> {
            Meal meal = new Meal("Dinner", Meal.DietPlan.VEGETARIAN, 500.0, Meal.MealType.Dinner);
            meal.addAllergen("Dairy");
            
            Prisoner prisoner = new Prisoner("Bob", "Jones", 35, "Assault",
                LocalDate.now().minusYears(1), 7, "None", "Active");
            
            MealDelivery delivery = new MealDelivery(LocalDateTime.now().plusHours(3), prisoner, meal);
            
            // Modify association class attributes
            assertEquals(MealDelivery.DeliveryStatus.SCHEDULED, delivery.getStatus());
            delivery.setStatus(MealDelivery.DeliveryStatus.DELIVERED);
            assertEquals(MealDelivery.DeliveryStatus.DELIVERED, delivery.getStatus());
            
            Meal.clearExtent();
            Prisoner.clearExtent();
            MealDelivery.clearExtent();
        });

        runTest("testAssociationClass_NullValidation", () -> {
            Meal meal = new Meal("Snack", Meal.DietPlan.STANDARD, 200.0, Meal.MealType.Breakfast);
            meal.addAllergen("Peanuts");
            Prisoner prisoner = new Prisoner("Alice", "Brown", 28, "Burglary",
                LocalDate.now().minusYears(1), 4, "None", "Active");
            
            // Cannot create without required associations
            assertThrows(InvalidReferenceException.class, () -> {
                new MealDelivery(LocalDateTime.now(), null, meal);
            });
            
            assertThrows(InvalidReferenceException.class, () -> {
                new MealDelivery(LocalDateTime.now(), prisoner, null);
            });
            
            Meal.clearExtent();
            Prisoner.clearExtent();
        });
    }
}
