package com.webdevproject.quizly.controller;

import com.webdevproject.quizly.model.Quiz;
import com.webdevproject.quizly.model.StudentQuizResponse;
import com.webdevproject.quizly.model.User;
import com.webdevproject.quizly.service.EmailService;
import com.webdevproject.quizly.service.QuizResponseService;
import com.webdevproject.quizly.service.QuizService;
import com.webdevproject.quizly.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/student")
public class StudentController {
    @Autowired
    private QuizService quizService;

    @Autowired
    private UserService userService;

    @Autowired
    private QuizResponseService responseService;

    @Autowired
    private EmailService emailService;

    // To display the student's dashboard
    @GetMapping("/dashboard")
    public String userLogin(){

        return "student-dashboard";
    }


    @GetMapping("/takequiz")
    public String showTakeQuizForm() {
        return "take_quiz";
    }


    // Method to show the "View Quiz Result" page
    @GetMapping("/viewQuizResult")
    public String showViewQuizResult() {
        return "view_quiz_result";
    }

    @GetMapping("/quiz")
    public String findQuiz(@RequestParam String quizId, RedirectAttributes redirectAttributes) {
        Quiz quiz = quizService.getQuizByUniqueQuizID(quizId);

        if (quiz == null) {
            redirectAttributes.addFlashAttribute("error", "Quiz not found.");
            return "redirect:/student/takequiz";
        }

        return "redirect:/student/quiz/" + quizId;
    }

    // GET request to show the quiz form based on quiz ID
    @GetMapping("/quiz/{quizId}")
    public String showQuiz(@PathVariable String quizId,Principal principal, Model model, RedirectAttributes redirectAttributes) {
        Quiz quiz = quizService.getQuizByUniqueQuizID(quizId);
        if (quiz == null) {
            redirectAttributes.addFlashAttribute("error", "Invalid quiz.");
            return "redirect:/student/takequiz";
        }

        if (principal != null) {
            User student = userService.findByUsername(principal.getName());
            if (student != null && "STUDENT".equals(student.getRole())) {
                if (student == null) {
                    redirectAttributes.addFlashAttribute("error", "User not found.");
                    return "redirect:/student/takequiz";
                }

                if (responseService.hasStudentAlreadyTakenQuiz(student, quiz)) {
                    redirectAttributes.addFlashAttribute("error", "You have already taken this quiz.");
                    return "redirect:/student/takequiz";
                }

                model.addAttribute("quiz", quiz);
                model.addAttribute("response", new StudentQuizResponse());
                return "quiz_page";
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "You are not authorized to submit this quiz.");
                return "redirect:/student/dashboard";
            }
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "You must be logged in to submit a quiz.");
            return "redirect:/login";
        }

    }

    // POST request to submit the quiz response
    @PostMapping("/submitquiz")
    public String submitQuiz(@ModelAttribute StudentQuizResponse response, @RequestParam("uniqueQuizID") String uniqueQuizID, Principal principal, RedirectAttributes redirectAttributes) {
        Quiz quiz = quizService.getQuizByUniqueQuizID(uniqueQuizID);
        if (principal != null) {
            User student = userService.findByUsername(principal.getName());
            if (student != null && "STUDENT".equals(student.getRole())) {
                response.setStudent(student);
                if (response.getSelectedAnswer() == null || response.getSelectedAnswer().isEmpty()) {
                    redirectAttributes.addFlashAttribute("errorMessage", "Please select an option.");
                    return "redirect:/student/quiz/" + uniqueQuizID;
                }

                response.setQuiz(quiz);
                response.setCorrect(response.getSelectedAnswer().equalsIgnoreCase(quiz.getCorrectAnswer()));
                responseService.submitResponse(response);
                emailService.sendQuizSubmissionConfirmation("ra.kagadaluraju@northeastern.edu", response.getQuiz().getTitle());
                return "submit_quiz_sucess";
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "You are not authorized to submit this quiz.");
                return "redirect:/student/dashboard";
            }
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "You must be logged in to submit a quiz.");
            return "redirect:/login";
        }

    }
}
