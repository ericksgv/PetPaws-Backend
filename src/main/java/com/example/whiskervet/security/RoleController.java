package com.example.whiskervet.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.whiskervet.entity.Role;

@RestController
public class RoleController {
    @Autowired
    UserRoleService userRoleService;

    @GetMapping("/user/roles")
    public String getUserRoles() {
        List<Role> userRoles = userRoleService.getUserRoles();
        return userRoles.get(0).getName().toString();
    }
}