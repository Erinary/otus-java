package ru.otus.erinary.instrumentation;

/*
    java -javaagent:MagicLog.jar -jar MagicLog.jar
*/

public class InstrumentationMain {

    public static void main(String[] args) {
        System.out.println("Main started");
        MyClass myClass = new MyClass();
        myClass.print("404");
        myClass.sayHi("username");
    }
}
