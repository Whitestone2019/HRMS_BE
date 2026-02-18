package com.whitestone.hrms.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileUploadUtil {
    
    public static String saveFile(MultipartFile file, String uploadDir) throws IOException {
        String basePath = System.getProperty("user.dir");
        Path uploadPath = Paths.get(basePath, uploadDir);
        
        // Create directory if it doesn't exist
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new IllegalArgumentException("File name is empty");
        }

        // Sanitize filename
        String sanitizedFilename = originalFilename.replaceAll("[^a-zA-Z0-9._-]", "_");
        
        // Generate unique filename
        String timestamp = String.valueOf(System.currentTimeMillis());
        String uniqueFilename = timestamp + "_" + sanitizedFilename;

        // Save file
        Path filePath = uploadPath.resolve(uniqueFilename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return uploadDir + File.separator + uniqueFilename;
    }
}