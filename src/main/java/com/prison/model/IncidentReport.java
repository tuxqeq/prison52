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
    private List<Guard> reportingGuards;      // Guard[0..*] to IncidentReport[0..*] - many-to-many
    private Director reviewingDirector;       // Director reviewing
    private Punishment punishment;            // Punishment resulting from incident
    private IncidentReport relatedIncident;   // Reflex: IncidentReport[0..1] to IncidentReport[0..1]

    public IncidentReport(LocalDate date, String description, Status status) {
        super(date, description);  // Call Report constructor
        setStatus(status);
        this.severity = "";  // Initialize to empty string
        this.peopleInvolved = new ArrayList<>();  // Initialize required list
        this.relatedIncident = null;  // Initialize reflex association
        this.reportingGuards = new ArrayList<>();
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
    // Many-to-many: Guard[0..*] to IncidentReport[0..*]
    public void addReportingGuard(Guard guard) {
        if (guard == null) {
            throw new InvalidReferenceException("Guard cannot be null.");
        }
        if (!reportingGuards.contains(guard)) {
            reportingGuards.add(guard);
            if (!guard.getReportedIncidents().contains(this)) {
                guard.addReportedIncident(this);
            }
        }
    }
    
    public void removeReportingGuard(Guard guard) {
        if (reportingGuards.contains(guard)) {
            reportingGuards.remove(guard);
            if (guard.getReportedIncidents().contains(this)) {
                guard.removeReportedIncident(this);
            }
        }
    }
    
    public List<Guard> getReportingGuards() {
        return Collections.unmodifiableList(reportingGuards);
    }
    
    // Backward compatibility
    public Guard getReportingGuard() {
        return reportingGuards.isEmpty() ? null : reportingGuards.get(0);
    }
    
    public void setReportingGuard(Guard guard) {
        reportingGuards.clear();
        if (guard != null) {
            addReportingGuard(guard);
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
    
    /**
     * Sets related incident (Reflex Association)
     * IncidentReport[0..1] to IncidentReport[0..1]
     * Bidirectional - both reports reference each other
     */
    public void setRelatedIncident(IncidentReport incident) {
        if (incident == this) {
            throw new ValidationException("Incident report cannot be related to itself.");
        }
        
        if (this.relatedIncident != incident) {
            // Remove old relationship
            if (this.relatedIncident != null && this.relatedIncident.getRelatedIncident() == this) {
                IncidentReport oldRelated = this.relatedIncident;
                this.relatedIncident = null;
                oldRelated.setRelatedIncident(null);
            }
            
            this.relatedIncident = incident;
            
            // Create reverse connection
            if (incident != null && incident.getRelatedIncident() != this) {
                incident.setRelatedIncident(this);
            }
        }
    }
    
    /**
     * Gets related incident
     */
    public IncidentReport getRelatedIncident() {
        return relatedIncident;
    }
    
    /**
     * Removes the related incident relationship
     */
    public void removeRelatedIncident() {
        if (this.relatedIncident != null) {
            IncidentReport related = this.relatedIncident;
            this.relatedIncident = null;
            if (related.getRelatedIncident() == this) {
                related.relatedIncident = null;  // Direct access to avoid recursion
            }
        }
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
