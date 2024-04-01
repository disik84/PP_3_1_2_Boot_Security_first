package ru.kata.spring.boot_security.demo.model;

import lombok.Data;
import ru.kata.spring.boot_security.demo.security.SecurityUserDetails;

import javax.persistence.*;
import java.util.*;

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

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;

    private SecurityUserDetails securityUserDetails;

    public User() {
    }

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public User(String username, String password, String email, Set<Role> roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.roles = roles;
    }

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

    public StringBuilder getRoleUser() {
        StringBuilder sb = new StringBuilder();
        Iterator iterator = roles.iterator();
        while (iterator.hasNext()) {
            Role role = (Role) iterator.next();
            if (role.getName().contains("ROLE_USER")) {
                sb.append("USER");
            }
        }
        return sb;
    }

    public StringBuilder getRoleAdmin() {
        StringBuilder sb = new StringBuilder();
        Iterator iterator = roles.iterator();
        while (iterator.hasNext()) {
            Role role = (Role) iterator.next();
            if (role.getName().contains("ROLE_ADMIN")) {
                sb.append("ADMIN");
            }
        }
        return sb;
    }

    //Этот метод решил оставить в User, но удалил из UserServiceImp, потому что в шаблоне к нему удобнее обращаться
    //когда он в User
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
