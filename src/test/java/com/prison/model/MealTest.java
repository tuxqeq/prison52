package com.prison.model;

import com.prison.exception.*;
import com.prison.test.SimpleUnitTest;

public class MealTest extends SimpleUnitTest {
    public static void main(String[] args) {
        runTest("testMealNegativeCalories", () -> {
            assertThrows(NegativeNumberException.class, () -> {
                new Meal("Breakfast Special", Meal.DietPlan.STANDARD, -500.0, Meal.MealType.Breakfast);
            });
        });
        
        runTest("testMealAllergenRequirement", () -> {
            Meal meal = new Meal("Lunch Special", Meal.DietPlan.VEGETARIAN, 600.0, Meal.MealType.Lunch);
            meal.addAllergen("Gluten");
            assertEquals(1, meal.getAllergens().size());
            
            // Cannot remove last allergen due to [1..*] multiplicity
            assertThrows(ValidationException.class, () -> {
                meal.removeAllergen("Gluten");
            });
        });
    }
}
