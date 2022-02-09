package com.junling.demo.aspect;

public class LogAspect {

    public void before(){
        System.out.println("execute before method");
    }

    public void after(){
        System.out.println("execute after method");
    }

    public void afterThrowing(){
        System.out.println("execute after exception");
    }
}
