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
    private Director director;  // Director who manages this assignment
    public Assignment(String name, String description) {
        setName(name);
        setDescription(description);
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
    
    public void setDirector(Director director) {
        if (this.director != director) {
            if (this.director != null && this.director.getAssignments().contains(this)) {
                this.director.removeAssignment(this);
            }
            
            this.director = director;
            
            if (director != null && !director.getAssignments().contains(this)) {
                director.addAssignment(this);
            }
        }
    }
    
    public Director getDirector() {
        return director;
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
