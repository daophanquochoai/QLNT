package com.CNPM.QLNT.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestController {
    @RequestMapping("login")
    public String login(){
        return "login";
    }
    @RequestMapping("/admin/home")
    public String homePage(){
        return "admin/home";
    }
    @RequestMapping("/admin/roomCategory")
    public String roomCategoryPage(){
        return "admin/roomCategory";
    }
}
