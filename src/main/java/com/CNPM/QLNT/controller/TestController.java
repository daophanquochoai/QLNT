package com.CNPM.QLNT.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestController {
    @RequestMapping("login")
    public String login(){
        return "login";
    }
    @RequestMapping("/admin")
    public String homePage(){
        return "redirect:/admin/home";
    }
    @RequestMapping("/admin/{menu}")
    public String getMenuPage(@PathVariable String menu){
        System.out.println("admin/"+menu);
        return "admin/"+menu;
    }
}
