package ru.kata.spring.boot_security.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.RoleDaoImp;
import ru.kata.spring.boot_security.demo.model.Role;

import java.util.Set;

@Service
@Transactional(readOnly = true)
public class RoleServiceImp implements RoleService {
    private final RoleDaoImp roleDaoImp;

    public RoleServiceImp(RoleDaoImp roleDaoImp) {
        this.roleDaoImp = roleDaoImp;
    }

    @Override
    @Transactional
    public void addRole(Role role) {
        roleDaoImp.addRole(role);
    }

    @Override
    public Set<Role> setRolesForUser(String roleAdmin, String roleUser) {
        return roleDaoImp.setRolesForUser(roleAdmin, roleUser);
    }

    @Override
    public String getProfileRole() {
        return roleDaoImp.getProfileRole();
    }
}
