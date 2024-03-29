package ru.kata.spring.boot_security.demo.service;
//test

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.RoleDaoImp;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.dao.UserDaoImp;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.*;

@Service
@Transactional(readOnly = true)
public class UserServiceImp implements UserService {
    private final UserDao userDao;
    private final UserDaoImp userDaoImp;
    private final RoleDaoImp roleDaoImp;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImp(UserDao userDao, UserDaoImp userDaoImp, RoleDaoImp roleDaoImp, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.userDaoImp = userDaoImp;
        this.roleDaoImp = roleDaoImp;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String getPasswordHash(String password) {
        return passwordEncoder.encode(password);
    }

    @Override
    public List<User> getListUsers() {
        return userDao.findAll();
    }

    @Transactional
    public void addUser(User user) {
        userDao.save(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userDao.getById(id);
        userDao.delete(user);
    }

    @Override
    public Set<Role> setRolesForUser(String roleAdmin, String roleUser) {
        return roleDaoImp.setRolesForUser(roleAdmin, roleUser);
    }

    @Override
    public User findUserById(Long id) {
        return userDao.getById(id);
    }

    @Override
    public boolean checkNullEditUser(String username, String password, String email) {
        return userDaoImp.checkNullEditUser(username, password, email);
    }

    @Override
    public String getProfileRole() {
        return roleDaoImp.getProfileRole();
    }

    @Override
    @Transactional
    public User updateUser(String username, String password, String email, String roleAdmin, String roleUser) {
        return userDaoImp.updateUser(username, password, email, roleAdmin, roleUser);
    }

    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    public User setUserForUpdate(String id, String username, String password, String email, String roleAdmin, String roleUser) {
        User user = findUserById(Long.parseLong(id));
        user.setId(Long.parseLong(id));
        user.setUsername(username);
        user.setPassword(getPasswordHash(password));
        user.setEmail(email);
        user.setRoles(setRolesForUser(roleAdmin, roleUser));
        return user;
    }
}
