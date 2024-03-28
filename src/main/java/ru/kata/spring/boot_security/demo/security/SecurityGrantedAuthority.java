package ru.kata.spring.boot_security.demo.security;

import org.springframework.security.core.GrantedAuthority;
import ru.kata.spring.boot_security.demo.model.Role;

public class SecurityGrantedAuthority implements GrantedAuthority {

    Role role = new Role();

    @Override
    public String getAuthority() {
        return role.getName();
    }
}
