package ru.otus.erinary.proxy;

public class ProxyMain {

    public static void main(String[] args) {
        MyClassInterface myClass = ProxyWrapper.createMyClass(new MyClassImpl());
        myClass.print(404);
        myClass.sayHi("username");
    }
}
