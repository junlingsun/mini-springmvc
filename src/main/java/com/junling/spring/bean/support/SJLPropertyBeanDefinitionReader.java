package com.junling.spring.bean.support;

import com.junling.spring.annotation.SJLController;
import com.junling.spring.annotation.SJLService;
import com.junling.spring.bean.config.SJLBeanDefinition;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SJLPropertyBeanDefinitionReader {

    private String contextConfig;
    private Properties properties = new Properties();
    private List<String> classNames = new ArrayList<>();


    public SJLPropertyBeanDefinitionReader(String contextConfig) {
        this.contextConfig = contextConfig;
        loadConfig();
        doScan(properties.getProperty("scan-package")); //TODO: dynamically get properties
    }


    public List<SJLBeanDefinition> loadBeanDefinitions() {
        List<SJLBeanDefinition> beanDefinitionList = new ArrayList<>();

        for (String className: classNames) {
            SJLBeanDefinition beanDefinition = new SJLBeanDefinition();

            try {
                Class clazz = Class.forName(className);
                if (clazz.isInterface()) continue;
                if (!clazz.isAnnotationPresent(SJLController.class) && !clazz.isAnnotationPresent(SJLService.class)) continue;

                beanDefinition.setBeanName(convertToBeanName(clazz.getSimpleName()));
                beanDefinition.setClassName(clazz.getName()); //full class name; =className
                beanDefinitionList.add(beanDefinition);

                for (Class ifac:clazz.getInterfaces()) {
                    SJLBeanDefinition interfaceDefinition = new SJLBeanDefinition();
                    interfaceDefinition.setBeanName(convertToBeanName(ifac.getSimpleName())); //interface bean name
                    interfaceDefinition.setClassName(clazz.getName()); // implementation full class name
                    beanDefinitionList.add(interfaceDefinition);
                }

            }
            catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return beanDefinitionList;
    }

    private void loadConfig(){
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(contextConfig);
        try {
            properties.load(inputStream);
        }
        catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void doScan(String path) {
        URL resource = this.getClass().getResource("/" + path.replaceAll("\\.", "/"));

        if (resource == null) return;
        File file = new File(resource.getFile());

        for (File f: file.listFiles()) {
            if (f.isFile()) {
                if (!f.getName().endsWith(".class")) continue;
                classNames.add(path + "." + f.getName().replaceAll(".class", ""));
            }else {
                doScan(path + "." + f.getName());
            }
        }

    }

    private String convertToBeanName(String name) {
        char[] chars = name.toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);
        return String.valueOf(chars);
    }

    public Properties getPropertyReader(){
        return properties;
    }
}
