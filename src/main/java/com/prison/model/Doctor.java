package com.prison.model;

import com.prison.exception.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Doctor extends Staff {
    private static final long serialVersionUID = 1L;
    // Subclasses often have their own extent if we want to query just Doctors.
    private static List<Doctor> extent = new ArrayList<>();
    private List<String> specializations;
    private String licenseNumber;
    private String contactInfo;  // Contact information for the doctor
    private List<MedicalExamination> examinations;   // Examinations performed
    private List<MedicalRecord> medicalRecords;      // Medical records managed by this doctor
    private List<MedicalReport> medicalReports;      // Medical reports created by this doctor
    public Doctor(String name, String surname, int experienceYears, 
                  String shiftHour, String phone, String email, 
                  String licenseNumber, String contactInfo) {
        super(name, surname, experienceYears, shiftHour, phone, email);
        setLicenseNumber(licenseNumber);
        setContactInfo(contactInfo);
        this.specializations = new ArrayList<>();
        this.examinations = new ArrayList<>();
        this.medicalRecords = new ArrayList<>();
        this.medicalReports = new ArrayList<>();
        
        extent.add(this);
    }
    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) {
        if (licenseNumber == null || licenseNumber.trim().isEmpty()) {
            throw new EmptyStringException("License number cannot be empty.");
        }
        this.licenseNumber = licenseNumber;
    }

    public List<String> getSpecializations() {
        return Collections.unmodifiableList(specializations);
    }
    public void addSpecialization(String specialization) {
        if (specialization == null || specialization.trim().isEmpty()) {
            throw new EmptyStringException("Specialization cannot be empty.");
        }
        specializations.add(specialization);
    }

    public String getContactInfo() { return contactInfo; }
    public void setContactInfo(String contactInfo) {
        if (contactInfo == null || contactInfo.trim().isEmpty()) {
            throw new EmptyStringException("Contact info cannot be empty.");
        }
        this.contactInfo = contactInfo;
    }

    public void addExamination(MedicalExamination exam) {
        if (exam == null) {
            throw new InvalidReferenceException("Medical examination cannot be null.");
        }
        if (!examinations.contains(exam)) {
            examinations.add(exam);
            if (exam.getDoctor() != this) {
                exam.setDoctor(this);
            }
        }
    }
    
    public void removeExamination(MedicalExamination exam) {
        if (exam != null && examinations.contains(exam)) {
            examinations.remove(exam);
        }
    }
    
    public List<MedicalExamination> getExaminations() {
        return Collections.unmodifiableList(examinations);
    }
    public void addMedicalRecord(MedicalRecord record) {
        if (record == null) {
            throw new InvalidReferenceException("Medical record cannot be null.");
        }
        if (!medicalRecords.contains(record)) {
            medicalRecords.add(record);
            if (record.getAssignedDoctor() != this) {
                record.setAssignedDoctor(this);
            }
        }
    }
    
    public void removeMedicalRecord(MedicalRecord record) {
        if (record != null && medicalRecords.contains(record)) {
            medicalRecords.remove(record);
            if (record.getAssignedDoctor() == this) {
                record.setAssignedDoctor(null);
            }
        }
    }
    
    public List<MedicalRecord> getMedicalRecords() {
        return Collections.unmodifiableList(medicalRecords);
    }
    public void addMedicalReport(MedicalReport report) {
        if (report == null) {
            throw new InvalidReferenceException("Medical report cannot be null.");
        }
        if (!medicalReports.contains(report)) {
            medicalReports.add(report);
            if (report.getDoctor() != this) {
                report.setDoctor(this);
            }
        }
    }
    
    public void removeMedicalReport(MedicalReport report) {
        if (report != null && medicalReports.contains(report)) {
            medicalReports.remove(report);
        }
    }
    
    public List<MedicalReport> getMedicalReports() {
        return Collections.unmodifiableList(medicalReports);
    }
    public static List<Doctor> getDoctorExtent() {
        return Collections.unmodifiableList(extent);
    }
    
    // Note: Saving/Loading specific Doctor extent might be redundant if we save Staff extent, 
    // but for the assignment, we might want to demonstrate it or just rely on Staff persistence if we want polymorphic retrieval.
    // However, usually extent persistence is per class. 
    // If we save Staff extent, it will contain Doctors. 
    // If we save Doctor extent separately, we duplicate data if we are not careful.
    
    public static void saveDoctorExtent(String filename) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(extent);
        }
    }

    @SuppressWarnings("unchecked")
    public static void loadDoctorExtent(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            extent = (List<Doctor>) in.readObject();
        } catch (FileNotFoundException e) {
            extent = new ArrayList<>();
        }
    }
    
    public static void clearDoctorExtent() {
        extent.clear();
    }
}
