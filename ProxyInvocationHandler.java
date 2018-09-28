package com.company;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

class ProxyInvocationHandler  implements InvocationHandler {

    public ProxyInvocationHandler(Class<Test1> testClass, Object o, Object o1, Object o2) {

    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return "苏聪明";
    }
}