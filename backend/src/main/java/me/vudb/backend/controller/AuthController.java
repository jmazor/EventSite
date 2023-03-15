package me.vudb.backend.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class AuthController {

/*
    @PostMapping("/authenticate")
    @ResponseBody
    public Map<String, Object> login(Authentication authentication) {
        Map<String, Object> roleInfo = new HashMap<>();

        for (GrantedAuthority authority : authentication.getAuthorities()) {
            roleInfo.put("Role", authority.getAuthority());
        }

        return roleInfo;
    }

 */
}

