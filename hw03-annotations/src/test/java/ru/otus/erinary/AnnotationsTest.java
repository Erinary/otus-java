package ru.otus.erinary;

import ru.otus.erinary.annotations.After;
import ru.otus.erinary.annotations.Before;
import ru.otus.erinary.annotations.Test;
import ru.otus.erinary.assertion.DummyAssertion;

public class AnnotationsTest {

    @Before
    public void testBeforeA() {
        System.out.println("Execution before each A");
    }

    @After
    public void testAfterA() {
        System.out.println("Execution after each A");
    }

    @Test
    public void testOne() {
        System.out.println("Test1");
    }

    @Test
    public void testTwo() {
        System.out.println("Test2");
        throw new NullPointerException();
    }

    @After
    public void testAfterB() {
        System.out.println("Execution after each B");
    }

    @Test
    public void testThree() {
        System.out.println("Test3");
        DummyAssertion.assertEquals(1, 2);
    }

    @Before
    public void testBeforeB() {
        System.out.println("Execution before each B");
    }
}
