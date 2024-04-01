package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Set;

public interface UserService {
    List<User> getListUsers();

    void addUser(User user);

    void deleteUser(Long id);

    User getUserById(Long id);

    boolean checkNullEditUser(String username, String password, String email);

    String getPasswordHash(String password);

    User updateUser(String username, String password, String email, String roleAdmin, String roleUser);
}
