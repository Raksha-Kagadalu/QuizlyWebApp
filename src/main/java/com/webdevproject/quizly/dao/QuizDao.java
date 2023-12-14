package com.webdevproject.quizly.dao;

import com.webdevproject.quizly.model.Quiz;

import java.util.List;


public interface QuizDao {
    Quiz save(Quiz quiz);
    Quiz findById(Long id);
    void update(Quiz quiz);
    Quiz findByUniqueQuizID(String uniqueQuizID);
    List<Quiz> findQuizzesByProfessor(String professorUsername);

}