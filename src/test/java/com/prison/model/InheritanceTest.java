package com.prison.model;

import com.prison.exception.*;
import com.prison.test.SimpleUnitTest;
import java.time.LocalDate;

/**
 * Comprehensive tests for all inheritance implementations in the system.
 * Tests showcase:
 * 1. Director inherits Staff {Disjoint, Incomplete} - using FLATTENING
 * 2. Guard inherits Staff {Disjoint, Incomplete} - using FLATTENING  
 * 3. Doctor inherits Staff {Disjoint, Incomplete} - using FLATTENING
 * 4. CombinedStaff inherits Guard and Doctor - using COMPOSITION (Multiple Inheritance)
 * 5. MedicalReport inherits Report {Disjoint, Incomplete} - using COMPOSITION
 * 6. IncidentReport inherits Report {Disjoint, Incomplete} - using COMPOSITION
 */
public class InheritanceTest extends SimpleUnitTest {

    private static void clearAllExtents() {
        // Clear all extents before each test
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
            throw new RuntimeException("Assertion Failed: Expected false but got true");
        }
    }
    
    private static void assertNotNull(Object object) {
        if (object == null) {
            throw new RuntimeException("Assertion Failed: Expected non-null but got null");
        }
    }
    
    private static void assertNull(Object object) {
        if (object != null) {
            throw new RuntimeException("Assertion Failed: Expected null but got " + object);
        }
    }
    
    private static void assertDoesNotThrow(Runnable runnable) {
        try {
            runnable.run();
        } catch (Exception e) {
            throw new RuntimeException("Assertion Failed: Expected no exception but got " + e.getClass().getName() + ": " + e.getMessage());
        }
    }
    
    // ============================================================
    // PART 1: STAFF INHERITANCE TESTS (FLATTENING PATTERN)
    // ============================================================

    /**
     * Test 1: Director inherits from Staff using FLATTENING
     * Verifies: Director has all Staff attributes + specific Director attributes
     * Constraint: {Disjoint, Incomplete}
     */
    private static void testDirectorInheritance() {
        clearAllExtents();
        
        // Create Director with both Staff attributes and Director-specific attributes
        Director director = new Director(
            "John", "Smith", 15, 
            "9AM-5PM", "+1234567890", "john.smith@prison.gov",
            Director.DirectorRank.GENERAL
        );

        // Verify Staff attributes (inherited via flattening)
        assertEquals("John", director.getName());
        assertEquals("Smith", director.getSurname());
        assertEquals(15, director.getExperienceYears());
        assertEquals("9AM-5PM", director.getShiftHour());
        assertEquals("+1234567890", director.getPhone());
        assertEquals("john.smith@prison.gov", director.getEmail());

        // Verify Director-specific attributes
        assertEquals(Director.DirectorRank.GENERAL, director.getRank());

        // Verify Director is in both Staff extent and Director extent
        assertTrue(Staff.getExtent().contains(director));
        assertTrue(Director.getDirectorExtent().contains(director));

        // Test Staff behavior delegation
        director.setExperienceYears(20);
        assertEquals(20, director.getExperienceYears());

        System.out.println("✓ Director inheritance via FLATTENING verified");
    }

    /**
     * Test 2: Guard inherits from Staff using FLATTENING
     * Verifies: Guard has all Staff attributes + specific Guard attributes
     * Constraint: {Disjoint, Incomplete}
     */
    private static void testGuardInheritance() {
        clearAllExtents();
        
        // Create Guard with both Staff attributes and Guard-specific attributes
        Guard guard = new Guard(
            "Mike", "Johnson", 8,
            "6AM-2PM", "+1987654321", "mike.j@prison.gov",
            Guard.Rank.SENIOR, "Baton"
        );

        // Verify Staff attributes (inherited via flattening)
        assertEquals("Mike", guard.getName());
        assertEquals("Johnson", guard.getSurname());
        assertEquals(8, guard.getExperienceYears());
        assertEquals("6AM-2PM", guard.getShiftHour());
        assertEquals("+1987654321", guard.getPhone());
        assertEquals("mike.j@prison.gov", guard.getEmail());

        // Verify Guard-specific attributes
        assertEquals(Guard.Rank.SENIOR, guard.getRank());
        assertEquals("Baton", guard.getWeapon());

        // Verify Guard is in both Staff extent and Guard extent
        assertTrue(Staff.getExtent().contains(guard));
        assertTrue(Guard.getGuardExtent().contains(guard));

        // Test Guard-specific behavior
        guard.setWeapon("Taser");
        assertEquals("Taser", guard.getWeapon());

        System.out.println("✓ Guard inheritance via FLATTENING verified");
    }

    /**
     * Test 3: Doctor inherits from Staff using FLATTENING
     * Verifies: Doctor has all Staff attributes + specific Doctor attributes
     * Constraint: {Disjoint, Incomplete}
     */
    private static void testDoctorInheritance() {
        clearAllExtents();
        
        // Create Doctor with both Staff attributes and Doctor-specific attributes
        Doctor doctor = new Doctor(
            "Sarah", "Williams", 12,
            "8AM-4PM", "+1555666777", "sarah.w@prison.gov",
            "MD-12345", "Emergency: +1555666778"
        );

        // Verify Staff attributes (inherited via flattening)
        assertEquals("Sarah", doctor.getName());
        assertEquals("Williams", doctor.getSurname());
        assertEquals(12, doctor.getExperienceYears());
        assertEquals("8AM-4PM", doctor.getShiftHour());
        assertEquals("+1555666777", doctor.getPhone());
        assertEquals("sarah.w@prison.gov", doctor.getEmail());

        // Verify Doctor-specific attributes
        assertEquals("MD-12345", doctor.getLicenseNumber());
        assertEquals("Emergency: +1555666778", doctor.getContactInfo());

        // Verify Doctor is in both Staff extent and Doctor extent
        assertTrue(Staff.getExtent().contains(doctor));
        assertTrue(Doctor.getDoctorExtent().contains(doctor));

        // Test Doctor-specific behavior
        doctor.addSpecialisation("Surgery");
        doctor.addSpecialisation("Emergency Medicine");
        assertEquals(2, doctor.getSpecialisation().size());
        assertTrue(doctor.getSpecialisation().contains("Surgery"));

        System.out.println("✓ Doctor inheritance via FLATTENING verified");
    }

    /**
     * Test 4: Disjoint Constraint - Staff subclasses are mutually exclusive
     * Verifies: A staff member cannot be both Director and Guard simultaneously
     */
    private static void testDisjointConstraint() {
        clearAllExtents();
        
        Director director = new Director(
            "Alice", "Brown", 10,
            "9AM-5PM", "+1111111111", "alice@prison.gov",
            Director.DirectorRank.ASSISTANT
        );

        Guard guard = new Guard(
            "Bob", "Davis", 5,
            "2PM-10PM", "+2222222222", "bob@prison.gov",
            Guard.Rank.JUNIOR, "Baton"
        );

        Doctor doctor = new Doctor(
            "Carol", "Wilson", 7,
            "10AM-6PM", "+3333333333", "carol@prison.gov",
            "MD-99999", "Emergency: +3333333334"
        );

        // Verify each is only in their specific extent
        assertTrue(Director.getDirectorExtent().contains(director));
        assertFalse(Guard.getGuardExtent().contains(director));
        assertFalse(Doctor.getDoctorExtent().contains(director));

        assertTrue(Guard.getGuardExtent().contains(guard));
        assertFalse(Director.getDirectorExtent().contains(guard));
        assertFalse(Doctor.getDoctorExtent().contains(guard));

        assertTrue(Doctor.getDoctorExtent().contains(doctor));
        assertFalse(Director.getDirectorExtent().contains(doctor));
        assertFalse(Guard.getGuardExtent().contains(doctor));

        // All should be in Staff extent
        assertEquals(3, Staff.getExtent().size());
        assertTrue(Staff.getExtent().contains(director));
        assertTrue(Staff.getExtent().contains(guard));
        assertTrue(Staff.getExtent().contains(doctor));

        System.out.println("✓ Disjoint constraint verified - each staff member belongs to exactly one subclass");
    }

    /**
     * Test 5: Incomplete Constraint - Staff can exist without being fully specialized
     * Note: Since Staff is abstract, we verify through its concrete subclasses
     */
    private static void testIncompleteConstraint() {
        clearAllExtents();
        
        // Create different staff types
        Director director = new Director(
            "Tom", "Anderson", 5,
            "8AM-4PM", "+4444444444", "tom@prison.gov",
            Director.DirectorRank.REGIONAL
        );

        Guard guard = new Guard(
            "Emma", "Martinez", 3,
            "4PM-12AM", "+5555555555", "emma@prison.gov",
            Guard.Rank.CHIEF, "Pistol"
        );

        // Staff extent should contain both, showing inheritance is incomplete
        // (not all possible staff types need to be represented)
        assertEquals(2, Staff.getExtent().size());

        // We can have Director without Guard, showing incomplete constraint
        assertEquals(1, Director.getDirectorExtent().size());
        assertEquals(1, Guard.getGuardExtent().size());
        assertEquals(0, Doctor.getDoctorExtent().size());

        System.out.println("✓ Incomplete constraint verified - can have some staff types without all types");
    }

    // ============================================================
    // PART 2: COMBINED STAFF MULTIPLE INHERITANCE (COMPOSITION)
    // ============================================================

    /**
     * Test 6: CombinedStaff inherits from both Guard AND Doctor using COMPOSITION
     * Verifies: Multiple inheritance through composition/delegation
     */
    @Test
    @Order(6)
    @DisplayName("Test 6: CombinedStaff Multiple Inheritance - COMPOSITION Pattern")
    void testCombinedStaffMultipleInheritance() {
        // Create CombinedStaff with attributes from both Guard and Doctor
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

        // Verify Guard role capabilities through composition
        assertNotNull(combined.getGuardRole());
        assertTrue(combined.canPerformGuardDuties());
        assertEquals(Guard.Rank.LIEUTENANT, combined.getGuardRole().getRank());
        assertEquals("Pistol", combined.getGuardRole().getWeapon());

        // Verify Doctor role capabilities through composition
        assertNotNull(combined.getDoctorRole());
        assertTrue(combined.canPerformDoctorDuties());
        assertEquals("MD-77777", combined.getDoctorRole().getLicenseNumber());
        assertEquals("Emergency: +6666666667", combined.getDoctorRole().getContactInfo());

        // Test CombinedStaff-specific functionality
        combined.setAvailableForEmergency(true);
        assertTrue(combined.getAvailableForEmergency());
        assertDoesNotThrow(() -> combined.resolveToEmergency());

        // Verify in extent
        assertTrue(CombinedStaff.getCombinedStaffExtent().contains(combined));

        System.out.println("✓ CombinedStaff multiple inheritance via COMPOSITION verified");
    }

    /**
     * Test 7: CombinedStaff can perform both Guard and Doctor duties
     * Verifies: Delegation to composed role objects works correctly
     */
    @Test
    @Order(7)
    @DisplayName("Test 7: CombinedStaff Role Delegation")
    void testCombinedStaffRoleDelegation() {
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
        assertTrue(combined.getDoctorRole().getSpecialisation().contains("Trauma Care"));

        // Verify can perform both roles
        assertTrue(combined.canPerformGuardDuties());
        assertTrue(combined.canPerformDoctorDuties());

        System.out.println("✓ CombinedStaff role delegation verified");
    }

    /**
     * Test 8: CombinedStaff emergency functionality
     * Verifies: Specific CombinedStaff behavior beyond inherited roles
     */
    @Test
    @Order(8)
    @DisplayName("Test 8: CombinedStaff Emergency Functionality")
    void testCombinedStaffEmergency() {
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
        assertDoesNotThrow(() -> combined.resolveToEmergency());

        combined.setAvailableForEmergency(false);
        assertFalse(combined.getAvailableForEmergency());
        assertThrows(ValidationException.class, () -> combined.resolveToEmergency());

        System.out.println("✓ CombinedStaff emergency functionality verified");
    }

    // ============================================================
    // PART 3: REPORT INHERITANCE TESTS (COMPOSITION PATTERN)
    // ============================================================

    /**
     * Test 9: MedicalReport inherits from Report using COMPOSITION
     * Verifies: Report behavior through composition
     * Constraint: {Disjoint, Incomplete}
     */
    @Test
    @Order(9)
    @DisplayName("Test 9: MedicalReport Inheritance - COMPOSITION Pattern")
    void testMedicalReportInheritance() {
        // Create required dependencies
        Prisoner prisoner = new Prisoner(
            "P123", "Patient", "Doe", LocalDate.of(1980, 1, 1),
            LocalDate.of(2020, 1, 1), "Low", false
        );

        Doctor doctor = new Doctor(
            "Dr. House", "Medical", 20,
            "9AM-5PM", "+9999999999", "house@prison.gov",
            "MD-HOUSE", "Emergency: +9999999990"
        );

        MedicalRecord record = new MedicalRecord(prisoner, doctor);

        // Create MedicalReport
        MedicalReport medReport = new MedicalReport(
            LocalDate.now(), "Routine checkup", "Room 101",
            45.5, "Normal", doctor, record
        );

        // Verify Report attributes (through composition)
        assertEquals(LocalDate.now(), medReport.getDate());
        assertEquals("Routine checkup", medReport.getDescription());

        // Verify MedicalReport-specific attributes
        assertEquals("Room 101", medReport.getRoomNumber());
        assertEquals(45.5, medReport.getDuration());
        assertEquals("Normal", medReport.getSeverityLevel());
        assertEquals(doctor, medReport.getDoctor());
        assertEquals(record, medReport.getMedicalRecord());

        // Test Report behavior
        assertDoesNotThrow(() -> medReport.manageReport());

        // Verify in extent
        assertTrue(MedicalReport.getExtent().contains(medReport));

        System.out.println("✓ MedicalReport inheritance via COMPOSITION verified");
    }

    /**
     * Test 10: IncidentReport inherits from Report using COMPOSITION
     * Verifies: Report behavior through composition
     * Constraint: {Disjoint, Incomplete}
     */
    @Test
    @Order(10)
    @DisplayName("Test 10: IncidentReport Inheritance - COMPOSITION Pattern")
    void testIncidentReportInheritance() {
        // Create IncidentReport
        IncidentReport incReport = new IncidentReport(
            LocalDate.now(), "Fight in cafeteria",
            IncidentReport.Status.OPEN
        );

        // Verify Report attributes (through composition)
        assertEquals(LocalDate.now(), incReport.getDate());
        assertEquals("Fight in cafeteria", incReport.getDescription());

        // Verify IncidentReport-specific attributes
        assertEquals(IncidentReport.Status.OPEN, incReport.getStatus());

        // Test IncidentReport-specific behavior
        incReport.setSeverity("High");
        assertEquals("High", incReport.getSeverity());

        incReport.addPersonInvolved("Prisoner A");
        incReport.addPersonInvolved("Prisoner B");
        assertEquals(2, incReport.getPeopleInvolved().size());

        // Test Report behavior
        assertDoesNotThrow(() -> incReport.manageReport());

        // Verify in extent
        assertTrue(IncidentReport.getExtent().contains(incReport));

        System.out.println("✓ IncidentReport inheritance via COMPOSITION verified");
    }

    /**
     * Test 11: Report subclasses are Disjoint
     * Verifies: A report cannot be both MedicalReport and IncidentReport
     */
    @Test
    @Order(11)
    @DisplayName("Test 11: Report Disjoint Constraint")
    void testReportDisjointConstraint() {
        // Create dependencies
        Prisoner prisoner = new Prisoner(
            "P456", "Test", "Subject", LocalDate.of(1990, 5, 15),
            LocalDate.of(2021, 3, 10), "Medium", false
        );

        Doctor doctor = new Doctor(
            "Dr. Jekyll", "Hyde", 15,
            "8AM-4PM", "+1231231234", "jekyll@prison.gov",
            "MD-JEKYLL", "Emergency: +1231231235"
        );

        MedicalRecord record = new MedicalRecord(prisoner, doctor);

        // Create both types of reports
        MedicalReport medReport = new MedicalReport(
            LocalDate.now(), "Medical issue", "Room 202",
            30.0, "Moderate", doctor, record
        );

        IncidentReport incReport = new IncidentReport(
            LocalDate.now(), "Incident occurred",
            IncidentReport.Status.INREVIEW
        );

        // Verify they are in separate extents (disjoint)
        assertTrue(MedicalReport.getExtent().contains(medReport));
        assertFalse(IncidentReport.getExtent().contains(medReport));

        assertTrue(IncidentReport.getExtent().contains(incReport));
        assertFalse(MedicalReport.getExtent().contains(incReport));

        System.out.println("✓ Report disjoint constraint verified");
    }

    /**
     * Test 12: Report Incomplete Constraint
     * Verifies: Can have some report types without others
     */
    @Test
    @Order(12)
    @DisplayName("Test 12: Report Incomplete Constraint")
    void testReportIncompleteConstraint() {
        // Create only IncidentReports
        IncidentReport inc1 = new IncidentReport(
            LocalDate.now(), "Incident 1",
            IncidentReport.Status.OPEN
        );

        IncidentReport inc2 = new IncidentReport(
            LocalDate.now(), "Incident 2",
            IncidentReport.Status.RESOLVED
        );

        // We have IncidentReports without MedicalReports (incomplete)
        assertEquals(2, IncidentReport.getExtent().size());
        assertEquals(0, MedicalReport.getExtent().size());

        System.out.println("✓ Report incomplete constraint verified");
    }

    // ============================================================
    // PART 4: POLYMORPHISM AND TYPE CHECKING TESTS
    // ============================================================

    /**
     * Test 13: Polymorphic behavior of Staff hierarchy
     * Verifies: All Staff subclasses can be treated as Staff
     */
    @Test
    @Order(13)
    @DisplayName("Test 13: Staff Polymorphism")
    void testStaffPolymorphism() {
        Director director = new Director(
            "Poly", "Director", 10,
            "9AM-5PM", "+1010101010", "poly.d@prison.gov",
            Director.DirectorRank.GENERAL
        );

        Guard guard = new Guard(
            "Poly", "Guard", 8,
            "6AM-2PM", "+2020202020", "poly.g@prison.gov",
            Guard.Rank.SENIOR, "Baton"
        );

        Doctor doctor = new Doctor(
            "Poly", "Doctor", 12,
            "8AM-4PM", "+3030303030", "poly.doc@prison.gov",
            "MD-POLY", "Emergency: +3030303031"
        );

        // All should be treatable as Staff
        assertEquals(3, Staff.getExtent().size());

        // Test polymorphic behavior
        for (Staff staff : Staff.getExtent()) {
            assertNotNull(staff.getName());
            assertTrue(staff.getExperienceYears() > 0);
        }

        System.out.println("✓ Staff polymorphism verified");
    }

    /**
     * Test 14: Type-specific behavior
     * Verifies: Each subclass maintains its specific behavior
     */
    @Test
    @Order(14)
    @DisplayName("Test 14: Type-Specific Behavior")
    void testTypeSpecificBehavior() {
        Director director = new Director(
            "Type", "Director", 5,
            "9AM-5PM", "+4040404040", "type.d@prison.gov",
            Director.DirectorRank.ASSISTANT
        );

        Guard guard = new Guard(
            "Type", "Guard", 3,
            "2PM-10PM", "+5050505050", "type.g@prison.gov",
            Guard.Rank.JUNIOR, "Taser"
        );

        // Director-specific behavior
        assertEquals(Director.DirectorRank.ASSISTANT, director.getRank());

        // Guard-specific behavior
        assertEquals("Taser", guard.getWeapon());

        // Guard can have supervisors (specific to Guard)
        Guard supervisor = new Guard(
            "Super", "Visor", 10,
            "6AM-2PM", "+6060606060", "super@prison.gov",
            Guard.Rank.CHIEF, "Pistol"
        );

        guard.addSupervisor(supervisor);
        assertEquals(1, guard.getSupervisors().size());
        assertEquals(supervisor, guard.getSupervisors().get(0));

        System.out.println("✓ Type-specific behavior verified");
    }

    /**
     * Test 15: Inheritance attribute validation
     * Verifies: Inherited validation rules work correctly
     */
    @Test
    @Order(15)
    @DisplayName("Test 15: Inherited Validation Rules")
    void testInheritedValidation() {
        Director director = new Director(
            "Valid", "Director", 5,
            "9AM-5PM", "+7070707070", "valid@prison.gov",
            Director.DirectorRank.REGIONAL
        );

        // Test inherited validation from Staff
        assertThrows(EmptyStringException.class, () -> director.setName(""));
        assertThrows(EmptyStringException.class, () -> director.setSurname(null));
        assertThrows(NegativeNumberException.class, () -> director.setExperienceYears(-1));
        assertThrows(EmptyStringException.class, () -> director.setPhone(""));

        // Test Director-specific validation
        assertThrows(InvalidReferenceException.class, () -> director.setRank(null));

        System.out.println("✓ Inherited validation rules verified");
    }

    /**
     * Test 16: Extent management across inheritance hierarchy
     * Verifies: Proper extent tracking in inheritance hierarchy
     */
    @Test
    @Order(16)
    @DisplayName("Test 16: Extent Management in Inheritance")
    void testExtentManagement() {
        // Create multiple instances
        Director d1 = new Director("D1", "One", 5, "9AM-5PM", "+1", "d1@prison.gov", Director.DirectorRank.ASSISTANT);
        Director d2 = new Director("D2", "Two", 7, "9AM-5PM", "+2", "d2@prison.gov", Director.DirectorRank.REGIONAL);
        Guard g1 = new Guard("G1", "One", 3, "6AM-2PM", "+3", "g1@prison.gov", Guard.Rank.JUNIOR, "Baton");
        Guard g2 = new Guard("G2", "Two", 4, "2PM-10PM", "+4", "g2@prison.gov", Guard.Rank.SENIOR, "Taser");
        Doctor doc1 = new Doctor("Doc1", "One", 10, "8AM-4PM", "+5", "doc1@prison.gov", "MD-1", "Emergency: +5");

        // Verify specific extents
        assertEquals(2, Director.getDirectorExtent().size());
        assertEquals(2, Guard.getGuardExtent().size());
        assertEquals(1, Doctor.getDoctorExtent().size());

        // Verify Staff extent contains all
        assertEquals(5, Staff.getExtent().size());

        // Clear specific extent
        Director.clearDirectorExtent();
        assertEquals(0, Director.getDirectorExtent().size());

        System.out.println("✓ Extent management verified");
    }

    @AfterEach
    void tearDown() {
        // Clean up after each test
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
}
