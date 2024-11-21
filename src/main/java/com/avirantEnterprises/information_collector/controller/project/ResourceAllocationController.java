package com.avirantEnterprises.information_collector.controller.project;

import com.avirantEnterprises.information_collector.model.project.Feedback;
import com.avirantEnterprises.information_collector.model.project.ResourceAllocation;
import com.avirantEnterprises.information_collector.service.project.ResourceAllocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
public class ResourceAllocationController {
    @Autowired
    private ResourceAllocationService resourceAllocationService;

    @Value("${file.excel-dir}")
    private String excelDir;
    @GetMapping("/resourceAllocationForm")
    public String resourceAllocationForm(Model model) {
        model.addAttribute("resourceAllocation", new ResourceAllocation());
        return "project/resource/ResourceAllocation";
    }

    @PostMapping("/submitResourceAllocation")
    public String createResourceAllocation(@RequestParam("resourceName") String resourceName,
                                           @RequestParam("quantityNeeded") int quantityNeeded,
                                           @RequestParam("resourceType") String resourceType,
                                           @RequestParam("allocationDate") String allocationDate,
                                           @RequestParam("estimatedCost") double estimatedCost,
                                           @RequestParam("file") MultipartFile file, Model model) {
        try {
            resourceAllocationService.createResourceAllocation(resourceName, quantityNeeded, resourceType,
                    allocationDate, estimatedCost, file);
            model.addAttribute("message", "Resource allocation submitted successfully!");
            return "project/resource/success";
        } catch (IOException e) {
            model.addAttribute("errorMessage", "Could not upload file: " + e.getMessage());
            return "project/resource/ResourceAllocation";
        }
    }

        @GetMapping("/viewResourceAllocations")
    public String viewAllResourceAllocations(Model model) {
        List<ResourceAllocation> allocations = resourceAllocationService.getAllResourceAllocations();
        model.addAttribute("allocations", allocations);
        return "project/resource/DisplayResourceAllocation";
    }

    @GetMapping("/resource/{id}")
    public String viewFeedbackDetails(@PathVariable Long id, Model model) {
        ResourceAllocation resourceAllocation = resourceAllocationService.getResourceAllocationById(id);

        if (resourceAllocation == null) {
            model.addAttribute("errorMessage", "Feedback not found for the given ID.");
            return "error";
        }
        model.addAttribute("allocation", resourceAllocation);
        return "project/resource/Resource_view";
    }

    // Edit Resource Allocation Form
    @GetMapping("/editResourceAllocation/{id}")
    public String editResourceAllocationForm(@PathVariable("id") Long resourceAllocationId, Model model) {
        ResourceAllocation resourceAllocation = resourceAllocationService.getResourceAllocationById(resourceAllocationId);
        model.addAttribute("resourceAllocation", resourceAllocation);
        return "project/resource/EditResourceAllocation";
    }

    // Handle updating Resource Allocation
    @PostMapping("/updateResourceAllocation/{id}")
    public String updateResourceAllocation(@PathVariable("id") Long resourceAllocationId,
                                           @ModelAttribute ResourceAllocation resourceAllocation,
                                           @RequestParam("file") MultipartFile file,
                                           RedirectAttributes redirectAttributes) {
        try {
            if (!file.isEmpty()) {
                String filePath = saveFile(file);  // Save the file and get the path
                resourceAllocation.setFilePath(filePath);  // Store the file path
            }
            resourceAllocation.setId(resourceAllocationId);  // Set ID
            resourceAllocationService.updateResourceAllocation(resourceAllocation);  // Update in DB
            redirectAttributes.addFlashAttribute("message", "Resource allocation updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Error updating resource allocation: " + e.getMessage());
        }
        return "redirect:/viewResourceAllocations"; // Redirect to resource allocations page
    }

    // Handle deleting Resource Allocation
    @PostMapping("/deleteResourceAllocation/{id}")
    public String deleteResourceAllocation(@PathVariable("id") Long resourceAllocationId, RedirectAttributes redirectAttributes) {
        try {
            resourceAllocationService.deleteResourceAllocationById(resourceAllocationId);
            redirectAttributes.addFlashAttribute("message", "Resource allocation deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Error deleting resource allocation: " + e.getMessage());
        }
        return "redirect:/viewResourceAllocations";  // Redirect to the resource allocations list page
    }

    // Save the uploaded file (Excel)
    private String saveFile(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename(); // Unique file name
        Path dirPath = Paths.get(excelDir);  // Get the path for storing files
        Path filePath = dirPath.resolve(fileName);

        Files.createDirectories(filePath.getParent());  // Ensure the directory exists
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);  // Save the file
        return fileName; // Return file name only
    }
}
