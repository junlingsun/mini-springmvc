package com.junling.spring.aop.aspect;

import java.lang.reflect.Method;

public class SJLAdvice {

    private Object aspectInstance;
    private Method aspectMethod;
    private String throwName;

    public SJLAdvice(Object aspectInstance, Method aspectMethod) {
        this.aspectInstance = aspectInstance;
        this.aspectMethod = aspectMethod;
    }

    public void setThrowName(String throwName) {
        this.throwName = throwName;
    }

    public void setAspectInstance(Object aspectInstance) {
        this.aspectInstance = aspectInstance;
    }

    public void setAspectMethod(Method aspectMethod) {
        this.aspectMethod = aspectMethod;
    }

    public String getThrowName() {
        return throwName;
    }

    public Object getAspectInstance() {
        return aspectInstance;
    }

    public Method getAspectMethod() {
        return aspectMethod;
    }
}
