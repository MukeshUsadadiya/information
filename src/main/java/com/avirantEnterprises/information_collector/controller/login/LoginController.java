package com.avirantEnterprises.information_collector.controller.login;

import com.avirantEnterprises.information_collector.model.login.User;
import com.avirantEnterprises.information_collector.service.login.EmailService;
import com.avirantEnterprises.information_collector.service.login.UserLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private UserLoginService userLoginService;
    @Autowired
    private EmailService emailService;

    @GetMapping
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User());  // Add an empty user object for binding
        return "login/login";  // Render login.html page
    }

    @PostMapping("/sendOtp")
    public String sendOtp(@ModelAttribute("user") User user, Model model) {
        System.out.println("Entered sendOtp method");  // Add log for debugging

        if (StringUtils.isEmpty(user.getEmail())) {
            model.addAttribute("message", "Email cannot be empty.");
            return "login/login";
        }

        User existingUser = userLoginService.findByEmail(user.getEmail());
        if (existingUser == null) {
            model.addAttribute("message", "Email not registered.");
            return "login/login";
        }

        // Generate OTP and set expiry time (valid for 5 minutes)
        String otp = userLoginService.generateOtp();
        userLoginService.updateOtp(user.getEmail(), otp);

        // Send OTP email
        emailService.sendOtpEmail(user.getEmail(), otp);

        model.addAttribute("email", user.getEmail());
        model.addAttribute("message", "An OTP has been sent to your email.");
        return "login/otp";  // Redirect to OTP input page
    }


    @PostMapping("/verifyOtp")
    public String verifyOtp(@RequestParam("email") String email, @RequestParam("otp") String otp, Model model) {
        User user = userLoginService.findByEmail(email);

        if (user == null || !userLoginService.verifyOtp(email, otp)) {
            model.addAttribute("message", "Invalid or expired OTP.");
            return "login/otp";  // Redirect back to OTP page with error message
        }

        model.addAttribute("message", "Login successful!");
        return "login/userDashboard";  // Redirect to dashboard page after successful login
    }

}
