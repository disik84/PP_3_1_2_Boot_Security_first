package ru.kata.spring.boot_security.demo.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.service.UserServiceImp;

import javax.validation.Valid;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class RestUserController {

    private final UserServiceImp userServiceImp;

    private final UserDao userDao;

    public RestUserController(UserServiceImp userServiceImp, UserDao userDao) {
        this.userServiceImp = userServiceImp;
        this.userDao = userDao;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> apiGetAllUsers() {
        List<User> users = userServiceImp.getListUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> apiGetOneUser(@PathVariable("id") long id) {
        User user = userServiceImp.findUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> apiEditUser(@PathVariable("id") long id,
                                           @RequestBody User user) {
        if (userServiceImp.checkNullEditUser(user.getUsername(),user.getPassword(), user.getEmail()) == false) {
            return new ResponseEntity<>(user, HttpStatus.BAD_REQUEST);
        }
        user.setPassword(userServiceImp.getPasswordHash(user.getPassword()));
        userServiceImp.addUser(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
    @DeleteMapping("/users/{id}")
    public ResponseEntity<User> apiDeleteUser(@PathVariable("id") long id) {
        userServiceImp.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PostMapping("/users")
    public ResponseEntity<User> apiAddUser(@RequestBody User user) {
        user.setPassword(userServiceImp.getPasswordHash(user.getPassword()));
        try {
            userServiceImp.addUser(user);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>(user, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
