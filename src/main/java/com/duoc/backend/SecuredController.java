package com.duoc.backend;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

@RestController
public class SecuredController {

    @GetMapping("/greetings")
    public String greetings(@RequestParam(value = "name", defaultValue = "World") String name) {
        return "Hello {" + HtmlUtils.htmlEscape(name) + "}";
    }
}