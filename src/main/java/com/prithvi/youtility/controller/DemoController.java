package com.prithvi.youtility.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @GetMapping("/")
    public String demoResponse() {
        return "This is my demo Controller, Hello User !!!";
    }

    @GetMapping("/{userName}")
    public String demoResp(@PathVariable String userName) {
        return "This is also my demo Controller, Hello User "+userName+" !!!";
    }

    @GetMapping("/test")
    public String demoTestResponse() {
        return "This is my demo Controller, Hello User !!!";
    }

    @GetMapping("/getMessage")
    public String getMessage() {
        return "We Built A House, You Make It Home!!!";
    }
}
