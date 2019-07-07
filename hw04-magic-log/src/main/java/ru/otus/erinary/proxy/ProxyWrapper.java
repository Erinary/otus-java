package ru.otus.erinary.proxy;

import ru.otus.erinary.common.Log;
import ru.otus.erinary.common.MyClassInterface;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

class ProxyWrapper {

    static MyClassInterface createMyClass(MyClassInterface myClass) {
        InvocationHandler handler = new MyInvocationHandler(myClass);
        return (MyClassInterface) Proxy.newProxyInstance(ProxyWrapper.class.getClassLoader(),
                new Class<?>[]{MyClassInterface.class}, handler);
    }

    static class MyInvocationHandler implements InvocationHandler {
        private final MyClassInterface myClass;
        private final Set<String> loggedMethods;

        MyInvocationHandler(MyClassInterface myClass) {
            this.myClass = myClass;
            this.loggedMethods = new HashSet<>();
            for (Method method : myClass.getClass().getDeclaredMethods()) {
                if (method.getAnnotation(Log.class) != null) {
                    loggedMethods.add(method.getName() + Arrays.toString(method.getParameterTypes()));
                }
            }
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (loggedMethods.contains(method.getName() + Arrays.toString(method.getParameterTypes()))) {
                System.out.println("executed method: " + method.getName() + ", params: " + Arrays.toString(args));
            }
            return method.invoke(myClass, args);
        }
    }
}
