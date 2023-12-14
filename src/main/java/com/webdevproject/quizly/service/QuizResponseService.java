package com.webdevproject.quizly.service;

import com.webdevproject.quizly.dao.QuizResponseDao;
import com.webdevproject.quizly.model.Quiz;
import com.webdevproject.quizly.model.StudentQuizResponse;
import com.webdevproject.quizly.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizResponseService {

    @Autowired
    private QuizResponseDao quizResponseDao;

    public void submitResponse(StudentQuizResponse response) {
        quizResponseDao.save(response);
    }

    public boolean hasStudentAlreadyTakenQuiz(User student, Quiz quiz) {
        return quizResponseDao.hasStudentAlreadyTakenQuiz(student, quiz);
    }

    public StudentQuizResponse findByStudentAndQuiz(User student, Quiz quiz) {
        return quizResponseDao.findByStudentAndQuiz(student, quiz);
    }

    public List<StudentQuizResponse> getResponsesByQuiz(String uniqueQuizId) {
        return quizResponseDao.findResponsesByQuizId(uniqueQuizId);
    }
}
