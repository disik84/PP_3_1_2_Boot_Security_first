package ru.kata.spring.boot_security.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.service.UserServiceImp;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class RestUserController {

    private final UserServiceImp userServiceImp;

    private final UserDao userDao;

    private final UserService userService;

    public RestUserController(UserServiceImp userServiceImp, UserDao userDao, UserService userService) {
        this.userServiceImp = userServiceImp;
        this.userDao = userDao;
        this.userService = userService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> apiGetAllUsers() {
        List<User> users = userServiceImp.getListUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    /*@GetMapping("/users/{id}")
    public ResponseEntity<User> apiGetOneUser(@PathVariable("id") long id) {
        User user = userDao.findUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }*/
}
