package com.junling.demo.controller;

import com.junling.demo.service.TestService;
import com.junling.spring.annotation.SJLAutowire;
import com.junling.spring.annotation.SJLController;
import com.junling.spring.annotation.SJLRequestMapping;
import com.junling.spring.annotation.SJLRequestedParam;
import com.junling.spring.webmvc.components.SJLModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@SJLController
public class TestController {

    @SJLAutowire
    private TestService testService;

    @SJLRequestMapping("/demo")
    public void demo(@SJLRequestedParam("name") String name, @SJLRequestedParam("gender") String gender, HttpServletResponse resp, HttpServletRequest req){

        String res = testService.test(name, gender);
        try {
            resp.getWriter().write(res);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SJLRequestMapping("/test")
    public SJLModelAndView test(HttpServletRequest req, HttpServletResponse resp) {
        String viewName = "test";
        Map<String, Object> model = new HashMap<>();
        model.put("name", "abc");
        SJLModelAndView modelAndView = new SJLModelAndView(viewName, model);
        return modelAndView;
    }
}
