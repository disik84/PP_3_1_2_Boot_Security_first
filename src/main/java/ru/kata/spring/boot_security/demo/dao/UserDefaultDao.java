package ru.kata.spring.boot_security.demo.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleServiceImp;
import ru.kata.spring.boot_security.demo.service.UserServiceImp;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@Repository
public class UserDefaultDao {
    private final UserServiceImp userServiceImp;
    private final RoleServiceImp roleServiceImp;

    @Autowired
    public UserDefaultDao(UserServiceImp userServiceImp, RoleServiceImp roleServiceImp) {
        this.userServiceImp = userServiceImp;
        this.roleServiceImp = roleServiceImp;
    }

    @Transactional
    @PostConstruct
    public void setDefaultUser() {
        //Добавляе роли в таблицу roles
        Role roleUser = new Role("ROLE_USER");
        roleServiceImp.addRole(roleUser);
        Role roleAdmin = new Role("ROLE_ADMIN");
        roleServiceImp.addRole(roleAdmin);

        //Создаем роли для admin
        Set<Role> setRoleAdmin = new HashSet<>();
        setRoleAdmin.add(roleUser);
        setRoleAdmin.add(roleAdmin);
        //создаем admin
        User userAdmin = new User("admin", userServiceImp.getPasswordHash("admin"), "admin@google.com", setRoleAdmin);
        userServiceImp.addUser(userAdmin);

        //Создаем роли для user
        Set<Role> setRoleUser = new HashSet<>();
        setRoleUser.add(roleUser);
        //создаем user
        User userUser = new User("user", userServiceImp.getPasswordHash("user"), "user@google.com", setRoleUser);
        userServiceImp.addUser(userUser);
    }
}
