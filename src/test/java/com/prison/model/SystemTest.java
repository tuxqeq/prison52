package com.prison.model;

import com.prison.exception.*;
import com.prison.test.SimpleUnitTest;
import java.time.LocalDate;
import java.time.LocalTime;

public class SystemTest extends SimpleUnitTest {

    public static void main(String[] args) {
        System.out.println("Running SystemTest...");

        runTest("testGuardCreation", () -> {
            Guard.clearGuardExtent();
            Guard g = new Guard("Paul", "Blart", 5, "Day Shift", "555-0100", 
                "blart@prison.com", Guard.Rank.SENIOR, "Baton");
            assertEquals(Guard.Rank.SENIOR, g.getRank());
            assertEquals(1, Guard.getGuardExtent().size());
        });

        runTest("testGuardInvalidRank", () -> {
            assertThrows(InvalidReferenceException.class, () -> {
                new Guard("Paul", "Blart", 5, "Day Shift", "555-0100", 
                    "blart@prison.com", null, "Baton");
            });
        });

        runTest("testBlockNegativeCells", () -> {
            assertThrows(NegativeNumberException.class, () -> {
                new Block("A", -1, Block.BlockType.MINIMUM_SECURITY);
            });
        });

        runTest("testBlockEmptyName", () -> {
            assertThrows(EmptyStringException.class, () -> {
                new Block("", 10, Block.BlockType.MINIMUM_SECURITY);
            });
        });

        runTest("testVisitPastDate", () -> {
            Prisoner prisoner = new Prisoner("Test", "Prisoner", 30, "Test", 
                LocalDate.of(2020, 1, 1), 5, "None", "Active");
            Visitor visitor = new Visitor("Test", "Visitor", "555-0000", "Friend");
            
            assertThrows(InvalidDateException.class, () -> {
                new Visit(LocalDate.now().minusDays(1), 60, Visit.VisitType.FAMILY, visitor);
            });
            
            Prisoner.clearExtent();
            Visitor.clearExtent();
        });

        runTest("testScheduleEndTimeBeforeStartTime", () -> {
            assertThrows(InvalidDateException.class, () -> {
                new Schedule(LocalTime.of(10, 0), LocalTime.of(9, 0), Schedule.ActivityType.Work);
            });
        });

        runTest("testScheduleValid", () -> {
            Schedule.clearExtent();
            new Schedule(LocalTime.of(9, 0), LocalTime.of(10, 0), Schedule.ActivityType.Work);
            assertEquals(1, Schedule.getExtent().size());
        });

        runTest("testMedicalExamFutureDate", () -> {
            Prisoner prisoner = new Prisoner("Test", "Prisoner", 30, "Test", 
                LocalDate.of(2020, 1, 1), 5, "None", "Active");
            Doctor doctor = new Doctor("Dr.", "Test", 5, "9am-5pm", "555-0100", 
                "test@hospital.com", "MD123", "555-0101");
            
            assertThrows(InvalidDateException.class, () -> {
                new MedicalExamination(LocalDate.now().plusDays(1), MedicalExamination.ReasonForVisit.Routine, doctor);
            });
            
            Prisoner.clearExtent();
            Doctor.clearDoctorExtent();
        });

        runTest("testMealNegativeCalories", () -> {
            assertThrows(NegativeNumberException.class, () -> {
                new Meal("Test Meal", Meal.DietPlan.STANDARD, -500.0, Meal.MealType.Breakfast);
            });
        });
    }
}
