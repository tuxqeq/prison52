package com.prison.model;

import com.prison.exception.*;
import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class Visitor implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final int MaxAmountOfVisitPerMonth = 2;

    private static List<Visitor> extent = new ArrayList<>();

    private String name;
    private String surname;
    private String contactInfo;
    private String relationshipToPrisoner;
    private Map<LocalDate, Visit> visitsByDate;    // Qualified Association: Visit[0..*] to Visitor (qualified by date)

    public Visitor(String name, String surname, String contactInfo, String relationshipToPrisoner) {
        setName(name);
        setSurname(surname);
        setContactInfo(contactInfo);
        setRelationshipToPrisoner(relationshipToPrisoner);
        this.visitsByDate = new HashMap<>();
        extent.add(this);
    }

    public String getName() { return name; }
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new EmptyStringException("Name cannot be empty.");
        }
        this.name = name;
    }

    public String getSurname() { return surname; }
    public void setSurname(String surname) {
        if (surname == null || surname.trim().isEmpty()) {
            throw new EmptyStringException("Surname cannot be empty.");
        }
        this.surname = surname;
    }

    public String getContactInfo() { return contactInfo; }
    public void setContactInfo(String contactInfo) {
        if (contactInfo == null || contactInfo.trim().isEmpty()) {
            throw new EmptyStringException("Contact info cannot be empty.");
        }
        this.contactInfo = contactInfo;
    }

    public String getRelationshipToPrisoner() { return relationshipToPrisoner; }
    public void setRelationshipToPrisoner(String relationshipToPrisoner) {
        if (relationshipToPrisoner == null || relationshipToPrisoner.trim().isEmpty()) {
            throw new EmptyStringException("Relationship to prisoner cannot be empty.");
        }
        this.relationshipToPrisoner = relationshipToPrisoner;
    }

    public static int getMaxAmountOfVisitPerMonth() {
        return MaxAmountOfVisitPerMonth;
    }
    
    /**
     * Adds a visit by date (Qualified Association)
     * The qualifier is the visit date
     */
    public void addVisitByDate(LocalDate date, Visit visit) {
        if (date == null) {
            throw new InvalidReferenceException("Visit date cannot be null.");
        }
        if (visit == null) {
            throw new InvalidReferenceException("Visit cannot be null.");
        }
        
        // Check if a visit already exists for this date
        if (visitsByDate.containsKey(date)) {
            throw new ValidationException("A visit already exists for date: " + date);
        }
        
        visitsByDate.put(date, visit);
        if (visit.getVisitor() != this) {
            visit.setVisitor(this);
        }
    }
    
    /**
     * Removes a visit by date
     */
    public void removeVisitByDate(LocalDate date) {
        if (date == null) {
            throw new InvalidReferenceException("Visit date cannot be null.");
        }
        
        Visit visit = visitsByDate.remove(date);
        if (visit != null && visit.getVisitor() == this) {
            // Note: Visit still references this visitor; would need visit.setVisitor(null) if allowed
        }
    }
    
    /**
     * Gets a visit by date (Qualified Association)
     */
    public Visit getVisitByDate(LocalDate date) {
        return visitsByDate.get(date);
    }
    
    /**
     * Gets all visits (as a map)
     */
    public Map<LocalDate, Visit> getVisitsByDate() {
        return Collections.unmodifiableMap(visitsByDate);
    }
    
    /**
     * Gets all visits as a collection
     */
    public Collection<Visit> getVisits() {
        return Collections.unmodifiableCollection(visitsByDate.values());
    }
    
    /**
     * Updates a visit's date in the qualified association
     * This should be called when a visit's date changes
     */
    public void updateVisitDate(LocalDate oldDate, LocalDate newDate, Visit visit) {
        if (oldDate == null || newDate == null || visit == null) {
            throw new InvalidReferenceException("Parameters cannot be null.");
        }
        
        if (visitsByDate.get(oldDate) == visit) {
            visitsByDate.remove(oldDate);
            if (visitsByDate.containsKey(newDate)) {
                throw new ValidationException("A visit already exists for date: " + newDate);
            }
            visitsByDate.put(newDate, visit);
        }
    }

    public static List<Visitor> getExtent() {
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
            extent = (List<Visitor>) in.readObject();
        } catch (FileNotFoundException e) {
            extent = new ArrayList<>();
        }
    }

    public static void clearExtent() {
        extent.clear();
    }
}
