package com.avirantEnterprises.information_collector.controller.login;

import com.avirantEnterprises.information_collector.model.login.User;
import com.avirantEnterprises.information_collector.service.login.EmailService;
import com.avirantEnterprises.information_collector.service.login.UserLoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private UserLoginService userLoginService;
    @Autowired
    private EmailService emailService;

    @GetMapping("/user")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User());
        return "login/login";
    }

    @PostMapping("/sendOtp")
    public String sendOtp(@ModelAttribute("user") User user, Model model) {
        if (StringUtils.isEmpty(user.getEmail())) {
            model.addAttribute("message", "Email cannot be empty.");
            return "login/login";
        }

        User existingUser = userLoginService.findByEmail(user.getEmail());
        if (existingUser == null) {
            model.addAttribute("message", "Email not registered.");
            return "login/login";
        }

        String otp = userLoginService.generateOtp();
        userLoginService.updateOtp(user.getEmail(), otp);
        emailService.sendOtpEmail(user.getEmail(), otp);

        model.addAttribute("email", user.getEmail());
        model.addAttribute("message", "An OTP has been sent to your email.");
        return "login/otp";
    }

    @PostMapping("/verifyOtp")
    public String verifyOtp(@RequestParam("email") String email, @RequestParam("otp") String otp, Model model,HttpServletRequest request) {
        if (!userLoginService.verifyOtp(email, otp)) {
            model.addAttribute("message", "Invalid or expired OTP.");
            model.addAttribute("email", email);
            return "login/otp";  // Stay on the OTP page with an error message
        }

        // User found by email and OTP is valid, authenticate the user
        User user = userLoginService.findByEmail(email);
        if (user != null) {
            authenticateUser(user);
            request.getSession().setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
        }



        return "redirect:/login/userDashboard";  // Redirect to user dashboard on successful OTP verification
    }

    // Show user dashboard
    @GetMapping("/userDashboard")
    public String viewDashboard(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() ||
                "anonymousUser".equals(authentication.getPrincipal())) {
            return "redirect:/login/user";
        }

        // Principal is now the username (set in authenticateUser)
        Object principal = authentication.getPrincipal();

        if (principal instanceof String) {
            model.addAttribute("username", principal.toString()); // Display the username
        } else {
            model.addAttribute("username", "Unknown"); // Fallback in case of unexpected Principal
        }

        return "forms/userDashboard"; // Dashboard view for authenticated users
    }



    // Authenticate user in the Spring Security context
    private void authenticateUser(User user) {
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole().toUpperCase());

        // Set the username as the Principal instead of the User object
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user.getUsername(), null, Collections.singletonList(authority)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        System.out.println("User authenticated: " + user.getUsername());
    }



}
