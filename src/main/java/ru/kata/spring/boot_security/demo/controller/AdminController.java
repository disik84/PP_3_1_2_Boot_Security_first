package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserServiceImp;

import java.security.Principal;

@Controller
public class AdminController {
    private UserServiceImp userServiceImp;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public AdminController(UserServiceImp userServiceImp, PasswordEncoder passwordEncoder) {
        this.userServiceImp = userServiceImp;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping(value = "/admin")
    public String getAdminIndexPage(Principal principal, ModelMap model) {
        model.addAttribute("listUsers", userServiceImp.getListUsers());
        return "admin/index";
    }

    @GetMapping(value = "/admin/add-user")
    public String addUser(ModelMap model) {
        return "admin/add-user";
    }

    @PostMapping(value = "/admin/show-info-new-user")
    public String showInfoNewUser(@RequestParam(value = "username", required = false) String username,
                                  @RequestParam(value = "password", required = false) String password,
                                  @RequestParam(value = "email", required = false) String email,
                                  @RequestParam(value = "roleAdmin", required = false) String roleAdmin,
                                  @RequestParam(value = "roleUser", required = false) String roleUser,
                                  ModelMap model) {
        User user = new User(username, passwordEncoder.encode(password), email);
        Long userId = userServiceImp.addUserAndGetId(user);
        if (roleAdmin != null) {
            userServiceImp.addRole(userId, 2);
        }
        if (roleUser != null) {
            userServiceImp.addRole(userId, 1);
        }
        return "redirect:/admin";
    }

    @GetMapping(value = "/admin/delete-user")
    public String deletedUser(@RequestParam(value = "id", required = false) String id,
                              ModelMap model) {
        if (id != "") {
            userServiceImp.deleteUser(Long.parseLong(id));
        }
        return "redirect:/admin";
    }

    @GetMapping(value = "/admin/edit-user-form")
    public String editUserForm(@RequestParam(value = "id", required = false) String id,
                               ModelMap model) {
        if (id != "") {
            User user = userServiceImp.findUserById(Long.parseLong(id));
            model.addAttribute(user);
            return "/admin/edit-user-form";
        }
        return "/admin/edit-user-error";
    }

    @PostMapping(value = "/admin/edit-user")
    public String editUser(@RequestParam(value = "id", required = false) String id,
                           @RequestParam(value = "username", required = false) String username,
                           @RequestParam(value = "password", required = false) String password,
                           @RequestParam(value = "email", required = false) String email,
                           ModelMap model) {
        User user = userServiceImp.findUserById(Long.parseLong(id));
        user.setId(Long.parseLong(id));
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        userServiceImp.addUser(user);
        return "redirect:/admin";
    }
}