package com.junling.spring.aop;

import com.junling.spring.aop.aspect.SJLAdvice;
import com.junling.spring.aop.support.SJLAdvicedSupport;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

public class SJLjdkDynamicAopProxy implements InvocationHandler {
    private SJLAdvicedSupport advicedSupport;
    public SJLjdkDynamicAopProxy(SJLAdvicedSupport advicedSupport) {
        this.advicedSupport = advicedSupport;
    }


    public Object getProxyObject() {

        return Proxy.newProxyInstance(this.getClass().getClassLoader(), advicedSupport.getTargetClass().getInterfaces(),this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object targetInstance = advicedSupport.getTargetInstance();
        Class<?> targetClass = advicedSupport.getTargetClass();

        Map<String, SJLAdvice> adviceMap = advicedSupport.getAdvices(method, targetClass);
        SJLAdvice beforeAdvice = adviceMap.get("before");
        beforeAdvice.getAspectMethod().invoke(beforeAdvice.getAspectInstance());
        Object instance = null;
        try{
            instance = method.invoke(targetInstance, args);
        }catch (Exception e) {
            SJLAdvice throwAdvice = adviceMap.get("afterThrowing");
            throwAdvice.getAspectMethod().invoke(throwAdvice.getAspectInstance());
        }

        SJLAdvice afterAdvice = adviceMap.get("after");
        afterAdvice.getAspectMethod().invoke(afterAdvice.getAspectInstance());

        return instance;
    }
}
