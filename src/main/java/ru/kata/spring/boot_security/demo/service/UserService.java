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
    public User findByUsername(String username);

    public boolean checkNullEditUser(String id, String username, String password, String email);
}
