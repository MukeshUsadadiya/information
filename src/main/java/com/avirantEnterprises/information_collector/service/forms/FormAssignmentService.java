package com.avirantEnterprises.information_collector.service.forms;

import com.avirantEnterprises.information_collector.model.forms.FormAssignment;
import com.avirantEnterprises.information_collector.model.login.User;
import com.avirantEnterprises.information_collector.model.project.*;
import com.avirantEnterprises.information_collector.repository.forms.FormAssignmentRepository;
import com.avirantEnterprises.information_collector.repository.login.UserLoginRepository;
import com.avirantEnterprises.information_collector.repository.project.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class FormAssignmentService {
    @Autowired
    FeedbackRepository feedbackRepository;
    @Autowired
    TaskAssignmentRepository taskAssignmentRepository;
    @Autowired
    ProjectProposalRepository projectProposalRepository;
    @Autowired
    ProgressUpdateRepository progressUpdateRepository;

    @Autowired
    ResourceAllocationRepository resourceAllocationRepository;
    @Autowired
    private FormAssignmentRepository formAssignmentRepository;

    @Autowired
    private UserLoginRepository userLoginRepository;

    public void assignFormToUser(Long userId, String formName) {
        User user = userLoginRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        FormAssignment formAssignment = new FormAssignment();
        formAssignment.setUser(user);
        formAssignment.setFormName(formName);
        formAssignment.setAssignedAt(LocalDateTime.now());

        formAssignmentRepository.save(formAssignment);
    }




}
