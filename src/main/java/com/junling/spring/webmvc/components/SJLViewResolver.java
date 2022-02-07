package com.junling.spring.webmvc.components;

import java.io.File;

public class SJLViewResolver {

    private File templateRootDir;
    private final String DEFAULT_TEMPLATE_SUFFEX = ".html";

    public SJLViewResolver(String templateRoot) {
        String url = this.getClass().getClassLoader().getResource(templateRoot).getFile();
        this.templateRootDir = new  File(url);
    }

    public SJLView resolveViewName(String viewName) {

        viewName = (viewName.endsWith(DEFAULT_TEMPLATE_SUFFEX))?viewName:(viewName + DEFAULT_TEMPLATE_SUFFEX);
        File file = new File((templateRootDir.getPath() + "/" + viewName).replaceAll("/+", "/"));

        return new SJLView(file);
    }
}
