package com.prison.model;

import com.prison.exception.*;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MedicalRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    private static List<MedicalRecord> extent = new ArrayList<>();

    private List<String> allergies;
    private String bloodType;
    private LocalDate dateOfCreation;
    private List<String> history;              // [1..*] Medical history
    private String descriptionOfDiagnosis;
    private Prisoner prisoner;                       // Owner of this record
    private List<MedicalExamination> examinations;   // Examination history
    private Doctor assignedDoctor;                   // Primary doctor for this record

    public MedicalRecord(String bloodType, LocalDate dateOfCreation, String descriptionOfDiagnosis) {
        setBloodType(bloodType);
        setDateOfCreation(dateOfCreation);
        setDescriptionOfDiagnosis(descriptionOfDiagnosis);
        this.history = new ArrayList<>();  // Initialize required list
        this.allergies = new ArrayList<>();
        this.examinations = new ArrayList<>();
        extent.add(this);
    }
    public String getBloodType() { return bloodType; }
    public void setBloodType(String bloodType) {
        if (bloodType == null || bloodType.trim().isEmpty()) {
            throw new EmptyStringException("Blood type cannot be empty.");
        }
        this.bloodType = bloodType;
    }

    public LocalDate getDateOfCreation() { return dateOfCreation; }
    public void setDateOfCreation(LocalDate dateOfCreation) {
        if (dateOfCreation == null) {
            throw new InvalidReferenceException("Date of creation cannot be null.");
        }
        this.dateOfCreation = dateOfCreation;
    }

    public String getDescriptionOfDiagnosis() { return descriptionOfDiagnosis; }
    public void setDescriptionOfDiagnosis(String descriptionOfDiagnosis) {
        if (descriptionOfDiagnosis == null || descriptionOfDiagnosis.trim().isEmpty()) {
            throw new EmptyStringException("Description of diagnosis cannot be empty.");
        }
        this.descriptionOfDiagnosis = descriptionOfDiagnosis;
    }
    public List<String> getHistory() {
        return Collections.unmodifiableList(history);
    }
    public void addHistory(String item) {
        if (item == null || item.trim().isEmpty()) {
            throw new EmptyStringException("History item cannot be empty.");
        }
        history.add(item);
    }
    public void removeHistory(String item) {
        history.remove(item);
    }
    public void updateMedicalRecord(String newDiagnosis) {
        setDescriptionOfDiagnosis(newDiagnosis);
        addHistory("Updated: " + LocalDate.now() + " - " + newDiagnosis);
    }

    public void viewMedicalRecord() {
        System.out.println("Medical Record for: " + (prisoner != null ? prisoner.getName() : "Unknown"));
        System.out.println("Blood Type: " + bloodType);
        System.out.println("Diagnosis: " + descriptionOfDiagnosis);
    }

    public List<String> getAllergies() {
        return Collections.unmodifiableList(allergies);
    }
    public void addAllergy(String allergy) {
        if (allergy == null || allergy.trim().isEmpty()) {
            throw new EmptyStringException("Allergy cannot be empty.");
        }
        allergies.add(allergy);
    }
    /**
     * Sets the prisoner this record belongs to (one-time only)
     */
    public void setPrisoner(Prisoner prisoner) {
        if (this.prisoner != null && this.prisoner != prisoner) {
            throw new ValidationException("Medical record already assigned to another prisoner.");
        }
        this.prisoner = prisoner;
    }
    
    public Prisoner getPrisoner() {
        return prisoner;
    }
    
    /**
     * Adds an examination to this record
     */
    public void addExamination(MedicalExamination exam) {
        if (exam == null) {
            throw new InvalidReferenceException("Examination cannot be null.");
        }
        if (!examinations.contains(exam)) {
            examinations.add(exam);
        }
    }
    
    public List<MedicalExamination> getExaminations() {
        return Collections.unmodifiableList(examinations);
    }
    
    public void setAssignedDoctor(Doctor doctor) {
        if (this.assignedDoctor != doctor) {
            if (this.assignedDoctor != null && this.assignedDoctor.getMedicalRecords().contains(this)) {
                this.assignedDoctor.removeMedicalRecord(this);
            }
            
            this.assignedDoctor = doctor;
            
            if (doctor != null && !doctor.getMedicalRecords().contains(this)) {
                doctor.addMedicalRecord(this);
            }
        }
    }
    
    public Doctor getAssignedDoctor() {
        return assignedDoctor;
    }

    public static List<MedicalRecord> getExtent() {
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
            extent = (List<MedicalRecord>) in.readObject();
        } catch (FileNotFoundException e) {
            extent = new ArrayList<>();
        }
    }

    public static void clearExtent() {
        extent.clear();
    }
}
