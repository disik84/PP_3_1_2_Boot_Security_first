package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Set;

public interface UserService {
    List<User> getListUsers();

    User getUserById(Long id);

    User findUserById(long id);

    User findByUsername(String username);

    void addUser(User user);

    void deleteUser(Long id);

    boolean checkNullEditUser(String username, String password, String email);

    User updateUser(String username, String password, String email, String roleAdmin, String roleUser);

    User setUserForUpdate(String id, String username, String password, String email, String roleAdmin, String roleUser);

    String getPasswordHash(String password);
}
