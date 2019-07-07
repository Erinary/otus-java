package ru.otus.erinary.proxy;

import ru.otus.erinary.common.MyClassImpl;
import ru.otus.erinary.common.MyClassInterface;

public class ProxyWrapper {

    static MyClassInterface createMyClass() {
        return new MyClassImpl();
    }
}
