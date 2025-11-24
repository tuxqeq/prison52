package com.prison.model;

import com.prison.exception.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Block implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum BlockType {
        MINIMUM_SECURITY, MEDIUM_SECURITY, MAXIMUM_SECURITY, MEDICAL, ADMINISTRATIVE
    }

    private static List<Block> extent = new ArrayList<>();

    private String name;
    private int numOfCells;
    private BlockType type;
    private List<Cell> cells;              // Block owns Cells
    private List<Guard> assignedGuards;    // Guards supervise Blocks
    private List<Staff> staff;             // Staff assigned to Block
    private List<Schedule> schedules;      // Schedules for Block activities

    public Block(String name, int numOfCells, BlockType type) {
        setName(name);
        setNumOfCells(numOfCells);
        setType(type);
        this.cells = new ArrayList<>();
        this.assignedGuards = new ArrayList<>();
        this.staff = new ArrayList<>();
        this.schedules = new ArrayList<>();
        extent.add(this);
    }

    public String getName() { return name; }
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new EmptyStringException("Name cannot be empty.");
        }
        this.name = name;
    }

    public int getNumOfCells() { return numOfCells; }
    public void setNumOfCells(int numOfCells) {
        if (numOfCells < 0) {
            throw new NegativeNumberException("Number of cells cannot be negative.");
        }
        this.numOfCells = numOfCells;
    }

    public BlockType getType() { return type; }
    public void setType(BlockType type) {
        if (type == null) {
            throw new InvalidReferenceException("Block type cannot be null.");
        }
        this.type = type;
    }
    public int getAvailableCell() {
        return (int) cells.stream().filter(Cell::isAvailable).count();
    }

    public String getSecurityLevel() {
        return cells.stream()
            .map(cell -> cell.getSecurityLevel())
            .max((s1, s2) -> s1.compareTo(s2))
            .map(Enum::name)
            .orElse("UNKNOWN");
    }

    public int getMaxNumOfPrisonerInCell() {
        return cells.stream()
            .mapToInt(Cell::getCapasity)
            .max()
            .orElse(0);
    }
    public void manageBlock() {
        System.out.println("Managing block: " + name + " with " + cells.size() + " cells");
    }
    public void addCell(Cell cell) {
        if (cell == null) {
            throw new InvalidReferenceException("Cell cannot be null.");
        }
        if (!cells.contains(cell)) {
            cells.add(cell);
            
            if (cell.getBlock() != this) {
                cell.setBlock(this);
            }
        }
    }
    
    public void removeCell(Cell cell) {
        if (cell != null && cells.contains(cell)) {
            // First, ensure all prisoners are removed from the cell
            List<Prisoner> prisonersToReassign = new ArrayList<>(cell.getPrisoners());
            for (Prisoner prisoner : prisonersToReassign) {
                cell.removePrisoner(prisoner);
            }
            
            cells.remove(cell);
            if (cell.getBlock() == this) {
                cell.setBlock(null);
            }
        }
    }
    
    public List<Cell> getCells() {
        return Collections.unmodifiableList(cells);
    }
    
    public void assignGuard(Guard guard) {
        if (guard == null) {
            throw new InvalidReferenceException("Guard cannot be null.");
        }
        if (!assignedGuards.contains(guard)) {
            assignedGuards.add(guard);
            
            if (!guard.getAssignedBlocks().contains(this)) {
                guard.assignToBlock(this);
            }
        }
    }
    
    public void removeGuard(Guard guard) {
        if (guard != null && assignedGuards.contains(guard)) {
            assignedGuards.remove(guard);
            if (guard.getAssignedBlocks().contains(this)) {
                guard.removeFromBlock(this);
            }
        }
    }
    
    public List<Guard> getAssignedGuards() {
        return Collections.unmodifiableList(assignedGuards);
    }
    public void addStaff(Staff staffMember) {
        if (staffMember == null) {
            throw new InvalidReferenceException("Staff member cannot be null.");
        }
        if (!staff.contains(staffMember)) {
            staff.add(staffMember);
            if (staffMember.getAssignedBlock() != this) {
                staffMember.setAssignedBlock(this);
            }
        }
    }
    
    public void removeStaff(Staff staffMember) {
        if (staffMember != null && staff.contains(staffMember)) {
            staff.remove(staffMember);
            if (staffMember.getAssignedBlock() == this) {
                staffMember.setAssignedBlock(null);
            }
        }
    }
    
    public List<Staff> getStaff() {
        return Collections.unmodifiableList(staff);
    }
    public void addSchedule(Schedule schedule) {
        if (schedule == null) {
            throw new InvalidReferenceException("Schedule cannot be null.");
        }
        if (!schedules.contains(schedule)) {
            schedules.add(schedule);
            if (schedule.getBlock() != this) {
                schedule.setBlock(this);
            }
        }
    }
    
    public void removeSchedule(Schedule schedule) {
        if (schedule != null && schedules.contains(schedule)) {
            schedules.remove(schedule);
            if (schedule.getBlock() == this) {
                schedule.setBlock(null);
            }
        }
    }
    
    public List<Schedule> getSchedules() {
        return Collections.unmodifiableList(schedules);
    }
    
    public static List<Block> getExtent() {
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
            extent = (List<Block>) in.readObject();
        } catch (FileNotFoundException e) {
            extent = new ArrayList<>();
        }
    }

    public static void clearExtent() {
        extent.clear();
    }
}
