package com.example.gatewayservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallBackController {

    @GetMapping("userServiceFallBack")
    public String userServiceFallBack() {
        return "User service is taking longer than expected. Please try again later";
    }

    @GetMapping("todoServiceFallBack")
    public String todoServiceFallBack() {
        return "TODO service is taking longer than expected. Please try again later";
    }

}
