package com.avirantEnterprises.information_collector.controller.forms;

import com.avirantEnterprises.information_collector.model.forms.FormAssignment;
import com.avirantEnterprises.information_collector.model.login.User;
import com.avirantEnterprises.information_collector.model.project.TaskAssignment;
import com.avirantEnterprises.information_collector.repository.forms.FormAssignmentRepository;
import com.avirantEnterprises.information_collector.repository.login.UserLoginRepository;
import com.avirantEnterprises.information_collector.service.forms.FormAssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/userassign")
public class UserFormController {
    @Autowired
    private FormAssignmentRepository formAssignmentRepository;

    @Autowired
    private UserLoginRepository userLoginRepository;

    @Autowired
    private FormAssignmentService formAssignmentService;

    @GetMapping
    public String showAssignedForms(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> userOptional = userLoginRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            System.out.println("User authenticated2: " + user.getUsername());
            List<FormAssignment> assignments = formAssignmentRepository.findByUserId(user.getId());
            model.addAttribute("assignments", assignments);
        } else {
            model.addAttribute("error", "User not found.");
            return "errorPage"; // Redirect to an error page if user not found
        }
        return "forms/userForms";
    }

    @GetMapping("/fill")
    public String showFormToFill( @RequestParam String formName, Model model) {
        try {
            System.out.println("Fetching form data for  formName: " + formName);

            model.addAttribute("formName", formName);

            switch (formName) {
                case "Feedback":
                    return "redirect:/feedbackForm";
                case "ProgressUpdate":
                    return "redirect:/progressUpdateForm";
                case "ProjectProposal":
                    return "redirect:/projectProposalForm";
                case "ResourceAllocation":
                    return "redirect:/resourceAllocationForm";
                case "TaskAssignment":
                    return "redirect:/taskAssignForm";
                default:
                    System.err.println("Invalid form name: " + formName);
                    model.addAttribute("error", "Invalid form name: " + formName);
                    return "forms/errorPage";
            }
        }
        catch (RuntimeException e)
        {
            model.addAttribute("error", e.getMessage());
            return "forms/errorPage";
        }
    }
}
