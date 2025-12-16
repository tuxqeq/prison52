package com.prison.model;

import com.prison.exception.*;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Visit implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum ApprovalStatus {
        PENDING, APPROVED, REJECTED, COMPLETED
    }

    public enum VisitType {
        LAWYER, FAMILY, GENERAL
    }

    private static List<Visit> extent = new ArrayList<>();

    private String visitorID;      // Qualifier for qualified association
    private LocalDate date;
    private int duration;          // Duration in minutes
    private VisitType type;
    private ApprovalStatus approvalStatus;
    private Visitor visitor;       // Visit[0..*] to Visitor (Qualified Association by visitorID)
    private List<Director> directors;     // Director[0..*] to Visit[0..*] - many-to-many
    private Prisoner prisoner;     // Prisoner[1] to Visit[0..*] {ordered}

    public Visit(LocalDate date, int duration, VisitType type, String visitorID, Visitor visitor, Prisoner prisoner) {
        setDate(date);
        setDuration(duration);
        setType(type);
        setVisitorID(visitorID);
        this.approvalStatus = ApprovalStatus.PENDING;
        this.directors = new ArrayList<>();
        setVisitor(visitor);
        setPrisoner(prisoner);
        extent.add(this);
    }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) {
        if (date == null) {
            throw new InvalidReferenceException("Date cannot be null.");
        }
        if (date.isBefore(LocalDate.now())) {
            throw new InvalidDateException("Visit date cannot be in the past.");
        }
        this.date = date;
    }

    public int getDuration() { return duration; }
    public void setDuration(int duration) {
        if (duration <= 0) {
            throw new NegativeNumberException("Duration must be positive.");
        }
        this.duration = duration;
    }

    public VisitType getType() { return type; }
    public void setType(VisitType type) {
        if (type == null) {
            throw new InvalidReferenceException("Visit type cannot be null.");
        }
        this.type = type;
    }

    public ApprovalStatus getApprovalStatus() { return approvalStatus; }
    public void setApprovalStatus(ApprovalStatus approvalStatus) {
        if (approvalStatus == null) {
            throw new InvalidReferenceException("Approval status cannot be null.");
        }
        this.approvalStatus = approvalStatus;
    }
    
    public String getVisitorID() { return visitorID; }
    public void setVisitorID(String visitorID) {
        if (visitorID == null || visitorID.trim().isEmpty()) {
            throw new EmptyStringException("Visitor ID cannot be empty.");
        }
        this.visitorID = visitorID;
    }
    
    public void setVisitor(Visitor visitor) {
        if (visitor == null) {
            throw new InvalidReferenceException("Visitor cannot be null.");
        }
        this.visitor = visitor;
        
        // Qualified association - visitor manages visits by visitorID in a dictionary
        if (!visitor.getVisitsByVisitorID().containsValue(this)) {
            visitor.addVisitByVisitorID(this.visitorID, this);
        }
    }
    
    public Visitor getVisitor() {
        return visitor;
    }
    
    /**
     * Sets prisoner (ordered association)
     * Multiplicity: Prisoner[1] to Visit[0..*] {ordered}
     */
    public void setPrisoner(Prisoner prisoner) {
        if (prisoner == null) {
            throw new InvalidReferenceException("Prisoner cannot be null - visit must have a prisoner.");
        }
        
        if (this.prisoner != prisoner) {
            if (this.prisoner != null && this.prisoner.getVisits().contains(this)) {
                this.prisoner.removeVisit(this);
            }
            
            this.prisoner = prisoner;
            
            if (!prisoner.getVisits().contains(this)) {
                prisoner.addVisit(this);
            }
        }
    }
    
    public Prisoner getPrisoner() {
        return prisoner;
    }
    
    public void addDirector(Director director) {
        if (director == null) {
            throw new InvalidReferenceException("Director cannot be null.");
        }
        if (!directors.contains(director)) {
            directors.add(director);
            if (!director.getApprovedVisits().contains(this)) {
                director.addApprovedVisit(this);
            }
        }
    }
    
    public void removeDirector(Director director) {
        if (director != null && directors.contains(director)) {
            directors.remove(director);
            if (director.getApprovedVisits().contains(this)) {
                director.removeApprovedVisit(this);
            }
        }
    }
    
    public List<Director> getDirectors() {
        return Collections.unmodifiableList(directors);
    }
    
    // Backward compatibility methods
    public void setDirector(Director director) {
        directors.clear();
        if (director != null) {
            addDirector(director);
        }
    }
    
    public Director getDirector() {
        return directors.isEmpty() ? null : directors.get(0);
    }

    public static List<Visit> getExtent() {
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
            extent = (List<Visit>) in.readObject();
        } catch (FileNotFoundException e) {
            extent = new ArrayList<>();
        }
    }

    public static void clearExtent() {
        extent.clear();
    }
}
