package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface UserService {
    List<User> getListUsers();

    void addUser(User user);

    void deleteUser(Long id);

    Set<Role> setRolesForUser(String roleAdmin, String roleUser);

    User findUserById(Long id);

    boolean checkNullEditUser(String id, String username, String password, String email);

    String getProfileRole();

    String getPasswordHash(String password);

    User updateUser(String username, String password, String email, String roleAdmin, String roleUser);
}
