package com.junling.spring.aop.support;

import com.junling.spring.aop.aspect.SJLAdvice;
import com.junling.spring.aop.config.SJLAopConfig;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SJLAdvicedSupport {
    private SJLAopConfig aopConfig;
    private Class<?> targetClass;
    private Object targetInstance;
    private Pattern pointCutClassPattern;
    private Map<Method, Map<String, SJLAdvice>> methodMap;

    public SJLAdvicedSupport(SJLAopConfig aopConfig) {
        this.aopConfig = aopConfig;
    }



    public SJLAopConfig getAopConfig() {
        return aopConfig;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public Object getTargetInstance() {
        return targetInstance;
    }

    public void setAopConfig(SJLAopConfig aopConfig) {
        this.aopConfig = aopConfig;
    }

    public void setTargetClass(Class<?> targetClass) {
        this.targetClass = targetClass;

        parse();
    }

    private void parse() {
        String pointCut = aopConfig.getPointCut();
        pointCut = pointCut.replaceAll("\\.", "\\\\.");
        pointCut = pointCut.replaceAll("\\\\.\\*", ".*"); //TODO: need to check
        pointCut = pointCut.replaceAll("\\(", "\\\\(");
        pointCut = pointCut.replaceAll("\\)", "\\\\)");

        String pointCutForClass = pointCut
                .substring(0, pointCut.indexOf("\\(")-4)
                .substring(pointCut.lastIndexOf(" ")+1);
        this.pointCutClassPattern = Pattern.compile(pointCutForClass);

        methodMap = new HashMap<>();
        Pattern pointCutMethodPattern = Pattern.compile(pointCut);
        Map<String, Method> aspectMethodMap = new HashMap<>();

        try {
            Class aspectClass = Class.forName(aopConfig.getAspectClass());
            for (Method method: aspectClass.getDeclaredMethods()) {
                aspectMethodMap.put(method.getName(), method);
            }

            for (Method method: this.targetClass.getDeclaredMethods()) {
                String methodString = method.toString();
                if (methodString.contains("throws")) {
                    methodString.substring(0, methodString.indexOf("throws")).trim();
                }

                Matcher matcher = pointCutMethodPattern.matcher(methodString);
                if (matcher.matches()) {
                    Map<String, SJLAdvice> map = new HashMap<>();
                    if (aopConfig.getAspectBefore() != null && !aopConfig.getAspectBefore().equals("")) {
                        map.put("before", new SJLAdvice(aspectClass.getDeclaredConstructor().newInstance(),
                                aspectMethodMap.get(aopConfig.getAspectBefore())));
                    }

                    if (aopConfig.getAspectAfter() != null && !aopConfig.getAspectBefore().equals("")) {
                        map.put("after", new SJLAdvice(aspectClass.getDeclaredConstructor().newInstance(),
                                aspectMethodMap.get(aopConfig.getAspectAfter())));
                    }

                    if (aopConfig.getAspectAfterthrow() != null && !aopConfig.getAspectAfterthrow().equals("")) {
                        SJLAdvice advice = new SJLAdvice(aspectClass.getDeclaredConstructor().newInstance(),
                                aspectMethodMap.get(aopConfig.getAspectAfterthrow()));
                        advice.setThrowName(aopConfig.getAspectAfterThrowingName());
                        map.put("afterThrowing", advice);
                    }

                    methodMap.put(method, map);
                }

            }
        }
        catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    public void setTargetInstance(Object targetInstance) {
        this.targetInstance = targetInstance;
    }

    public boolean pointCutMatch() {

        return this.pointCutClassPattern.matcher(targetClass.getName()).matches();

    }

    public Map<String, SJLAdvice> getAdvices(Method method, Class<?> targetClass) {
        Map<String, SJLAdvice> adviceMap = methodMap.get(method);

        if (adviceMap==null || adviceMap.isEmpty()) {
            try {
                Method m = targetClass.getMethod(method.getName(),method.getParameterTypes());
                adviceMap = methodMap.get(m);
                methodMap.put(method, adviceMap);
            }
            catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return adviceMap;

    }
}
