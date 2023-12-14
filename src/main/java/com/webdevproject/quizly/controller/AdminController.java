package com.webdevproject.quizly.controller;

import com.webdevproject.quizly.model.User;
import com.webdevproject.quizly.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    // To display the admin's dashboard
    @GetMapping("/dashboard")
    public String userLogin(){

        return "admin-dashboard";
    }

    @GetMapping("/createUser")
    public String showCreateUserForm(Model model) {
        model.addAttribute("user", new User());
        return "create_user";
    }

    @PostMapping("/createUser")
    public String createUser(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        // Save the new user using the UserService
        User savedUser = userService.save(user);
        if (savedUser == null) {
            // User with the same username already exists
            redirectAttributes.addFlashAttribute("errorMessage", "Username already exists.");
            return "redirect:/admin/createUser";
        }
        redirectAttributes.addFlashAttribute("successMessage", "User created successfully.");
        return "redirect:/admin/createUser";
    }

    // In your Controller
    @GetMapping("/viewUsers")
    public String viewAllUsers(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "view_users";
    }


    // Method to show edit user form
    @GetMapping("/editUser/{id}")
    public String editUserForm(@PathVariable Long id, Model model) {
        User user = userService.findById(id);
        if (user != null) {
            model.addAttribute("user", user);
            return "edit_user";
        } else {
            return "redirect:/admin/viewUsers";
        }
    }

    // Method to handle edit user form submission
    @PostMapping("/updateUser")
    public String updateUser(@ModelAttribute User user) {
        userService.update(user);
        return "redirect:/admin/viewUsers";
    }

    // Method to delete a user
    @GetMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        userService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "User deleted successfully!");
        return "redirect:/admin/viewUsers";
    }


}
