package com.webdevproject.quizly.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendQuizSubmissionConfirmation(String to, String quizTitle) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("quizly.project@outlook.com");
        message.setTo(to);
        message.setSubject("Quiz Submission Confirmation");
        message.setText("Dear student,\n\nThis is a confirmation that you have successfully submitted the quiz: " + quizTitle + ".\n\nBest regards,\nQuizly Team");

        mailSender.send(message);
    }
}
