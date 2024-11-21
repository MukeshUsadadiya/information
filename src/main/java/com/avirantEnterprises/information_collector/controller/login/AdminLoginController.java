package com.avirantEnterprises.information_collector.controller.login;

import com.avirantEnterprises.information_collector.model.login.User;
import com.avirantEnterprises.information_collector.service.login.UserLoginService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller

public class AdminLoginController {
    @GetMapping("/login/admin")
    public String showAdminLoginPage() {
        // This returns the admin login page from /templates/login/adminlogin.html
        return "login/adminlogin";
    }

    @PostMapping("/login/admin")
    public String adminLogin(String username, String password) {
        // In a real application, you'd authenticate the user here
        if ("admin".equals(username) && "admin123".equals(password)) {
            return "redirect:/login/adminDashboard";  // On successful login, redirect to dashboard
        }
        return "redirect:/login/admin";  // If login fails, redirect back to the login page
    }

    @GetMapping("/login/adminDashboard")
    public String adminDashboard() {
        // This returns the admin dashboard page from /templates/login/adminDashboard.html
        return "login/adminDashboard";
    }

    @GetMapping("/login/projectforms")
        public String showProjectForms()
        {
            return "/login/projectforms";
        }
}
