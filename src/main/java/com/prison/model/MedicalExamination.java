package com.prison.model;

import com.prison.exception.*;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MedicalExamination implements Serializable {
    private static final long serialVersionUID = 1L;

    private static List<MedicalExamination> extent = new ArrayList<>();
    private LocalDate examinationDate;
    private String diagnosis;
    private String reasonForVisit;           // Reason for visit
    private List<String> prescription;       // [1..*] Prescription list
    private Doctor doctor;        // Examining doctor (Doctor ↔ MedicalExamination)

    public MedicalExamination(LocalDate examinationDate, String diagnosis, String reasonForVisit, 
                              Doctor doctor) {
        setExaminationDate(examinationDate);
        setDiagnosis(diagnosis);
        setReasonForVisit(reasonForVisit);
        this.prescription = new ArrayList<>(); // Initialize the prescription list
        setDoctor(doctor);
        extent.add(this);
    }
    public LocalDate getExaminationDate() { return examinationDate; }
    public void setExaminationDate(LocalDate examinationDate) {
        if (examinationDate == null) {
            throw new InvalidReferenceException("Examination date cannot be null.");
        }
        if (examinationDate.isAfter(LocalDate.now())) {
            throw new InvalidDateException("Examination date cannot be in the future.");
        }
        this.examinationDate = examinationDate;
    }

    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) {
        if (diagnosis == null || diagnosis.trim().isEmpty()) {
            throw new EmptyStringException("Diagnosis cannot be empty.");
        }
        this.diagnosis = diagnosis;
    }

    public String getReasonForVisit() { return reasonForVisit; }
    public void setReasonForVisit(String reasonForVisit) {
        if (reasonForVisit == null || reasonForVisit.trim().isEmpty()) {
            throw new EmptyStringException("Reason for visit cannot be empty.");
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
        System.out.println("Conducting examination");
    }

    public void recordFindings(String findings) {
        setDiagnosis(findings);
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
