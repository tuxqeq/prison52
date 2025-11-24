package com.prison.model;

import com.prison.exception.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Staff implements Serializable {
    private static final long serialVersionUID = 1L;
    // Note: Since Staff is abstract, we might want to store all staff here, or just let subclasses handle their own extents.
    // However, the requirement usually implies a per-class extent.
    // For polymorphism, a common extent for the base class is useful.
    private static List<Staff> extent = new ArrayList<>();
    private String name;
    private String surname;
    private int experienceYears;
    private String shiftHour;
    private String phone;
    private String email;
    private Block assignedBlock;    // Staff assigned to Block
    private Schedule schedule;      // Staff's work schedule
    public Staff(String name, String surname, int experienceYears, 
                 String shiftHour, String phone, String email) {
        setName(name);
        setSurname(surname);
        setExperienceYears(experienceYears);
        setShiftHour(shiftHour);
        setPhone(phone);
        setEmail(email);
        
        // Add to extent
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

    public int getExperienceYears() { return experienceYears; }
    public void setExperienceYears(int experienceYears) {
        if (experienceYears < 0) {
            throw new NegativeNumberException("Experience years cannot be negative.");
        }
        this.experienceYears = experienceYears;
    }

    public String getShiftHour() { return shiftHour; }
    public void setShiftHour(String shiftHour) {
        if (shiftHour == null || shiftHour.trim().isEmpty()) {
            throw new EmptyStringException("Shift hour cannot be empty.");
        }
        this.shiftHour = shiftHour;
    }

    public String getPhone() { return phone; }
    public void setPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            throw new EmptyStringException("Phone cannot be empty.");
        }
        this.phone = phone;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new EmptyStringException("Email cannot be empty.");
        }
        this.email = email;
    }
    public static List<Staff> getExtent() {
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
            extent = (List<Staff>) in.readObject();
        } catch (FileNotFoundException e) {
            extent = new ArrayList<>();
        }
    }
    public Block getAssignedBlock() {
        return assignedBlock;
    }
    
    public void setAssignedBlock(Block block) {
        if (this.assignedBlock != block) {
            // Remove from old block
            if (this.assignedBlock != null && this.assignedBlock.getStaff().contains(this)) {
                this.assignedBlock.removeStaff(this);
            }
            
            this.assignedBlock = block;
            
            // Add to new block
            if (block != null && !block.getStaff().contains(this)) {
                block.addStaff(this);
            }
        }
    }
    
    public Schedule getSchedule() {
        return schedule;
    }
    
    public void setSchedule(Schedule schedule) {
        if (this.schedule != schedule) {
            // Remove from old schedule
            if (this.schedule != null && this.schedule.getStaff() == this) {
                this.schedule.setStaff(null);
            }
            
            this.schedule = schedule;
            
            // Set in new schedule
            if (schedule != null && schedule.getStaff() != this) {
                schedule.setStaff(this);
            }
        }
    }
    
    public static void clearExtent() {
        extent.clear();
    }
}
