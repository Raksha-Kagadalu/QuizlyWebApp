package com.webdevproject.quizly.controller;

import com.webdevproject.quizly.model.Quiz;
import com.webdevproject.quizly.model.StudentQuizResponse;
import com.webdevproject.quizly.model.User;
import com.webdevproject.quizly.service.QuizResponseService;
import com.webdevproject.quizly.service.QuizService;
import com.webdevproject.quizly.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.OutputStream;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/professor")
public class ProfessorController {

    @Autowired
    private UserService userService;

    @Autowired
    private QuizService quizService;

    @Autowired
    private QuizResponseService quizResponseService;

    // To display the professor's dashboard
    @GetMapping("/dashboard")
    public String userLogin(){
        return "professor-dashboard";
    }

    // To show the form to create a new quiz
    @GetMapping("/createquiz")
    public ModelAndView createQuizForm() {
        ModelAndView modelAndView = new ModelAndView("create_quiz");
        modelAndView.addObject("quiz", new Quiz());
        return modelAndView;
    }

    // To handle the submission of the create new quiz form
    @PostMapping("/createquiz")
    public String createQuiz(@ModelAttribute Quiz quiz, RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); // Get logged in username
        User professor = userService.findByUsername(username); // Fetch the professor
        quiz.setProfessor(professor); // Set the professor for the quiz

        Quiz createdQuiz = quizService.createQuiz(quiz);
        redirectAttributes.addFlashAttribute("createdQuiz", createdQuiz);
        return "redirect:/professor/quizcreated";
    }

    //to redirect and display the unique quiz id
    @GetMapping("/quizcreated")
    public String quizCreated(Model model) {
        return model.containsAttribute("createdQuiz") ? "quiz_created" : "redirect:/professor/dashboard";
    }

    @GetMapping("/viewQuizzes")
    public String viewAllQuizzes(Principal principal, Model model) {
        String professorUsername = principal.getName();
        List<Quiz> quizzes = quizService.getQuizzesByProfessor(professorUsername);
        model.addAttribute("quizzes", quizzes);
        return "view_quizzes";
    }

    @GetMapping("/viewQuiz/{id}")
    public String viewQuiz(@PathVariable Long id, Model model) {
        Quiz quiz = quizService.findById(id);
        if (quiz != null) {
            model.addAttribute("quiz", quiz);
            return "view_quiz";
        } else {
            return "redirect:/professor/dashboard";
        }
    }

    @GetMapping("/editQuiz/{id}")
    public String editQuizForm(@PathVariable Long id, Model model) {
        Quiz quiz = quizService.findById(id);
        if (quiz != null) {
            model.addAttribute("quiz", quiz);
            return "edit_quiz";
        } else {
            return "redirect:/professor/dashboard";
        }
    }

    @PostMapping("/editQuiz/{id}")
    public String updateQuiz(@PathVariable Long id, @ModelAttribute Quiz quiz, RedirectAttributes redirectAttributes) {
        quizService.update(quiz);
        redirectAttributes.addFlashAttribute("success", "Quiz updated successfully.");
        return "redirect:/professor/dashboard";
    }

    @PostMapping("/updateQuiz")
    public String updateQuiz(@ModelAttribute("quiz") Quiz quiz) {
        try {
            quizService.update(quiz);
//            redirectAttributes.addFlashAttribute("successMessage", "Quiz updated successfully!");
        } catch (Exception e) {
            e.printStackTrace();
//            redirectAttributes.addFlashAttribute("errorMessage", "Error updating quiz: " + e.getMessage());
        }
        return "redirect:/professor/dashboard";
    }

    @GetMapping("/quizAnalyticsPage")
    public String showQuizAnalyticsPage() {
        return "quiz-analytics";
    }

    @GetMapping("/quizAttendance")
    public String showQuizSearchPage() {
        return "quiz_attendance";
    }

    @GetMapping("/downloadExcel")
    public void downloadExcel(@RequestParam String quizId, HttpServletResponse response) throws IOException {
        List<StudentQuizResponse> responses = quizResponseService.getResponsesByQuiz(quizId);
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=quiz-responses.xlsx");

        try {
            //to write the Excel file to the response's output stream
            generateExcelFileForQuiz(responses, response.getOutputStream());
            //to ensures that all data is written to the response stream.
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void generateExcelFileForQuiz(List<StudentQuizResponse> responses, OutputStream outputStream) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Quiz Results");


            Row headerRow = sheet.createRow(0);
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            Font font = workbook.createFont();
            font.setFontName("Arial");
            font.setBold(true);
            headerStyle.setFont(font);

            String[] columnHeaders = {"User ID", "Username", "Result"};
            for (int i = 0; i < columnHeaders.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columnHeaders[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowNum = 1;
            for (StudentQuizResponse response : responses) {
                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(response.getStudent().getId());
                row.createCell(1).setCellValue(response.getStudent().getUsername());
                row.createCell(2).setCellValue(response.isCorrect() ? "1" : "0");
            }

            for (int i = 0; i < columnHeaders.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(outputStream);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to generate Excel file", e);
        }
    }
}
