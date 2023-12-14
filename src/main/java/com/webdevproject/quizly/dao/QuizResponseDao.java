package com.webdevproject.quizly.dao;

import com.webdevproject.quizly.model.Quiz;
import com.webdevproject.quizly.model.StudentQuizResponse;
import com.webdevproject.quizly.model.User;

import java.util.List;

public interface QuizResponseDao {

    void save(StudentQuizResponse response);
    boolean hasStudentAlreadyTakenQuiz(User student, Quiz quiz);
    List<StudentQuizResponse> findByQuizUniqueID(String uniqueQuizID);

    StudentQuizResponse findByStudentAndQuiz(User student, Quiz quiz);

    List<StudentQuizResponse> findResponsesByQuizId(String quizId);
}
