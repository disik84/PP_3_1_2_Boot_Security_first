package ru.kata.spring.boot_security.demo.service;
//test
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.dao.UserDaoImp;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.security.SecurityUserDetails;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class UserServiceImp implements UserService /*, UserDetailsService*/ {
    private UserDao userDao;

    private UserDaoImp userDaoImp;

    //private SecurityUserDetails securityUserDetails;

    //private User user;

    //public UserServiceImp(UserDao userDao, UserDaoImp userDaoImp, SecurityUserDetails securityUserDetails) {
    public UserServiceImp(UserDao userDao, UserDaoImp userDaoImp) {
        this.userDao = userDao;
        this.userDaoImp = userDaoImp;
        //this.securityUserDetails = securityUserDetails;
    }

    @Override
    public List<User> getListUsers() {
        return userDao.findAll();
    }

    @Transactional
    public void addUser(User user) {
        userDao.save(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userDao.getById(id);
        userDao.delete(user);
    }

    @Override
    public Set<Role> setRolesForUser(String roleAdmin, String roleUser) {
        return userDaoImp.setRolesForUser(roleAdmin, roleUser);
    }

    @Override
    public User findUserById(Long id) {
        return userDao.getById(id);
    }

    @Override
    public boolean checkNullEditUser(String id, String username, String password, String email) {
        return userDaoImp.checkNullEditUser(id, username, password, email);
    }

    @Override
    public boolean getRoleCheckbox(User user, String role) {
        return userDaoImp.getRoleCheckbox(user, role);
    }

    @Override
    public String getProfileRole() {
        return userDaoImp.getProfileRole();
    }

    @Override
    public String getPasswordHash(String password) {
        return userDaoImp.getPasswordHash(password);
    }

    @Override
    public User createUser(String username, String password, String email, String roleAdmin, String roleUser) {
        return userDaoImp.createUser(username, password, email, roleAdmin, roleUser);
    }

    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    //Берет любую пачку ролей и из этой пачки  делает пачку Autorities с точно такими же строками
    /*private Collection<? extends GrantedAuthority> mapRolesToAutorities(Collection<Role> roles) {
        return roles.stream().map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toList());
    }*/

    //Здесь из нашего User делаем UserDetails которому нужны только username, password и Autorities
    // можно посмотреть здесь https://www.youtube.com/watch?v=HvovW6Uh1yU на таймкоде 1 час 15 мин
    /*@Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User %s not found: ", username));
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                user.getAuthorities());
    }*/

}
