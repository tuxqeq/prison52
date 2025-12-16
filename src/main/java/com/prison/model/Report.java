package com.prison.model;

import com.prison.exception.*;
import java.io.*;
import java.time.LocalDate;

/**
 * Abstract base class for all reports in the prison system.
 */
public abstract class Report implements Serializable {
    private static final long serialVersionUID = 1L;
    protected LocalDate date;
    protected String description;
    protected java.util.List<Director> directors;  // Director[0..*] to Report[0..*] - many-to-many
    public Report(LocalDate date, String description) {
        setDate(date);
        setDescription(description);
        this.directors = new java.util.ArrayList<>();
    }
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        if (date == null) {
            throw new InvalidReferenceException("Date cannot be null.");
        }
        if (date.isAfter(LocalDate.now())) {
            throw new InvalidDateException("Report date cannot be in the future.");
        }
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new EmptyStringException("Description cannot be empty.");
        }
        this.description = description;
    }
    public abstract void manageReport();
    
    // Many-to-many: Director[0..*] to Report[0..*]
    public void addDirector(Director director) {
        if (director == null) {
            throw new InvalidReferenceException("Director cannot be null.");
        }
        if (!directors.contains(director)) {
            directors.add(director);
            if (!director.getSupervisedReports().contains(this)) {
                director.addSupervisedReport(this);
            }
        }
    }
    
    public void removeDirector(Director director) {
        if (directors.contains(director)) {
            directors.remove(director);
            if (director.getSupervisedReports().contains(this)) {
                director.removeSupervisedReport(this);
            }
        }
    }
    
    public java.util.List<Director> getDirectors() {
        return java.util.Collections.unmodifiableList(directors);
    }
    
    // Backward compatibility
    public void setSupervisingDirector(Director director) {
        if (director != null) {
            addDirector(director);
        }
    }
    
    public Director getSupervisingDirector() {
        return directors.isEmpty() ? null : directors.get(0);
    }
}
