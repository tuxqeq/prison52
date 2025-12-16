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

    private LocalDate date;
    private int duration;          // Duration in minutes
    private VisitType type;
    private ApprovalStatus approvalStatus;
    private Visitor visitor;       // Visit[0..*] to Visitor (Qualified Association)
    private Director director;     // Director[0..*] to Visit[0..*]
    private Prisoner prisoner;     // Prisoner[1] to Visit[0..*] {ordered}

    public Visit(LocalDate date, int duration, VisitType type, Visitor visitor, Prisoner prisoner) {
        setDate(date);
        setDuration(duration);
        setType(type);
        this.approvalStatus = ApprovalStatus.PENDING;
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
    
    public void setVisitor(Visitor visitor) {
        if (visitor == null) {
            throw new InvalidReferenceException("Visitor cannot be null.");
        }
        this.visitor = visitor;
        
        // Qualified association - visitor manages visits by date in a dictionary
        if (!visitor.getVisitsByDate().containsValue(this)) {
            visitor.addVisitByDate(this.date, this);
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
    
    public void setDirector(Director director) {
        if (this.director != director) {
            if (this.director != null && this.director.getApprovedVisits().contains(this)) {
                this.director.removeApprovedVisit(this);
            }
            
            this.director = director;
            
            if (director != null && !director.getApprovedVisits().contains(this)) {
                director.addApprovedVisit(this);
            }
        }
    }
    
    public Director getDirector() {
        return director;
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
