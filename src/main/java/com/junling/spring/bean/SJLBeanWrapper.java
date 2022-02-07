package com.junling.spring.bean;

public class SJLBeanWrapper {

    private Object instance;
    private Class clazz;

    public SJLBeanWrapper(Object instance) {
        this.instance = instance;
        clazz = instance.getClass();
    }

    public Class getClazz() {
        return clazz;
    }

    public Object getInstance() {
        return instance;
    }
}
