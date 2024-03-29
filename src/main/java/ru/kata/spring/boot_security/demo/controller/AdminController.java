package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.service.UserServiceImp;

import java.security.Principal;

@Controller
public class AdminController {
    private final UserServiceImp userServiceImp;

    @Autowired
    public AdminController(UserServiceImp userServiceImp) {
        this.userServiceImp = userServiceImp;
    }

    @GetMapping(value = "/admin")
    public String getAdminIndexPage(Principal principal, ModelMap model) {
        model.addAttribute("username", principal.getName());
        model.addAttribute("profileRole", userServiceImp.getProfileRole());
        model.addAttribute("listUsers", userServiceImp.getListUsers());
        return "admin/index";
    }

    @GetMapping(value = "/admin/add-user")
    public String addUser(Principal principal, ModelMap model) {
        model.addAttribute("username", principal.getName());
        model.addAttribute("profileRole", userServiceImp.getProfileRole());
        return "admin/add-user";
    }

    @PostMapping(value = "/admin/show-info-new-user")
    public String showInfoNewUser(@RequestParam(value = "username", required = false) String username,
                                  @RequestParam(value = "password", required = false) String password,
                                  @RequestParam(value = "email", required = false) String email,
                                  @RequestParam(value = "roleAdmin", required = false) String roleAdmin,
                                  @RequestParam(value = "roleUser", required = false) String roleUser,
                                  ModelMap model) {
        userServiceImp.addUser(userServiceImp.updateUser(username, password, email, roleAdmin, roleUser));
        return "redirect:/admin";
    }

    @GetMapping(value = "/admin/delete-user")
    public String deletedUser(@RequestParam(value = "id") String id,
                              ModelMap model) {
        userServiceImp.deleteUser(Long.parseLong(id));
        return "redirect:/admin";
    }

    @PostMapping(value = "/admin/edit-user")
    public String editUser(@RequestParam(value = "id", required = false) String id,
                           @RequestParam(value = "username", required = false) String username,
                           @RequestParam(value = "password", required = false) String password,
                           @RequestParam(value = "email", required = false) String email,
                           @RequestParam(value = "roleAdmin", required = false) String roleAdmin,
                           @RequestParam(value = "roleUser", required = false) String roleUser,
                           ModelMap model) {
        if (userServiceImp.checkNullEditUser(username, password, email) == false) {
            model.addAttribute("id", id);
            return "admin/edit-user-error";
        }
        userServiceImp.addUser(userServiceImp.setUserForUpdate(id, username, password, email, roleAdmin, roleUser));
        return "redirect:/admin";
    }
}