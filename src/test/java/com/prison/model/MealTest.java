package com.prison.model;

import com.prison.exception.*;
import com.prison.test.SimpleUnitTest;

public class MealTest extends SimpleUnitTest {
    public static void main(String[] args) {
        runTest("testMealNegativeCalories", () -> {
            assertThrows(NegativeNumberException.class, () -> {
                new Meal(Meal.DietPlan.STANDARD, -500);
            });
        });
    }
}
