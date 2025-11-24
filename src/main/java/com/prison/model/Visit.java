package com.prison.model;

import com.prison.exception.*;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Visit implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum ApprovalStatus {
        PENDING, APPROVED, REJECTED, COMPLETED
    }

    private static List<Visit> extent = new ArrayList<>();

    private LocalDate date;
    private LocalTime time;
    private ApprovalStatus approvalStatus;
    private String rejectionReason;
    private Visitor visitor;       // Visitor (per authoritative table)
    private Director director;     // Director who approves/rejects visit

    public Visit(LocalDate date, LocalTime time, Visitor visitor) {
        setDate(date);
        setTime(time);
        this.approvalStatus = ApprovalStatus.PENDING;
        setVisitor(visitor);
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

    public LocalTime getTime() { return time; }
    public void setTime(LocalTime time) {
        if (time == null) {
            throw new InvalidReferenceException("Time cannot be null.");
        }
        this.time = time;
    }

    public ApprovalStatus getApprovalStatus() { return approvalStatus; }
    public void setApprovalStatus(ApprovalStatus approvalStatus) {
        if (approvalStatus == null) {
            throw new InvalidReferenceException("Approval status cannot be null.");
        }
        this.approvalStatus = approvalStatus;
    }

    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) {
        // Only allow setting rejection reason if status is REJECTED
        if (this.approvalStatus == ApprovalStatus.REJECTED && (rejectionReason == null || rejectionReason.trim().isEmpty())) {
             throw new EmptyStringException("Rejection reason is required when status is REJECTED.");
        }
        this.rejectionReason = rejectionReason;
    }
    
    public void setVisitor(Visitor visitor) {
        if (visitor == null) {
            throw new InvalidReferenceException("Visitor cannot be null.");
        }
        this.visitor = visitor;
        
        if (!visitor.getVisits().contains(this)) {
            visitor.addVisit(this);
        }
    }
    
    public Visitor getVisitor() {
        return visitor;
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
