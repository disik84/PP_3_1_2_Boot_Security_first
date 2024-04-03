package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import ru.kata.spring.boot_security.demo.service.RoleServiceImp;
import ru.kata.spring.boot_security.demo.service.UserServiceImp;

import java.security.Principal;

@Controller
public class AdminController {
    private final UserServiceImp userServiceImp;
    private final RoleServiceImp roleServiceImp;

    public AdminController(UserServiceImp userServiceImp, RoleServiceImp roleServiceImp) {
        this.userServiceImp = userServiceImp;
        this.roleServiceImp = roleServiceImp;
    }

    @GetMapping(value = "/admin")
    public String getAdminIndexPage(Principal principal, ModelMap model) {
        model.addAttribute("username", principal.getName());
        model.addAttribute("profileRole", roleServiceImp.getProfileRole());
        model.addAttribute("listUsers", userServiceImp.getListUsers());
        return "admin/index";
    }
}