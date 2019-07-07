package ru.otus.erinary.proxy;

import ru.otus.erinary.common.MyClassInterface;

public class ProxyMain {

    public static void main(String[] args) {
        MyClassInterface myClass = ProxyWrapper.createMyClass();
        myClass.print(404);
    }
}
