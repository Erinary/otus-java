package ru.otus.erinary.instrumentation.classes;

import ru.otus.erinary.annotation.Log;

public class MyAnotherClass {

    @Log
    public void sayBye(String name) {
        System.out.println("Bye, " + name);
    }
}
