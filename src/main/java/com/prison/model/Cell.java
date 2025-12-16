package com.prison.model;

import com.prison.exception.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Cell implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum SecurityLevel {
        LOW, MEDIUM, HIGH
    }

    private static List<Cell> extent = new ArrayList<>();
    private int cellNumber;
    private String type;           // Type of cell
    private int capasity;          // Capacity (diagram spelling)
    private SecurityLevel securityLevel;
    private Block block;                    // Cell belongs to Block (Aggregation)
    
    public Cell(int cellNumber, String type, int capasity, SecurityLevel securityLevel) {
        setCellNumber(cellNumber);
        setType(type);
        setCapasity(capasity);
        setSecurityLevel(securityLevel);
        
        extent.add(this);
    }
    public int getCellNumber() { return cellNumber; }
    public void setCellNumber(int cellNumber) {
        if (cellNumber <= 0) {
            throw new NegativeNumberException("Cell number must be positive.");
        }
        this.cellNumber = cellNumber;
    }

    public String getType() { return type; }
    public void setType(String type) {
        this.type = type;  
    }

    public int getCapasity() { return capasity; }
    public void setCapasity(int capasity) {
        if (capasity <= 0) {
            throw new NegativeNumberException("Capasity must be greater than zero.");
        }
        this.capasity = capasity;
    }

    public SecurityLevel getSecurityLevel() { return securityLevel; }
    public void setSecurityLevel(SecurityLevel securityLevel) {
        if (securityLevel == null) {
            throw new InvalidReferenceException("Security level cannot be null.");
        }
        this.securityLevel = securityLevel;
    }

    public void setBlock(Block block) {
        
        if (this.block != block) {
            Block oldBlock = this.block;
            this.block = block;
            
            
            if (oldBlock != null && oldBlock.getCells().contains(this)) {
                oldBlock.removeCell(this);
            }
            if (block != null && !block.getCells().contains(this)) {
                block.addCell(this);
            }
        }
    }
    
    public Block getBlock() {
        return block;
    }

    public static List<Cell> getExtent() {
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
            extent = (List<Cell>) in.readObject();
        } catch (FileNotFoundException e) {
            extent = new ArrayList<>();
        }
    }
    
    public static void clearExtent() {
        extent.clear();
    }
}
