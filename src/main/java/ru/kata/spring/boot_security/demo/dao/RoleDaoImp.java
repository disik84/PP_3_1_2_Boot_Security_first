package ru.kata.spring.boot_security.demo.dao;

import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.Role;

@Repository
public class RoleDaoImp {
    RoleDao roleDao;

    public RoleDaoImp(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    public void addRole(Role role) {
        roleDao.save(role);
    }
}
