package com.prison.test;

public class SimpleUnitTest {
    
    public static void assertEquals(Object expected, Object actual) {
        if (expected == null && actual == null) return;
        if (expected != null && expected.equals(actual)) return;
        throw new RuntimeException("Assertion Failed: Expected " + expected + " but got " + actual);
    }

    public static void assertEquals(int expected, int actual) {
        if (expected != actual) {
            throw new RuntimeException("Assertion Failed: Expected " + expected + " but got " + actual);
        }
    }

    public static void assertTrue(boolean condition) {
        if (!condition) {
            throw new RuntimeException("Assertion Failed: Expected true but got false");
        }
    }

    public static void assertThrows(Class<? extends Throwable> expectedType, Runnable runnable) {
        try {
            runnable.run();
        } catch (Throwable t) {
            if (expectedType.isInstance(t)) {
                return; // Success
            }
            throw new RuntimeException("Assertion Failed: Expected exception " + expectedType.getName() + " but got " + t.getClass().getName());
        }
        throw new RuntimeException("Assertion Failed: Expected exception " + expectedType.getName() + " but no exception was thrown");
    }

    public static void runTest(String testName, Runnable test) {
        try {
            test.run();
            System.out.println("[PASS] " + testName);
        } catch (Exception e) {
            System.out.println("[FAIL] " + testName + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}
