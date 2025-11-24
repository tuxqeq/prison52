package com.prison.model;

import com.prison.exception.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * CombinedStaff represents staff members who can handle multiple roles
 * (both Guard and Doctor capabilities) and are available for emergency situations.
 * 
 * Since Java doesn't support multiple inheritance, this class extends Staff
 * and delegates to Guard and Doctor objects for role-specific behavior.
 */
public class CombinedStaff extends Staff {
    private static final long serialVersionUID = 1L;

    private static List<CombinedStaff> extent = new ArrayList<>();
    private Boolean availableForEmergency;  // [0..1] - nullable
    private Guard guardRole;    // Guard capabilities
    private Doctor doctorRole;  // Doctor capabilities
    public CombinedStaff(String name, String surname, int experienceYears, 
                        String shiftHour, String phone, String email,
                        Guard.Rank guardRank, String weapon, String licenseNumber, String contactInfo) {
        super(name, surname, experienceYears, shiftHour, phone, email);
        this.availableForEmergency = null;  // Initially null
        
        // Create role delegates (both Guard and Doctor)
        this.guardRole = new Guard(name, surname, experienceYears, shiftHour, phone, email, guardRank, weapon);
        this.doctorRole = new Doctor(name, surname, experienceYears, shiftHour, phone, email, licenseNumber, contactInfo);
        
        extent.add(this);
    }
    public Boolean getAvailableForEmergency() {
        return availableForEmergency;
    }

    public void setAvailableForEmergency(Boolean availableForEmergency) {
        this.availableForEmergency = availableForEmergency;
    }
    public void resolveToEmergency() {
        if (availableForEmergency == null || !availableForEmergency) {
            throw new ValidationException("Staff member is not available for emergency.");
        }
        // Emergency resolution logic
        System.out.println(getName() + " " + getSurname() + " is resolving emergency.");
    }
    public Guard getGuardRole() {
        return guardRole;
    }
    
    public Doctor getDoctorRole() {
        return doctorRole;
    }
    
    public boolean canPerformGuardDuties() {
        return guardRole != null;
    }
    
    public boolean canPerformDoctorDuties() {
        return doctorRole != null;
    }
    public static List<CombinedStaff> getCombinedStaffExtent() {
        return Collections.unmodifiableList(extent);
    }

    public static void saveCombinedStaffExtent(String filename) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(extent);
        }
    }

    @SuppressWarnings("unchecked")
    public static void loadCombinedStaffExtent(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            extent = (List<CombinedStaff>) in.readObject();
        } catch (FileNotFoundException e) {
            extent = new ArrayList<>();
        }
    }

    public static void clearCombinedStaffExtent() {
        extent.clear();
    }
}
