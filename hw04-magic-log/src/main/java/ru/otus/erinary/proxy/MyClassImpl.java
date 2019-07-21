package ru.otus.erinary.proxy;

import ru.otus.erinary.annotation.Log;

public class MyClassImpl implements MyClassInterface {

    @Log
    @Override
    public void print(int a) {
        System.out.println("Got a number: " + a);
    }

    @Override
    public void sayHi(String name) {
        System.out.println("Hi, " + name);
    }
}
