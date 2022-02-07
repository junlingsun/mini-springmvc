package com.junling.spring.webmvc.servlet;

import com.junling.spring.annotation.*;
import com.junling.spring.context.SJLApplicationContext;
import com.junling.spring.webmvc.components.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SJLServlet extends HttpServlet {

    private SJLApplicationContext applicationContext;
    private List<SJLHandlerMapping> handlerMappings = new ArrayList<>();
    private Map<SJLHandlerMapping, SJLHandlerAdaptor> handlerAdaptorMap = new HashMap<>();
    private List<SJLViewResolver> viewResolvers = new ArrayList<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doDispatch(req, resp);
    }

    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) {

        SJLHandlerMapping handlerMapping = getHandlerMapping(req);
        if (handlerMapping == null) {
            dispatch(req, resp, new SJLModelAndView("404"));
            return;
        }

        SJLHandlerAdaptor handlerAdaptor = handlerAdaptorMap.get(handlerMapping);

        SJLModelAndView modelAndView = handlerAdaptor.handle(req, resp, handlerMapping);

        processDispatchResult(req, resp, modelAndView);


    }

    private void processDispatchResult(HttpServletRequest req, HttpServletResponse resp, SJLModelAndView modelAndView) {
        if (modelAndView == null) return;
        if (viewResolvers == null || viewResolvers.size() == 0) return;

        for (SJLViewResolver viewResolver: viewResolvers) {
            SJLView view = viewResolver.resolveViewName(modelAndView.getViewName());
            view.render(req, resp, modelAndView.getModel());
            return;
        }
    }

    private void dispatch(HttpServletRequest req, HttpServletResponse resp, SJLModelAndView sjlModelAndView) {
    }



    private SJLHandlerMapping getHandlerMapping(HttpServletRequest req) {
        String requestURI = req.getRequestURI();
        String contextPath = req.getContextPath();
        String url = ("/"+requestURI).replaceAll(contextPath, "").replaceAll("/+", "/");

        for (SJLHandlerMapping handlerMapping: handlerMappings) {
            Pattern pattern = handlerMapping.getPattern();
            Matcher matcher = pattern.matcher(url);
            if (matcher.matches()) {
                return handlerMapping;
            }
        }
        return null;

    }

    @Override
    public void init(ServletConfig config) throws ServletException {

        applicationContext = new SJLApplicationContext(config.getInitParameter("contextConfig"));

        //strategies
        initStrategies();

    }

    private void initStrategies() {

        initHandlerMapping();
        initHandlerAdapters();
        initViewResolver();

    }

    private void initViewResolver() {

        Properties properties = applicationContext.getPropertyReader();
        String templatePath = properties.getProperty("template-path");
        String templateUrl = this.getClass().getClassLoader().getResource(templatePath).getFile();
        File file = new File(templateUrl);
        File[] files = file.listFiles();
        if (files == null || files.length==0) return;
        for (File f: files) {
            viewResolvers.add(new SJLViewResolver(templatePath));
        }


    }

    private void initHandlerAdapters() {
        if (handlerMappings.isEmpty()) return;
        for (SJLHandlerMapping handlerMapping: handlerMappings) {
            handlerAdaptorMap.put(handlerMapping, new SJLHandlerAdaptor());
        }
    }

    private void initHandlerMapping() {

        if (applicationContext.getBeanDefinitionCount() == 0) return;

        for (String beanName: applicationContext.getBeanNames()) {
            Object instance = applicationContext.getBean(beanName);
            Class clazz = instance.getClass();
            Method[] methods = clazz.getDeclaredMethods();
            String baseUrl = "";
            if (clazz.isAnnotationPresent(SJLRequestMapping.class)) {
                baseUrl = ((SJLRequestMapping)clazz.getAnnotation(SJLRequestMapping.class)).value();
            }

            for (Method method: methods) {
                if (!method.isAnnotationPresent(SJLRequestMapping.class)) continue;
                SJLRequestMapping annotation = method.getAnnotation(SJLRequestMapping.class);

                String url = ("/" + baseUrl + "/" + annotation.value()).replaceAll("/+", "/");
                String regex = url.replaceAll("\\*", ".*");
                Pattern pattern = Pattern.compile(regex);

                SJLHandlerMapping handlerMapping = new SJLHandlerMapping();
                handlerMapping.setPattern(pattern);
                handlerMapping.setMethod(method);
                handlerMapping.setInstance(instance);
                handlerMappings.add(handlerMapping);
            }
        }
    }














}
