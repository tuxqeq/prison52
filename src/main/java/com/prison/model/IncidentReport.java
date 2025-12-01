package com.prison.model;

import com.prison.exception.*;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IncidentReport extends Report {
    private static final long serialVersionUID = 1L;

    public enum Severity {
        LOW, MEDIUM, HIGH, CRITICAL
    }

    public enum Status {
        OPEN, INREVIEW, RESOLVED
    }

    private static List<IncidentReport> extent = new ArrayList<>();
    private String severity;                  // Severity description
    private Status status;                    // Status of the incident
    private List<String> peopleInvolved;      // [1..*] People involved in incident
    private Guard reportingGuard;             // Guard who reported
    private Director reviewingDirector;       // Director reviewing
    private Punishment punishment;            // Punishment resulting from incident

    public IncidentReport(LocalDate date, String description, Status status) {
        super(date, description);  // Call Report constructor
        setStatus(status);
        this.severity = "";  // Initialize to empty string
        this.peopleInvolved = new ArrayList<>();  // Initialize required list
        extent.add(this);
    }
    public Status getStatus() { return status; }
    public void setStatus(Status status) {
        if (status == null) {
            throw new InvalidReferenceException("Status cannot be null.");
        }
        this.status = status;
    }
    public String getSeverity() { return severity; }
    public void setSeverity(String severity) {
        if (severity == null) {
            throw new InvalidReferenceException("Severity cannot be null.");
        }
        this.severity = severity;
    }
    public List<String> getPeopleInvolved() {
        return Collections.unmodifiableList(peopleInvolved);
    }
    public void addPersonInvolved(String person) {
        if (person == null || person.trim().isEmpty()) {
            throw new EmptyStringException("Person cannot be empty.");
        }
        peopleInvolved.add(person);
    }
    public void removePersonInvolved(String person) {
        peopleInvolved.remove(person);
    }
    @Override
    public void manageReport() {
        System.out.println("Managing incident report - Status: " + status);
    }

    public String getDescription() { return description; }
    public void setDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new EmptyStringException("Description cannot be empty.");
        }
        this.description = description;
    }
    public void set(Guard guard) {
        if (guard == null) {
            throw new InvalidReferenceException("Reporting guard cannot be null.");
        }
        // Remove from old guard if exists
        if (this.reportingGuard != null && this.reportingGuard != guard) {
            this.reportingGuard.removeIncidentReport(this);
        }
        this.reportingGuard = guard;
        
        if (!guard.getReportedIncidents().contains(this)) {
            guard.addIncidentReport(this);
        }
    }
    
    public Guard getReportingGuard() {
        return reportingGuard;
    }
    
    public void setReportingGuard(Guard guard) {
        if (this.reportingGuard != guard) {
            if (this.reportingGuard != null && this.reportingGuard.getReportedIncidents().contains(this)) {
                this.reportingGuard.removeReportedIncident(this);
            }
            
            this.reportingGuard = guard;
            
            if (guard != null && !guard.getReportedIncidents().contains(this)) {
                guard.addReportedIncident(this);
            }
        }
    }

    public void removeReportingGuard() {
        if (this.reportingGuard != null) {
            Guard oldGuard = this.reportingGuard;
            this.reportingGuard = null;
            oldGuard.removeReportedIncident(this);
        }
    }

    public void setReviewingDirector(Director director) {
        if (this.reviewingDirector != director) {
            if (this.reviewingDirector != null && this.reviewingDirector.getReviewedIncidentReports().contains(this)) {
                this.reviewingDirector.removeReviewedIncidentReport(this);
            }
            
            this.reviewingDirector = director;
            
            if (director != null && !director.getReviewedIncidentReports().contains(this)) {
                director.addReviewedIncidentReport(this);
            }
        }
    }
    
    public Director getReviewingDirector() {
        return reviewingDirector;
    }
    
    public void setPunishment(Punishment punishment) {
        this.punishment = punishment;
        if (punishment != null && punishment.getIncident() != this) {
            punishment.setIncident(this);
        }
    }
    
    public Punishment getPunishment() {
        return punishment;
    }

    public static List<IncidentReport> getExtent() {
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
            extent = (List<IncidentReport>) in.readObject();
        } catch (FileNotFoundException e) {
            extent = new ArrayList<>();
        }
    }

    public static void clearExtent() {
        extent.clear();
    }
}
