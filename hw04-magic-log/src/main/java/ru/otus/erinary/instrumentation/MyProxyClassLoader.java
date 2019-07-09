package ru.otus.erinary.instrumentation;

class MyProxyClassLoader extends ClassLoader {

    //TODO убрать хардкод класса
    private static final String CLASS_NAME = "ru.otus.erinary.instrumentation.MyClass";

    Class<?> defineClass(byte[] originalClass) {
        return super.defineClass(CLASS_NAME, originalClass, 0, originalClass.length);
    }
}
