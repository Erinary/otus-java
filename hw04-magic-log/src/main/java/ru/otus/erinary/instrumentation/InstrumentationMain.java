package ru.otus.erinary.instrumentation;

import ru.otus.erinary.common.MyClassImpl;

public class InstrumentationMain {

    public static void main(String[] args) {
        MyClassImpl myClass = new MyClassImpl();
        myClass.print(404);
        myClass.sayHi("username");
    }
}
