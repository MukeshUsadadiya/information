//package com.avirantEnterprises.information_collector.service.personal;
//
//import com.avirantEnterprises.information_collector.model.personal.User;
//import com.itextpdf.text.Document;
//import com.itextpdf.text.DocumentException;
//import com.itextpdf.text.Element;
//import com.itextpdf.text.Image;
//import com.itextpdf.text.Paragraph;
//import com.itextpdf.text.pdf.PdfPTable;
//import com.itextpdf.text.pdf.PdfWriter;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//
//@Service
//public class PdfService {
//
//    @Autowired
//    private RegistrationService registrationService;
//
//    public byte[] generateUserProfilePdf(Long userId) throws IOException, DocumentException {
//        User user = registrationService.getUserById(userId);
//
//        if (user == null) {
//            throw new RuntimeException("User not found");
//        }
//
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        Document document = new Document();
//        PdfWriter.getInstance(document, outputStream);
//        document.open();
//
//        document.add(new Paragraph("User Profile"));
//        document.add(new Paragraph("\n"));
//
//        PdfPTable table = new PdfPTable(2);
//        table.setWidthPercentage(100);
//        table.setSpacingBefore(10f);
//        table.setSpacingAfter(10f);
//
//        table.addCell("Name");
//        table.addCell(user.getName());
//
//        table.addCell("Date of Birth");
//        table.addCell(user.getDob().toString());
//
//        table.addCell("Contact Info");
//        table.addCell(user.getContact());
//
//        document.add(table);
//
//        if (user.getProfilePicPath() != null) {
//            Image image = Image.getInstance(Files.readAllBytes(Paths.get(user.getProfilePicPath())));
//            image.setAlignment(Element.ALIGN_CENTER);
//            image.scaleToFit(100, 400); // Set the size of the image
//            document.add(image);
//        }
//
//        document.close();
//
//        return outputStream.toByteArray();
//    }
//}

package com.avirantEnterprises.information_collector.service.personal;


import com.avirantEnterprises.information_collector.model.project.*;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class PdfService {

   // @Autowired
    //private RegistrationService registrationService;

    private final Path rootLocation = Paths.get("upload-dir");

    /*public byte[] generateUserProfilePdf(Long userId) throws IOException, DocumentException {
        User user = registrationService.getUserById(userId);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, outputStream);
        document.open();

        document.add(new Paragraph("User Profile"));
        document.add(new Paragraph("\n"));

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        table.addCell("Name");
        table.addCell(user.getName());

        table.addCell("Date of Birth");
        table.addCell(user.getDob().toString());

        table.addCell("Contact Info");
        table.addCell(user.getContact());

        document.add(table);

        if (user.getProfilePicPath() != null) {
            String sanitizedFilePath = rootLocation.resolve(Paths.get(user.getProfilePicPath()))
                    .normalize().toAbsolutePath().toString();
            Image image = Image.getInstance(Files.readAllBytes(Paths.get(sanitizedFilePath)));
            image.setAlignment(Element.ALIGN_CENTER);
            image.scaleToFit(100, 400); // Set the size of the image
            document.add(image);
        }

        document.close();

        return outputStream.toByteArray();
    }*/

    public byte[] generateFeedbackPdf(Feedback feedback) throws IOException, DocumentException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, outputStream);
        document.open();

        document.add(new Paragraph("Feedback Form"));
        document.add(new Paragraph("\n"));

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        table.addCell("Feedback Type");
        table.addCell(feedback.getFeedbackType());
        table.addCell("Issue Title");
        table.addCell(feedback.getIssueTitle());
        table.addCell("Description");
        table.addCell(feedback.getDescription());
        table.addCell("Priority");
        table.addCell(feedback.getPriority());
        table.addCell("Status");
        table.addCell(feedback.getStatus());

        document.add(table);
        document.close();

        return outputStream.toByteArray();
    }

    public byte[] generateProgressUpdatePdf(ProgressUpdate progressUpdate) throws IOException, DocumentException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, outputStream);
        document.open();

        document.add(new Paragraph("Progress Update Form"));
        document.add(new Paragraph("\n"));

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        table.addCell("Milestone Title");
        table.addCell(progressUpdate.getMilestoneTitle());
        table.addCell("Progress Description");
        table.addCell(progressUpdate.getProgressDescription());
        table.addCell("Completion Percentage");
        table.addCell(String.valueOf(progressUpdate.getCompletionPercentage()));
        table.addCell("Date of Update");
        table.addCell(progressUpdate.getDateOfUpdate());
        table.addCell("Current Status");
        table.addCell(progressUpdate.getCurrentStatus());

        document.add(table);
        document.close();

        return outputStream.toByteArray();
    }

    // Project Proposal PDF generation
    public byte[] generateProjectProposalPdf(ProjectProposal projectProposal) throws IOException, DocumentException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, outputStream);
        document.open();

        document.add(new Paragraph("Project Proposal Form"));
        document.add(new Paragraph("\n"));

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        table.addCell("Project Title");
        table.addCell(projectProposal.getProjectTitle());
        table.addCell("Project Description");
        table.addCell(projectProposal.getProjectDescription());
        table.addCell("Department");
        table.addCell(projectProposal.getDepartment());
        table.addCell("Start Date");
        table.addCell(projectProposal.getStartDate());
        table.addCell("Estimated Budget");
        table.addCell(String.valueOf(projectProposal.getEstimatedBudget()));

        document.add(table);
        document.close();

        return outputStream.toByteArray();
    }

    // Resource Allocation PDF generation
    public byte[] generateResourceAllocationPdf(ResourceAllocation resourceAllocation) throws IOException, DocumentException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, outputStream);
        document.open();

        document.add(new Paragraph("Resource Allocation Form"));
        document.add(new Paragraph("\n"));

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        table.addCell("Resource Name");
        table.addCell(resourceAllocation.getResourceName());
        table.addCell("Quantity Needed");
        table.addCell(String.valueOf(resourceAllocation.getQuantityNeeded()));
        table.addCell("Resource Type");
        table.addCell(resourceAllocation.getResourceType());
        table.addCell("Allocation Date");
        table.addCell(resourceAllocation.getAllocationDate());
        table.addCell("Estimated Cost");
        table.addCell(String.valueOf(resourceAllocation.getEstimatedCost()));

        document.add(table);
        document.close();

        return outputStream.toByteArray();
    }

    // Task Assignment PDF generation
    public byte[] generateTaskAssignmentPdf(TaskAssignment taskAssignment) throws IOException, DocumentException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, outputStream);
        document.open();

        document.add(new Paragraph("Task Assignment Form"));
        document.add(new Paragraph("\n"));

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        table.addCell("Task Name");
        table.addCell(taskAssignment.getTaskName());
        table.addCell("Description");
        table.addCell(taskAssignment.getDescription());
        table.addCell("Assignee Name");
        table.addCell(taskAssignment.getAssigneeName());
        table.addCell("Due Date");
        table.addCell(taskAssignment.getDueDate().toString());
        table.addCell("Priority");
        table.addCell(taskAssignment.getPriority());

        document.add(table);
        document.close();

        return outputStream.toByteArray();
    }
}

