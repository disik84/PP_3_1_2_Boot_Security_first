package ru.kata.spring.boot_security.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.RoleDaoImp;
import ru.kata.spring.boot_security.demo.model.Role;

@Service
@Transactional(readOnly = true)
public class RoleServiceImp implements RoleService {
    private RoleDaoImp roleDaoImp;

    public RoleServiceImp(RoleDaoImp roleDaoImp) {
        this.roleDaoImp = roleDaoImp;
    }

    @Override
    @Transactional
    public void addRole(Role role) {
        roleDaoImp.addRole(role);
    }
}
