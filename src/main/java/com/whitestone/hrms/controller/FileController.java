package com.whitestone.hrms.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.whitestone.hrms.service.FileService;
import com.whitestone.hrms.service.FileUploadUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/file")
@CrossOrigin(origins = "*")
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);
    private final String UPLOAD_DIR = "uploads";
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
        // Ensure upload directory exists
        ensureUploadDirectoryExists();
    }

    private void ensureUploadDirectoryExists() {
        try {
            String basePath = System.getProperty("user.dir");
            File uploadDir = new File(basePath + File.separator + UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
                logger.info("Created upload directory: {}", uploadDir.getAbsolutePath());
            }
        } catch (Exception e) {
            logger.error("Failed to create upload directory: {}", e.getMessage());
        }
    }

    // ---- Upload endpoint ----
    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> upload(@RequestParam("file") MultipartFile file) {
        logger.info("Upload request received - File: {}, Size: {}", 
            file.getOriginalFilename(), file.getSize());
        
        Map<String, String> response = new HashMap<>();
        
        try {
            if (file.isEmpty()) {
                response.put("error", "File is empty");
                return ResponseEntity.badRequest().body(response);
            }

            if (file.getSize() > 10 * 1024 * 1024) { // 10MB limit
                response.put("error", "File size exceeds 10MB limit");
                return ResponseEntity.badRequest().body(response);
            }

            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || originalFilename.isEmpty()) {
                response.put("error", "Invalid file name");
                return ResponseEntity.badRequest().body(response);
            }

            // Check file extension
            String extension = originalFilename.substring(originalFilename.lastIndexOf('.') + 1).toLowerCase();
            if (!Arrays.asList("pdf", "doc", "docx", "txt", "xls", "xlsx", "jpg", "jpeg", "png").contains(extension)) {
                response.put("error", "Unsupported file type. Allowed: PDF, DOC, DOCX, TXT, Excel, Images");
                return ResponseEntity.badRequest().body(response);
            }

            // Save file using service
            String savedFilePath = fileService.saveFile(file);
            
            response.put("message", "File uploaded successfully");
            response.put("filePath", savedFilePath);
            response.put("fileName", new File(savedFilePath).getName());
            
            logger.info("File uploaded successfully: {}", savedFilePath);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Upload failed: {}", e.getMessage(), e);
            response.put("error", "Upload failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // ---- List all files ----
    @GetMapping("/list")
    public ResponseEntity<List<Map<String, Object>>> listFiles() {
        logger.info("Listing all files");
        
        try {
            List<Map<String, Object>> files = fileService.getAllFiles();
            return ResponseEntity.ok(files);
        } catch (Exception e) {
            logger.error("Failed to list files: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
        }
    }

    // ---- View/Download file ----
    @GetMapping("/view/{filename:.+}")
    public ResponseEntity<byte[]> viewFile(@PathVariable String filename) {
        logger.info("View request for file: {}", filename);
        
        try {
            File file = fileService.getFile(filename);
            
            if (!file.exists()) {
                logger.warn("File not found: {}", filename);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(("File not found: " + filename).getBytes());
            }

            byte[] fileContent = Files.readAllBytes(file.toPath());
            String mimeType = Files.probeContentType(file.toPath());
            
            HttpHeaders headers = new HttpHeaders();
            
            if (filename.toLowerCase().endsWith(".pdf")) {
                headers.setContentType(MediaType.APPLICATION_PDF);
                headers.setContentDispositionFormData("inline", filename);
            } else {
                headers.setContentType(MediaType.parseMediaType(mimeType != null ? mimeType : "application/octet-stream"));
                headers.setContentDispositionFormData("attachment", filename);
            }
            
            headers.setContentLength(fileContent.length);
            headers.setCacheControl("no-cache, no-store, must-revalidate");
            headers.setPragma("no-cache");
            headers.setExpires(0);
            
            logger.info("Serving file: {} (Size: {} bytes)", filename, fileContent.length);
            return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
            
        } catch (Exception e) {
            logger.error("Failed to serve file {}: {}", filename, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Error serving file: " + e.getMessage()).getBytes());
        }
    }

    // ---- Download file ----
    @GetMapping("/download/{filename:.+}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String filename) {
        logger.info("Download request for file: {}", filename);
        
        try {
            File file = fileService.getFile(filename);
            
            if (!file.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(("File not found: " + filename).getBytes());
            }

            byte[] fileContent = Files.readAllBytes(file.toPath());
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", filename);
            headers.setContentLength(fileContent.length);
            
            logger.info("Downloading file: {} (Size: {} bytes)", filename, fileContent.length);
            return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
            
        } catch (Exception e) {
            logger.error("Failed to download file {}: {}", filename, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Error downloading file: " + e.getMessage()).getBytes());
        }
    }

    // ---- Delete file ----
    @DeleteMapping("/delete/{filename:.+}")
    public ResponseEntity<Map<String, String>> deleteFile(@PathVariable String filename) {
        logger.info("Delete request for file: {}", filename);
        
        Map<String, String> response = new HashMap<>();
        
        try {
            boolean deleted = fileService.deleteFile(filename);
            
            if (deleted) {
                response.put("message", "File deleted successfully");
                response.put("fileName", filename);
                logger.info("File deleted: {}", filename);
                return ResponseEntity.ok(response);
            } else {
                response.put("error", "File not found");
                logger.warn("File not found for deletion: {}", filename);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            
        } catch (Exception e) {
            logger.error("Failed to delete file {}: {}", filename, e.getMessage(), e);
            response.put("error", "Failed to delete file: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // ---- Edit/Update file ----
    @PutMapping("/edit/{oldFilename:.+}")
    public ResponseEntity<Map<String, String>> editFile(
            @PathVariable String oldFilename,
            @RequestParam("file") MultipartFile newFile) {
        
        logger.info("Edit request - Old file: {}, New file: {}", 
            oldFilename, newFile.getOriginalFilename());
        
        Map<String, String> response = new HashMap<>();
        
        try {
            if (newFile.isEmpty()) {
                response.put("error", "New file is empty");
                return ResponseEntity.badRequest().body(response);
            }

            // First delete the old file
            boolean oldFileDeleted = fileService.deleteFile(oldFilename);
            if (!oldFileDeleted) {
                logger.warn("Old file not found for editing: {}", oldFilename);
            }

            // Save the new file
            String newFilePath = fileService.saveFile(newFile);
            
            response.put("message", "File updated successfully");
            response.put("oldFileName", oldFilename);
            response.put("newFileName", new File(newFilePath).getName());
            response.put("filePath", newFilePath);
            
            logger.info("File updated - Old: {}, New: {}", oldFilename, newFilePath);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Failed to edit file {}: {}", oldFilename, e.getMessage(), e);
            response.put("error", "Failed to update file: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // ---- Get file info ----
    @GetMapping("/info/{filename:.+}")
    public ResponseEntity<Map<String, Object>> getFileInfo(@PathVariable String filename) {
        logger.info("File info request: {}", filename);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            File file = fileService.getFile(filename);
            
            if (!file.exists()) {
                response.put("error", "File not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            response.put("fileName", file.getName());
            response.put("fileSize", file.length());
            response.put("lastModified", file.lastModified());
            response.put("filePath", file.getAbsolutePath());
            response.put("canRead", file.canRead());
            response.put("canWrite", file.canWrite());
            
            // Get file extension and type
            String name = file.getName();
            int dotIndex = name.lastIndexOf('.');
            if (dotIndex > 0) {
                response.put("fileExtension", name.substring(dotIndex + 1).toLowerCase());
            }
            
            logger.info("File info retrieved: {}", filename);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Failed to get file info {}: {}", filename, e.getMessage(), e);
            response.put("error", "Failed to get file info: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}