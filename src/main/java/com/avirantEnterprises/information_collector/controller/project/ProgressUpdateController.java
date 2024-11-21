package com.avirantEnterprises.information_collector.controller.project;

import com.avirantEnterprises.information_collector.model.project.ProgressUpdate;
import com.avirantEnterprises.information_collector.service.project.ProgressUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Controller
public class ProgressUpdateController {
    @Value("${file.pdf-dir}")
    private String pdfDir;

    @Value("${file.excel-dir}")
    private String excelDir;
    @Autowired
    private ProgressUpdateService progressUpdateService;

    @GetMapping("/progressUpdateForm")
    public String progressUpdateForm(Model model) {
        model.addAttribute("progressUpdate", new ProgressUpdate());
        return "project/progress/ProgressUpdateForm";
    }

    @PostMapping("/submitProgressUpdate")
    public String submitProgressUpdate(@ModelAttribute("progressUpdate") ProgressUpdate progressUpdate,
                                       @RequestParam("file") MultipartFile file,
                                       Model model) {
        try {
            progressUpdateService.createProgressUpdate(progressUpdate.getMilestoneTitle(),
                    progressUpdate.getProgressDescription(),
                    progressUpdate.getCompletionPercentage(),
                    progressUpdate.getDateOfUpdate(),
                    progressUpdate.getCurrentStatus(),
                    file);
            model.addAttribute("message", "Progress update submitted successfully!");
            return "project/progress/success";
        } catch (IOException e) {
            model.addAttribute("errorMessage", "Error uploading file: " + e.getMessage());
            return "project/progress/ProgressUpdateForm";
        }
    }

    @GetMapping("/uploads/{filename}")
    @ResponseBody
    public ResponseEntity<FileSystemResource> viewFile(@PathVariable("filename") String filename) {
        String fileExtension = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
        Path filePath = null;
        MediaType mediaType = null;

        // Determine file path and set content type based on file extension
        if ("pdf".equals(fileExtension)) {
            filePath = Paths.get(pdfDir).resolve(filename);
            mediaType = MediaType.APPLICATION_PDF;  // Content-Type for PDF
        } else if ("xls".equals(fileExtension) || "xlsx".equals(fileExtension)) {
            filePath = Paths.get(excelDir).resolve(filename);
            mediaType = MediaType.APPLICATION_OCTET_STREAM;  // Content-Type for Excel
        }

        // Return the file if it exists
        if (filePath != null && filePath.toFile().exists()) {
            FileSystemResource resource = new FileSystemResource(filePath.toFile());
            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=" + filename)
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();  // Return 404 if file not found
        }
    }
    @GetMapping("/viewProgressUpdates")
    public String viewProgressUpdates(Model model) {
        List<ProgressUpdate> updates = progressUpdateService.getAllProgressUpdates();
        model.addAttribute("updates", updates);
        return "project/progress/DisplayProgressUpdate";
    }

    @GetMapping("/editProgressUpdate/{id}")
    public String editProgressUpdateForm(@PathVariable("id") Long progressUpdateId, Model model) {
        ProgressUpdate progressUpdate = progressUpdateService.getProgressUpdateById(progressUpdateId);
        model.addAttribute("progressUpdate", progressUpdate);  // Ensure the object is added to the model
        return "project/progress/EditProgressUpdate";
    }

    @PostMapping("/updateProgressUpdate/{id}")
    public String updateProgressUpdate(@PathVariable("id") Long progressUpdateId,
                                       @ModelAttribute ProgressUpdate progressUpdate,
                                       @RequestParam("file") MultipartFile file,
                                       RedirectAttributes redirectAttributes) {
        try {
            if (!file.isEmpty()) {
                String filePath = saveFile(file);
                progressUpdate.setFilePath(filePath); // Store the file path
            }

            progressUpdate.setId(progressUpdateId); // Set ID
            progressUpdateService.updateProgressUpdate(progressUpdate); // Update in DB
            redirectAttributes.addFlashAttribute("message", "Progress update updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Error updating progress update: " + e.getMessage());
        }
        return "redirect:/viewProgressUpdates"; // Redirect to progress updates page
    }

    private String saveFile(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename(); // Unique file name
        Path dirPath = file.getOriginalFilename().endsWith(".pdf") ? Paths.get(pdfDir) : Paths.get(excelDir);
        Path filePath = dirPath.resolve(fileName);

        Files.createDirectories(filePath.getParent());
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        return fileName; // Return file name only
    }
    // Method to delete a Progress Update
    @PostMapping("/deleteProgressUpdate/{id}")
    public String deleteProgressUpdate(@PathVariable("id") Long progressUpdateId, RedirectAttributes redirectAttributes) {
        try {
            progressUpdateService.deleteProgressUpdateById(progressUpdateId);
            redirectAttributes.addFlashAttribute("message", "Progress update deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Error deleting progress update: " + e.getMessage());
        }
        return "redirect:/viewProgressUpdates";  // Redirect to the progress updates list page
    }
}
