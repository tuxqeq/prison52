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

    private LocalDate dateOfCreation;
    private List<String> history;              // [1..*] Medical history
    private String descriptionOfDiagnosis;
    private Prisoner prisoner;                       // Owner of this record
    private List<MedicalExamination> examinations;   // MedicalRecord[1] to MedicalExamination[0..*]
    private Doctor assignedDoctor;                   // Doctor[0..*] to MedicalRecord[1]
    private List<MedicalReport> medicalReports;      // COMPOSITION: MedicalReport[0..*] to MedicalRecord[1..1]

    public MedicalRecord(LocalDate dateOfCreation, String descriptionOfDiagnosis) {
        setDateOfCreation(dateOfCreation);
        setDescriptionOfDiagnosis(descriptionOfDiagnosis);
        this.history = new ArrayList<>();  // Initialize required list
        this.examinations = new ArrayList<>();
        this.medicalReports = new ArrayList<>();  // Composition - reports owned by this record
        extent.add(this);
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
        System.out.println("Diagnosis: " + descriptionOfDiagnosis);
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
            if (exam.getMedicalRecord() != this) {
                exam.setMedicalRecord(this);
            }
        }
    }
    
    public void removeExamination(MedicalExamination exam) {
        if (exam != null && examinations.contains(exam)) {
            examinations.remove(exam);
            if (exam.getMedicalRecord() == this) {
                exam.setMedicalRecord(null);
            }
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
    
    /**
     * Adds a medical report (COMPOSITION)
     * MedicalReport[0..*] to MedicalRecord[1..1]
     * Reports cannot exist without a record and cannot be shared
     */
    public void addMedicalReport(MedicalReport report) {
        if (report == null) {
            throw new InvalidReferenceException("Medical report cannot be null.");
        }
        
        // Composition constraint: report cannot belong to another record
        if (report.getMedicalRecord() != null && report.getMedicalRecord() != this) {
            throw new ValidationException("Medical report already belongs to another record - composition violation.");
        }
        
        if (!medicalReports.contains(report)) {
            medicalReports.add(report);
            if (report.getMedicalRecord() != this) {
                report.setMedicalRecord(this);
            }
        }
    }
    
    /**
     * Removes a medical report (COMPOSITION - also deletes the report)
     */
    public void removeMedicalReport(MedicalReport report) {
        if (report != null && medicalReports.contains(report)) {
            medicalReports.remove(report);
            // Composition: delete the part when removed from whole
            report.delete();
        }
    }
    
    /**
     * Gets all medical reports
     */
    public List<MedicalReport> getMedicalReports() {
        return Collections.unmodifiableList(medicalReports);
    }
    
    /**
     * Deletes this medical record (COMPOSITION - cascades to all reports)
     */
    public void delete() {
        // Composition: when whole is deleted, all parts must be deleted
        List<MedicalReport> reportsCopy = new ArrayList<>(medicalReports);
        for (MedicalReport report : reportsCopy) {
            report.delete();  // Delete each report
        }
        medicalReports.clear();
        
        // Remove from extent
        extent.remove(this);
        
        // Remove other associations
        if (assignedDoctor != null) {
            assignedDoctor.removeMedicalRecord(this);
        }
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
