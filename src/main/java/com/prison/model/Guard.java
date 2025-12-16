package com.prison.model;

import com.prison.exception.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Guard extends Staff {
    private static final long serialVersionUID = 1L;

    public enum Rank {
        JUNIOR, SENIOR, CHIEF, LIEUTENANT
    }

    private static List<Guard> extent = new ArrayList<>();
    private Rank rank;
    private String weapon;  // Weapon assigned to guard
    private List<IncidentReport> reportedIncidents;  // Guard[0..*] to IncidentReport[0..*] - many-to-many
    private List<Guard> subordinates;          // Guard[0..*] to Guard[0..*] (Reflex) - many-to-many
    private List<Guard> supervisors;           // Guard[0..*] to Guard[0..*] (Reflex) - many-to-many
    private List<Meal> supervisedMeals;        // Guard[0..*] to Meal[0..*] - many-to-many
    private List<MedicalReport> medicalReports; // Guard[0..*] to MedicalReport[0..*] - many-to-many

    public Guard(String name, String surname, int experienceYears, 
                 String shiftHour, String phone, String email, Rank rank, String weapon) {
        super(name, surname, experienceYears, shiftHour, phone, email);
        setRank(rank);
        setWeapon(weapon);
        this.reportedIncidents = new ArrayList<>();
        this.subordinates = new ArrayList<>();
        this.supervisors = new ArrayList<>();
        this.supervisedMeals = new ArrayList<>();
        this.medicalReports = new ArrayList<>();
        extent.add(this);
    }

    public Rank getRank() { return rank; }
    public void setRank(Rank rank) {
        if (rank == null) {
            throw new InvalidReferenceException("Rank cannot be null.");
        }
        this.rank = rank;
    }

    public String getWeapon() { return weapon; }
    public void setWeapon(String weapon) {
        this.weapon = weapon;  // Weapon can be null (unarmed guard)
    }
    
    public void addReportedIncident(IncidentReport incident) {
        if (incident == null) {
            throw new InvalidReferenceException("Incident cannot be null.");
        }
        if (!reportedIncidents.contains(incident)) {
            reportedIncidents.add(incident);
            if (incident.getReportingGuard() != this) {
                incident.setReportingGuard(this);
            }
        }
    }
    
    public void removeReportedIncident(IncidentReport incident) {
        if (incident != null && reportedIncidents.contains(incident)) {
            reportedIncidents.remove(incident);
            if (incident.getReportingGuard() == this) {
                incident.setReportingGuard(null);
            }
        }
    }
    
    public List<IncidentReport> getReportedIncidents() {
        return Collections.unmodifiableList(reportedIncidents);
    }
    
    
    public void addIncidentReport(IncidentReport incident) {
        addReportedIncident(incident);
    }
    
    public void removeIncidentReport(IncidentReport incident) {
        removeReportedIncident(incident);
    }
    // Many-to-many: Guard[0..*] to Guard[0..*] (Supervisors)
    public void addSupervisor(Guard supervisor) {
        if (supervisor == null) {
            throw new InvalidReferenceException("Supervisor cannot be null.");
        }
        if (supervisor == this) {
            throw new ValidationException("Guard cannot be their own supervisor.");
        }
        if (!supervisors.contains(supervisor)) {
            supervisors.add(supervisor);
            if (!supervisor.getSubordinates().contains(this)) {
                supervisor.addSubordinate(this);
            }
        }
    }
    
    public void removeSupervisor(Guard supervisor) {
        if (supervisors.contains(supervisor)) {
            supervisors.remove(supervisor);
            if (supervisor.getSubordinates().contains(this)) {
                supervisor.removeSubordinate(this);
            }
        }
    }
    
    public List<Guard> getSupervisors() {
        return Collections.unmodifiableList(supervisors);
    }
    
    // Backward compatibility
    public void setSupervisor(Guard supervisor) {
        supervisors.clear();
        if (supervisor != null) {
            addSupervisor(supervisor);
        }
    }
    
    public Guard getSupervisor() {
        return supervisors.isEmpty() ? null : supervisors.get(0);
    }
    
    // Many-to-many: Guard[0..*] to Guard[0..*] (Subordinates)
    public void addSubordinate(Guard subordinate) {
        if (subordinate == null) {
            throw new InvalidReferenceException("Subordinate cannot be null.");
        }
        if (subordinate == this) {
            throw new ValidationException("Guard cannot be their own subordinate.");
        }
        if (!subordinates.contains(subordinate)) {
            subordinates.add(subordinate);
            if (!subordinate.getSupervisors().contains(this)) {
                subordinate.addSupervisor(this);
            }
        }
    }
    
    public void removeSubordinate(Guard subordinate) {
        if (subordinates.contains(subordinate)) {
            subordinates.remove(subordinate);
            if (subordinate.getSupervisors().contains(this)) {
                subordinate.removeSupervisor(this);
            }
        }
    }
    
    public List<Guard> getSubordinates() {
        return Collections.unmodifiableList(subordinates);
    }
    // Many-to-many: Guard[0..*] to Meal[0..*]
    public void addMeal(Meal meal) {
        if (meal == null) {
            throw new InvalidReferenceException("Meal cannot be null.");
        }
        if (!supervisedMeals.contains(meal)) {
            supervisedMeals.add(meal);
            if (!meal.getSupervisingGuards().contains(this)) {
                meal.addSupervisingGuard(this);
            }
        }
    }
    
    public void removeMeal(Meal meal) {
        if (supervisedMeals.contains(meal)) {
            supervisedMeals.remove(meal);
            if (meal.getSupervisingGuards().contains(this)) {
                meal.removeSupervisingGuard(this);
            }
        }
    }
    
    public List<Meal> getMeals() {
        return Collections.unmodifiableList(supervisedMeals);
    }
    
    // Backward compatibility
    public void addSupervisedMeal(Meal meal) {
        addMeal(meal);
    }
    
    public void removeSupervisedMeal(Meal meal) {
        removeMeal(meal);
    }
    
    public List<Meal> getSupervisedMeals() {
        return getMeals();
    }
    public void addMedicalReport(MedicalReport report) {
        if (report == null) {
            throw new InvalidReferenceException("Medical report cannot be null.");
        }
        if (!medicalReports.contains(report)) {
            medicalReports.add(report);
            if (report.getGuard() != this) {
                report.setGuard(this);
            }
        }
    }
    
    public void removeMedicalReport(MedicalReport report) {
        if (report != null && medicalReports.contains(report)) {
            medicalReports.remove(report);
            if (report.getGuard() == this) {
                report.setGuard(null);
            }
        }
    }
    
    public List<MedicalReport> getMedicalReports() {
        return Collections.unmodifiableList(medicalReports);
    }

    public static List<Guard> getGuardExtent() {
        return Collections.unmodifiableList(extent);
    }

    public static void saveGuardExtent(String filename) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(extent);
        }
    }

    @SuppressWarnings("unchecked")
    public static void loadGuardExtent(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            extent = (List<Guard>) in.readObject();
        } catch (FileNotFoundException e) {
            extent = new ArrayList<>();
        }
    }

    public static void clearGuardExtent() {
        extent.clear();
    }
}
