package com.junling.spring.webmvc.components;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

public class SJLHandlerMapping {

    private Method method;
    private Object instance;
    private Pattern pattern;


    public Method getMethod() {
        return method;
    }


    public void setMethod(Method method) {
        this.method = method;
    }

    public Object getInstance() {
        return instance;
    }

    public void setInstance(Object instance) {
        this.instance = instance;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }
}
