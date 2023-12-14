package com.webdevproject.quizly.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Random;
import java.util.UUID;

@Entity
@Table(name="quizzes")
public class Quiz {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 3, max = 100)
    private String title;

    @NotNull
    @Size(min = 3, max = 100)
    private String uniqueQuizID;

    @NotNull
    @Size(min = 10, max = 500)
    private String questionContent;

    @NotNull
    @Size(min = 1, max = 100)
    private String optionA;
    @NotNull
    @Size(min = 1, max = 100)
    private String optionB;
    @NotNull
    @Size(min = 1, max = 100)
    private String optionC;
    @NotNull
    @Size(min = 1, max = 100)
    private String optionD;

    @NotNull
    @Pattern(regexp = "[A-D]")
    private String correctAnswer;

    // New field to store the time limit
//    @NotNull
//    // private Duration timeLimit;
//    private String timeLimit;

    @ManyToOne
    @JoinColumn(name = "professor_id")
    private User professor;

    @PrePersist
    private void ensureUniqueQuizID() {
       // uniqueQuizID = UUID.randomUUID().toString();
        Random random = new Random();
        int randomNumber = 1000 + random.nextInt(9000); // Generates a number between 1000 and 9999
        uniqueQuizID = "quiz" + randomNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUniqueQuizID() {
        return uniqueQuizID;
    }

    public void setUniqueQuizID(String uniqueQuizID) {
        this.uniqueQuizID = uniqueQuizID;
    }

    public String getQuestionContent() {
        return questionContent;
    }

    public void setQuestionContent(String questionContent) {
        this.questionContent = questionContent;
    }

    public String getOptionA() {
        return optionA;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

//    public String getTimeLimit() {
//        return timeLimit;
//    }
//
//    public void setTimeLimit(String timeLimit) {
//        this.timeLimit = timeLimit;
//    }

    public User getProfessor() {
        return professor;
    }

    public void setProfessor(User professor) {
        this.professor = professor;
    }
}
