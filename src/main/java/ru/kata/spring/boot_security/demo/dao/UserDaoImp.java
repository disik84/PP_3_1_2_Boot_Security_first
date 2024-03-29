package ru.kata.spring.boot_security.demo.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserServiceImp;

@Repository
public class UserDaoImp {
    private final UserServiceImp userServiceImp;
    private final RoleDaoImp roleDaoImp;

    @Autowired
    public UserDaoImp(@Lazy UserServiceImp userServiceImp, RoleDaoImp roleDaoImp) {
        this.userServiceImp = userServiceImp;
        this.roleDaoImp = roleDaoImp;
    }

    public boolean checkNullEditUser(String username, String password, String email) {
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

}
