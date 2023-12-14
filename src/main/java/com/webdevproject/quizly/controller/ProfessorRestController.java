package com.webdevproject.quizly.controller;

import com.webdevproject.quizly.model.Quiz;
import com.webdevproject.quizly.model.StudentQuizResponse;
import com.webdevproject.quizly.service.QuizAnalyticsService;
import com.webdevproject.quizly.service.QuizResponseService;
import com.webdevproject.quizly.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/professor/api")
public class ProfessorRestController {
    @Autowired
    private QuizAnalyticsService quizAnalyticsService;

    @Autowired
    private QuizService quizService;

    @Autowired
    private QuizResponseService quizResponseService;

    @GetMapping("/quizAnalytics")
    public ResponseEntity<?> getQuizAnalytics(@RequestParam String uniqueQuizID) {
        try {
            Quiz quiz = quizService.getQuizByUniqueQuizID(uniqueQuizID);
            // Get the quiz details
            if (quiz == null) {
                return ResponseEntity.notFound().build();
            }

            Map<String, Object> analyticsData = new HashMap<>();
            analyticsData.put("question", quiz.getQuestionContent());
            analyticsData.put("options", Map.of(
                    "A", quiz.getOptionA(),
                    "B", quiz.getOptionB(),
                    "C", quiz.getOptionC(),
                    "D", quiz.getOptionD()
            ));
            // to get only the counts from the QuizAnalyticsService
            Map<String, Integer> counts = quizAnalyticsService.getQuizAnalytics(uniqueQuizID);
            analyticsData.put("counts", counts);

            return ResponseEntity.ok(analyticsData);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/quizattendees")
    public ResponseEntity<?> getQuizAttendees(@RequestParam String quizId) {
        try {
            List<StudentQuizResponse> responses = quizResponseService.getResponsesByQuiz(quizId);
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
