package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserServiceImp;

import java.security.Principal;

@Controller
public class UserController {
    UserServiceImp userServiceImp;

    public UserController(UserServiceImp userServiceImp) {
        this.userServiceImp = userServiceImp;
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
        model.addAttribute("user", userServiceImp.findByUsername(principal.getName()));
        return "user";
    }
}
