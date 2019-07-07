package ru.otus.erinary.runner;

import ru.otus.erinary.annotations.After;
import ru.otus.erinary.annotations.Before;
import ru.otus.erinary.annotations.Test;
import ru.otus.erinary.assertion.AssertionException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TestRunner {

    private List<Method> beforeEachList;
    private List<Method> afterEachList;
    private List<Method> testList;
    private Map<String, TestResult> testResultMap;

    public TestRunner() {
        this.beforeEachList = new ArrayList<>();
        this.afterEachList = new ArrayList<>();
        this.testList = new ArrayList<>();
        this.testResultMap = new LinkedHashMap<>();
    }

    public void run(Class<?> testClass) {
        sortMethodsByAnnotation(testClass.getDeclaredMethods());
        for (Method testMethod : testList) {
            Constructor<?> constructor = testClass.getConstructors()[0];
            Object instance;
            try {
                instance = constructor.newInstance();
            } catch (Exception e) {
                System.out.println("Can't instantiate test");
                e.printStackTrace(System.out);
                return;
            }
            try {
                for (Method m : beforeEachList) {
                    m.invoke(instance);
                }
                testMethod.invoke(instance);
                testResultMap.put(testMethod.getName(), TestResult.PASSED);
            } catch (Exception e) {
                if (e instanceof InvocationTargetException) {
                    if (e.getCause() instanceof AssertionException) {
                        testResultMap.put(testMethod.getName(), TestResult.FAILED);
                        System.out.println("Test failed because of: " + e.getCause().getMessage());
                    } else {
                        testResultMap.put(testMethod.getName(), TestResult.EXCEPTION);
                        System.out.println("Exception in test");
                        e.getCause().printStackTrace(System.out);
                    }
                } else {
                    testResultMap.put(testMethod.getName(), TestResult.EXCEPTION);
                    e.printStackTrace(System.out);
                }
            } finally {
                for (Method m : afterEachList) {
                    try {
                        m.invoke(instance);
                    } catch (Exception e) {
                        System.out.println("AfterEach block failed");
                        e.printStackTrace(System.out);
                    }
                }
            }
            System.out.println("--------------");
        }
        System.out.println("Test set results:");
        testResultMap.forEach((key, value) -> System.out.println(key + " " + value));
    }

    private void sortMethodsByAnnotation(Method[] methods) {
        for (Method method : methods) {
            if (method.getAnnotation(Test.class) != null) {
                testList.add(method);
            } else if (method.getAnnotation(Before.class) != null) {
                beforeEachList.add(method);
            } else if (method.getAnnotation(After.class) != null) {
                afterEachList.add(method);
            }
        }
    }

    private enum TestResult {
        PASSED, FAILED, EXCEPTION
    }
}
