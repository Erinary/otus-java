package ru.otus.erinary;

import ru.otus.erinary.annotations.After;
import ru.otus.erinary.annotations.Before;
import ru.otus.erinary.annotations.Test;

@SuppressWarnings("WeakerAccess")
public class AnnotationsTest {

    @Before
    void testBeforeA() {
        System.out.println("Execution before each A");
    }

    @After
    void testAfterA() {
        System.out.println("Execution after each A");
    }

    @Test
    void testOne() {
        System.out.println("Test1");
    }

    @Test
    void testTwo() {
        System.out.println("Test2");
    }

    @After
    void testAfterB() {
        System.out.println("Execution after each B");
    }

    @Test
    void testThree() {
        System.out.println("Test3");
    }

    @Before
    void testBeforeB() {
        System.out.println("Execution before each B");
    }
}
