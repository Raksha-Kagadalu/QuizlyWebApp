package com.webdevproject.quizly.dao;

import com.webdevproject.quizly.model.Quiz;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import com.webdevproject.quizly.util.HibernateUtil;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

@Repository
public class QuizDaoImpl implements QuizDao{

    @Override
    public Quiz save(Quiz quiz) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.save(quiz);
            session.getTransaction().commit();
            return quiz;
        }
    }

    @Override
    public Quiz findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Quiz.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void update(Quiz quiz) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(quiz);
            transaction.commit();

        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public List<Quiz> findQuizzesByProfessor(String professorUsername) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Quiz q WHERE q.professor.username = :username";
            Query<Quiz> query = session.createQuery(hql, Quiz.class);
            query.setParameter("username", professorUsername);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public Quiz findByUniqueQuizID(String uniqueQuizID) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Quiz WHERE uniqueQuizID = :uniqueQuizID", Quiz.class)
                    .setParameter("uniqueQuizID", uniqueQuizID)
                    .uniqueResult();
        }
    }

}
