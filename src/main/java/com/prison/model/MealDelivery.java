package com.prison.model;

import com.prison.exception.*;
import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MealDelivery implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum DeliveryStatus {
        SCHEDULED, DELIVERED, CANCELLED
    }

    private static List<MealDelivery> extent = new ArrayList<>();

    private LocalDateTime deliveryTime;
    private DeliveryStatus status;
    private Prisoner prisoner;   // Recipient
    private Meal meal;           // Meal being delivered

    public MealDelivery(LocalDateTime deliveryTime, Prisoner prisoner, Meal meal) {
        setDeliveryTime(deliveryTime);
        this.status = DeliveryStatus.SCHEDULED;
        setPrisoner(prisoner);
        setMeal(meal);
        extent.add(this);
    }

    public LocalDateTime getDeliveryTime() { return deliveryTime; }
    public void setDeliveryTime(LocalDateTime deliveryTime) {
        if (deliveryTime == null) {
            throw new InvalidReferenceException("Delivery time cannot be null.");
        }
        this.deliveryTime = deliveryTime;
    }

    public DeliveryStatus getStatus() { return status; }
    public void setStatus(DeliveryStatus status) {
        if (status == null) {
            throw new InvalidReferenceException("Status cannot be null.");
        }
        this.status = status;
    }
    public void set(Prisoner prisoner) {
        if (prisoner == null) {
            throw new InvalidReferenceException("Prisoner cannot be null.");
        }
        this.prisoner = prisoner;
        
        if (!prisoner.getMealDeliveries().contains(this)) {
            prisoner.addMealDelivery(this);
        }
    }
    
    public Prisoner getPrisoner() {
        return prisoner;
    }
    
    public void setPrisoner(Prisoner prisoner) {
        if (prisoner == null) {
            throw new InvalidReferenceException("Prisoner cannot be null.");
        }
        this.prisoner = prisoner;
        
        if (!prisoner.getMealDeliveries().contains(this)) {
            prisoner.addMealDelivery(this);
        }
    }
    
    public void setMeal(Meal meal) {
        if (meal == null) {
            throw new InvalidReferenceException("Meal cannot be null.");
        }
        this.meal = meal;
        
        if (!meal.getDeliveries().contains(this)) {
            meal.addDelivery(this);
        }
    }
    
    public Meal getMeal() {
        return meal;
    }

    public static List<MealDelivery> getExtent() {
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
            extent = (List<MealDelivery>) in.readObject();
        } catch (FileNotFoundException e) {
            extent = new ArrayList<>();
        }
    }

    public static void clearExtent() {
        extent.clear();
    }
}
