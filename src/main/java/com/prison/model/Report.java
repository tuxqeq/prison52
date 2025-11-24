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
    protected Director supervisingDirector;  // Director who supervises this report
    public Report(LocalDate date, String description) {
        setDate(date);
        setDescription(description);
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
    
    public void setSupervisingDirector(Director director) {
        if (this.supervisingDirector != director) {
            if (this.supervisingDirector != null && this.supervisingDirector.getSupervisedReports().contains(this)) {
                this.supervisingDirector.removeSupervisedReport(this);
            }
            
            this.supervisingDirector = director;
            
            if (director != null && !director.getSupervisedReports().contains(this)) {
                director.addSupervisedReport(this);
            }
        }
    }
    
    public Director getSupervisingDirector() {
        return supervisingDirector;
    }
}
