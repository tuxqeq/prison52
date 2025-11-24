# Prison Management System

A comprehensive Java-based prison management system implementing object-oriented design principles, including class hierarchies, associations, composition patterns, and persistent storage.

## 📋 Project Overview

This project demonstrates a complete prison management system with 22 interconnected classes managing prisoners, staff, facilities, medical records, visits, court cases, and more. The system implements:

- **Class Hierarchies**: Abstract classes (Staff, Report) with concrete implementations
- **Design Patterns**: Composition (Block → Cell), Delegation (CombinedStaff), XOR constraints
- **Bidirectional Associations**: Automatic synchronization between related objects
- **Exception Handling**: 5 custom exception types for comprehensive validation
- **Persistence**: Java serialization for saving/loading entire object graphs
- **Class Extent Management**: Tracks all instances of each class

## 🏗️ Architecture

### Core Entities

**Staff Hierarchy:**
- `Staff` (abstract)
  - `Guard` - Security personnel with ranks and weapons
  - `Doctor` - Medical staff with specializations and licenses
  - `Director` - Administrative staff with approval authorities
  - `CombinedStaff` - Dual-role staff (guard + doctor capabilities)

**Report Hierarchy:**
- `Report` (abstract)
  - `IncidentReport` - Security incident documentation
  - `MedicalReport` - Medical examination records

**Other Key Classes:**
- `Prisoner` - Incarcerated individuals with comprehensive tracking
- `Cell` / `Block` - Facility management (composition pattern)
- `Visit` / `Visitor` - Visitor management with approval workflow
- `CourtCase` / `Charges` - Legal proceedings
- `Punishment` - Disciplinary actions
- `MedicalRecord` / `MedicalExamination` - Healthcare tracking
- `Schedule` - Activity scheduling
- `Meal` / `MealDelivery` - Food service management
- `Assignment` - Work assignments

### Key Features

**6 Attribute Types Implemented:**
1. **Basic** - Strings, integers, dates (e.g., `name`, `age`, `dateOfStart`)
2. **Complex** - Enums (e.g., `SecurityLevel`, `Rank`, `ApprovalStatus`)
3. **Multi-value** - Required lists [1..*] (e.g., `possession`, `allergyInfo`)
4. **Class/Static** - Shared across instances (e.g., `maxAmountOfVisitPerMonth`)
5. **Derived** - Calculated values (e.g., `remainingSentenceTime`, `availablePlace`)
6. **Optional** - Nullable fields [0..1] (e.g., `rejectionReason`, `weapon`)

**Exception Handling:**
- `EmptyStringException` - Validates non-empty strings
- `NegativeNumberException` - Prevents negative values
- `InvalidDateException` - Date range validation
- `InvalidReferenceException` - Null reference checks
- `ValidationException` - Business logic constraints (XOR, capacity limits)

**Persistence Mechanism:**
- All classes implement `Serializable`
- Static extent lists track all instances
- `saveExtent(filename)` - Serialize objects to .ser files
- `loadExtent(filename)` - Deserialize from disk
- Automatic relationship preservation

## 🚀 Getting Started

### Prerequisites

- Java Development Kit (JDK) 8 or higher
- Terminal/Command Line access
- (Optional) IDE like IntelliJ IDEA or VS Code

### Installation

1. **Clone or download the project**
   ```bash
   cd /path/to/prison52
   ```

2. **Compile the project**
   ```bash
   javac -d bin src/main/java/com/prison/**/*.java
   ```

### Running the Application

#### Option 1: Run Main Program
```bash
java -cp bin com.prison.Main
```

**What it does:**
- Creates sample prisoners, staff, cells, and blocks
- Demonstrates object relationships
- Shows persistence (save → clear → load)
- Displays extent management

**Expected Output:**
```
--- Prison Management System Demo ---

Creating Prisoners...
Prisoner 1: Prisoner{name='John', surname='Doe', age=35, crime='Theft'}
Total Prisoners in Extent: 2

Creating Staff...
Doctor: Gregory House, Lic: MED-12345

--- Persistence Demo ---
Saving prisoners to prisoners.ser...
Clearing memory...
Total Prisoners after clear: 0
Loading prisoners from prisoners.ser...
Total Prisoners after load: 2
```

#### Option 2: Using an IDE

1. Open `src/main/java/com/prison/Main.java`
2. Click the green ▶ play button next to `public static void main`
3. View output in the IDE console

### Running Tests

The project includes 42 comprehensive unit tests covering all classes and features.

#### Run All Tests
```bash
bash run_tests.sh
```

**Expected Output:**
```
Compiling test classes...
Running tests...

========================================
Test Suite: BlockTest
========================================
✓ testMandatoryAttributes PASSED
✓ testDerivedAttributes PASSED
✓ testCompositionConstraint PASSED

========================================
Test Suite: PrisonerTest
========================================
✓ testMandatoryAttributes PASSED
✓ testInvalidName PASSED
✓ testInvalidAge PASSED
✓ testAgeAttribute PASSED
✓ testExtentAddition PASSED
✓ testPersistence PASSED

... (42 tests total)

========================================
All tests completed successfully!
Total: 42 tests passed, 0 tests failed
========================================
```

#### Test Coverage

- **Attribute Validation Tests**: String/number/date validation
- **Exception Handling Tests**: Each exception type verified
- **Association Tests**: Bidirectional linking and synchronization
- **Constraint Tests**: XOR constraints, capacity limits
- **Extent Tests**: Object tracking and registration
- **Persistence Tests**: Save/load/data integrity
- **Derived Attribute Tests**: Calculated values verification
- **System Integration Tests**: Complex multi-object scenarios

## 📁 Project Structure

```
prison52/
├── src/
│   ├── main/java/com/prison/
│   │   ├── Main.java                 # Entry point - demo application
│   │   ├── model/                    # All domain classes (22 classes)
│   │   │   ├── Prisoner.java
│   │   │   ├── Cell.java
│   │   │   ├── Block.java
│   │   │   ├── Staff.java (abstract)
│   │   │   ├── Guard.java
│   │   │   ├── Doctor.java
│   │   │   ├── Director.java
│   │   │   ├── CombinedStaff.java
│   │   │   ├── Report.java (abstract)
│   │   │   ├── IncidentReport.java
│   │   │   ├── MedicalReport.java
│   │   │   ├── Visit.java
│   │   │   ├── Visitor.java
│   │   │   ├── CourtCase.java
│   │   │   ├── Charges.java
│   │   │   ├── Punishment.java
│   │   │   ├── MedicalRecord.java
│   │   │   ├── MedicalExamination.java
│   │   │   ├── Schedule.java
│   │   │   ├── Meal.java
│   │   │   ├── MealDelivery.java
│   │   │   └── Assignment.java
│   │   └── exception/                # Custom exceptions (5 types)
│   │       ├── EmptyStringException.java
│   │       ├── NegativeNumberException.java
│   │       ├── InvalidDateException.java
│   │       ├── InvalidReferenceException.java
│   │       └── ValidationException.java
│   └── test/java/com/prison/
│       ├── model/                    # Unit tests (42 tests)
│       │   ├── PrisonerTest.java
│       │   ├── CellTest.java
│       │   ├── BlockTest.java
│       │   ├── GuardTest.java
│       │   ├── DoctorTest.java
│       │   ├── DirectorTest.java
│       │   ├── VisitTest.java
│       │   ├── SystemTest.java
│       │   └── ... (18 test files)
│       └── test/
│           └── SimpleUnitTest.java   # Custom test framework
├── bin/                              # Compiled .class files
├── prisoners.ser                     # Persisted data (generated)
├── run_tests.sh                      # Test execution script
└── README.md                         # This file
```

## 💡 Usage Examples

### Creating and Managing Prisoners

```java
// Create a prisoner
Prisoner prisoner = new Prisoner(
    "Alice", "Johnson", 30, "Burglary",
    LocalDate.of(2023, 1, 1), 5, "None", "Active"
);

// Add possessions (multi-value attribute [1..*])
prisoner.addPossession("Watch");
prisoner.addPossession("Book");

// Add allergy info (required list)
prisoner.addAllergyInfo("Peanuts");

// Check derived attribute
int remaining = prisoner.getRemainingSentenceTime(); // Calculated

// Access extent
List<Prisoner> allPrisoners = Prisoner.getExtent();
System.out.println("Total prisoners: " + allPrisoners.size());
```

### Cell Management with Composition

```java
// Create a block (composition pattern)
Block block = new Block("A", Block.BlockType.MEDIUM_SECURITY, 100);

// Create cells (automatically added to block)
Cell cell1 = new Cell(101, "Standard", 2, Cell.SecurityLevel.MEDIUM);
Cell cell2 = new Cell(102, "Solitary", 1, Cell.SecurityLevel.HIGH);

// Add cells to block
block.addCell(cell1);
block.addCell(cell2);

// Assign prisoner to cell
cell1.addPrisoner(prisoner);

// Check derived attributes
int available = cell1.getAvailablePlace(); // capacity - prisoners.size()
int availableCells = block.getAvailableCell(); // Count of available cells
```

### Staff Management

```java
// Create a doctor
Doctor doctor = new Doctor(
    "Gregory", "House", 15, "9am-5pm",
    "555-0100", "house@hospital.com",
    "MED-12345", "555-0101"
);
doctor.addSpecialization("Diagnostician");

// Create a guard
Guard guard = new Guard(
    "Paul", "Blart", 5, "Day Shift",
    "555-0200", "blart@prison.com",
    Guard.Rank.SENIOR, "Baton"
);

// Create combined staff (delegation pattern)
CombinedStaff combined = new CombinedStaff(
    "Jane", "Doe", 8, "Rotating", "555-0300", "jane@prison.com",
    Guard.Rank.JUNIOR, "Taser", "MED-67890", "555-0301", true
);
// Can perform both guard and doctor duties
```

### Visit Management with Approval Workflow

```java
// Create a visitor
Visitor visitor = new Visitor("Bob", "Smith", "Brother", "555-1234");

// Create a visit (requires Director approval)
Visit visit = new Visit(
    LocalDate.now().plusDays(7),
    LocalTime.of(14, 0),
    visitor
);

// Director approves visit
Director director = new Director(
    "Warden", "Norton", 20, "8am-6pm",
    "555-0300", "norton@prison.com",
    Director.DirectorRank.GENERAL
);
director.addApprovedVisit(visit);
visit.setApprovalStatus(Visit.ApprovalStatus.APPROVED);

// Or reject with reason
visit.setApprovalStatus(Visit.ApprovalStatus.REJECTED);
visit.setRejectionReason("Security concerns");
```

### XOR Constraints

```java
// Prisoner can have EITHER court cases OR punishments (not both)
Prisoner p = new Prisoner(...);

// Add a court case
CourtCase courtCase = new CourtCase(...);
p.addCourtCase(courtCase); // ✅ OK

// Try to add punishment - XOR violation!
Punishment punishment = new Punishment(...);
p.addPunishment(punishment); // ❌ Throws ValidationException
// "XOR Constraint Violation: Prisoner cannot have both punishments and court cases"

// Must clear court cases first
p.removeCourtCase(courtCase);
p.addPunishment(punishment); // ✅ Now OK
```

### Persistence

```java
// Create objects
Prisoner p1 = new Prisoner("John", "Doe", 35, ...);
Prisoner p2 = new Prisoner("Jane", "Smith", 40, ...);

// Save to disk
Prisoner.saveExtent("prisoners.ser");

// Simulate application restart
Prisoner.clearExtent();
System.out.println(Prisoner.getExtent().size()); // 0

// Load from disk
Prisoner.loadExtent("prisoners.ser");
System.out.println(Prisoner.getExtent().size()); // 2

// All data intact (attributes, relationships, etc.)
Prisoner loaded = Prisoner.getExtent().get(0);
System.out.println(loaded.getName()); // "John"
```

## 🔍 Key Implementation Details

### Bidirectional Associations

All associations are bidirectional and automatically synchronized:

```java
// Adding prisoner to cell updates both sides
cell.addPrisoner(prisoner);
// Automatically calls: prisoner.setCell(cell)

// Removing maintains consistency
cell.removePrisoner(prisoner);
// Automatically calls: prisoner.setCell(null)
```

### Derived Attributes

Calculated values with no setters:

```java
// Prisoner: remaining sentence time
public int getRemainingSentenceTime() {
    int yearsServed = Period.between(dateOfStart, LocalDate.now()).getYears();
    return Math.max(0, sentenceYears - yearsServed);
}

// Cell: available space
public int getAvailablePlace() {
    return capasity - prisoners.size();
}

// Schedule: duration in minutes
public long getDuration() {
    return java.time.Duration.between(startTime, endTime).toMinutes();
}
```

### Class Extent Pattern

Every class tracks all instances:

```java
public class Prisoner implements Serializable {
    private static List<Prisoner> extent = new ArrayList<>();
    
    public Prisoner(...) {
        // Constructor logic
        extent.add(this); // Auto-registration
    }
    
    public static List<Prisoner> getExtent() {
        return Collections.unmodifiableList(extent);
    }
    
    public static void clearExtent() {
        extent.clear();
    }
}
```

## 🧪 Testing

### Test Framework

Custom lightweight testing framework (`SimpleUnitTest`):

```java
public class PrisonerTest extends SimpleUnitTest {
    public static void main(String[] args) {
        runTest("testName", () -> {
            // Test logic
            assertEquals(expected, actual);
        });
    }
}
```

### Running Individual Tests

```bash
# Compile and run specific test
javac -d bin -cp bin src/test/java/com/prison/model/PrisonerTest.java
java -cp bin com.prison.model.PrisonerTest
```

## 📊 Assignment Compliance

This project implements all requirements from Assignment 4:

✅ **Requirement 1**: Project skeleton with 22 classes created  
✅ **Requirement 2a**: All 6 attribute types implemented  
✅ **Requirement 2b**: Full attribute implementation in all classes  
✅ **Requirement 2c**: 5 exception types with comprehensive validation  
✅ **Requirement 3**: Class extent and persistence in all 22 classes  
✅ **Bonus**: Complete unit test coverage (42 tests)  

## 🛠️ Troubleshooting

### Compilation Errors

```bash
# If you see "javac: command not found"
# Install Java JDK or use an IDE

# If you see "package does not exist"
# Make sure you're in the project root directory
cd /Users/tuxqeq/Documents/ITJ/BYT/prison52
```

### Runtime Errors

```bash
# If you see "ClassNotFoundException"
# Recompile the project
javac -d bin src/main/java/com/prison/**/*.java

# If you see "FileNotFoundException" for .ser files
# Normal on first run - files are created when saving
# Or delete existing .ser files to start fresh
rm *.ser
```

### Test Failures

```bash
# If tests fail, check:
# 1. All source files are compiled
# 2. Test files are compiled
# 3. No manual changes broke associations

# Recompile everything
javac -d bin src/main/java/com/prison/**/*.java
javac -d bin -cp bin src/test/java/com/prison/**/*.java
bash run_tests.sh
```

## 📝 License

This project is an academic assignment for educational purposes.

## 👥 Author

Prison Management System - BYT Assignment 4 Implementation

---

**Note**: This is a demonstration project implementing object-oriented programming concepts. It is not intended for production use in actual prison management systems.
