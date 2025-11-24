package com.prison.model;

import com.prison.exception.*;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Charges implements Serializable {
    private static final long serialVersionUID = 1L;

    private static List<Charges> extent = new ArrayList<>();

    private String description;
    private String lawSection;
    private String severityLevel;      // Severity level
    private LocalDate dateFiled;       // Date filed
    private Prisoner prisoner;     // Defendant
    private CourtCase courtCase;   // Court case

    public Charges(String description, String lawSection, String severityLevel, LocalDate dateFiled,
                   Prisoner prisoner, CourtCase courtCase) {
        setDescription(description);
        setLawSection(lawSection);
        setSeverityLevel(severityLevel);
        setDateFiled(dateFiled);
        setPrisoner(prisoner);
        setCourtCase(courtCase);
        extent.add(this);
    }
    public String getDescription() { return description; }
    public void setDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new EmptyStringException("Description cannot be empty.");
        }
        this.description = description;
    }

    public String getLawSection() { return lawSection; }
    public void setLawSection(String lawSection) {
        if (lawSection == null || lawSection.trim().isEmpty()) {
            throw new EmptyStringException("Law section cannot be empty.");
        }
        this.lawSection = lawSection;
    }

    public String getSeverityLevel() { return severityLevel; }
    public void setSeverityLevel(String severityLevel) {
        if (severityLevel == null || severityLevel.trim().isEmpty()) {
            throw new EmptyStringException("Severity level cannot be empty.");
        }
        this.severityLevel = severityLevel;
    }

    public LocalDate getDateFiled() { return dateFiled; }
    public void setDateFiled(LocalDate dateFiled) {
        if (dateFiled == null) {
            throw new InvalidReferenceException("Date filed cannot be null.");
        }
        this.dateFiled = dateFiled;
    }
    /**
     * Determines if charge is a felony based on severity level
     * Formula: true if severityLevel contains "HIGH" or "FELONY"
     */
    public boolean isFelony() {
        if (severityLevel == null) return false;
        String level = severityLevel.toUpperCase();
        return level.contains("HIGH") || level.contains("FELONY") || level.contains("SEVERE");
    }
    
    public void setPrisoner(Prisoner prisoner) {
        if (prisoner == null) {
            throw new InvalidReferenceException("Prisoner cannot be null.");
        }
        this.prisoner = prisoner;
        
        if (!prisoner.getCourtCases().contains(courtCase)) {
            prisoner.addCourtCase(courtCase);
        }
    }
    
    public Prisoner getPrisoner() {
        return prisoner;
    }
    
    public void setCourtCase(CourtCase courtCase) {
        if (courtCase == null) {
            throw new InvalidReferenceException("Court case cannot be null.");
        }
        this.courtCase = courtCase;
        
        if (!courtCase.getCharges().contains(this)) {
            courtCase.addCharge(this);
        }
    }
    
    public CourtCase getCourtCase() {
        return courtCase;
    }

    public static List<Charges> getExtent() {
        return Collections.unmodifiableList(extent);
    }

    public static void saveExtent(String filename) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(extent);
        }
    }

    @SuppressWarnings("unchecked")
    public static void loadExtent(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            extent = (List<Charges>) in.readObject();
        } catch (FileNotFoundException e) {
            extent = new ArrayList<>();
        }
    }

    public static void clearExtent() {
        extent.clear();
    }
}
