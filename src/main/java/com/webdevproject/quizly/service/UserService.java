package com.webdevproject.quizly.service;

import com.webdevproject.quizly.dao.UserDao;
import com.webdevproject.quizly.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;





import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User save(User user) {
        if (userDao.existsByUsername(user.getUsername())) {
            return null;
        }
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        return userDao.save(user);
    }

    public User findById(Long id) {
        return userDao.findById(id);
    }

    public void update(User user) {
        userDao.update(user);
    }

    public void delete(Long id) {
        userDao.delete(id);
    }

    public List<User> findAll() {
        return userDao.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
               AuthorityUtils.createAuthorityList(user.getRole())
        );
    }


    public User findByUsername(String username) {

        return userDao.findByUsername(username);
    }
}
