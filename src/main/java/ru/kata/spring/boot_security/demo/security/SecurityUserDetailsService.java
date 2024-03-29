package ru.kata.spring.boot_security.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.dao.UserDaoImp;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserServiceImp;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SecurityUserDetailsService implements UserDetailsService {
    private UserServiceImp userServiceImp;

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