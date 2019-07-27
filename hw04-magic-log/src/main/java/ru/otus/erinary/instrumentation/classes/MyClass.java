package ru.otus.erinary.instrumentation.classes;

import ru.otus.erinary.annotation.Log;

public class MyClass {

    @Log
    public void print(String a) {
        System.out.println("Got a number: " + a);
    }

    @Log
    public void sayHi(String name) {
        System.out.println("Hi, " + name);
    }
}
