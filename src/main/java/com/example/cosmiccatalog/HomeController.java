package com.example.cosmiccatalog;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "index"; // resolves to src/main/resources/templates/index.html
    }
}

