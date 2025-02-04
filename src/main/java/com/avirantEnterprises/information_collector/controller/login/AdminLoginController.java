package com.avirantEnterprises.information_collector.controller.login;

import com.avirantEnterprises.information_collector.model.login.User;
import com.avirantEnterprises.information_collector.service.login.UserLoginService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
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
import java.util.List;
import java.util.Optional;

@Controller
public class AdminLoginController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserLoginService userLoginService;
    @GetMapping("/login/admin")
    public String showAdminLoginPage() {
        // This returns the admin login page from /templates/login/adminlogin.html
        return "login/adminlogin";
    }

    @PostMapping("/login/admin")
    public String login(@ModelAttribute("user") User user, Model model, HttpServletRequest request) {
        if (StringUtils.isEmpty(user.getUsername()) || StringUtils.isEmpty(user.getPassword())) {
            model.addAttribute("message", "Username and password cannot be empty.");
            return "login/adminlogin";  // Stay on the login page if credentials are empty
        }

        // Fetch the admin user from the database using Optional
        Optional<User> optionalAdmin = userLoginService.findByUsername(user.getUsername());
        if (optionalAdmin.isEmpty()) {
            model.addAttribute("message", "Invalid username.");
            return "login/adminlogin";  // Stay on the login page if username is invalid
        }

        User existingAdmin = optionalAdmin.get();

        // Check if the entered password matches the stored password
        if (!userLoginService.checkPassword(user.getPassword(), existingAdmin.getPassword())) {
            model.addAttribute("message", "Invalid password.");
            return "login/adminlogin";  // Stay on the login page if password is incorrect
        }

        // Admin found and authenticated, set authentication in the Spring Security context
        authenticateAdmin(existingAdmin);
        request.getSession().setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

        return "redirect:/adminDashboard";  // Redirect to admin dashboard on successful login
        // Redirect to admin dashboard on successful login
    }


    private void authenticateAdmin(User admin) {
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + admin.getRole().toUpperCase());

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                admin.getUsername(), admin.getPassword(), Collections.singletonList(authority)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        System.out.println("Admin authenticated: " + admin.getUsername());
    }

    @GetMapping("/adminDashboard")
    public String adminDashboard(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() ||
                "anonymousUser".equals(authentication.getPrincipal())) {
            return "redirect:/admin/login";  // Redirect to login if not authenticated
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof String) {
            model.addAttribute("username", principal.toString());  // Display the username
        } else {
            model.addAttribute("username", "Unknown");
        }

        return "login/adminDashboard";
    }

    @GetMapping("/login/projectforms")
        public String showProjectForms()
        {
            return "/login/projectforms";

        }
    @GetMapping("/allUsers")
    public String getAllUser(Model model)
    {
        List<User> users =userLoginService.getAllUsers();
        model.addAttribute("users",users);
        return"login/AllUsers";
    }
}

