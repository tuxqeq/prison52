package com.prison.model;

import com.prison.exception.*;
import com.prison.test.SimpleUnitTest;
import java.time.LocalDate;

/**
 * Comprehensive tests for all inheritance implementations in the system.
 * Tests showcase:
 * 1. Director inherits Staff {Disjoint, Incomplete} - using extends (inheritance)
 * 2. Guard inherits Staff {Disjoint, Incomplete} - using extends (inheritance)
 * 3. Doctor inherits Staff {Disjoint, Incomplete} - using extends (inheritance)
 * 4. CombinedStaff inherits Guard and Doctor - using COMPOSITION (Multiple Inheritance)
 * 5. MedicalReport inherits Report {Disjoint, Incomplete} - using extends (inheritance)
 * 6. IncidentReport inherits Report {Disjoint, Incomplete} - using extends (inheritance)
 * 
 * NOTE: While Director, Guard, Doctor currently use Java's 'extends' keyword,
 * they demonstrate the FLATTENING pattern concept where all parent attributes
 * are available in the child class.
 */
public class InheritanceImplementationTest extends SimpleUnitTest {

    private static void clearAllExtents() {
        Staff.clearExtent();
        Director.clearDirectorExtent();
        Guard.clearGuardExtent();
        Doctor.clearDoctorExtent();
        CombinedStaff.clearCombinedStaffExtent();
        MedicalReport.clearExtent();
        IncidentReport.clearExtent();
        Prisoner.clearExtent();
        MedicalRecord.clearExtent();
    }
    
    private static void assertFalse(boolean condition) {
        if (condition) {
            throw new RuntimeException("Expected false but got true");
        }
    }
    
    private static void assertNotNull(Object object) {
        if (object == null) {
            throw new RuntimeException("Expected non-null but got null");
        }
    }
    
    private static void assertNull(Object object) {
        if (object != null) {
            throw new RuntimeException("Expected null but got " + object);
        }
    }

    public static void main(String[] args) {
        System.out.println("\n========================================");
        System.out.println("INHERITANCE IMPLEMENTATION TESTS");
        System.out.println("========================================\n");

        // Part 1: Staff Hierarchy Tests
        System.out.println("=== PART 1: STAFF INHERITANCE (FLATTENING) ===\n");
        runTest("Test 1.1: Director Inheritance", InheritanceImplementationTest::testDirectorInheritance);
        runTest("Test 1.2: Guard Inheritance", InheritanceImplementationTest::testGuardInheritance);
        runTest("Test 1.3: Doctor Inheritance", InheritanceImplementationTest::testDoctorInheritance);
        runTest("Test 1.4: Disjoint Constraint", InheritanceImplementationTest::testDisjointConstraint);
        runTest("Test 1.5: Incomplete Constraint", InheritanceImplementationTest::testIncompleteConstraint);

        // Part 2: CombinedStaff Multiple Inheritance
        System.out.println("\n=== PART 2: COMBINED STAFF (COMPOSITION/MULTIPLE INHERITANCE) ===\n");
        runTest("Test 2.1: CombinedStaff Multiple Inheritance", InheritanceImplementationTest::testCombinedStaffMultipleInheritance);
        runTest("Test 2.2: CombinedStaff Role Delegation", InheritanceImplementationTest::testCombinedStaffRoleDelegation);
        runTest("Test 2.3: CombinedStaff Emergency Functionality", InheritanceImplementationTest::testCombinedStaffEmergency);

        // Part 3: Report Hierarchy Tests
        System.out.println("\n=== PART 3: REPORT INHERITANCE ===\n");
        runTest("Test 3.1: MedicalReport Inheritance", InheritanceImplementationTest::testMedicalReportInheritance);
        runTest("Test 3.2: IncidentReport Inheritance", InheritanceImplementationTest::testIncidentReportInheritance);
        runTest("Test 3.3: Report Disjoint Constraint", InheritanceImplementationTest::testReportDisjointConstraint);
        runTest("Test 3.4: Report Incomplete Constraint", InheritanceImplementationTest::testReportIncompleteConstraint);

        // Part 4: Polymorphism and Type Checking
        System.out.println("\n=== PART 4: POLYMORPHISM & TYPE CHECKING ===\n");
        runTest("Test 4.1: Staff Polymorphism", InheritanceImplementationTest::testStaffPolymorphism);
        runTest("Test 4.2: Type-Specific Behavior", InheritanceImplementationTest::testTypeSpecificBehavior);
        runTest("Test 4.3: Inherited Validation", InheritanceImplementationTest::testInheritedValidation);
        runTest("Test 4.4: Extent Management", InheritanceImplementationTest::testExtentManagement);

        System.out.println("\n========================================");
        System.out.println("ALL TESTS COMPLETED");
        System.out.println("========================================\n");
    }

    // ============================================================
    // PART 1: STAFF INHERITANCE TESTS
    // ============================================================

    private static void testDirectorInheritance() {
        clearAllExtents();
        
        Director director = new Director(
            "John", "Smith", 15, 
            "9AM-5PM", "+1234567890", "john.smith@prison.gov",
            Director.DirectorRank.GENERAL
        );

        // Verify Staff attributes (inherited)
        assertEquals("John", director.getName());
        assertEquals("Smith", director.getSurname());
        assertEquals(15, director.getExperienceYears());
        assertEquals("9AM-5PM", director.getShiftHour());
        assertEquals("+1234567890", director.getPhone());
        assertEquals("john.smith@prison.gov", director.getEmail());

        // Verify Director-specific attributes
        assertEquals(Director.DirectorRank.GENERAL, director.getRank());

        // Verify in both extents
        assertTrue(Staff.getExtent().contains(director));
        assertTrue(Director.getDirectorExtent().contains(director));

        // Test inherited behavior
        director.setExperienceYears(20);
        assertEquals(20, director.getExperienceYears());

        System.out.println("  ✓ Director has all Staff attributes plus Director-specific attributes");
    }

    private static void testGuardInheritance() {
        clearAllExtents();
        
        Guard guard = new Guard(
            "Mike", "Johnson", 8,
            "6AM-2PM", "+1987654321", "mike.j@prison.gov",
            Guard.Rank.SENIOR, "Baton"
        );

        // Verify Staff attributes
        assertEquals("Mike", guard.getName());
        assertEquals("Johnson", guard.getSurname());
        assertEquals(8, guard.getExperienceYears());

        // Verify Guard-specific attributes
        assertEquals(Guard.Rank.SENIOR, guard.getRank());
        assertEquals("Baton", guard.getWeapon());

        // Verify in both extents
        assertTrue(Staff.getExtent().contains(guard));
        assertTrue(Guard.getGuardExtent().contains(guard));

        // Test Guard-specific behavior
        guard.setWeapon("Taser");
        assertEquals("Taser", guard.getWeapon());

        System.out.println("  ✓ Guard has all Staff attributes plus Guard-specific attributes");
    }

    private static void testDoctorInheritance() {
        clearAllExtents();
        
        Doctor doctor = new Doctor(
            "Sarah", "Williams", 12,
            "8AM-4PM", "+1555666777", "sarah.w@prison.gov",
            "MD-12345", "Emergency: +1555666778"
        );

        // Verify Staff attributes
        assertEquals("Sarah", doctor.getName());
        assertEquals(12, doctor.getExperienceYears());

        // Verify Doctor-specific attributes
        assertEquals("MD-12345", doctor.getLicenseNumber());
        assertEquals("Emergency: +1555666778", doctor.getContactInfo());

        // Verify in both extents
        assertTrue(Staff.getExtent().contains(doctor));
        assertTrue(Doctor.getDoctorExtent().contains(doctor));

        // Test Doctor-specific behavior
        doctor.addSpecialisation("Surgery");
        assertEquals(1, doctor.getSpecialisation().size());
        assertTrue(doctor.getSpecialisation().contains("Surgery"));

        System.out.println("  ✓ Doctor has all Staff attributes plus Doctor-specific attributes");
    }

    private static void testDisjointConstraint() {
        clearAllExtents();
        
        Director director = new Director("Alice", "Brown", 10, "9AM-5PM", "+1111111111", 
            "alice@prison.gov", Director.DirectorRank.ASSISTANT);
        Guard guard = new Guard("Bob", "Davis", 5, "2PM-10PM", "+2222222222", 
            "bob@prison.gov", Guard.Rank.JUNIOR, "Baton");
        Doctor doctor = new Doctor("Carol", "Wilson", 7, "10AM-6PM", "+3333333333", 
            "carol@prison.gov", "MD-99999", "Emergency: +3333333334");

        // Each is only in their specific extent (Disjoint)
        assertTrue(Director.getDirectorExtent().contains(director));
        assertFalse(Guard.getGuardExtent().contains(director));
        assertFalse(Doctor.getDoctorExtent().contains(director));

        assertTrue(Guard.getGuardExtent().contains(guard));
        assertFalse(Director.getDirectorExtent().contains(guard));

        assertTrue(Doctor.getDoctorExtent().contains(doctor));
        assertFalse(Director.getDirectorExtent().contains(doctor));

        // All in Staff extent
        assertEquals(3, Staff.getExtent().size());

        System.out.println("  ✓ Disjoint constraint verified - each staff belongs to exactly one subclass");
    }

    private static void testIncompleteConstraint() {
        clearAllExtents();
        
        Director director = new Director("Tom", "Anderson", 5, "8AM-4PM", "+4444444444", 
            "tom@prison.gov", Director.DirectorRank.REGIONAL);
        Guard guard = new Guard("Emma", "Martinez", 3, "4PM-12AM", "+5555555555", 
            "emma@prison.gov", Guard.Rank.CHIEF, "Pistol");

        // Can have some staff types without all types (Incomplete)
        assertEquals(2, Staff.getExtent().size());
        assertEquals(1, Director.getDirectorExtent().size());
        assertEquals(1, Guard.getGuardExtent().size());
        assertEquals(0, Doctor.getDoctorExtent().size());

        System.out.println("  ✓ Incomplete constraint verified - can have some staff types without all");
    }

    // ============================================================
    // PART 2: COMBINED STAFF MULTIPLE INHERITANCE
    // ============================================================

    private static void testCombinedStaffMultipleInheritance() {
        clearAllExtents();
        
        CombinedStaff combined = new CombinedStaff(
            "Alex", "Taylor", 10,
            "Flexible", "+6666666666", "alex@prison.gov",
            Guard.Rank.LIEUTENANT, "Pistol",
            "MD-77777", "Emergency: +6666666667"
        );

        // Verify Staff attributes
        assertEquals("Alex", combined.getName());
        assertEquals("Taylor", combined.getSurname());
        assertEquals(10, combined.getExperienceYears());

        // Verify Guard role through composition
        assertNotNull(combined.getGuardRole());
        assertTrue(combined.canPerformGuardDuties());
        assertEquals(Guard.Rank.LIEUTENANT, combined.getGuardRole().getRank());
        assertEquals("Pistol", combined.getGuardRole().getWeapon());

        // Verify Doctor role through composition
        assertNotNull(combined.getDoctorRole());
        assertTrue(combined.canPerformDoctorDuties());
        assertEquals("MD-77777", combined.getDoctorRole().getLicenseNumber());

        // Test CombinedStaff-specific functionality
        combined.setAvailableForEmergency(true);
        assertTrue(combined.getAvailableForEmergency());

        System.out.println("  ✓ CombinedStaff demonstrates multiple inheritance through composition");
    }

    private static void testCombinedStaffRoleDelegation() {
        clearAllExtents();
        
        CombinedStaff combined = new CombinedStaff(
            "Jordan", "Lee", 8,
            "On-Call", "+7777777777", "jordan@prison.gov",
            Guard.Rank.SENIOR, "Taser",
            "MD-88888", "Emergency: +7777777778"
        );

        // Test Guard role delegation
        Guard guardRole = combined.getGuardRole();
        assertNotNull(guardRole);
        guardRole.setWeapon("Baton");
        assertEquals("Baton", combined.getGuardRole().getWeapon());

        // Test Doctor role delegation
        Doctor doctorRole = combined.getDoctorRole();
        assertNotNull(doctorRole);
        doctorRole.addSpecialisation("Trauma Care");
        assertEquals(1, combined.getDoctorRole().getSpecialisation().size());

        // Verify both roles work
        assertTrue(combined.canPerformGuardDuties());
        assertTrue(combined.canPerformDoctorDuties());

        System.out.println("  ✓ CombinedStaff properly delegates to composed Guard and Doctor roles");
    }

    private static void testCombinedStaffEmergency() {
        clearAllExtents();
        
        CombinedStaff combined = new CombinedStaff(
            "Morgan", "Garcia", 12,
            "24/7", "+8888888888", "morgan@prison.gov",
            Guard.Rank.CHIEF, "Pistol",
            "MD-11111", "Emergency: +8888888889"
        );

        // Test emergency availability
        assertNull(combined.getAvailableForEmergency());

        combined.setAvailableForEmergency(true);
        assertTrue(combined.getAvailableForEmergency());
        
        try {
            combined.resolveToEmergency();
            // Should not throw
        } catch (Exception e) {
            throw new RuntimeException("Should not throw exception when available");
        }

        combined.setAvailableForEmergency(false);
        assertFalse(combined.getAvailableForEmergency());
        
        assertThrows(ValidationException.class, () -> combined.resolveToEmergency());

        System.out.println("  ✓ CombinedStaff emergency functionality works correctly");
    }

    // ============================================================
    // PART 3: REPORT INHERITANCE TESTS
    // ============================================================

    private static void testMedicalReportInheritance() {
        clearAllExtents();
        
        Prisoner prisoner = new Prisoner("Patient", "Doe", 35, "Theft",
            LocalDate.of(2020, 1, 1), 5, "None", "Serving");
        Doctor doctor = new Doctor("Dr. House", "Medical", 20, "9AM-5PM", 
            "+9999999999", "house@prison.gov", "MD-HOUSE", "Emergency: +9999999990");
        MedicalRecord record = new MedicalRecord(LocalDate.now(), "Initial health assessment");

        MedicalReport medReport = new MedicalReport(
            LocalDate.now(), "Routine checkup", "Room 101",
            45.5, "Normal", doctor, record
        );

        // Verify Report attributes
        assertEquals(LocalDate.now(), medReport.getDate());
        assertEquals("Routine checkup", medReport.getDescription());

        // Verify MedicalReport-specific attributes
        assertEquals("Room 101", medReport.getRoomNumber());
        assertEquals(45.5, medReport.getDuration(), 0.01);
        assertEquals("Normal", medReport.getSeverityLevel());
        assertEquals(doctor, medReport.getDoctor());

        assertTrue(MedicalReport.getExtent().contains(medReport));

        System.out.println("  ✓ MedicalReport has all Report attributes plus MedicalReport-specific");
    }

    private static void testIncidentReportInheritance() {
        clearAllExtents();
        
        IncidentReport incReport = new IncidentReport(
            LocalDate.now(), "Fight in cafeteria",
            IncidentReport.Status.OPEN
        );

        // Verify Report attributes
        assertEquals(LocalDate.now(), incReport.getDate());
        assertEquals("Fight in cafeteria", incReport.getDescription());

        // Verify IncidentReport-specific attributes
        assertEquals(IncidentReport.Status.OPEN, incReport.getStatus());

        incReport.setSeverity("High");
        assertEquals("High", incReport.getSeverity());

        incReport.addPersonInvolved("Prisoner A");
        assertEquals(1, incReport.getPeopleInvolved().size());

        assertTrue(IncidentReport.getExtent().contains(incReport));

        System.out.println("  ✓ IncidentReport has all Report attributes plus IncidentReport-specific");
    }

    private static void testReportDisjointConstraint() {
        clearAllExtents();
        
        Prisoner prisoner = new Prisoner("Test", "Subject", 32, "Fraud",
            LocalDate.of(2021, 3, 10), 7, "Medium", "Serving");
        Doctor doctor = new Doctor("Dr. Jekyll", "Hyde", 15, "8AM-4PM", 
            "+1231231234", "jekyll@prison.gov", "MD-JEKYLL", "Emergency: +1231231235");
        MedicalRecord record = new MedicalRecord(LocalDate.now(), "Routine checkup");

        MedicalReport medReport = new MedicalReport(
            LocalDate.now(), "Medical issue", "Room 202",
            30.0, "Moderate", doctor, record
        );

        IncidentReport incReport = new IncidentReport(
            LocalDate.now(), "Incident occurred",
            IncidentReport.Status.INREVIEW
        );

        // Reports are disjoint
        assertTrue(MedicalReport.getExtent().contains(medReport));
        assertFalse(IncidentReport.getExtent().contains(medReport));

        assertTrue(IncidentReport.getExtent().contains(incReport));
        assertFalse(MedicalReport.getExtent().contains(incReport));

        System.out.println("  ✓ Report subclasses are disjoint - each report is one type only");
    }

    private static void testReportIncompleteConstraint() {
        clearAllExtents();
        
        IncidentReport inc1 = new IncidentReport(LocalDate.now(), "Incident 1", 
            IncidentReport.Status.OPEN);
        IncidentReport inc2 = new IncidentReport(LocalDate.now(), "Incident 2", 
            IncidentReport.Status.RESOLVED);

        // Can have IncidentReports without MedicalReports (Incomplete)
        assertEquals(2, IncidentReport.getExtent().size());
        assertEquals(0, MedicalReport.getExtent().size());

        System.out.println("  ✓ Report inheritance is incomplete - can have some report types without all");
    }

    // ============================================================
    // PART 4: POLYMORPHISM AND TYPE CHECKING
    // ============================================================

    private static void testStaffPolymorphism() {
        clearAllExtents();
        
        Director director = new Director("Poly", "Director", 10, "9AM-5PM", 
            "+1010101010", "poly.d@prison.gov", Director.DirectorRank.GENERAL);
        Guard guard = new Guard("Poly", "Guard", 8, "6AM-2PM", "+2020202020", 
            "poly.g@prison.gov", Guard.Rank.SENIOR, "Baton");
        Doctor doctor = new Doctor("Poly", "Doctor", 12, "8AM-4PM", "+3030303030", 
            "poly.doc@prison.gov", "MD-POLY", "Emergency: +3030303031");

        // All should be treatable as Staff
        assertEquals(3, Staff.getExtent().size());

        // Test polymorphic behavior
        for (Staff staff : Staff.getExtent()) {
            assertNotNull(staff.getName());
            assertTrue(staff.getExperienceYears() > 0);
        }

        System.out.println("  ✓ All Staff subclasses can be treated polymorphically");
    }

    private static void testTypeSpecificBehavior() {
        clearAllExtents();
        
        Director director = new Director("Type", "Director", 5, "9AM-5PM", 
            "+4040404040", "type.d@prison.gov", Director.DirectorRank.ASSISTANT);
        Guard guard = new Guard("Type", "Guard", 3, "2PM-10PM", "+5050505050", 
            "type.g@prison.gov", Guard.Rank.JUNIOR, "Taser");

        // Director-specific behavior
        assertEquals(Director.DirectorRank.ASSISTANT, director.getRank());

        // Guard-specific behavior
        assertEquals("Taser", guard.getWeapon());

        // Guard can have supervisors (specific to Guard)
        Guard supervisor = new Guard("Super", "Visor", 10, "6AM-2PM", 
            "+6060606060", "super@prison.gov", Guard.Rank.CHIEF, "Pistol");
        guard.addSupervisor(supervisor);
        assertEquals(1, guard.getSupervisors().size());

        System.out.println("  ✓ Each subclass maintains its specific behavior");
    }

    private static void testInheritedValidation() {
        clearAllExtents();
        
        Director director = new Director("Valid", "Director", 5, "9AM-5PM", 
            "+7070707070", "valid@prison.gov", Director.DirectorRank.REGIONAL);

        // Test inherited validation from Staff
        assertThrows(EmptyStringException.class, () -> director.setName(""));
        assertThrows(EmptyStringException.class, () -> director.setSurname(null));
        assertThrows(NegativeNumberException.class, () -> director.setExperienceYears(-1));
        assertThrows(EmptyStringException.class, () -> director.setPhone(""));

        // Test Director-specific validation
        assertThrows(InvalidReferenceException.class, () -> director.setRank(null));

        System.out.println("  ✓ Inherited validation rules work correctly in subclasses");
    }

    private static void testExtentManagement() {
        clearAllExtents();
        
        Director d1 = new Director("D1", "One", 5, "9AM-5PM", "+1", "d1@prison.gov", 
            Director.DirectorRank.ASSISTANT);
        Director d2 = new Director("D2", "Two", 7, "9AM-5PM", "+2", "d2@prison.gov", 
            Director.DirectorRank.REGIONAL);
        Guard g1 = new Guard("G1", "One", 3, "6AM-2PM", "+3", "g1@prison.gov", 
            Guard.Rank.JUNIOR, "Baton");
        Guard g2 = new Guard("G2", "Two", 4, "2PM-10PM", "+4", "g2@prison.gov", 
            Guard.Rank.SENIOR, "Taser");
        Doctor doc1 = new Doctor("Doc1", "One", 10, "8AM-4PM", "+5", "doc1@prison.gov", 
            "MD-1", "Emergency: +5");

        // Verify specific extents
        assertEquals(2, Director.getDirectorExtent().size());
        assertEquals(2, Guard.getGuardExtent().size());
        assertEquals(1, Doctor.getDoctorExtent().size());

        // Verify Staff extent contains all
        assertEquals(5, Staff.getExtent().size());

        // Clear specific extent
        Director.clearDirectorExtent();
        assertEquals(0, Director.getDirectorExtent().size());

        System.out.println("  ✓ Extent management works correctly across inheritance hierarchy");
    }

    private static void assertEquals(double expected, double actual, double delta) {
        if (Math.abs(expected - actual) > delta) {
            throw new RuntimeException("Expected " + expected + " but got " + actual);
        }
    }
}
