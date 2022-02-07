package com.junling.demo.service.impl;

import com.junling.demo.service.TestService;
import com.junling.spring.annotation.SJLService;

@SJLService
public class TestServiceImpl implements TestService {

    @Override
    public String test(String name, String gender) {
        String res = ("The gender of " + name + "is " + gender);
        return res;
    }
}
