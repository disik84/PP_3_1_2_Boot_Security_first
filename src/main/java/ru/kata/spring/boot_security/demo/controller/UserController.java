package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.security.SecurityUserDetailsService;
import ru.kata.spring.boot_security.demo.service.UserServiceImp;

import java.security.Principal;

@Controller
public class UserController {
    private UserServiceImp userServiceImp;
    private SecurityUserDetailsService securityUserDetailsService;

    public UserController(UserServiceImp userServiceImp, SecurityUserDetailsService securityUserDetailsService) {
        this.userServiceImp = userServiceImp;
        this.securityUserDetailsService = securityUserDetailsService;
    }

    @GetMapping(value = "/")
    public String getIndexPage(Principal principal, ModelMap model) {
        if (principal != null) {
            model.addAttribute("login", "logout");
            model.addAttribute("login_text", "Выход");
        } else {
            model.addAttribute("login", "login");
            model.addAttribute("login_text", "Вход");
        }
        return "index";
    }

    @GetMapping(value = "/user")
    public String getAdminIndexPage(Principal principal, ModelMap model) {
        model.addAttribute("username", principal.getName());
        model.addAttribute("profileRole", userServiceImp.getProfileRole());
        model.addAttribute("user", securityUserDetailsService.findByUsername(principal.getName()));
        return "user";
    }
}
