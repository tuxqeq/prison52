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
    private int duration;           // Duration in minutes
    private String severityLevel;
    private Doctor doctor;           // Doctor who created the report
    private Guard guard;             // Guard involved in medical report workflow

    public MedicalReport(LocalDate date, String description, String roomNumber, 
                         int duration, String severityLevel, Doctor doctor) {
        super(date, description);  // Call Report constructor
        setRoomNumber(roomNumber);
        setDuration(duration);
        setSeverityLevel(severityLevel);
        setDoctor(doctor);
        extent.add(this);
    }
    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) {
        if (roomNumber == null || roomNumber.trim().isEmpty()) {
            throw new EmptyStringException("Room number cannot be empty.");
        }
        this.roomNumber = roomNumber;
    }

    public int getDuration() { return duration; }
    public void setDuration(int duration) {
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
