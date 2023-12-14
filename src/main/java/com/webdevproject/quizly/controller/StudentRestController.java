package com.webdevproject.quizly.controller;

import com.webdevproject.quizly.model.Quiz;
import com.webdevproject.quizly.model.StudentQuizResponse;
import com.webdevproject.quizly.model.User;
import com.webdevproject.quizly.service.QuizResponseService;
import com.webdevproject.quizly.service.QuizService;
import com.webdevproject.quizly.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/student/api/")
public class StudentRestController {
    @Autowired
    private QuizService quizService;

    @Autowired
    private QuizResponseService responseService;

    @Autowired
    private UserService userService;

    @GetMapping("/quizresult")
    public ResponseEntity<?> getQuizResult(@RequestParam String uniqueQuizID, Principal principal) {
        //  System.out.println("Unique Quiz ID: " + uniqueQuizID + ", Username: " + principal.getName());
        User student = userService.findByUsername(principal.getName());
        Quiz quiz = quizService.getQuizByUniqueQuizID(uniqueQuizID);
        //System.out.println("Quiz: " + quiz + ", Student: " + student);
        if (student == null || quiz == null) {
            return ResponseEntity.badRequest().body("Invalid student or quiz ID.");
        }
        StudentQuizResponse quizResult = responseService.findByStudentAndQuiz(student, quiz);
      //  System.out.println("Quiz Result: " + quizResult);

        if (quizResult == null) {
            return ResponseEntity.badRequest().body("No result found for the provided quiz ID, or you did not take this quiz.");
        }
        // System.out.println("Quiz Result is there "+quiz.getQuestionContent());
        Map<String, String> options = Map.of(
                "A", quiz.getOptionA(),
                "B", quiz.getOptionB(),
                "C", quiz.getOptionC(),
                "D", quiz.getOptionD()
        );

        Map<String, Object> result = new HashMap<>();
        result.put("questionContent", quiz.getQuestionContent());
//        System.out.println(result.get("questionContent"));
        result.put("selectedAnswer", quizResult.getSelectedAnswer());
        result.put("selectedAnswerContent", options.get(quizResult.getSelectedAnswer()));
        result.put("correctAnswer", quiz.getCorrectAnswer());
        result.put("correctAnswerContent", options.get(quiz.getCorrectAnswer()));
        return ResponseEntity.ok(result);
    }
}
