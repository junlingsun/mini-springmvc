package com.junling.spring.bean.config;

public class SJLBeanDefinition {

    private String beanName;
    private String className;



    public void setClassName(String className) {
        this.className = className;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getClassName() {
        return className;
    }

    public String getBeanName() {
        return beanName;
    }
}
