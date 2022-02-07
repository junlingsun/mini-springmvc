package com.junling.spring.context;

import com.junling.spring.annotation.SJLAutowire;
import com.junling.spring.bean.SJLBeanWrapper;
import com.junling.spring.bean.config.SJLBeanDefinition;
import com.junling.spring.bean.support.SJLPropertyBeanDefinitionReader;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class SJLApplicationContext {

    private String contextConfig;
    private SJLPropertyBeanDefinitionReader reader;
    private Map<String, SJLBeanDefinition> beanDefinitionMap = new HashMap<>();
    private Map<String, SJLBeanWrapper> factoryBeanInstanceCache = new HashMap<>();
    private Map<String, Object> factoryBeanObjectCache = new HashMap<>();


    public SJLApplicationContext(String contextConfig){
        this.contextConfig = contextConfig;
        reader = new SJLPropertyBeanDefinitionReader(contextConfig);
        List<SJLBeanDefinition> beanDefinitionList = reader.loadBeanDefinitions();
        registerBeanDefinition(beanDefinitionList);

    }


    public Object getBean(Class clazz) {
        return getBean(convertToBeanName(clazz.getSimpleName()));
    }

    public Object getBean(String beanName){

        Object instance = initiateBean(beanDefinitionMap.get(beanName));

        SJLBeanWrapper beanWrapper = new SJLBeanWrapper(instance);
        factoryBeanInstanceCache.put(beanName, beanWrapper);

        //dependency injection
        populateBean(beanWrapper);

        return instance;
    }

    private void populateBean(SJLBeanWrapper beanWrapper) {

        Object instance = beanWrapper.getInstance();
        Class clazz = beanWrapper.getClazz();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field: fields) {
            if (!field.isAnnotationPresent(SJLAutowire.class)) continue;
            String fieldName = convertToBeanName(field.getType().getSimpleName());
            field.setAccessible(true);

            Object filedInstance = null;

            if (!factoryBeanInstanceCache.containsKey(fieldName)) {
                filedInstance = getBean(fieldName);
            }

            try {
                field.set(instance, factoryBeanInstanceCache.get(fieldName).getInstance());
            }
            catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }


    private Object initiateBean(SJLBeanDefinition sjlBeanDefinition) {

        String className = sjlBeanDefinition.getClassName();
        String beanName = sjlBeanDefinition.getBeanName();
        try {
            Class<?> clazz = Class.forName(className);

            //TODO: AOP

            Object instance = clazz.getDeclaredConstructor().newInstance();
            factoryBeanObjectCache.put(beanName, instance);

            return instance;
        }
        catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;

    }


    private void registerBeanDefinition(List<SJLBeanDefinition> beanDefinitionList) {
        for (SJLBeanDefinition beanDefinition: beanDefinitionList) {
            beanDefinitionMap.put(beanDefinition.getBeanName(), beanDefinition);
        }
    }


    private String convertToBeanName(String name) {
        char[] chars = name.toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);
        return String.valueOf(chars);
    }


    public int getBeanDefinitionCount() {
        return beanDefinitionMap.size();
    }

    public List<String> getBeanNames() {
        List<String> list = new ArrayList<>();
        list.addAll(beanDefinitionMap.keySet());
        return list;
    }

    public Properties getPropertyReader(){
        return reader.getPropertyReader();
    }
}
