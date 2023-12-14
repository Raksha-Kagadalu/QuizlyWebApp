package com.webdevproject.quizly.dao;

import com.webdevproject.quizly.model.Quiz;
import com.webdevproject.quizly.model.StudentQuizResponse;
import com.webdevproject.quizly.model.User;
import com.webdevproject.quizly.util.HibernateUtil;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

@Repository
public class QuizResponseDaoImpl implements QuizResponseDao{

    @Override
    public List<StudentQuizResponse> findByQuizUniqueID(String uniqueQuizID) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<StudentQuizResponse> query = session.createQuery("FROM StudentQuizResponse WHERE quiz.uniqueQuizID = :uniqueQuizID", StudentQuizResponse.class);
            query.setParameter("uniqueQuizID", uniqueQuizID);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void save(StudentQuizResponse response) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.save(response);
            session.getTransaction().commit();
        }
    }

    @Override
    public boolean hasStudentAlreadyTakenQuiz(User student, Quiz quiz) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT count(*) FROM StudentQuizResponse WHERE student = :student AND quiz = :quiz";
            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("student", student);
            query.setParameter("quiz", quiz);
            return query.uniqueResult() > 0;
        }
    }

    @Override
    public StudentQuizResponse findByStudentAndQuiz(User student, Quiz quiz) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<StudentQuizResponse> query = session.createQuery("FROM StudentQuizResponse q WHERE q.student = :student AND q.quiz = :quiz", StudentQuizResponse.class);
            query.setParameter("student", student);
            query.setParameter("quiz", quiz);
            return query.uniqueResult();
        }
    }

    @Override
    public List<StudentQuizResponse> findResponsesByQuizId(String uniqueQuizId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<StudentQuizResponse> query = session.createQuery(
                    "FROM StudentQuizResponse WHERE quiz.uniqueQuizID = :uniqueQuizId",
                    StudentQuizResponse.class);
            query.setParameter("uniqueQuizId", uniqueQuizId);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

}
