package com.prison.model;

import com.prison.exception.*;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CourtCase implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum CaseStatus {
        PENDING, IN_PROGRESS, CLOSED, APPEAL
    }

    private static List<CourtCase> extent = new ArrayList<>();

    private LocalDate courtDate;
    private CaseStatus status;
    private String judgeName;
    private List<Charges> charges;   // COMPOSITION: Charges[0..*] to CourtCase[1]

    public CourtCase(LocalDate courtDate, CaseStatus status, String judgeName) {
        setCourtDate(courtDate);
        setStatus(status);
        setJudgeName(judgeName);
        this.charges = new ArrayList<>();
        extent.add(this);
    }

    public LocalDate getCourtDate() { return courtDate; }
    public void setCourtDate(LocalDate courtDate) {
        if (courtDate == null) {
            throw new InvalidReferenceException("Court date cannot be null.");
        }
        this.courtDate = courtDate;
    }

    public CaseStatus getStatus() { return status; }
    public void setStatus(CaseStatus status) {
        if (status == null) {
            throw new InvalidReferenceException("Case status cannot be null.");
        }
        this.status = status;
    }

    public String getJudgeName() { return judgeName; }
    public void setJudgeName(String judgeName) {
        if (judgeName == null || judgeName.trim().isEmpty()) {
            throw new EmptyStringException("Judge name cannot be empty.");
        }
        this.judgeName = judgeName;
    }
    /**
     * Adds a charge to this court case (COMPOSITION)
     */
    public void addCharge(Charges charge) {
        if (charge == null) {
            throw new InvalidReferenceException("Charge cannot be null.");
        }
        // Composition: charge cannot belong to another case
        if (charge.getCourtCase() != null && charge.getCourtCase() != this) {
            throw new ValidationException("Charge already belongs to another court case - composition violation.");
        }
        if (!charges.contains(charge)) {
            charges.add(charge);
            if (charge.getCourtCase() != this) {
                charge.setCourtCase(this);
            }
        }
    }
    
    /**
     * Removes a charge (COMPOSITION - also deletes the charge)
     */
    public void removeCharge(Charges charge) {
        if (charges.contains(charge)) {
            charges.remove(charge);
            charge.delete();
        }
    }
    
    /**
     * Deletes this court case (COMPOSITION - cascades to all charges)
     */
    public void delete() {
        List<Charges> chargesCopy = new ArrayList<>(charges);
        for (Charges charge : chargesCopy) {
            charge.delete();
        }
        charges.clear();
        extent.remove(this);
    }
    
    public List<Charges> getCharges() {
        return Collections.unmodifiableList(charges);
    }
    
    public List<Prisoner> getPrisoners() {
        List<Prisoner> prisoners = new ArrayList<>();
        for (Charges charge : charges) {
            if (charge.getPrisoner() != null && !prisoners.contains(charge.getPrisoner())) {
                prisoners.add(charge.getPrisoner());
            }
        }
        return Collections.unmodifiableList(prisoners);
    }

    public static List<CourtCase> getExtent() {
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
            extent = (List<CourtCase>) in.readObject();
        } catch (FileNotFoundException e) {
            extent = new ArrayList<>();
        }
    }

    public static void clearExtent() {
        extent.clear();
    }
}
