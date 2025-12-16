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
    private List<Block> assignedBlocks;    // Block[0..*] to Staff[0..*] - many-to-many
    private List<Schedule> schedules;  // Staff[0..*] to Schedule[0..*]
    
    public Staff(String name, String surname, int experienceYears, 
                 String shiftHour, String phone, String email) {
        setName(name);
        setSurname(surname);
        setExperienceYears(experienceYears);
        setShiftHour(shiftHour);
        setPhone(phone);
        setEmail(email);
        this.assignedBlocks = new ArrayList<>();
        this.schedules = new ArrayList<>();
        
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
    // Many-to-many: Block[0..*] to Staff[0..*]
    public void addBlock(Block block) {
        if (block == null) {
            throw new InvalidReferenceException("Block cannot be null.");
        }
        if (!assignedBlocks.contains(block)) {
            assignedBlocks.add(block);
            if (!block.getStaffMembers().contains(this)) {
                block.addStaff(this);
            }
        }
    }
    
    public void removeBlock(Block block) {
        if (assignedBlocks.contains(block)) {
            assignedBlocks.remove(block);
            if (block.getStaffMembers().contains(this)) {
                block.removeStaff(this);
            }
        }
    }
    
    public List<Block> getBlocks() {
        return Collections.unmodifiableList(assignedBlocks);
    }
    
    // Backward compatibility
    public Block getAssignedBlock() {
        return assignedBlocks.isEmpty() ? null : assignedBlocks.get(0);
    }
    
    public void setAssignedBlock(Block block) {
        assignedBlocks.clear();
        if (block != null) {
            addBlock(block);
        }
    }
    
    /**
     * Adds a schedule (many-to-many association)
     * Staff[0..*] to Schedule[0..*]
     */
    public void addSchedule(Schedule schedule) {
        if (schedule == null) {
            throw new InvalidReferenceException("Schedule cannot be null.");
        }
        if (!schedules.contains(schedule)) {
            schedules.add(schedule);
            if (!schedule.getStaffMembers().contains(this)) {
                schedule.addStaff(this);
            }
        }
    }
    
    /**
     * Removes a schedule
     */
    public void removeSchedule(Schedule schedule) {
        if (schedule != null && schedules.contains(schedule)) {
            schedules.remove(schedule);
            if (schedule.getStaffMembers().contains(this)) {
                schedule.removeStaff(this);
            }
        }
    }
    
    /**
     * Gets all schedules
     */
    public List<Schedule> getSchedules() {
        return Collections.unmodifiableList(schedules);
    }
    
    public static void clearExtent() {
        extent.clear();
    }
}
