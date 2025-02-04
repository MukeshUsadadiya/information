package com.avirantEnterprises.information_collector.controller.forms;

import com.avirantEnterprises.information_collector.repository.forms.FormAssignmentRepository;
import com.avirantEnterprises.information_collector.repository.login.UserLoginRepository;
import com.avirantEnterprises.information_collector.service.forms.FormAssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/forms")
public class FormAssignmentController {
    @Autowired
    private FormAssignmentService formAssignmentService;

    @Autowired
    private UserLoginRepository userLoginRepository;

    @Autowired
    private FormAssignmentRepository formAssignmentRepository;

    @GetMapping("/assign")
    public String getFormAssignmentPage(Model model) {
        model.addAttribute("users", userLoginRepository.findAll());
        return "forms/assignForm";
    }

    @PostMapping("/assign")
    public String assignFormToUser(@RequestParam Long userId, @RequestParam String formName) {
        formAssignmentService.assignFormToUser(userId, formName);
        return "redirect:/admin/forms/assign";
    }

    @GetMapping("/assigned")
    public String getAssignedForms(Model model) {
        model.addAttribute("assignments", formAssignmentRepository.findAll());
        return "forms/assignedForms";
    }
}
