package ru.kata.spring.boot_security.demo.dao;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.*;

@Repository
public class RoleDaoImp {
    private RoleDao roleDao;

    public RoleDaoImp(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    public void addRole(Role role) {
        roleDao.save(role);
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

}
