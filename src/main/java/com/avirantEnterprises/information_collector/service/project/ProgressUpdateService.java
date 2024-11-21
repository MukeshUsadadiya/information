package com.avirantEnterprises.information_collector.service.project;

import com.avirantEnterprises.information_collector.model.project.ProgressUpdate;
import com.avirantEnterprises.information_collector.repository.project.ProgressUpdateRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class ProgressUpdateService {
    @Value("${file.excel-dir}")
    private String excelDir;

    @Value("${file.pdf-dir}")
    private String pdfDir;

    private final ProgressUpdateRepository progressUpdateRepository;

    public ProgressUpdateService(ProgressUpdateRepository progressUpdateRepository) {
        this.progressUpdateRepository = progressUpdateRepository;
    }

    public ProgressUpdate createProgressUpdate(String milestoneTitle, String progressDescription, int completionPercentage,
                                               String dateOfUpdate, String currentStatus, MultipartFile file) throws IOException {
        ProgressUpdate progressUpdate = new ProgressUpdate();
        progressUpdate.setMilestoneTitle(milestoneTitle);
        progressUpdate.setProgressDescription(progressDescription);
        progressUpdate.setCompletionPercentage(completionPercentage);
        progressUpdate.setDateOfUpdate(dateOfUpdate);
        progressUpdate.setCurrentStatus(currentStatus);

        // Handle file upload for Excel or PDF reports
        if (file != null && !file.isEmpty()) {
            String filePath = saveFile(file);
            progressUpdate.setFilePath(filePath); // Set the file path to the entity
        }

        return progressUpdateRepository.save(progressUpdate);
    }

    // Method to save Excel or PDF based on file type
    public String saveFile(MultipartFile file) throws IOException {
        // Extract file extension
        String originalFileName = file.getOriginalFilename();
        String fileExtension = getFileExtension(originalFileName);

        // Determine directory based on file type
        String directory = "";
        if (fileExtension.equalsIgnoreCase("xlsx") || fileExtension.equalsIgnoreCase("xls")) {
            directory = excelDir;
        } else if (fileExtension.equalsIgnoreCase("pdf")) {
            directory = pdfDir;
        } else {
            throw new IllegalArgumentException("Invalid file type. Only Excel and PDF are allowed.");
        }

        // Create the target file path
        Path path = Paths.get(directory).resolve(originalFileName);
        Files.createDirectories(path.getParent()); // Ensure directory exists
        Files.copy(file.getInputStream(), path);  // Save the file

        return path.toString();  // Return the file path for storage in the database
    }

    // Helper method to extract the file extension
    private String getFileExtension(String fileName) {
        if (fileName != null && fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        return "";
    }
    public List<ProgressUpdate> getAllProgressUpdates() {
        return progressUpdateRepository.findAll();
    }
    public ProgressUpdate getProgressUpdateById(Long id) {
        return progressUpdateRepository.findById(id).orElse(null);  // Return null if not found
    }

    // Method to update an existing progress update
    public void updateProgressUpdate(ProgressUpdate progressUpdate) {
        progressUpdateRepository.save(progressUpdate);  // Save updated progress update
    }

    // Method to delete a progress update by ID
    public void deleteProgressUpdateById(Long id) {
        progressUpdateRepository.deleteById(id);  // Delete the progress update from the repository
    }

}
