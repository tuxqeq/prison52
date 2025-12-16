package com.prison.model;

import com.prison.exception.*;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MedicalReport extends Report {
    private static final long serialVersionUID = 1L;

    private static List<MedicalReport> extent = new ArrayList<>();
    private String roomNumber;
    private double duration;           // Duration in minutes
    private String severityLevel;
    private Doctor doctor;           // Doctor who created the report
    private Guard guard;             // Guard[0..*] to MedicalReport[0..*]
    private MedicalRecord medicalRecord;  // COMPOSITION: MedicalReport[0..*] to MedicalRecord[1..1]

    public MedicalReport(LocalDate date, String description, String roomNumber, 
                         double duration, String severityLevel, Doctor doctor, MedicalRecord medicalRecord) {
        super(date, description);  // Call Report constructor
        setRoomNumber(roomNumber);
        setDuration(duration);
        setSeverityLevel(severityLevel);
        setDoctor(doctor);
        setMedicalRecord(medicalRecord);  // Required for composition
        extent.add(this);
    }
    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) {
        if (roomNumber == null || roomNumber.trim().isEmpty()) {
            throw new EmptyStringException("Room number cannot be empty.");
        }
        this.roomNumber = roomNumber;
    }

    public double getDuration() { return duration; }
    public void setDuration(double duration) {
        if (duration < 0) {
            throw new NegativeNumberException("Duration cannot be negative.");
        }
        this.duration = duration;
    }

    public String getSeverityLevel() { return severityLevel; }
    public void setSeverityLevel(String severityLevel) {
        if (severityLevel == null || severityLevel.trim().isEmpty()) {
            throw new EmptyStringException("Severity level cannot be empty.");
        }
        this.severityLevel = severityLevel;
    }
    @Override
    public void manageReport() {
        System.out.println("Managing medical report");
    }
    public void setDoctor(Doctor doctor) {
        if (doctor == null) {
            throw new InvalidReferenceException("Doctor cannot be null.");
        }
        this.doctor = doctor;
        if (!doctor.getMedicalReports().contains(this)) {
            doctor.addMedicalReport(this);
        }
    }

    public Doctor getDoctor() {
        return doctor;
    }
    
    public void set(Guard guard) {
        if (this.guard != guard) {
            if (this.guard != null && this.guard.getMedicalReports().contains(this)) {
                this.guard.removeMedicalReport(this);
            }
            
            this.guard = guard;
            
            if (guard != null && !guard.getMedicalReports().contains(this)) {
                guard.addMedicalReport(this);
            }
        }
    }
    
    public Guard getGuard() {
        return guard;
    }
    
    public void setGuard(Guard guard) {
        if (this.guard != guard) {
            if (this.guard != null && this.guard.getMedicalReports().contains(this)) {
                this.guard.removeMedicalReport(this);
            }
            
            this.guard = guard;
            
            if (guard != null && !guard.getMedicalReports().contains(this)) {
                guard.addMedicalReport(this);
            }
        }
    }
    
    /**
     * Sets medical record (COMPOSITION - report must belong to exactly one record)
     * MedicalReport[0..*] to MedicalRecord[1..1]
     */
    public void setMedicalRecord(MedicalRecord record) {
        if (record == null) {
            throw new InvalidReferenceException("Medical record cannot be null - composition requires parent.");
        }
        
        // Composition: cannot change record once set (part belongs to only one whole)
        if (this.medicalRecord != null && this.medicalRecord != record) {
            throw new ValidationException("Medical report already belongs to another record - composition violation.");
        }
        
        this.medicalRecord = record;
        
        if (!record.getMedicalReports().contains(this)) {
            record.addMedicalReport(this);
        }
    }
    
    /**
     * Gets medical record
     */
    public MedicalRecord getMedicalRecord() {
        return medicalRecord;
    }
    
    /**
     * Deletes this medical report (COMPOSITION)
     * Called when removing from record or when record is deleted
     */
    public void delete() {
        // Note: Do not remove from medicalRecord here - the parent MedicalRecord
        // handles clearing its list in its own delete() method
        
        // Remove from extent
        extent.remove(this);
        
        // Clean up other associations
        if (guard != null) {
            guard.removeMedicalReport(this);
        }
        if (doctor != null) {
            doctor.removeMedicalReport(this);
        }
    }

    public static List<MedicalReport> getExtent() {
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
            extent = (List<MedicalReport>) in.readObject();
        } catch (FileNotFoundException e) {
            extent = new ArrayList<>();
        }
    }

    public static void clearExtent() {
        extent.clear();
    }
}
