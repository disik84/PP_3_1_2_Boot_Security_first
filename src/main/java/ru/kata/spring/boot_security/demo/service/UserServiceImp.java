package ru.kata.spring.boot_security.demo.service;
//test

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.dao.UserDaoImp;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.*;

@Service
@Transactional(readOnly = true)
public class UserServiceImp implements UserService {
    private final UserDao userDao;
    private final UserDaoImp userDaoImp;
    private final RoleServiceImp roleServiceImp;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImp(UserDao userDao, UserDaoImp userDaoImp, RoleServiceImp roleServiceImp, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.userDaoImp = userDaoImp;
        this.roleServiceImp = roleServiceImp;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> getListUsers() {
        return userDao.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userDao.getById(id);
    }

    @Override
    public User findUserById(long id) {
        return userDao.findUserById(id);
    }

    @Override
    public User findByUsername(String username) {
        return userDao.findByUsername(username);
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
    public boolean checkNullEditUser(String username, String password, String email) {
        return userDaoImp.checkNullEditUser(username, password, email);
    }

    @Override
    public User updateUser(String username, String password, String email, String roleAdmin, String roleUser) {
        return userDaoImp.updateUser(username, password, email, roleAdmin, roleUser);
    }

    public User setUserForUpdate(String id, String username, String password, String email, String roleAdmin, String roleUser) {
        User user = getUserById(Long.parseLong(id));
        user.setId(Long.parseLong(id));
        user.setUsername(username);
        user.setPassword(getPasswordHash(password));
        user.setEmail(email);
        user.setRoles(roleServiceImp.setRolesForUser(roleAdmin, roleUser));
        return user;
    }

    @Override
    public String getPasswordHash(String password) {
        return passwordEncoder.encode(password);
    }
}
