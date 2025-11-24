package com.prison.model;

import com.prison.exception.*;
import com.prison.test.SimpleUnitTest;
import java.time.LocalDate;

public class MealDeliveryTest extends SimpleUnitTest {
    public static void main(String[] args) {
        runTest("testMealDeliveryValidation", () -> {
            Prisoner prisoner = new Prisoner("Test", "Prisoner", 30, "Test", 
                LocalDate.of(2020, 1, 1), 5, "None", "Active");
            Meal meal = new Meal(Meal.DietPlan.STANDARD, 600);
            
            assertThrows(InvalidReferenceException.class, () -> {
                new MealDelivery(null, prisoner, meal);
            });
            
            Prisoner.clearExtent();
            Meal.clearExtent();
        });
    }
}
