package com.example.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class SpringBootController {

    @RequestMapping(value = "/springBoot/say")
    public @ResponseBody String say() {
        return "Hello,springBoot!";
    }

    @RequestMapping(value = "/abc/{name}")
    public @ResponseBody String name(@PathVariable("name") String name) {
        return name;
    }



}
