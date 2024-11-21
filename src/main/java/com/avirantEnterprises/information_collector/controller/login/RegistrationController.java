package com.avirantEnterprises.information_collector.controller.login;

import com.avirantEnterprises.information_collector.model.login.User;
import com.avirantEnterprises.information_collector.service.login.EmailService;
import com.avirantEnterprises.information_collector.service.login.UserLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/register")
public class RegistrationController {

   @Autowired
    private UserLoginService userLoginService;
    @GetMapping
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "login/register";  // Render register.html page
    }

    @PostMapping
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        userLoginService.saveUser(user);
        model.addAttribute("message", "Registration successful. You can now log in.");
        return "login/register";  // Render register.html page after registration
    }
}
