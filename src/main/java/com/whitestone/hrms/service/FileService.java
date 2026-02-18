package com.whitestone.hrms.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Service
public class FileService {
    
    private final String UPLOAD_DIR = "uploads";
    private final Set<String> ALLOWED_EXTENSIONS = new HashSet<>(Arrays.asList(
        "pdf", "doc", "docx", "txt", "xls", "xlsx", "jpg", "jpeg", "png", "ppt", "pptx"
    ));

    public String saveFile(MultipartFile file) throws IOException {
        // Get upload directory
        String basePath = System.getProperty("user.dir");
        Path uploadPath = Paths.get(basePath, UPLOAD_DIR);
        
        // Create directory if it doesn't exist
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Sanitize filename
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new IllegalArgumentException("File name is empty");
        }

        // Check file extension
        String extension = "";
        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex > 0) {
            extension = originalFilename.substring(dotIndex + 1).toLowerCase();
            if (!ALLOWED_EXTENSIONS.contains(extension)) {
                throw new IllegalArgumentException("Unsupported file type: " + extension);
            }
        }

        // Generate unique filename to prevent overwrites
        String sanitizedFilename = originalFilename.replaceAll("[^a-zA-Z0-9._-]", "_");
        String timestamp = String.valueOf(System.currentTimeMillis());
        String uniqueFilename = timestamp + "_" + sanitizedFilename;

        // Save file
        Path filePath = uploadPath.resolve(uniqueFilename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Return relative path
        return UPLOAD_DIR + File.separator + uniqueFilename;
    }

    public File getFile(String filename) {
        String basePath = System.getProperty("user.dir");
        Path filePath = Paths.get(basePath, UPLOAD_DIR, filename);
        return filePath.toFile();
    }

    public List<Map<String, Object>> getAllFiles() throws IOException {
        String basePath = System.getProperty("user.dir");
        Path uploadPath = Paths.get(basePath, UPLOAD_DIR);
        
        List<Map<String, Object>> fileList = new ArrayList<>();
        
        if (!Files.exists(uploadPath)) {
            return fileList;
        }

        Files.list(uploadPath)
            .filter(Files::isRegularFile)
            .sorted((p1, p2) -> {
                try {
                    return Files.getLastModifiedTime(p2).compareTo(Files.getLastModifiedTime(p1));
                } catch (IOException e) {
                    return 0;
                }
            })
            .forEach(path -> {
                try {
                    File file = path.toFile();
                    Map<String, Object> fileInfo = new HashMap<>();
                    
                    fileInfo.put("fileName", file.getName());
                    fileInfo.put("fileSize", file.length());
                    fileInfo.put("lastModified", file.lastModified());
                    fileInfo.put("formattedDate", new Date(file.lastModified()).toString());
                    
                    // Get file extension and type
                    String name = file.getName();
                    int dotIndex = name.lastIndexOf('.');
                    if (dotIndex > 0) {
                        String extension = name.substring(dotIndex + 1).toLowerCase();
                        fileInfo.put("fileExtension", extension);
                        fileInfo.put("fileType", getFileType(extension));
                    }
                    
                    // Add view and download URLs
                    fileInfo.put("viewUrl", "/api/file/view/" + file.getName());
                    fileInfo.put("downloadUrl", "/api/file/download/" + file.getName());
                    
                    fileList.add(fileInfo);
                } catch (Exception e) {
                    // Skip problematic files
                }
            });

        return fileList;
    }

    public boolean deleteFile(String filename) throws IOException {
        File file = getFile(filename);
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }

    private String getFileType(String extension) {
        switch (extension.toLowerCase()) {
            case "pdf":
                return "PDF Document";
            case "doc":
            case "docx":
                return "Word Document";
            case "xls":
            case "xlsx":
                return "Excel Spreadsheet";
            case "jpg":
            case "jpeg":
            case "png":
                return "Image";
            case "txt":
                return "Text File";
            case "ppt":
            case "pptx":
                return "PowerPoint Presentation";
            default:
                return "File";
        }
    }
}