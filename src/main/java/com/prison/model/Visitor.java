package com.prison.model;

import com.prison.exception.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Visitor implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final int MaxAmountOfVisitPerMonth = 2;

    private static List<Visitor> extent = new ArrayList<>();

    private String name;
    private String surname;
    private String contactInfo;
    private String relationshipToPrisoner;
    private List<Visit> visits;    // Visits made by this visitor

    public Visitor(String name, String surname, String contactInfo, String relationshipToPrisoner) {
        setName(name);
        setSurname(surname);
        setContactInfo(contactInfo);
        setRelationshipToPrisoner(relationshipToPrisoner);
        this.visits = new ArrayList<>();
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
     * Adds a visit to this visitor's history
     */
    public void addVisit(Visit visit) {
        if (visit == null) {
            throw new InvalidReferenceException("Visit cannot be null.");
        }
        if (!visits.contains(visit)) {
            visits.add(visit);
            if (visit.getVisitor() != this) {
                visit.setVisitor(this);
            }
        }
    }
    
    public List<Visit> getVisits() {
        return Collections.unmodifiableList(visits);
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
