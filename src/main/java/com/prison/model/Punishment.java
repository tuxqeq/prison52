package com.prison.model;

import com.prison.exception.*;
import java.io.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Punishment implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum PunishmentType {
        SOLITARY_CONFINEMENT, LOSS_OF_PRIVILEGES, EXTRA_DUTY, WARNING
    }

    private static List<Punishment> extent = new ArrayList<>();
    private String type;
    private String description;           // Description of punishment
    private LocalDate startDate;          // Start date
    private int duration;                 // Duration in days
    private String status;                // Status (Active, Completed, etc.)
    private IncidentReport incident;   // Incident that caused this punishment
    private Prisoner prisoner;         // Prisoner being punished
    private Director approvingDirector; // Director who approved the punishment

    public Punishment(String type, String description, LocalDate startDate, int duration, String status) {
        setType(type);
        setDescription(description);
        setStartDate(startDate);
        setDuration(duration);
        setStatus(status);
        extent.add(this);
    }
    public String getType() { return type; }
    public void setType(String type) {
        if (type == null || type.trim().isEmpty()) {
            throw new EmptyStringException("Punishment type cannot be empty.");
        }
        this.type = type;
    }

    public String getDescription() { return description; }
    public void setDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new EmptyStringException("Description cannot be empty.");
        }
        this.description = description;
    }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) {
        if (startDate == null) {
            throw new InvalidReferenceException("Start date cannot be null.");
        }
        this.startDate = startDate;
    }

    public int getDuration() { return duration; }
    public void setDuration(int duration) {
        if (duration < 0) {
            throw new NegativeNumberException("Duration cannot be negative.");
        }
        this.duration = duration;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new EmptyStringException("Status cannot be empty.");
        }
        this.status = status;
    }
    /**
     * Calculates remaining days of punishment
     */
    public int getRemainingTime() {
        if (startDate == null) return duration;
        long daysElapsed = ChronoUnit.DAYS.between(startDate, LocalDate.now());
        int remaining = duration - (int)daysElapsed;
        return Math.max(0, remaining);  
    }
    
    public void applyPunishment() {
        System.out.println("Applying punishment: " + type + " for " + duration + " days");
    }
    
    public void setIncident(IncidentReport incident) {
        if (incident == null) {
            throw new InvalidReferenceException("Incident cannot be null - every punishment has a cause.");
        }
        this.incident = incident;
        
        if (incident.getPunishment() != this) {
            incident.setPunishment(this);
        }
    }
    
    public IncidentReport getIncident() {
        return incident;
    }
    
    public void set(Prisoner prisoner) {
        if (prisoner == null) {
            throw new InvalidReferenceException("Prisoner cannot be null.");
        }
        this.prisoner = prisoner;
        
        if (!prisoner.getPunishments().contains(this)) {
            prisoner.addPunishment(this);
        }
    }
    
    public Prisoner getPrisoner() {
        return prisoner;
    }
    
    public void setPrisoner(Prisoner prisoner) {
        this.prisoner = prisoner;
    }
    
    public void setApprovingDirector(Director director) {
        if (this.approvingDirector != director) {
            if (this.approvingDirector != null && this.approvingDirector.getApprovedPunishments().contains(this)) {
                this.approvingDirector.removeApprovedPunishment(this);
            }
            
            this.approvingDirector = director;
            
            if (director != null && !director.getApprovedPunishments().contains(this)) {
                director.addApprovedPunishment(this);
            }
        }
    }
    
    public Director getApprovingDirector() {
        return approvingDirector;
    }

    public static List<Punishment> getExtent() {
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
            extent = (List<Punishment>) in.readObject();
        } catch (FileNotFoundException e) {
            extent = new ArrayList<>();
        }
    }

    public static void clearExtent() {
        extent.clear();
    }
}
