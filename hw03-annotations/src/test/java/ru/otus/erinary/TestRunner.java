package ru.otus.erinary;

import ru.otus.erinary.annotations.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TestRunner {

    public static void main(String[] args) {
        run(AnnotationsTest.class);
    }

    private static void run(Class<?> testClass) {
        Method[] methods = testClass.getDeclaredMethods();
        for (Method method: methods) {
            try {
                Constructor<?> constructor = testClass.getEnclosingConstructor();
                Object instance = constructor.newInstance();
                if (method.getAnnotation(Test.class) != null) {
                    method.invoke(instance);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}
