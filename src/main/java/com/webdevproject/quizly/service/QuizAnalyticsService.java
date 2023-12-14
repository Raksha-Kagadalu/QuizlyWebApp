package com.webdevproject.quizly.service;

import com.webdevproject.quizly.dao.QuizResponseDao;
import com.webdevproject.quizly.model.StudentQuizResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QuizAnalyticsService {

    @Autowired
    private QuizResponseDao quizResponseDao;

    public Map<String, Integer> getQuizAnalytics(String uniqueQuizID) {
        List<StudentQuizResponse> responses = quizResponseDao.findByQuizUniqueID(uniqueQuizID);
        Map<String, Integer> counts = new HashMap<>();
        // Initialize counts
        counts.put("A", 0);
        counts.put("B", 0);
        counts.put("C", 0);
        counts.put("D", 0);

        for (StudentQuizResponse response : responses) {
            String selectedOption = response.getSelectedAnswer();
            counts.put(selectedOption, counts.getOrDefault(selectedOption, 0) + 1);
        }
        return counts;
    }
}
