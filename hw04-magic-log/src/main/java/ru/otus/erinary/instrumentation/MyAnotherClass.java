package ru.otus.erinary.instrumentation;

import ru.otus.erinary.annotation.Log;

class MyAnotherClass {

    @Log
    void sayBye(String name) {
        System.out.println("Bye, " + name);
    }
}
