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
    private Prisoner prisoner;   // One-to-one or many-to-one: Prisoner following this schedule
    private Block block;         // Schedule belongs to Block
    private Staff staff;         // Staff member assigned to schedule

    public Schedule(LocalTime startTime, LocalTime endTime, ActivityType type) {
        setStartTime(startTime);
        setEndTime(endTime);
        setType(type);
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
    
    public void setPrisoner(Prisoner prisoner) {
        this.prisoner = prisoner;
        if (prisoner != null && prisoner.getSchedule() != this) {
            prisoner.setSchedule(this);
        }
    }
    
    public Prisoner getPrisoner() {
        return prisoner;
    }
    
    public void setBlock(Block block) {
        if (this.block != block) {
            // Remove from old block
            if (this.block != null && this.block.getSchedules().contains(this)) {
                this.block.removeSchedule(this);
            }
            
            this.block = block;
            
            // Add to new block
            if (block != null && !block.getSchedules().contains(this)) {
                block.addSchedule(this);
            }
        }
    }
    
    public Block getBlock() {
        return block;
    }
    
    public void setStaff(Staff staff) {
        if (this.staff != staff) {
            // Remove from old staff
            if (this.staff != null && this.staff.getSchedule() == this) {
                this.staff.setSchedule(null);
            }
            
            this.staff = staff;
            
            // Set in new staff
            if (staff != null && staff.getSchedule() != this) {
                staff.setSchedule(this);
            }
        }
    }
    
    public Staff getStaff() {
        return staff;
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
