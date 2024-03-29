package ru.kata.spring.boot_security.demo.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserServiceImp;

import javax.annotation.PostConstruct;
import java.util.*;

@Repository
public class UserDaoImp  {

    private UserDao userDao;

    private UserServiceImp userServiceImp;

    private RoleDaoImp roleDaoImp;

    @Autowired
    public UserDaoImp(UserDao userDao, @Lazy UserServiceImp userServiceImp, RoleDaoImp roleDaoImp) {
        this.userDao = userDao;
        this.userServiceImp = userServiceImp;
        this.roleDaoImp = roleDaoImp;
    }

    public boolean checkNullEditUser(String id, String username, String password, String email) {
        if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            return false;
        }
        return true;
    }

    public User updateUser(String username, String password, String email, String roleAdmin, String roleUser) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(userServiceImp.getPasswordHash(password));
        user.setEmail(email);
        user.setRoles(roleDaoImp.setRolesForUser(roleAdmin, roleUser));
        return user;
    }

    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

}
