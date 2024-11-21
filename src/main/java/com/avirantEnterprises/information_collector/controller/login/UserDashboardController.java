package com.avirantEnterprises.information_collector.controller.login;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")

public class UserDashboardController {
    // Endpoint for rendering the dashboard page
   /* @GetMapping

    public String showDashboard() {
        // This method returns the Thymeleaf template to be rendered
        return "dashboard"; // The name of the Thymeleaf template (dashboard.html)
    }



    @GetMapping("/userforms")
    public String userforms() {
        return "login/userforms"; // Ensure this corresponds to userforms.html
    }

    // Endpoint for handling the Manage Users page
    @GetMapping("/manage-users")
    public String manageUsers() {
        return "manage-users"; // Replace with actual template name
    }

    // Endpoint for handling the Manage Projects page
    @GetMapping("/manage-projects")
    public String manageProjects() {
        return "manage-projects"; // Replace with actual template name
    }

    // Endpoint for handling the View Reports page
    @GetMapping("/view-reports")
    public String viewReports() {
        return "view-reports"; // Replace with actual template name
    }

    // Endpoint for handling the settings page
    @GetMapping("/settings")
    public String settings() {
        return "settings"; // Replace with actual template name
    }

    // Logout functionality (if needed)
    @GetMapping("/logout")
    public String logout() {
        // Clear any session or authentication logic here
        return "redirect:/login/login"; // Redirect to login page after
    }
*/
}
