package org.auth.facul.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/test")
public class ControllerTest {
    @GetMapping("/admin")
    public String getTestRoleAdmin() {
        return "Hello world admin";
    }

    @GetMapping("/user")
    public String getTestRoleUser() {
        return "Hello world user";
    }
}
