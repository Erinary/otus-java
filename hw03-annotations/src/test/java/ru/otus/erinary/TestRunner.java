package ru.otus.erinary;

import ru.otus.erinary.annotations.After;
import ru.otus.erinary.annotations.Before;
import ru.otus.erinary.annotations.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TestRunner {

    public static void main(String[] args) {
        run(AnnotationsTest.class);
    }

    private static void run(Class<?> testClass) {
        List<Method> beforeEachList = new ArrayList<>();
        List<Method> afterEachList = new ArrayList<>();
        List<Method> testList = new ArrayList<>();
        Method[] methods = testClass.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getAnnotation(Test.class) != null) {
                testList.add(method);
            } else if (method.getAnnotation(Before.class) != null) {
                beforeEachList.add(method);
            } else if (method.getAnnotation(After.class) != null) {
                afterEachList.add(method);
            }
        }
        for (Method testMethod : testList) {
            try {
                Constructor<?> constructor = testClass.getConstructors()[0];
                Object instance = constructor.newInstance();

                for (Method m : beforeEachList) {
                    m.invoke(instance);
                }
                testMethod.invoke(instance);
                for (Method m : afterEachList) {
                    m.invoke(instance);
                }
            } catch (Exception e) {
                if (e instanceof InvocationTargetException) {
                    System.out.println("Exception in test");
                    e.getCause().printStackTrace(System.out);
                } else {
                    e.printStackTrace(System.out);
                }
            }
            System.out.println("--------------");
        }
    }
}
