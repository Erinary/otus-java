package ru.otus.erinary.assertion;

public class DummyAssertion {

    public static void assertEquals(int expected, int actual) {
        if (expected != actual) {
            throw new AssertionException("Assertion failed. Expected: " + expected + ", actual: " + actual);
        }
    }
}
