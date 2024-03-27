package ru.kata.spring.boot_security.demo.model;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.kata.spring.boot_security.demo.service.UserServiceImp;

import javax.persistence.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

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

    public User(Long id, String username, String password, String email, Set<Role> roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.roles = roles;
    }

    @ManyToMany
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
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
                sb.append("USER");
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
}
