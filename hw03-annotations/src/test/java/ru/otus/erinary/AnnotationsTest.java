package ru.otus.erinary;

import ru.otus.erinary.annotations.After;
import ru.otus.erinary.annotations.Before;
import ru.otus.erinary.annotations.Test;

@SuppressWarnings("WeakerAccess")
public class AnnotationsTest {

    @Before
    void testBefore() {
        System.out.println("Execution before each");
    }

    @After
    void testAfter() {
        System.out.println("Execution after each");
    }

    @Test
    void testOne() {
        System.out.println("Test1");
    }

    @Test
    void testTwo() {
        System.out.println("Test2");
    }

    @Test
    void testThree() {
        System.out.println("Test3");
    }
}
