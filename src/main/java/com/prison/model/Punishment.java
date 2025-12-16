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
    private List<Prisoner> prisoners;     // Punishment[0..*] to Prisoner[0..*] - many-to-many
    private List<Director> directors;     // Director[0..*] to Punishment[0..*] - many-to-many

    public Punishment(String type, String description, LocalDate startDate, int duration, String status) {
        setType(type);
        setDescription(description);
        setStartDate(startDate);
        setDuration(duration);
        setStatus(status);
        this.prisoners = new ArrayList<>();
        this.directors = new ArrayList<>();
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
    
    // Many-to-many: Punishment[0..*] to Prisoner[0..*]
    public void addPrisoner(Prisoner prisoner) {
        if (prisoner == null) {
            throw new InvalidReferenceException("Prisoner cannot be null.");
        }
        if (!prisoners.contains(prisoner)) {
            prisoners.add(prisoner);
            if (!prisoner.getPunishments().contains(this)) {
                prisoner.addPunishment(this);
            }
        }
    }
    
    public void removePrisoner(Prisoner prisoner) {
        if (prisoners.contains(prisoner)) {
            prisoners.remove(prisoner);
            if (prisoner.getPunishments().contains(this)) {
                prisoner.removePunishment(this);
            }
        }
    }
    
    public List<Prisoner> getPrisoners() {
        return Collections.unmodifiableList(prisoners);
    }
    
    // Many-to-many: Director[0..*] to Punishment[0..*]
    public void addDirector(Director director) {
        if (director == null) {
            throw new InvalidReferenceException("Director cannot be null.");
        }
        if (!directors.contains(director)) {
            directors.add(director);
            if (!director.getPunishments().contains(this)) {
                director.addPunishment(this);
            }
        }
    }
    
    public void removeDirector(Director director) {
        if (directors.contains(director)) {
            directors.remove(director);
            if (director.getPunishments().contains(this)) {
                director.removePunishment(this);
            }
        }
    }
    
    public List<Director> getDirectors() {
        return Collections.unmodifiableList(directors);
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
