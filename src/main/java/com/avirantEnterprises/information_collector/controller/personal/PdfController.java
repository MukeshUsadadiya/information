package com.avirantEnterprises.information_collector.controller.personal;

import com.avirantEnterprises.information_collector.model.project.*;
import com.avirantEnterprises.information_collector.service.personal.PdfService;
import com.avirantEnterprises.information_collector.service.project.*;
import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;

@Controller
public class PdfController {

    @Autowired
    private PdfService pdfService;

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private ProgressUpdateService progressUpdateService;
    @Autowired
    private ProjectProposalService projectProposalService;

    @Autowired
    private ResourceAllocationService resourceAllocationService;
    @Autowired
    private TaskAssignmentService taskAssignmentService;
   /* @GetMapping("/profile/download/{id}")
    public ResponseEntity<byte[]> downloadUserProfilePdf(@PathVariable Long id) throws IOException, DocumentException {
        byte[] pdfContent = pdfService.generateUserProfilePdf(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "user_profile_" + id + ".pdf");
        return ResponseEntity.ok().headers(headers).body(pdfContent);
    }*/
    @GetMapping("/form/download/feedback/{id}")
    public ResponseEntity<byte[]> downloadFeedbackPdf(@PathVariable Long id) throws IOException, DocumentException {
        Feedback feedback = feedbackService.getFeedbackById(id);
        byte[] pdfContent = pdfService.generateFeedbackPdf(feedback);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "Feedback_form_" + id + ".pdf");
        return ResponseEntity.ok().headers(headers).body(pdfContent);
    }

    @GetMapping("/form/download/progress-update/{id}")
    public ResponseEntity<byte[]> downloadProgressUpdatePdf(@PathVariable Long id) throws IOException, DocumentException {
        ProgressUpdate progressUpdate = progressUpdateService.getProgressUpdateById(id);
        byte[] pdfContent = pdfService.generateProgressUpdatePdf(progressUpdate);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "ProgressUpdate_form_" + id + ".pdf");
        return ResponseEntity.ok().headers(headers).body(pdfContent);
    }

    // Endpoint to download Project Proposal PDF
    @GetMapping("/form/download/project-proposal/{id}")
    public ResponseEntity<byte[]> downloadProjectProposalPdf(@PathVariable Long id) throws IOException, DocumentException {
        ProjectProposal projectProposal = projectProposalService.getProjectProposalById(id);
        byte[] pdfContent = pdfService.generateProjectProposalPdf(projectProposal);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "ProjectProposal_form_" + id + ".pdf");
        return ResponseEntity.ok().headers(headers).body(pdfContent);
    }

    // Endpoint to download Resource Allocation PDF
    @GetMapping("/form/download/resource-allocation/{id}")
    public ResponseEntity<byte[]> downloadResourceAllocationPdf(@PathVariable Long id) throws IOException, DocumentException {
        ResourceAllocation resourceAllocation = resourceAllocationService.getResourceAllocationById(id);
        byte[] pdfContent = pdfService.generateResourceAllocationPdf(resourceAllocation);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "ResourceAllocation_form_" + id + ".pdf");
        return ResponseEntity.ok().headers(headers).body(pdfContent);
    }

    // Endpoint to download Task Assignment PDF
    @GetMapping("/form/download/task-assignment/{id}")
    public ResponseEntity<byte[]> downloadTaskAssignmentPdf(@PathVariable Long id) throws IOException, DocumentException {
        TaskAssignment taskAssignment = taskAssignmentService.getTaskById(id);
        byte[] pdfContent = pdfService.generateTaskAssignmentPdf(taskAssignment);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "TaskAssignment_form_" + id + ".pdf");
        return ResponseEntity.ok().headers(headers).body(pdfContent);
    }
}
