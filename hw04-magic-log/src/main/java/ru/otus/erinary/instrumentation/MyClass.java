package ru.otus.erinary.instrumentation;

import ru.otus.erinary.annotation.Log;

class MyClass {

    @Log
    void print(String a) {
        System.out.println("Got a number: " + a);
    }

    @Log
    void sayHi(String name) {
        System.out.println("Hi, " + name);
    }
}
