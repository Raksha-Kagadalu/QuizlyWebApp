package com.webdevproject.quizly.service;

import com.webdevproject.quizly.model.Quiz;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.webdevproject.quizly.dao.QuizDao;

import java.util.List;

@Service
public class QuizService {
    @Autowired
    private QuizDao quizDao;

    public Quiz createQuiz(Quiz quiz) {
        return quizDao.save(quiz);
    }

    public Quiz findById(Long id) {
        return quizDao.findById(id);
    }

    public void update(Quiz quiz) {
        quizDao.update(quiz);
    }

    // to fetch quizzes based on the professor's username
    public List<Quiz> getQuizzesByProfessor(String professorUsername) {
        return quizDao.findQuizzesByProfessor(professorUsername);
    }

    public Quiz getQuizByUniqueQuizID(String uniqueQuizID) {

        return quizDao.findByUniqueQuizID(uniqueQuizID);
    }
}
