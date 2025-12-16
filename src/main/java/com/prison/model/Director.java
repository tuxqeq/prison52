package com.prison.model;

import com.prison.exception.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Director extends Staff {
    private static final long serialVersionUID = 1L;

    public enum DirectorRank {
        ASSISTANT, REGIONAL, GENERAL
    }

    private static List<Director> extent = new ArrayList<>();

    private DirectorRank rank;
    private Block assignedBlock;  // Single block assigned to director (from diagram)
    private List<Punishment> approvedPunishments;         // Punishments approved
    private List<IncidentReport> reviewedIncidentReports; // Incident reports reviewed
    private List<Visit> approvedVisits;                   // Visits approved/rejected
    private List<Assignment> assignments;                 // Assignments managed
    private List<Report> supervisedReports;               // Reports supervised (abstract)

    public Director(String name, String surname, int experienceYears, 
                    String shiftHour, String phone, String email, DirectorRank rank) {
        super(name, surname, experienceYears, shiftHour, phone, email);
        setRank(rank);
        this.approvedPunishments = new ArrayList<>();
        this.reviewedIncidentReports = new ArrayList<>();
        this.approvedVisits = new ArrayList<>();
        this.assignments = new ArrayList<>();
        this.supervisedReports = new ArrayList<>();
        extent.add(this);
    }

    public DirectorRank getRank() { return rank; }
    public void setRank(DirectorRank rank) {
        if (rank == null) {
            throw new InvalidReferenceException("Rank cannot be null.");
        }
        this.rank = rank;
    }
    
    public Block getAssignedBlock() { return assignedBlock; }
    public void setAssignedBlock(Block block) {
        this.assignedBlock = block;  
    }
    // Many-to-many: Director[0..*] to Punishment[0..*]
    public void addPunishment(Punishment punishment) {
        if (punishment == null) {
            throw new InvalidReferenceException("Punishment cannot be null.");
        }
        if (!approvedPunishments.contains(punishment)) {
            approvedPunishments.add(punishment);
            if (!punishment.getDirectors().contains(this)) {
                punishment.addDirector(this);
            }
        }
    }
    
    public void removePunishment(Punishment punishment) {
        if (punishment != null && approvedPunishments.contains(punishment)) {
            approvedPunishments.remove(punishment);
            if (punishment.getDirectors().contains(this)) {
                punishment.removeDirector(this);
            }
        }
    }
    
    public List<Punishment> getPunishments() {
        return Collections.unmodifiableList(approvedPunishments);
    }
    
    // Keep old names for backward compatibility
    public void addApprovedPunishment(Punishment punishment) {
        addPunishment(punishment);
    }
    
    public void removeApprovedPunishment(Punishment punishment) {
        removePunishment(punishment);
    }
    
    public List<Punishment> getApprovedPunishments() {
        return getPunishments();
    }
    public void addReviewedIncidentReport(IncidentReport report) {
        if (report == null) {
            throw new InvalidReferenceException("Incident report cannot be null.");
        }
        if (!reviewedIncidentReports.contains(report)) {
            reviewedIncidentReports.add(report);
            if (report.getReviewingDirector() != this) {
                report.setReviewingDirector(this);
            }
        }
    }
    
    public void removeReviewedIncidentReport(IncidentReport report) {
        if (report != null && reviewedIncidentReports.contains(report)) {
            reviewedIncidentReports.remove(report);
            if (report.getReviewingDirector() == this) {
                report.setReviewingDirector(null);
            }
        }
    }
    
    public List<IncidentReport> getReviewedIncidentReports() {
        return Collections.unmodifiableList(reviewedIncidentReports);
    }
    public void addApprovedVisit(Visit visit) {
        if (visit == null) {
            throw new InvalidReferenceException("Visit cannot be null.");
        }
        if (!approvedVisits.contains(visit)) {
            approvedVisits.add(visit);
            if (visit.getDirector() != this) {
                visit.setDirector(this);
            }
        }
    }
    
    public void removeApprovedVisit(Visit visit) {
        if (visit != null && approvedVisits.contains(visit)) {
            approvedVisits.remove(visit);
            if (visit.getDirector() == this) {
                visit.setDirector(null);
            }
        }
    }
    
    public List<Visit> getApprovedVisits() {
        return Collections.unmodifiableList(approvedVisits);
    }
    public void addAssignment(Assignment assignment) {
        if (assignment == null) {
            throw new InvalidReferenceException("Assignment cannot be null.");
        }
        if (!assignments.contains(assignment)) {
            assignments.add(assignment);
            if (assignment.getDirector() != this) {
                assignment.setDirector(this);
            }
        }
    }
    
    public void removeAssignment(Assignment assignment) {
        if (assignment != null && assignments.contains(assignment)) {
            assignments.remove(assignment);
            if (assignment.getDirector() == this) {
                assignment.setDirector(null);
            }
        }
    }
    
    public List<Assignment> getAssignments() {
        return Collections.unmodifiableList(assignments);
    }
    public void addSupervisedReport(Report report) {
        if (report == null) {
            throw new InvalidReferenceException("Report cannot be null.");
        }
        if (!supervisedReports.contains(report)) {
            supervisedReports.add(report);
            if (report.getSupervisingDirector() != this) {
                report.setSupervisingDirector(this);
            }
        }
    }
    
    public void removeSupervisedReport(Report report) {
        if (report != null && supervisedReports.contains(report)) {
            supervisedReports.remove(report);
            if (report.getSupervisingDirector() == this) {
                report.setSupervisingDirector(null);
            }
        }
    }
    
    public List<Report> getSupervisedReports() {
        return Collections.unmodifiableList(supervisedReports);
    }

    public static List<Director> getDirectorExtent() {
        return Collections.unmodifiableList(extent);
    }

    public static void saveDirectorExtent(String filename) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(extent);
        }
    }

    @SuppressWarnings("unchecked")
    public static void loadDirectorExtent(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            extent = (List<Director>) in.readObject();
        } catch (FileNotFoundException e) {
            extent = new ArrayList<>();
        }
    }

    public static void clearDirectorExtent() {
        extent.clear();
    }
}
