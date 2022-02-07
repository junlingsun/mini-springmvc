package com.junling.spring.webmvc.components;

import java.util.Map;

public class SJLModelAndView {
    private String viewName;
    private Map<String,Object> model;


    public SJLModelAndView(String viewName) {
        this.viewName = viewName;

    }

    public SJLModelAndView(String viewName, Map<String, Object> model) {
        this.viewName = viewName;
        this.model = model;
    }

    public String getViewName() {
        return viewName;
    }

    public Map<String, Object> getModel() {
        return model;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public void setModel(Map<String, Object> model) {
        this.model = model;
    }
}
