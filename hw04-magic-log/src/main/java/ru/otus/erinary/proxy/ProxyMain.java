package ru.otus.erinary.proxy;

import ru.otus.erinary.common.MyClassImpl;
import ru.otus.erinary.common.MyClassInterface;

public class ProxyMain {

    public static void main(String[] args) {
        MyClassInterface myClass = ProxyWrapper.createMyClass(new MyClassImpl());
        myClass.print(404);
        myClass.sayHi("username");
    }
}
