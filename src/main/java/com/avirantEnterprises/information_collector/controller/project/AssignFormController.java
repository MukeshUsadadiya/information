package com.avirantEnterprises.information_collector.controller.project;

import com.avirantEnterprises.information_collector.service.login.UserLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class AssignFormController {
    @Autowired
    UserLoginService userLoginService;
    @GetMapping("/assign-forms")
    public String showAssignFormsPage(Model model) {
        // Fetch all usernames
        List<String> usernames = userLoginService.getAllUsers()
                .stream()
                .map(user -> user.getUsername()) // Extract usernames
                .collect(Collectors.toList());
        model.addAttribute("usernames", usernames); // Add usernames to the model
        return "assignForms"; // Return the Thymeleaf template
    }
}
