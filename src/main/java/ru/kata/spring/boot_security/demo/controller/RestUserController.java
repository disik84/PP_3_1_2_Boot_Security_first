package ru.kata.spring.boot_security.demo.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserServiceImp;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RestUserController {

    private final UserServiceImp userServiceImp;

    public RestUserController(UserServiceImp userServiceImp, UserDao userDao) {
        this.userServiceImp = userServiceImp;
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
        if (!userServiceImp.checkNullEditUser(user.getUsername(), user.getPassword(), user.getEmail())) {
            return new ResponseEntity<>(user, HttpStatus.BAD_REQUEST);
        }
        user.setPassword(userServiceImp.getPasswordHash(user.getPassword()));
        try {
            userServiceImp.addUser(user);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>(user, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<User> apiDeleteUser(@PathVariable("id") long id) {
        userServiceImp.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/users")
    public ResponseEntity<User> apiAddUser(@RequestBody User user) {
        if (!userServiceImp.checkNullEditUser(user.getUsername(), user.getPassword(), user.getEmail())) {
            return new ResponseEntity<>(user, HttpStatus.BAD_REQUEST);
        }
        user.setPassword(userServiceImp.getPasswordHash(user.getPassword()));
        try {
            userServiceImp.addUser(user);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>(user, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
