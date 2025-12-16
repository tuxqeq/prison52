package com.prison.model;

import com.prison.exception.*;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MedicalExamination implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum ReasonForVisit {
        Routine, Injury, Complaint
    }

    private static List<MedicalExamination> extent = new ArrayList<>();
    private LocalDate dateOfExamination;
    private ReasonForVisit reasonForVisit;           // Reason for visit
    private List<String> prescription;       // [1..*] Prescription list
    private Doctor doctor;        // Examining doctor (Doctor ↔ MedicalExamination)
    private MedicalRecord medicalRecord;  // MedicalRecord[1] to MedicalExamination[0..*]

    public MedicalExamination(LocalDate dateOfExamination, ReasonForVisit reasonForVisit, 
                              Doctor doctor) {
        setDateOfExamination(dateOfExamination);
        setReasonForVisit(reasonForVisit);
        this.prescription = new ArrayList<>(); // Initialize the prescription list
        setDoctor(doctor);
        extent.add(this);
    }
    public LocalDate getDateOfExamination() { return dateOfExamination; }
    public void setDateOfExamination(LocalDate dateOfExamination) {
        if (dateOfExamination == null) {
            throw new InvalidReferenceException("Examination date cannot be null.");
        }
        if (dateOfExamination.isAfter(LocalDate.now())) {
            throw new InvalidDateException("Examination date cannot be in the future.");
        }
        this.dateOfExamination = dateOfExamination;
    }

    public ReasonForVisit getReasonForVisit() { return reasonForVisit; }
    public void setReasonForVisit(ReasonForVisit reasonForVisit) {
        if (reasonForVisit == null) {
            throw new InvalidReferenceException("Reason for visit cannot be null.");
        }
        this.reasonForVisit = reasonForVisit;
    }
    public List<String> getPrescription() {
        return Collections.unmodifiableList(prescription);
    }
    public void addPrescription(String item) {
        if (item == null || item.trim().isEmpty()) {
            throw new EmptyStringException("Prescription item cannot be empty.");
        }
        prescription.add(item);
    }
    public void removePrescription(String item) {
        prescription.remove(item);
    }
    public void conductExamination() {
        System.out.println("Conducting examination for: " + reasonForVisit);
    }
    public void set(Doctor doctor) {
        if (doctor == null) {
            throw new InvalidReferenceException("Doctor cannot be null.");
        }
        this.doctor = doctor;
        
        if (!doctor.getExaminations().contains(this)) {
            doctor.addExamination(this);
        }
    }
    
    public Doctor getDoctor() {
        return doctor;
    }
    
    public void setDoctor(Doctor doctor) {
        if (doctor == null) {
            throw new InvalidReferenceException("Doctor cannot be null.");
        }
        if (this.doctor != doctor) {
            if (this.doctor != null && this.doctor.getExaminations().contains(this)) {
                this.doctor.removeExamination(this);
            }
            
            this.doctor = doctor;
            
            if (!doctor.getExaminations().contains(this)) {
                doctor.addExamination(this);
            }
        }
    }
    
    public void setMedicalRecord(MedicalRecord record) {
        if (record == null) {
            throw new InvalidReferenceException("Medical record cannot be null.");
        }
        if (this.medicalRecord != record) {
            if (this.medicalRecord != null && this.medicalRecord.getExaminations().contains(this)) {
                this.medicalRecord.removeExamination(this);
            }
            
            this.medicalRecord = record;
            
            if (!record.getExaminations().contains(this)) {
                record.addExamination(this);
            }
        }
    }
    
    public MedicalRecord getMedicalRecord() {
        return medicalRecord;
    }

    public static List<MedicalExamination> getExtent() {
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
            extent = (List<MedicalExamination>) in.readObject();
        } catch (FileNotFoundException e) {
            extent = new ArrayList<>();
        }
    }

    public static void clearExtent() {
        extent.clear();
    }
}
