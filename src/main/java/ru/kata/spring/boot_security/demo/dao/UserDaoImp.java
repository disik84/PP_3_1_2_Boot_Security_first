package ru.kata.spring.boot_security.demo.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import javax.annotation.PostConstruct;
import java.util.*;

@Repository
public class UserDaoImp  {

    private UserDao userDao;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserDaoImp(PasswordEncoder passwordEncoder, UserDao userDao) {
        this.passwordEncoder = passwordEncoder;
        this.userDao = userDao;
    }

    public Set<Role> setRolesForUser(String roleAdmin, String roleUser) {
        Set<Role> setRole = new HashSet<>();
        Role userRole = new Role();
        Role userAdmin = new Role();
        if (!Optional.ofNullable(roleAdmin).isEmpty()) {
            userRole.setId(2L);
            userRole.setName("ROLE_ADMIN");
            setRole.add(userRole);
        }
        if (!Optional.ofNullable(roleUser).isEmpty()) {
            userAdmin.setId(1L);
            userAdmin.setName("ROLE_USER");
            setRole.add(userAdmin);
        }
        return setRole;
    }

    public boolean checkNullEditUser(String id, String username, String password, String email) {
        if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            return false;
        }
        return true;
    }

    public boolean getRoleCheckbox(User user, String roleStr) {
        Set setRoles = user.getRoles();
        boolean checkbox = false;
        Iterator iterator = setRoles.iterator();
        while (iterator.hasNext()) {
            Role role = (Role) iterator.next();
            if (role.getName().contains(roleStr)) {
                checkbox = true;
            }
        }
        return checkbox;
    }

    public String getProfileRole() {
        Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>) SecurityContextHolder
                .getContext().getAuthentication().getAuthorities();
        String s = new String();
        for (SimpleGrantedAuthority a : authorities) {
            s = s + a;
        }
        s = s.replaceAll("ROLE_", " ");
        return s;
    }

    public String getPasswordHash(String password) {
        return passwordEncoder.encode(password);
    }

    public User createUser(String username, String password, String email, String roleAdmin, String roleUser) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(getPasswordHash(password));
        user.setEmail(email);
        user.setRoles(setRolesForUser(roleAdmin, roleUser));
        return user;
    }

    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

}
