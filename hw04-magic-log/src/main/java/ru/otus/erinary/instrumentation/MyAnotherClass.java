package ru.otus.erinary.instrumentation;

import ru.otus.erinary.common.Log;

class MyAnotherClass {

    @Log
    void sayBye(String name) {
        System.out.println("Bye, " + name);
    }
}
