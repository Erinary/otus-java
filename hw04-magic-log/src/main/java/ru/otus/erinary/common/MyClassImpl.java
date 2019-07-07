package ru.otus.erinary.common;

public class MyClassImpl implements MyClassInterface {

    @Log
    @Override
    public void print (int a) {
        System.out.println("Got a number: " + a);
    }
}
