package ru.kata.spring.boot_security.demo.service;
//test

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.dao.UserDaoImp;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.*;

@Service
@Transactional(readOnly = true)
public class UserServiceImp implements UserService {
    private UserDao userDao;

    private UserDaoImp userDaoImp;

    public UserServiceImp(UserDao userDao, UserDaoImp userDaoImp) {
        this.userDao = userDao;
        this.userDaoImp = userDaoImp;
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
        return userDaoImp.setRolesForUser(roleAdmin, roleUser);
    }

    @Override
    public User findUserById(Long id) {
        return userDao.getById(id);
    }

    @Override
    public boolean checkNullEditUser(String id, String username, String password, String email) {
        return userDaoImp.checkNullEditUser(id, username, password, email);
    }

    @Override
    public boolean getRoleCheckbox(User user, String role) {
        return userDaoImp.getRoleCheckbox(user, role);
    }

    @Override
    public String getProfileRole() {
        return userDaoImp.getProfileRole();
    }

    @Override
    public String getPasswordHash(String password) {
        return userDaoImp.getPasswordHash(password);
    }

    @Override
    @Transactional
    public User updateUser(String username, String password, String email, String roleAdmin, String roleUser) {
        return userDaoImp.updateUser(username, password, email, roleAdmin, roleUser);
    }

    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

}
