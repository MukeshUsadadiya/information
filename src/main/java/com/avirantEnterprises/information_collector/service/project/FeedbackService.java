package com.avirantEnterprises.information_collector.service.project;
import com.avirantEnterprises.information_collector.model.project.Feedback;
import com.avirantEnterprises.information_collector.repository.project.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Value("${file.photo-dir}")
    private String photoDir;// Directory for storing image files

    @Value("${file.pdf-dir}")
    private String pdfDir;  // Directory for storing PDF files

    public void saveFeedback(String feedbackType, String issueTitle, String description, String priority,
                             String status, MultipartFile attachment) throws IOException {

        String attachmentPath = null;

        if (attachment != null && !attachment.isEmpty()) {
            String originalFileName = attachment.getOriginalFilename();
            if (originalFileName != null) {
                String fileExtension = getFileExtension(originalFileName);

                if ("pdf".equalsIgnoreCase(fileExtension)) {
                    attachmentPath = saveFile(attachment, pdfDir);
                } else if (isImageFile(fileExtension)) {
                    attachmentPath = saveFile(attachment, photoDir);
                } else {
                    throw new IllegalArgumentException("Unsupported file type: " + fileExtension);
                }
            }
        }

        Feedback feedback = new Feedback();
        feedback.setFeedbackType(feedbackType);
        feedback.setIssueTitle(issueTitle);
        feedback.setDescription(description);
        feedback.setPriority(priority);
        feedback.setStatus(status);
        feedback.setAttachment(attachmentPath);

        feedbackRepository.save(feedback);
    }
    public List<Feedback> getAllFeedbacks() {
        return feedbackRepository.findAll(); // Fetch all feedback records from database
    }
    private String getFileExtension(String fileName) {
        int index = fileName.lastIndexOf('.');
        return (index > 0) ? fileName.substring(index + 1) : "";
    }

    private boolean isImageFile(String fileExtension) {
        return List.of("jpg", "jpeg", "png", "gif").contains(fileExtension.toLowerCase());
    }

    private String saveFile(MultipartFile file, String directory) throws IOException {
        String newFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path targetPath = Paths.get(directory).resolve(newFileName).normalize();
        Files.createDirectories(targetPath.getParent());
        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        return targetPath.toString();
    }
    public Feedback getFeedbackById(Long id)
    {
        return feedbackRepository.findById(id).orElse(null);
    }
}
