package ru.kata.spring.boot_security.demo.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserServiceImp;

@Service
public class SecurityUserDetailsService implements UserDetailsService {
    private final UserServiceImp userServiceImp;

    public SecurityUserDetailsService(UserServiceImp userServiceImp) {
        this.userServiceImp = userServiceImp;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userServiceImp.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found " + username);
        }
        return new SecurityUserDetails(user);
    }
}