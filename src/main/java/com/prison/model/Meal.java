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

    public enum MealType {
        Breakfast, Lunch, Dinner
    }

    private static List<Meal> extent = new ArrayList<>();

    private String description;
    private DietPlan dietPlan;
    private Double calories;                 // Changed to wrapper Double
    private MealType mealType;
    private List<String> allergens;          // [1..*] At least one allergen required
    private List<MealDelivery> deliveries;   // Delivery history
    private List<Guard> supervisingGuards;   // Guard[0..*] to Meal[0..*] - many-to-many

    public Meal(String description, DietPlan dietPlan, Double calories, MealType mealType) {
        setDescription(description);
        setDietPlan(dietPlan);
        setCalories(calories);
        setMealType(mealType);
        this.allergens = new ArrayList<>();
        this.deliveries = new ArrayList<>();
        this.supervisingGuards = new ArrayList<>();
        extent.add(this);
    }

    public String getDescription() { return description; }
    public void setDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new EmptyStringException("Description cannot be empty.");
        }
        this.description = description;
    }

    public DietPlan getDietPlan() { return dietPlan; }
    public void setDietPlan(DietPlan dietPlan) {
        if (dietPlan == null) {
            throw new InvalidReferenceException("Diet plan cannot be null.");
        }
        this.dietPlan = dietPlan;
    }

    public Double getCalories() { return calories; }
    public void setCalories(Double calories) {
        if (calories == null) {
            throw new InvalidReferenceException("Calories cannot be null.");
        }
        if (calories < 0) {
            throw new NegativeNumberException("Calories cannot be negative.");
        }
        this.calories = calories;
    }

    public MealType getMealType() { return mealType; }
    public void setMealType(MealType mealType) {
        if (mealType == null) {
            throw new InvalidReferenceException("Meal type cannot be null.");
        }
        this.mealType = mealType;
    }

    public List<String> getAllergens() {
        return Collections.unmodifiableList(allergens);
    }

    public void addAllergen(String allergen) {
        if (allergen == null || allergen.trim().isEmpty()) {
            throw new EmptyStringException("Allergen cannot be empty.");
        }
        if (!allergens.contains(allergen)) {
            allergens.add(allergen);
        }
    }

    public void removeAllergen(String allergen) {
        if (allergens.size() <= 1) {
            throw new ValidationException("Cannot remove allergen - at least one allergen is required [1..*].");
        }
        allergens.remove(allergen);
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
    
    // Many-to-many: Guard[0..*] to Meal[0..*]
    public void addSupervisingGuard(Guard guard) {
        if (guard == null) {
            throw new InvalidReferenceException("Guard cannot be null.");
        }
        if (!supervisingGuards.contains(guard)) {
            supervisingGuards.add(guard);
            if (!guard.getMeals().contains(this)) {
                guard.addMeal(this);
            }
        }
    }
    
    public void removeSupervisingGuard(Guard guard) {
        if (supervisingGuards.contains(guard)) {
            supervisingGuards.remove(guard);
            if (guard.getMeals().contains(this)) {
                guard.removeMeal(this);
            }
        }
    }
    
    public List<Guard> getSupervisingGuards() {
        return Collections.unmodifiableList(supervisingGuards);
    }
    
    // Backward compatibility
    public void setSupervisingGuard(Guard guard) {
        supervisingGuards.clear();
        if (guard != null) {
            addSupervisingGuard(guard);
        }
    }
    
    public Guard getSupervisingGuard() {
        return supervisingGuards.isEmpty() ? null : supervisingGuards.get(0);
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
