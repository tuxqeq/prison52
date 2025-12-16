package com.prison.model;

import com.prison.exception.*;
import java.io.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Schedule implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum ActivityType {
        Work, Exercise, Meal, Medical, Court, Cell, Visit
    }

    private static List<Schedule> extent = new ArrayList<>();

    private LocalTime startTime;
    private LocalTime endTime;
    private ActivityType type;
    private List<Prisoner> prisoners;   // Prisoner[0..*] to Schedule[0..*]
    private Block block;                // Block[1] to Schedule[1] - mandatory
    private List<Staff> staffMembers;   // Staff[0..*] to Schedule[0..*]

    public Schedule(LocalTime startTime, LocalTime endTime, ActivityType type, Block block) {
        setStartTime(startTime);
        setEndTime(endTime);
        setType(type);
        setBlock(block);  // Required - Schedule must have a Block
        this.prisoners = new ArrayList<>();
        this.staffMembers = new ArrayList<>();
        extent.add(this);
    }

    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) {
        if (startTime == null) {
            throw new InvalidReferenceException("Start time cannot be null.");
        }
        this.startTime = startTime;
    }

    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) {
        if (endTime == null) {
            throw new InvalidReferenceException("End time cannot be null.");
        }
        if (startTime != null && endTime.isBefore(startTime)) {
            throw new InvalidDateException("End time cannot be before start time.");
        }
        this.endTime = endTime;
    }

    public ActivityType getType() { return type; }
    public void setType(ActivityType type) {
        if (type == null) {
            throw new InvalidReferenceException("Activity type cannot be null.");
        }
        this.type = type;
    }
    /**
     * Calculates duration in minutes
     */
    public long getDuration() {
        if (startTime == null || endTime == null) return 0;
        return java.time.Duration.between(startTime, endTime).toMinutes();
    }
    
    public void manageSchedule() {
        System.out.println("Managing schedule: " + type + " from " + startTime + " to " + endTime);
    }
    
    /**
     * Adds a prisoner (many-to-many association)
     * Prisoner[0..*] to Schedule[0..*]
     */
    public void addPrisoner(Prisoner prisoner) {
        if (prisoner == null) {
            throw new InvalidReferenceException("Prisoner cannot be null.");
        }
        if (!prisoners.contains(prisoner)) {
            prisoners.add(prisoner);
            if (!prisoner.getSchedules().contains(this)) {
                prisoner.addSchedule(this);
            }
        }
    }
    
    /**
     * Removes a prisoner
     */
    public void removePrisoner(Prisoner prisoner) {
        if (prisoner != null && prisoners.contains(prisoner)) {
            prisoners.remove(prisoner);
            if (prisoner.getSchedules().contains(this)) {
                prisoner.removeSchedule(this);
            }
        }
    }
    
    /**
     * Gets all prisoners
     */
    public List<Prisoner> getPrisoners() {
        return Collections.unmodifiableList(prisoners);
    }
    
    /**
     * Sets block (mandatory 1-to-1 association)
     * Block[1] to Schedule[1]
     */
    public void setBlock(Block block) {
        if (block == null) {
            throw new InvalidReferenceException("Block cannot be null - schedule must belong to a block.");
        }
        
        if (this.block != block) {
            // Remove from old block
            if (this.block != null && this.block.getSchedules().contains(this)) {
                this.block.removeSchedule(this);
            }
            
            this.block = block;
            
            // Add to new block
            if (!block.getSchedules().contains(this)) {
                block.addSchedule(this);
            }
        }
    }
    
    public Block getBlock() {
        return block;
    }
    
    /**
     * Adds a staff member (many-to-many association)
     * Staff[0..*] to Schedule[0..*]
     */
    public void addStaff(Staff staff) {
        if (staff == null) {
            throw new InvalidReferenceException("Staff cannot be null.");
        }
        if (!staffMembers.contains(staff)) {
            staffMembers.add(staff);
            if (!staff.getSchedules().contains(this)) {
                staff.addSchedule(this);
            }
        }
    }
    
    /**
     * Removes a staff member
     */
    public void removeStaff(Staff staff) {
        if (staff != null && staffMembers.contains(staff)) {
            staffMembers.remove(staff);
            if (staff.getSchedules().contains(this)) {
                staff.removeSchedule(this);
            }
        }
    }
    
    /**
     * Gets all staff members
     */
    public List<Staff> getStaffMembers() {
        return Collections.unmodifiableList(staffMembers);
    }

    public static List<Schedule> getExtent() {
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
            extent = (List<Schedule>) in.readObject();
        } catch (FileNotFoundException e) {
            extent = new ArrayList<>();
        }
    }

    public static void clearExtent() {
        extent.clear();
    }
}
