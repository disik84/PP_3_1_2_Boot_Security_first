package ru.kata.spring.boot_security.demo.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.model.Role;

@Service
public class SecurityGrantedAuthority implements GrantedAuthority {

    Role role = new Role();

    @Override
    public String getAuthority() {
        return role.getName();
    }
}
