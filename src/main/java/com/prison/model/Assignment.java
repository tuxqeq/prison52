package com.prison.model;

import com.prison.exception.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Assignment implements Serializable {
    private static final long serialVersionUID = 1L;

    private static List<Assignment> extent = new ArrayList<>();
    private String name;
    private String description;
    private List<Director> directors;  // Director[0..*] to Assignment[0..*] - many-to-many
    
    public Assignment(String name, String description) {
        setName(name);
        setDescription(description);
        this.directors = new ArrayList<>();
        extent.add(this);
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new EmptyStringException("Name cannot be empty.");
        }
        this.name = name;
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
    public void manageAssignment() {
        System.out.println("Managing assignment: " + name);
        System.out.println("Description: " + description);
    }
    
    // Many-to-many: Assignment[0..*] to Director[0..*]
    public void addDirector(Director director) {
        if (director == null) {
            throw new InvalidReferenceException("Director cannot be null.");
        }
        if (!directors.contains(director)) {
            directors.add(director);
            if (!director.getAssignments().contains(this)) {
                director.addAssignment(this);
            }
        }
    }
    
    public void removeDirector(Director director) {
        if (director != null && directors.contains(director)) {
            directors.remove(director);
            if (director.getAssignments().contains(this)) {
                director.removeAssignment(this);
            }
        }
    }
    
    public List<Director> getDirectors() {
        return Collections.unmodifiableList(directors);
    }
    
    // Backward compatibility
    public Director getDirector() {
        return directors.isEmpty() ? null : directors.get(0);
    }
    
    public void setDirector(Director director) {
        directors.clear();
        if (director != null) {
            addDirector(director);
        }
    }
    
    public static List<Assignment> getExtent() {
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
            extent = (List<Assignment>) in.readObject();
        } catch (FileNotFoundException e) {
            extent = new ArrayList<>();
        }
    }

    public static void clearExtent() {
        extent.clear();
    }

    @Override
    public String toString() {
        return "Assignment{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
