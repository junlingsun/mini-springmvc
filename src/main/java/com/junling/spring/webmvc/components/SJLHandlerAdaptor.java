package com.junling.spring.webmvc.components;

import com.junling.spring.annotation.SJLRequestedParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SJLHandlerAdaptor {

    public SJLModelAndView handle(HttpServletRequest req, HttpServletResponse resp, SJLHandlerMapping handlerMapping) {

        Map<String, String[]> parameterMap = req.getParameterMap(); //TODO: need to check what is included in parameterMap
        //obtain bean name from method
        Method method =handlerMapping.getMethod();

        Map<String,Integer> parameterIndexMap = new HashMap<>();
        Annotation[][] pas = method.getParameterAnnotations();
        Class<?>[] parameterTypes = method.getParameterTypes();

        for (int i = 0; i < pas.length; i++) {

            if (parameterTypes[i] == HttpServletRequest.class || parameterTypes[i] == HttpServletResponse.class) {
                parameterIndexMap.put(parameterTypes[i].getTypeName(), i);
            }
            if (pas[i].length == 0) continue;
            for (Annotation annotation: pas[i]) {
                if (annotation instanceof  SJLRequestedParam) {
                    String paramName = ((SJLRequestedParam) annotation).value(); //TODO: valaue() can be null;
                    parameterIndexMap.put(paramName, i);
                }
            }
        }

        Object[] parameters = new Object[parameterTypes.length];
        for (String paramName: parameterMap.keySet()) {
            String[] paramValues = parameterMap.get(paramName);
            String value = Arrays.toString(paramValues).replaceAll("\\[", "")
                    .replaceAll("\\]", "").replaceAll("\\s", "");
            if (!parameterIndexMap.containsKey(paramName)) continue;
            int index = parameterIndexMap.get(paramName);
            parameters[index] = TypeConvertor(value, parameterTypes[index]);
        }

        //TODO: getTypeName vs GetName

        if (parameterIndexMap.containsKey(HttpServletRequest.class.getTypeName())) {
            int index = parameterIndexMap.get(HttpServletRequest.class.getTypeName());
            parameters[index] = req;
        }

        if (parameterIndexMap.containsKey(HttpServletResponse.class.getTypeName())) {
            int index = parameterIndexMap.get(HttpServletResponse.class.getTypeName());
            parameters[index] = resp;
        }

        try {
            Object result = method.invoke(handlerMapping.getInstance(), parameters); //TODO: handle the scenario if ioc cannot find the bean

            if (result != null) {
                return (SJLModelAndView) result;
            }
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Object TypeConvertor(String value, Class<?> parameterType) {
        if (parameterType == String.class) return value;
        if (parameterType == Integer.class) return Integer.valueOf(value);
        if (parameterType == Double.class) return Double.valueOf(value);//TODO: more types

        if (value != null) return value; //return if parameterType is req or resp.

        return null;
    }

    private String convertToBeanName(String name) {
        char[] chars = name.toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);
        return String.valueOf(chars);
    }
}


