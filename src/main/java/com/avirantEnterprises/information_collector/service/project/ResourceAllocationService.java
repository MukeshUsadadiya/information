package com.avirantEnterprises.information_collector.service.project;

import com.avirantEnterprises.information_collector.model.project.ResourceAllocation;
import com.avirantEnterprises.information_collector.repository.project.ResourceAllocationRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;


@Service
public class ResourceAllocationService {
    @Value("${file.excel-dir}")
    private String excelDir;

    private final ResourceAllocationRepository resourceAllocationRepository;

    public ResourceAllocationService(ResourceAllocationRepository resourceAllocationRepository) {
        this.resourceAllocationRepository = resourceAllocationRepository;
    }

    public ResourceAllocation createResourceAllocation(String resourceName, int quantityNeeded, String resourceType,
                                                       String allocationDate, double estimatedCost, MultipartFile file) throws IOException {
        ResourceAllocation resourceAllocation = new ResourceAllocation();
        resourceAllocation.setResourceName(resourceName);
        resourceAllocation.setQuantityNeeded(quantityNeeded);
        resourceAllocation.setResourceType(resourceType);
        resourceAllocation.setAllocationDate(allocationDate);
        resourceAllocation.setEstimatedCost(estimatedCost);

        // Handle file upload
        if (file != null && !file.isEmpty()) {
            String filePath = saveUploadedFile(file);
            resourceAllocation.setFilePath(filePath);
        }

        return resourceAllocationRepository.save(resourceAllocation);
    }

    public String saveUploadedFile(MultipartFile file) throws IOException {
        // Create the directory if it doesn't exist
        File dir = new File(excelDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // Get the original file name and generate a path
        String fileName = file.getOriginalFilename();
        Path filePath = Paths.get(excelDir, fileName);

        // Save the file to the specified directory
        file.transferTo(filePath.toFile());

        // Return the file path to store in the database
        return filePath.toString();
    }



    public List<ResourceAllocation> getAllResourceAllocations() {
        return resourceAllocationRepository.findAll();
    }

    // Get a Resource Allocation by ID
    public ResourceAllocation getResourceAllocationById(Long id) {
        Optional<ResourceAllocation> allocation = resourceAllocationRepository.findById(id);
        return allocation.orElse(null);  // Return null if not found
    }

    // Update a Resource Allocation
    public void updateResourceAllocation(ResourceAllocation resourceAllocation) {
        resourceAllocationRepository.save(resourceAllocation);  // Save updated resource allocation
    }

    // Delete a Resource Allocation by ID
    public void deleteResourceAllocationById(Long id) {
        resourceAllocationRepository.deleteById(id);  // Delete the resource allocation from the repository
    }
}
