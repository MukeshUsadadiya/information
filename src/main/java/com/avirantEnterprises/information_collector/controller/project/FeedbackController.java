package com.avirantEnterprises.information_collector.controller.project;

import com.avirantEnterprises.information_collector.model.project.Feedback;


import com.avirantEnterprises.information_collector.service.project.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Controller
public class FeedbackController {

    @Value("${file.pdf-dir}")
    private String pdfDir;

    @Value("${file.photo-dir}")
    private String photoDir;

    @Autowired
    private FeedbackService feedbackService;


    @GetMapping("/feedbackForm")
    public String feedbackForm(Model model) {
        model.addAttribute("feedback", new Feedback());
        return "project/feedback/FeedbackForm";
    }

    @PostMapping("/submitFeedback")
    public String submitFeedback(@RequestParam("feedbackType") String feedbackType,
                                 @RequestParam("issueTitle") String issueTitle,
                                 @RequestParam("description") String description,
                                 @RequestParam("priority") String priority,
                                 @RequestParam("status") String status,
                                 @RequestParam("attachment") MultipartFile attachment,
                                 Model model) {

        try {
            feedbackService.saveFeedback(feedbackType, issueTitle, description, priority, status, attachment);
            model.addAttribute("successMessage", "Feedback submitted successfully!");
            return "project/feedback/success"; // Redirect to feedback list page after successful submission
        } catch (IOException e) {
            model.addAttribute("errorMessage", "Error while saving feedback: " + e.getMessage());
            return "project/feedback/feedbackForm"; // Stay on the form page in case of error
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "project/feedback/feedbackForm"; // Stay on the form page in case of invalid file type error
        }
    }



    @GetMapping("/allFeedbacks")
    public String displayAllFeedbacks(Model model) {
        List<Feedback> feedbackList = feedbackService.getAllFeedbacks(); // Get all feedbacks
        model.addAttribute("feedbackList", feedbackList); // Add feedback list to model
        return "project/feedback/DisplayFeedback"; // Thymeleaf or JSP page to display feedback list
    }


    @GetMapping("/viewFile/{filename:.+}")
    public ResponseEntity<Resource> viewFile(@PathVariable String filename) {
        try {
            Path filePath;

            // Check if the file is an image or PDF based on the extension
            if (filename.endsWith(".pdf")) {
                // For PDF
                filePath = Paths.get(pdfDir).resolve(filename).normalize();
            } else {
                // For images (assumes image files have extensions like .jpg, .png, etc.)
                filePath = Paths.get(photoDir).resolve(filename).normalize();
            }

            File file = filePath.toFile();
            if (!file.exists() || !file.canRead()) {
                return ResponseEntity.notFound().build(); // Return not found if file doesn't exist
            }

            Resource resource = new FileSystemResource(file);

            // Determine content type based on file extension
            MediaType mediaType;
            if (filename.endsWith(".pdf")) {
                mediaType = MediaType.APPLICATION_PDF;
            } else if (filename.endsWith(".jpg") || filename.endsWith(".jpeg") || filename.endsWith(".png")) {
                mediaType = MediaType.IMAGE_JPEG; // Customize based on file type
            } else {
                return ResponseEntity.badRequest().build(); // Handle unsupported file types
            }

            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/feedback/{id}")
    public String viewFeedbackDetails(@PathVariable Long id, Model model) {
        Feedback feedback = feedbackService.getFeedbackById(id);

        if (feedback == null) {
            model.addAttribute("errorMessage", "Feedback not found for the given ID.");
            return "error";
        }
        model.addAttribute("feedback", feedback);
        return "project/feedback/Feedback_View";
    }


    /*@GetMapping("/viewFile/{filename:.+}")
    public ResponseEntity<Resource> viewFile(@PathVariable String filename) {
        try {
            // Determine if the file is PDF or an image based on extension
            Path filePath = null;
            if (filename.toLowerCase().endsWith(".pdf")) {
                filePath = Paths.get(pdfDir).resolve(filename).normalize();
            } else {
                filePath = Paths.get(uploadDir).resolve(filename).normalize();
            }

            if (!Files.exists(filePath) || !Files.isReadable(filePath)) {
                return ResponseEntity.notFound().build(); // Return 404 if file not found
            }

            Resource resource = new UrlResource(filePath.toUri());

            // Check content type and set appropriate response header
            String contentType = Files.probeContentType(filePath);
            HttpHeaders headers = new HttpHeaders();
            if (contentType != null && contentType.startsWith("image")) {
                headers.setContentType(MediaType.parseMediaType(contentType));
                return ResponseEntity.ok().headers(headers).body(resource);
            } else if (contentType != null && contentType.equals("application/pdf")) {
                headers.setContentType(MediaType.APPLICATION_PDF);
                return ResponseEntity.ok().headers(headers).body(resource);
            } else {
                headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");
                return ResponseEntity.ok().headers(headers).body(resource);
            }

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    /*@GetMapping("/editFeedback/{id}")
    public String editFeedbackForm(@PathVariable("id") Long feedbackId, Model model) {
        Feedback feedback = feedbackService.getFeedbackById(feedbackId);
        if (feedback == null) {
            model.addAttribute("errorMessage", "Feedback not found.");
            return "feedback/notFound";
        }
        model.addAttribute("feedback", feedback);
        return "project/feedback/edit";
    }

    @PostMapping("/updateFeedback/{id}")
    public String updateFeedback(
            @PathVariable("id") Long feedbackId,
            @ModelAttribute Feedback feedback,
            @RequestParam("file") MultipartFile file,
            Model model) {
        try {
            if (!file.isEmpty()) {
                String filePath;
                if (file.getOriginalFilename().toLowerCase().endsWith(".pdf")) {
                    filePath = saveFile(file, pdfDir);
                } else {
                    filePath = saveFile(file, uploadDir);
                }
                feedback.setAttachment(filePath);
            }
            feedbackService.updateFeedback(feedbackId, feedback);
            model.addAttribute("message", "Feedback updated successfully!");
        } catch (IOException e) {
            model.addAttribute("errorMessage", "Error updating feedback: " + e.getMessage());
        }
        return "project/feedback/edit";
    }

    @PostMapping("/deleteFeedback/{id}")
    public String deleteFeedback(@PathVariable Long id, Model model) {
        try {
            feedbackService.deleteFeedback(id);
            model.addAttribute("message", "Feedback deleted successfully.");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error deleting feedback: " + e.getMessage());
        }
        return "project/feedback/DisplayFeedback";
    }

   */
}
