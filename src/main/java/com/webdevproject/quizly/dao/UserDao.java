package com.webdevproject.quizly.dao;

import com.webdevproject.quizly.model.User;

import java.util.List;

public interface UserDao {
    User save(User user);
    boolean existsByUsername(String username);
    User findById(Long id);
    void update(User user);
    void delete(Long id);
    List<User> findAll();
    User findByUsername(String username);

}
