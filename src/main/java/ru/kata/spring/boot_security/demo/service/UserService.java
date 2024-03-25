package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.Collection;
import java.util.List;

public interface UserService {
    public List<User> getListUsers();
    public Long addUserAndGetId(User user);
    public void addUser(User user);
    public void addRole(Long userId, int roleId);
    public void deleteUser(Long id);
}
