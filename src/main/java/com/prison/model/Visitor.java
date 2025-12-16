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
    private Map<String, Visit> visitsByVisitorID;    // Qualified Association: Visit[0..*] to Visitor (qualified by visitorID)

    public Visitor(String name, String surname, String contactInfo, String relationshipToPrisoner) {
        setName(name);
        setSurname(surname);
        setContactInfo(contactInfo);
        setRelationshipToPrisoner(relationshipToPrisoner);
        this.visitsByVisitorID = new HashMap<>();
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
     * Adds a visit by visitorID (Qualified Association)
     * The qualifier is the visitorID
     */
    public void addVisitByVisitorID(String visitorID, Visit visit) {
        if (visitorID == null || visitorID.trim().isEmpty()) {
            throw new EmptyStringException("Visitor ID cannot be empty.");
        }
        if (visit == null) {
            throw new InvalidReferenceException("Visit cannot be null.");
        }
        
        // Check if a visit already exists for this visitorID
        if (visitsByVisitorID.containsKey(visitorID)) {
            throw new ValidationException("A visit already exists for visitorID: " + visitorID);
        }
        
        visitsByVisitorID.put(visitorID, visit);
        if (visit.getVisitor() != this) {
            visit.setVisitor(this);
        }
    }
    
    /**
     * Removes a visit by visitorID
     */
    public void removeVisitByVisitorID(String visitorID) {
        if (visitorID == null || visitorID.trim().isEmpty()) {
            throw new EmptyStringException("Visitor ID cannot be empty.");
        }
        
        Visit visit = visitsByVisitorID.remove(visitorID);
        if (visit != null && visit.getVisitor() == this) {
            // Note: Visit still references this visitor
        }
    }
    
    /**
     * Gets a visit by visitorID (Qualified Association)
     */
    public Visit getVisitByVisitorID(String visitorID) {
        return visitsByVisitorID.get(visitorID);
    }
    
    /**
     * Gets all visits (as a map qualified by visitorID)
     */
    public Map<String, Visit> getVisitsByVisitorID() {
        return Collections.unmodifiableMap(visitsByVisitorID);
    }
    
    /**
     * Gets all visits as a collection
     */
    public Collection<Visit> getVisits() {
        return Collections.unmodifiableCollection(visitsByVisitorID.values());
    }
    
    /**
     * Updates a visit's visitorID in the qualified association
     * This should be called when a visit's visitorID changes
     */
    public void updateVisitVisitorID(String oldVisitorID, String newVisitorID, Visit visit) {
        if (oldVisitorID == null || newVisitorID == null || visit == null) {
            throw new InvalidReferenceException("Parameters cannot be null.");
        }
        
        if (visitsByVisitorID.get(oldVisitorID) == visit) {
            visitsByVisitorID.remove(oldVisitorID);
            if (visitsByVisitorID.containsKey(newVisitorID)) {
                throw new ValidationException("A visit already exists for visitorID: " + newVisitorID);
            }
            visitsByVisitorID.put(newVisitorID, visit);
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
