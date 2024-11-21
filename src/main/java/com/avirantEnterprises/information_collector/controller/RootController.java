package com.avirantEnterprises.information_collector.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootController {
    @GetMapping("/dashboard")
    public String dashboard()
    {
        return "index";
    }
}
