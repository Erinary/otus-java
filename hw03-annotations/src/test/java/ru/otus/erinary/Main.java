package ru.otus.erinary;

import ru.otus.erinary.runner.TestRunner;

public class Main {

    public static void main(String[] args) {
        TestRunner testRunner = new TestRunner();
        testRunner.run(AnnotationsTest.class);
    }
}
