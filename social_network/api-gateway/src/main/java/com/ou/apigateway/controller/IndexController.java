package com.ou.apigateway.controller;

import java.util.Map;

import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@PropertySource("classpath:configs.properties")
public class IndexController {
    @RequestMapping("/login")
    public String index(Model model, @RequestParam Map<String, String> params){
        if(params.containsKey("error")){
            model.addAttribute("loginError", true);
        }
        return "pages/index";
    }
}
