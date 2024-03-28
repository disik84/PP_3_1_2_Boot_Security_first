package ru.kata.spring.boot_security.demo.model;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.service.UserServiceImp;

import javax.annotation.PostConstruct;
import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    private String password;

    private String email;

    public User() {
    }

    public User(String username, String password, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.roles = roles;
    }

    public User(String username, String password, String email, Set<Role> roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.roles = roles;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;

    public StringBuilder getSimpleRoles() {
        StringBuilder sb = new StringBuilder();
        Iterator iterator = roles.iterator();
        while (iterator.hasNext()) {
            Role role = (Role) iterator.next();
            if (role.getName().contains("ROLE_ADMIN")) {
                sb.append("ADMIN ");
            }
            if (role.getName().contains("ROLE_USER")) {
                sb.append("USER ");
            }
        }
        return sb;
    }

    public boolean getRoleCheckbox(String roleStr) {
        boolean checkbox = false;
        Iterator iterator = getRoles().iterator();
        while (iterator.hasNext()) {
            Role role = (Role) iterator.next();
            if (role.getName().contains(roleStr)) {
                checkbox = true;
            }
        }
        return checkbox;
    }

    //Берет любую пачку ролей и из этой пачки  делает пачку Autorities с точно такими же строками
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles().stream().map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toList());
    }


}
