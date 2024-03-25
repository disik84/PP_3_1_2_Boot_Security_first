package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class UserServiceImp implements UserService, UserDetailsService {
    UserDao userDao;

    @PersistenceContext
    EntityManager em;

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public List<User> getListUsers() {
        return userDao.findAll();
    }

    @Transactional
    public Long addUserAndGetId(User user) {
        userDao.save(user);
        System.out.println("---addUserAndGetId---");
        System.out.println(user.getId());
        System.out.println("---addUserAndGetId---");
        return user.getId();
    }

    @Transactional
    public void addUser(User user) {
        userDao.save(user);
    }

    @Transactional
    public void addRole(Long userId, int roleId) {
        em.createNativeQuery("INSERT INTO users_roles (user_id, role_id) VALUES (?, ?)")
                .setParameter(1, userId)
                .setParameter(2, roleId)
                .executeUpdate();
        em.flush();
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userDao.getById(id);
        userDao.delete(user);
    }

    public User findUserById(Long id) {
        return userDao.getById(id);
    }

    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    //Берет любую пачку ролей и из этой пачки  делает пачку Autorities с точно такими же строками
    private Collection<? extends GrantedAuthority> mapRolesToAutorities(Collection<Role> roles) {
        return roles.stream().map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toList());
    }

    //Здесь из нашего User делаем UserDetails которому нужны только username, password и Autorities
    // можно посмотреть здесь https://www.youtube.com/watch?v=HvovW6Uh1yU на таймкоде 1 час 15 мин
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User %s not found: ", username));
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                mapRolesToAutorities(user.getRoles()));
    }

}
