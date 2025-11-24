package com.prison.model;

import com.prison.exception.*;
import java.io.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Prisoner implements Serializable {
    private static final long serialVersionUID = 1L;

    // --- Class Extent (Static Collection) ---
    private static List<Prisoner> extent = new ArrayList<>();

    // --- Attributes ---
    private String name;
    private String surname;
    private int age;
    private String crime;
    private List<String> possession;        
    private LocalDate dateOfStart;
    private int sentenceYears;
    private String restriction;
    private String status;
    private List<String> allergyInfo;       
    
    // Class/Static Attribute
    private static final int maxAmountOfVisitPerMonth = 2;
    
    // --- Associations (Per Authoritative Table) ---
    private Cell currentCell;                            // Many-to-one: Current cell assignment
    private List<Punishment> punishments;                // XOR with CourtCase: Punishments received
    private List<CourtCase> courtCases;                  // XOR with Punishment: Legal cases
    private List<MealDelivery> mealDeliveries;           // One-to-many: Meal delivery history
    private Schedule schedule;                           // Many-to-one: Daily schedule

    // --- Constructor ---
    public Prisoner(String name, String surname, int age, String crime,
                    LocalDate dateOfStart, int sentenceYears, String restriction, String status) {
        setName(name);
        setSurname(surname);
        setAge(age);
        setCrime(crime);
        setDateOfStart(dateOfStart);
        setSentenceYears(sentenceYears);
        setRestriction(restriction);
        setStatus(status);
        
        // Initialize required lists [1..*]
        this.possession = new ArrayList<>();
        this.allergyInfo = new ArrayList<>();
        
        // Initialize association collections (per authoritative table)
        this.punishments = new ArrayList<>();
        this.courtCases = new ArrayList<>();
        this.mealDeliveries = new ArrayList<>();
        
        // Add to extent automatically
        extent.add(this);
    }

    public static int getMaxAmountOfVisitPerMonth() {
        return maxAmountOfVisitPerMonth;
    }

    // --- Getters and Setters with Validation ---

    public String getName() { return name; }
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new EmptyStringException("Name cannot be empty.");
        }
        this.name = name;
    }

    public String getSurname() { return surname; }
    public void setSurname(String surname) {
        if (surname == null || surname.trim().isEmpty()) {
            throw new EmptyStringException("Surname cannot be empty.");
        }
        this.surname = surname;
    }

    public int getAge() { return age; }
    public void setAge(int age) {
        if (age < 0) {
            throw new NegativeNumberException("Age cannot be negative.");
        }
        this.age = age;
    }

    public String getCrime() { return crime; }
    public void setCrime(String crime) {
        if (crime == null || crime.trim().isEmpty()) {
            throw new EmptyStringException("Crime cannot be empty.");
        }
        this.crime = crime;
    }

    public LocalDate getDateOfStart() { return dateOfStart; }
    public void setDateOfStart(LocalDate dateOfStart) {
        if (dateOfStart == null) {
            throw new InvalidReferenceException("Date of start cannot be null.");
        }
        this.dateOfStart = dateOfStart;
    }

    public int getSentenceYears() { return sentenceYears; }
    public void setSentenceYears(int sentenceYears) {
        if (sentenceYears < 0) {
            throw new NegativeNumberException("Sentence years cannot be negative.");
        }
        this.sentenceYears = sentenceYears;
    }

    public String getRestriction() { return restriction; }
    public void setRestriction(String restriction) {
        this.restriction = restriction;  // Can be null
    }

    public String getStatus() { return status; }
    public void setStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new EmptyStringException("Status cannot be empty.");
        }
        this.status = status;
    }

    // --- Multi-value Attribute Methods (Required Lists [1..*]) ---
    
    public List<String> getPossession() {
        return Collections.unmodifiableList(possession);
    }
    public void addPossession(String item) {
        if (item == null || item.trim().isEmpty()) {
            throw new EmptyStringException("Possession item cannot be empty.");
        }
        possession.add(item);
    }
    public void removePossession(String item) {
        possession.remove(item);
    }

    public List<String> getAllergyInfo() {
        return Collections.unmodifiableList(allergyInfo);
    }
    public void addAllergyInfo(String allergy) {
        if (allergy == null || allergy.trim().isEmpty()) {
            throw new EmptyStringException("Allergy info cannot be empty.");
        }
        allergyInfo.add(allergy);
    }
    public void removeAllergyInfo(String allergy) {
        allergyInfo.remove(allergy);
    }

    // --- Derived Attribute: /remainingSentenceTime ---
    /**
    /**
     * Calculates remaining sentence time in years
    /**
     * Formula: sentenceYears - (now - dateOfStart)
     */
    public int getRemainingSentenceTime() {
        if (dateOfStart == null) return sentenceYears;
        int yearsServed = Period.between(dateOfStart, LocalDate.now()).getYears();
        int remaining = sentenceYears - yearsServed;
        return Math.max(0, remaining);  // Cannot be negative
    }

    // --- Methods from Diagram ---
    
    /**
    /**
     * Assigns prisoner to a cell
     */
    public void assignPrisonerToCell(Cell cell) {
        if (cell == null) {
            throw new InvalidReferenceException("Cell cannot be null.");
        }
        setCurrentCell(cell);
    }

    /**
    /**
     * Removes prisoner from current cell
     */
    public void removePrisonerFromCell() {
        if (currentCell != null) {
            currentCell.removePrisoner(this);
        }
        this.currentCell = null;
    }

    /**
    /**
     * Request an item (adds to possession list)
     */
    public void requestItem(String item) {
        addPossession(item);
    }

    /**
    /**
     * General prisoner management method
     */
    public void managePrisoner() {
        // Management logic - can be expanded
        System.out.println("Managing prisoner: " + name + " " + surname);
    }

    // --- Association Management Methods ---
    
    /**
    /**
     * Assigns prisoner to a cell
    /**
     * Handles bidirectional link and removes from old cell if any
     */
    public void assignToCell(Cell cell) {
        // Remove from old cell
        if (this.currentCell != null && this.currentCell != cell) {
            this.currentCell.removePrisoner(this);
        }
        
        // Set new cell
        this.currentCell = cell;
        
        // Add to new cell's prisoner list
        if (cell != null && !cell.getPrisoners().contains(this)) {
            cell.addPrisoner(this);
        }
    }
    
    /**
    /**
     * Sets current cell (wrapper for assignToCell)
     */
    public void setCurrentCell(Cell cell) {
        assignToCell(cell);
    }
    
    /**
    /**
     * Gets current cell assignment
     */
    public Cell getCurrentCell() {
        return currentCell;
    }
    

    

    

    

    
    /**
    /**
     * Adds a punishment
    /**
     * Called by Punishment.setPrisoner()
    /**
     * XOR Constraint: Prisoner can have EITHER punishments OR court cases, not both
     */
    public void addPunishment(Punishment punishment) {
        if (punishment == null) {
            throw new InvalidReferenceException("Punishment cannot be null.");
        }
        // XOR Constraint: Cannot have both punishments and court cases
        if (!courtCases.isEmpty()) {
            throw new ValidationException("XOR Constraint Violation: Prisoner cannot have both punishments and court cases. Clear court cases first.");
        }
        if (!punishments.contains(punishment)) {
            punishments.add(punishment);
            if (punishment.getPrisoner() != this) {
                punishment.setPrisoner(this);
            }
        }
    }
    
    /**
    /**
     * Gets all punishments
     */
    public List<Punishment> getPunishments() {
        return Collections.unmodifiableList(punishments);
    }
    
    /**
    /**
     * Adds a court case (via Charges association class)
    /**
     * Called by charges linkage
    /**
     * XOR Constraint: Prisoner can have EITHER court cases OR punishments, not both
     */
    public void addCourtCase(CourtCase courtCase) {
        if (courtCase == null) {
            throw new InvalidReferenceException("Court case cannot be null.");
        }
        // XOR Constraint: Cannot have both court cases and punishments
        if (!punishments.isEmpty()) {
            throw new ValidationException("XOR Constraint Violation: Prisoner cannot have both court cases and punishments. Clear punishments first.");
        }
        if (!courtCases.contains(courtCase)) {
            courtCases.add(courtCase);
        }
    }
    
    /**
    /**
     * Gets all court cases
     */
    public List<CourtCase> getCourtCases() {
        return Collections.unmodifiableList(courtCases);
    }
    
    /**
    /**
     * Adds a meal delivery
    /**
     * Called by MealDelivery.setPrisoner()
     */
    public void addMealDelivery(MealDelivery delivery) {
        if (delivery == null) {
            throw new InvalidReferenceException("Meal delivery cannot be null.");
        }
        if (!mealDeliveries.contains(delivery)) {
            mealDeliveries.add(delivery);
            if (delivery.getPrisoner() != this) {
                delivery.setPrisoner(this);
            }
        }
    }
    
    /**
    /**
     * Gets all meal deliveries
     */
    public List<MealDelivery> getMealDeliveries() {
        return Collections.unmodifiableList(mealDeliveries);
    }
    
    /**

    /**
    /**
     * Sets the daily schedule
     */
    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
        if (schedule != null && schedule.getPrisoner() != this) {
            schedule.setPrisoner(this);
        }
    }
    
    /**
    /**
     * Gets the daily schedule
     */
    public Schedule getSchedule() {
        return schedule;
    }
    
    // Note: Visit association is between Visitor and Visit, NOT Prisoner and Visit
    // Visits can be queried through Visit.getExtent() filtered by prisoner
    
    // Note: MedicalRecord, MedicalExamination, MedicalReport are NOT directly connected to Prisoner
    // per the authoritative relationship table
    
    // Note: IncidentReport is NOT directly connected to Prisoner per the authoritative table

    // --- Extent Management Methods ---
    
    public static List<Prisoner> getExtent() {
        return Collections.unmodifiableList(extent);
    }

    // --- Persistence Methods (Save/Load) ---
    
    public static void saveExtent(String filename) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(extent);
        }
    }

    @SuppressWarnings("unchecked")
    public static void loadExtent(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            extent = (List<Prisoner>) in.readObject();
        } catch (FileNotFoundException e) {
            // File doesn't exist yet, start with empty list
            extent = new ArrayList<>();
        }
    }
    
    public static void clearExtent() {
        extent.clear();
    }

    @Override
    public String toString() {
        return "Prisoner{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", age=" + getAge() +
                ", crime='" + crime + '\'' +
                '}';
    }
}
