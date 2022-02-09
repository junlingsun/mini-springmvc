package com.junling.spring.bean.support;

import com.junling.spring.bean.config.SJLBeanDefinition;

import java.util.List;

public interface SJLBeanDefinitionReader {
    public List<SJLBeanDefinition> loadBeanDefinitions();
}
