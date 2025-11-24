package com.prison.model;

import com.prison.exception.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Meal implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum DietPlan {
        STANDARD, VEGETARIAN, VEGAN, HALAL, KOSHER, DIABETIC
    }

    private static List<Meal> extent = new ArrayList<>();

    private DietPlan dietPlan;
    private int calories;
    private List<MealDelivery> deliveries;   // Delivery history
    private Guard supervisingGuard;          // Guard supervising this meal

    public Meal(DietPlan dietPlan, int calories) {
        setDietPlan(dietPlan);
        setCalories(calories);
        this.deliveries = new ArrayList<>();
        extent.add(this);
    }

    public DietPlan getDietPlan() { return dietPlan; }
    public void setDietPlan(DietPlan dietPlan) {
        if (dietPlan == null) {
            throw new InvalidReferenceException("Diet plan cannot be null.");
        }
        this.dietPlan = dietPlan;
    }

    public int getCalories() { return calories; }
    public void setCalories(int calories) {
        if (calories < 0) {
            throw new NegativeNumberException("Calories cannot be negative.");
        }
        this.calories = calories;
    }
    /**
     * Adds a delivery instance
     */
    public void addDelivery(MealDelivery delivery) {
        if (delivery == null) {
            throw new InvalidReferenceException("Meal delivery cannot be null.");
        }
        if (!deliveries.contains(delivery)) {
            deliveries.add(delivery);
            if (delivery.getMeal() != this) {
                delivery.setMeal(this);
            }
        }
    }
    
    public void setSupervisingGuard(Guard guard) {
        if (this.supervisingGuard != guard) {
            if (this.supervisingGuard != null && this.supervisingGuard.getSupervisedMeals().contains(this)) {
                this.supervisingGuard.removeSupervisedMeal(this);
            }
            
            this.supervisingGuard = guard;
            
            if (guard != null && !guard.getSupervisedMeals().contains(this)) {
                guard.addSupervisedMeal(this);
            }
        }
    }
    
    public Guard getSupervisingGuard() {
        return supervisingGuard;
    }
    
    public List<MealDelivery> getDeliveries() {
        return Collections.unmodifiableList(deliveries);
    }

    public static List<Meal> getExtent() {
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
            extent = (List<Meal>) in.readObject();
        } catch (FileNotFoundException e) {
            extent = new ArrayList<>();
        }
    }

    public static void clearExtent() {
        extent.clear();
    }
}
