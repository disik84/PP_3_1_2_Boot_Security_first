package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface UserService {
    public List<User> getListUsers();

    public void addUser(User user);

    public void deleteUser(Long id);

    public Set<Role> setRolesForUser(String roleAdmin, String roleUser);

    public User findUserById(Long id);

    public boolean checkNullEditUser(String id, String username, String password, String email);

    public boolean getRoleCheckbox(User user, String role);

    public String getProfileRole();

    public String getPasswordHash(String password);

    public User createUser(String username, String password, String email, String roleAdmin, String roleUser);
}
