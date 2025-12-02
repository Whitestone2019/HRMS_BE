//package com.whitestone.hrms.controller;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Base64;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//import java.util.concurrent.CompletableFuture;
//import java.util.concurrent.TimeUnit;
//import java.util.concurrent.TimeoutException;
//
//import javax.annotation.PostConstruct;
//import javax.crypto.SecretKey;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.mantra.morfinauth.DeviceInfo;
//import com.mantra.morfinauth.MorfinAuth;
//import com.mantra.morfinauth.MorfinAuth_Callback;
//import com.mantra.morfinauth.enums.DeviceDetection;
//import com.mantra.morfinauth.enums.DeviceModel;
//import com.mantra.morfinauth.enums.FingerPostion;
//import com.mantra.morfinauth.enums.ImageFormat;
//import com.mantra.morfinauth.enums.LogLevel;
//import com.mantra.morfinauth.enums.TemplateFormat;
//import com.whitestone.entity.Fingerprint;
//import com.whitestone.entity.TraineeMaster;
//import com.whitestone.entity.UserRoleMaintenance;
//import com.whitestone.entity.usermaintenance;
//import com.whitestone.hrms.repo.FingerprintRepository;
//import com.whitestone.hrms.repo.TraineemasterRepository;
//import com.whitestone.hrms.repo.UserRoleMaintenanceRepository;
//import com.whitestone.hrms.repo.usermaintenanceRepository;
//import com.whitestone.hrms.service.ErrorMessageService;
//
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.security.Keys;
//
//@RestController
//@RequestMapping("/api/fingerprint")
//@CrossOrigin(origins = "*")
//public class FingerprintController implements MorfinAuth_Callback {
//	
//	
//	@Autowired
//	private usermaintenanceRepository usermaintenanceRepository;
//
//	@Autowired
//	private TraineemasterRepository traineemasterRepository;
//	
//	@Autowired
//	private UserRoleMaintenanceRepository userRoleMaintenanceRepository;
//	
//	@Autowired
//	private ErrorMessageService errorMessageService;
//
//    private MorfinAuth morfinAuth;
//    private DeviceInfo deviceInfo;
//    private byte[] savedTemplate;
//    private volatile CompletableFuture<CaptureResult> captureFuture;
//    private boolean sdkInitialized = false;
//    private String initializationError = null;
//    private boolean deviceConnected = false;
//    private long lastCaptureTime = 0;
//    private static final int MATCH_THRESHOLD = 480;
//    
//    // Store last captured image
//    private byte[] lastCapturedImage;
//
//    private static class CaptureResult {
//        int errorCode, quality, nfiq;
//        byte[] template, image;
//        CaptureResult(int e, int q, int n, byte[] t, byte[] img) {
//            this.errorCode = e; this.quality = q; this.nfiq = n; this.template = t; this.image = img;
//        }
//    }
//
//    @PostConstruct
//    public void initialize() {
//        try {
//            String root = "D:\\BE_Workspace\\HRMS.zip_expanded\\hrms";
//            System.out.println("Project Root = " + root);
//            
//            // Check if directory exists
//            java.io.File rootDir = new java.io.File(root);
//            if (!rootDir.exists()) {
//                initializationError = "Directory does not exist: " + root;
//                System.err.println(initializationError);
//                return;
//            }
//            
//            // CORRECTED DLL names
//            String[] dlls = {
//                "MARC31_TBB_DLL.dll",
//                "Morfin_Auth_Core.dll", 
//                "iengine_ansi_iso.dll",
//                "Morfin_Auth.dll"
//            };
//            
//            // Load each DLL with verification
//            for (String dll : dlls) {
//                String dllPath = root + "\\" + dll;
//                java.io.File dllFile = new java.io.File(dllPath);
//                
//                if (!dllFile.exists()) {
//                    initializationError = "DLL file not found: " + dllPath;
//                    System.err.println(initializationError);
//                    return;
//                }
//                
//                try {
//                    System.load(dllPath);
//                    System.out.println("✓ Loaded: " + dll);
//                } catch (UnsatisfiedLinkError e) {
//                    initializationError = "Failed to load DLL: " + dll + " - " + e.getMessage();
//                    System.err.println(initializationError);
//                    return;
//                }
//            }
//            
//            System.out.println("ALL DLLs LOADED SUCCESSFULLY!");
//            
//            // Initialize MorfinAuth
//            morfinAuth = new MorfinAuth(this);
//            morfinAuth.SetLogProperties(root, LogLevel.ERROR);
//            
//            System.out.println("MorfinAuth initialized");
//            System.out.println("SDK Version: " + morfinAuth.GetSDKVersion());
//            
//            // Check supported devices
//            List<String> supportedDevices = new ArrayList<>();
//            morfinAuth.GetSupportedDevices(supportedDevices);
//            System.out.println("Supported devices: " + supportedDevices);
//            
//            // Force device detection
//            checkDeviceConnection();
//            
//        } catch (Exception e) {
//            initializationError = "Initialization failed: " + e.getMessage();
//            System.err.println(initializationError);
//            e.printStackTrace();
//        }
//    }
//    
//    private void checkDeviceConnection() {
//        try {
//            List<String> connectedDevices = new ArrayList<>();
//            int deviceCheck = morfinAuth.GetConnectedDevices(connectedDevices);
//            System.out.println("Device check - Result: " + deviceCheck + ", Devices: " + connectedDevices);
//            
//            if (deviceCheck == 0 && !connectedDevices.isEmpty()) {
//                System.out.println("Found connected device: " + connectedDevices.get(0));
//                initializeDevice(connectedDevices.get(0));
//            } else {
//                System.out.println("No devices found or error: " + morfinAuth.GetErrorMessage(deviceCheck));
//            }
//        } catch (Exception e) {
//            System.err.println("Error checking device connection: " + e.getMessage());
//        }
//    }
//    
//    @Override 
//    public void OnDeviceDetection(String deviceName, DeviceDetection detection) {
//        System.out.println("OnDeviceDetection: " + deviceName + " - " + detection);
//        
//        if (detection == DeviceDetection.CONNECTED) {
//            deviceConnected = true;
//            System.out.println("Device connected: " + deviceName);
//            initializeDevice(deviceName);
//        } else {
//            deviceConnected = false;
//            sdkInitialized = false;
//            System.out.println("Device disconnected: " + deviceName);
//        }
//    }
//    
//    private void initializeDevice(String deviceName) {
//        try {
//            System.out.println("Initializing device: " + deviceName);
//            Thread.sleep(1000); // Wait for device to be ready
//            
//            deviceInfo = new DeviceInfo();
//            int ret = -1;
//            
//            // First try without client key (for most devices)
//            System.out.println("Trying initialization WITHOUT client key...");
//            
//            if (deviceName.contains("MFS500")) {
//                ret = morfinAuth.Init(DeviceModel.MFS500, null, deviceInfo);
//                System.out.println("MFS500 init without key: " + ret + " - " + morfinAuth.GetErrorMessage(ret));
//                
//                // If failed, try with empty client key
//                if (ret != 0) {
//                    System.out.println("Trying MFS500 with empty client key...");
//                    ret = morfinAuth.Init(DeviceModel.MFS500, new byte[0], deviceInfo);
//                    System.out.println("MFS500 init with empty key: " + ret + " - " + morfinAuth.GetErrorMessage(ret));
//                }
//            } 
//            else if (deviceName.contains("MELO31")) {
//                ret = morfinAuth.Init(DeviceModel.MELO31, null, deviceInfo);
//                System.out.println("MELO31 init: " + ret + " - " + morfinAuth.GetErrorMessage(ret));
//            } 
//            else if (deviceName.contains("MARC10")) {
//                ret = morfinAuth.Init(DeviceModel.MARC10, null, deviceInfo);
//                System.out.println("MARC10 init: " + ret + " - " + morfinAuth.GetErrorMessage(ret));
//            } 
//            else {
//                // Auto-detect by trying all models without client key first
//                System.out.println("Auto-detecting device model...");
//                for (DeviceModel model : DeviceModel.values()) {
//                    System.out.println("Trying model without key: " + model);
//                    ret = morfinAuth.Init(model, null, deviceInfo);
//                    System.out.println(model + " init without key: " + ret + " - " + morfinAuth.GetErrorMessage(ret));
//                    if (ret == 0) break;
//                    
//                    // If MFS500 failed without key, try with empty key
//                    if (model == DeviceModel.MFS500 && ret != 0) {
//                        System.out.println("Trying MFS500 with empty key...");
//                        ret = morfinAuth.Init(DeviceModel.MFS500, new byte[0], deviceInfo);
//                        System.out.println("MFS500 with empty key: " + ret + " - " + morfinAuth.GetErrorMessage(ret));
//                        if (ret == 0) break;
//                    }
//                }
//            }
//
//            if (ret == 0) {
//                sdkInitialized = true;
//                initializationError = null;
//                System.out.println("✓ DEVICE INITIALIZED SUCCESSFULLY!");
//                System.out.println("  Model: " + deviceInfo.Model);
//                System.out.println("  Serial: " + deviceInfo.SerialNo);
//                System.out.println("  Make: " + deviceInfo.Make);
//                System.out.println("  Firmware: " + deviceInfo.Firmware);
//                System.out.println("  Resolution: " + deviceInfo.Width + "x" + deviceInfo.Height + " @ " + deviceInfo.DPI + " DPI");
//                
//                // Test device communication
//                testDeviceCommunication();
//            } else {
//                sdkInitialized = false;
//                initializationError = "Init failed: " + morfinAuth.GetErrorMessage(ret) + " (Code: " + ret + ")";
//                System.err.println("✗ " + initializationError);
//                
//                // Try alternative initialization approach
//                tryAlternativeInitialization();
//            }
//        } catch (Exception e) {
//            sdkInitialized = false;
//            initializationError = "Device init error: " + e.getMessage();
//            System.err.println("✗ " + initializationError);
//        }
//    }
//    
//    private void tryAlternativeInitialization() {
//        try {
//            System.out.println("Trying alternative initialization approach...");
//            
//            // Try with different device models and null client key
//            for (DeviceModel model : DeviceModel.values()) {
//                System.out.println("Alternative try: " + model);
//                int ret = morfinAuth.Init(model, null, deviceInfo);
//                if (ret == 0) {
//                    sdkInitialized = true;
//                    initializationError = null;
//                    System.out.println("✓ ALTERNATIVE INIT SUCCESS with " + model);
//                    break;
//                }
//                System.out.println("Alternative failed: " + ret + " - " + morfinAuth.GetErrorMessage(ret));
//            }
//        } catch (Exception e) {
//            System.err.println("Alternative initialization failed: " + e.getMessage());
//        }
//    }
//    
//    private void testDeviceCommunication() {
//        try {
//            System.out.println("Testing device communication...");
//            
//            // Check if device is responsive
//            boolean isConnected = morfinAuth.IsDeviceConnected(DeviceModel.MFS500);
//            System.out.println("Device responsive: " + isConnected);
//            
//            if (isConnected) {
//                System.out.println("✓ Device communication test PASSED");
//            } else {
//                System.err.println("✗ Device communication test FAILED");
//            }
//        } catch (Exception e) {
//            System.err.println("Device communication test error: " + e.getMessage());
//        }
//    }
//    
//    private ResponseEntity<Map<String, Object>> createErrorResponse(String message) {
//        Map<String, Object> response = new HashMap<>();
//        response.put("success", false);
//        response.put("message", message);
//        response.put("sdkInitialized", sdkInitialized);
//        response.put("deviceConnected", deviceConnected);
//        if (initializationError != null) {
//            response.put("initializationError", initializationError);
//        }
//        return ResponseEntity.ok(response);
//    }
//    
//    private boolean checkSDKInitialized() {
//        if (!sdkInitialized) {
//            System.err.println("SDK not initialized. Device connected: " + deviceConnected);
//            if (initializationError != null) {
//                System.err.println("Initialization error: " + initializationError);
//            }
//            return false;
//        }
//        return true;
//    }
//
//    @PostMapping("/capture-sync")
//    public ResponseEntity<Map<String, Object>> captureSync(@RequestBody Map<String, Integer> req) {
//        Map<String, Object> response = new HashMap<>();
//        
//        if (!checkSDKInitialized()) {
//            return createErrorResponse("Fingerprint device not ready. " + 
//                (deviceConnected ? "Device detected but initialization failed." : "No device connected."));
//        }
//        
//        int quality = req.getOrDefault("quality", 60);
//        System.out.println("Starting synchronous capture with quality: " + quality);
//
//        try {
//            int[] qualityArray = new int[1], nfiq = new int[1];
//            
//            // Use AutoCapture (synchronous) as per SDK documentation
//            System.out.println("Calling AutoCapture...");
//            int ret = morfinAuth.AutoCapture(quality, 15000, qualityArray, nfiq);
//            
//            System.out.println("AutoCapture returned: " + ret + " - " + (ret == 0 ? "SUCCESS" : morfinAuth.GetErrorMessage(ret)));
//            
//            if (ret == 0) {
//                // AutoCapture succeeded immediately - get template and image
//                int[] templateLen = new int[]{3000};
//                byte[] template = new byte[templateLen[0]];
//                int templateRet = morfinAuth.GetTemplate(template, templateLen, TemplateFormat.FMR_V2011);
//                
//                // Get image
//                int[] imageLen = new int[]{(deviceInfo.Width * deviceInfo.Height + 5000)};
//                byte[] imageData = new byte[imageLen[0]];
//                int imageRet = morfinAuth.GetImage(imageData, imageLen, 0, ImageFormat.BMP);
//                
//                if (templateRet == 0 && imageRet == 0) {
//                    savedTemplate = Arrays.copyOf(template, templateLen[0]);
//                    lastCapturedImage = Arrays.copyOf(imageData, imageLen[0]);
//                    
//                    String base64Image = Base64.getEncoder().encodeToString(lastCapturedImage);
//                    
//                    response.put("success", true);
//                    response.put("quality", qualityArray[0]);
//                    response.put("nfiq", nfiq[0]);
//                    response.put("imageBase64", base64Image);
//                    response.put("templateBase64", Base64.getEncoder().encodeToString(savedTemplate));
//                    response.put("templateSize", savedTemplate.length);
//                    response.put("message", "Fingerprint captured successfully!");
//                    
//                    System.out.println("Capture successful - Quality: " + qualityArray[0] + ", NFIQ: " + nfiq[0]);
//                } else {
//                    response.put("success", false);
//                    response.put("message", "Failed to get template/image: " + 
//                        morfinAuth.GetErrorMessage(templateRet != 0 ? templateRet : imageRet));
//                }
//            } else {
//                response.put("success", false);
//                response.put("message", "Capture failed: " + morfinAuth.GetErrorMessage(ret));
//            }
//            
//        } catch (Exception e) {
//            System.err.println("Capture error: " + e.getMessage());
//            response.put("success", false);
//            response.put("message", "Capture error: " + e.getMessage());
//        }
//        return ResponseEntity.ok(response);
//    }
//
//    @PostMapping("/capture-async")
//    public ResponseEntity<Map<String, Object>> captureAsync(@RequestBody Map<String, Integer> req) {
//        Map<String, Object> response = new HashMap<>();
//        
//        if (!checkSDKInitialized()) {
//            return createErrorResponse("Fingerprint device not ready.");
//        }
//        
//        if (captureFuture != null && !captureFuture.isDone()) {
//            return createErrorResponse("Another capture operation is in progress. Please wait.");
//        }
//        
//        int quality = req.getOrDefault("quality", 60);
//        System.out.println("Starting asynchronous capture with quality: " + quality);
//
//        try {
//            captureFuture = new CompletableFuture<>();
//            
//            // Use StartCapture (asynchronous)
//            System.out.println("Calling StartCapture...");
//            int ret = morfinAuth.StartCapture(quality, 15000);
//            
//            System.out.println("StartCapture returned: " + ret + " - " + (ret == 0 ? "SUCCESS" : morfinAuth.GetErrorMessage(ret)));
//            
//            if (ret == 0) {
//                // Wait for completion via callback
//                CaptureResult result = captureFuture.get(20, TimeUnit.SECONDS);
//                
//                if (result.errorCode != 0) {
//                    return createErrorResponse("Capture failed: " + morfinAuth.GetErrorMessage(result.errorCode));
//                }
//                
//                if (result.template != null) {
//                    savedTemplate = result.template;
//                }
//                
//                String base64Image = null;
//                if (result.image != null) {
//                    lastCapturedImage = result.image;
//                    base64Image = Base64.getEncoder().encodeToString(lastCapturedImage);
//                }
//                
//                response.put("success", true);
//                response.put("quality", result.quality);
//                response.put("nfiq", result.nfiq);
//                if (base64Image != null) {
//                    response.put("imageBase64", base64Image);
//                }
//                response.put("templateBase64", savedTemplate != null ? Base64.getEncoder().encodeToString(savedTemplate) : null);
//                response.put("templateSize", savedTemplate != null ? savedTemplate.length : 0);
//                response.put("message", "Fingerprint captured successfully!");
//                
//                System.out.println("Async capture successful - Quality: " + result.quality + ", NFIQ: " + result.nfiq);
//            } else {
//                response.put("success", false);
//                response.put("message", "Capture start failed: " + morfinAuth.GetErrorMessage(ret));
//            }
//            
//        } catch (TimeoutException e) {
//            response.put("success", false);
//            response.put("message", "Capture timeout. Please try again.");
//        } catch (Exception e) {
//            System.err.println("Capture error: " + e.getMessage());
//            response.put("success", false);
//            response.put("message", "Capture error: " + e.getMessage());
//        } finally {
//            captureFuture = null;
//        }
//        return ResponseEntity.ok(response);
//    }
//
//    @PostMapping("/verify")
//    public ResponseEntity<Map<String, Object>> verify() {
//        Map<String, Object> response = new HashMap<>();
//        
//        if (!checkSDKInitialized()) {
//            return createErrorResponse("Fingerprint device not available.");
//        }
//        
//        if (savedTemplate == null) {
//            response.put("success", false);
//            response.put("message", "No fingerprint template saved. Please capture a fingerprint first.");
//            return ResponseEntity.ok(response);
//        }
//        
//        try {
//            int[] qualityArray = new int[1], nfiq = new int[1];
//            
//            System.out.println("Starting verification capture...");
//            int ret = morfinAuth.AutoCapture(60, 15000, qualityArray, nfiq);
//            
//            if (ret != 0) { 
//                return createErrorResponse("Verification capture failed: " + morfinAuth.GetErrorMessage(ret));
//            }
//
//            // Get verification template
//            int[] templateLen = new int[]{3000};
//            byte[] verifyTemplate = new byte[templateLen[0]];
//            int templateRet = morfinAuth.GetTemplate(verifyTemplate, templateLen, TemplateFormat.FMR_V2011);
//            
//            if (templateRet != 0) {
//                return createErrorResponse("Failed to get verification template: " + morfinAuth.GetErrorMessage(templateRet));
//            }
//
//            // Match templates
//            int[] score = new int[1];
//            int matchRet = morfinAuth.MatchTemplate(
//                Arrays.copyOf(verifyTemplate, templateLen[0]), 
//                savedTemplate, 
//                score, 
//                TemplateFormat.FMR_V2011
//            );
//            
//            if (matchRet != 0) {
//                return createErrorResponse("Template matching failed: " + morfinAuth.GetErrorMessage(matchRet));
//            }
//
//            // Get verification image
//            int[] imageLen = new int[]{(deviceInfo.Width * deviceInfo.Height + 5000)};
//            byte[] imageData = new byte[imageLen[0]];
//            int imageRet = morfinAuth.GetImage(imageData, imageLen, 0, ImageFormat.BMP);
//            
//            String base64Image = null;
//            if (imageRet == 0) {
//                base64Image = Base64.getEncoder().encodeToString(Arrays.copyOf(imageData, imageLen[0]));
//            }
//
//            // FIXED: Use proper threshold for Mantra SDK
//            boolean matched = score[0] >= MATCH_THRESHOLD;
//            
//            response.put("success", true);
//            response.put("matched", matched);
//            response.put("score", score[0]);
//            response.put("threshold", MATCH_THRESHOLD);
//            if (base64Image != null) {
//                response.put("imageBase64", base64Image);
//            }
//            response.put("message", matched ? 
//                "Fingerprint verified successfully! Score: " + score[0] + "/1000" : 
//                "Fingerprint verification failed. Score: " + score[0] + "/1000 (needs " + MATCH_THRESHOLD + "+)");
//            
//            System.out.println("Verification result - Score: " + score[0] + "/1000, Matched: " + matched + ", Threshold: " + MATCH_THRESHOLD);
//            
//        } catch (Exception e) {
//            response.put("success", false);
//            response.put("message", "Verification error: " + e.getMessage());
//        }
//        return ResponseEntity.ok(response);
//    }
//    
//    @GetMapping("/status")
//    public ResponseEntity<Map<String, Object>> getStatus() {
//        Map<String, Object> response = new HashMap<>();
//        response.put("sdkInitialized", sdkInitialized);
//        response.put("deviceConnected", deviceConnected);
//        response.put("templateSaved", savedTemplate != null);
//        
//        if (deviceInfo != null && sdkInitialized) {
//            response.put("deviceModel", deviceInfo.Model);
//            response.put("serialNumber", deviceInfo.SerialNo);
//            response.put("deviceMake", deviceInfo.Make);
//            response.put("firmware", deviceInfo.Firmware);
//        } else {
//            response.put("deviceModel", null);
//            response.put("serialNumber", null);
//            response.put("deviceMake", null);
//            response.put("firmware", null);
//        }
//        
//        if (initializationError != null) {
//            response.put("initializationError", initializationError);
//        }
//        
//        // Test current device status
//        if (morfinAuth != null) {
//            try {
//                List<String> connectedDevices = new ArrayList<>();
//                int deviceCheck = morfinAuth.GetConnectedDevices(connectedDevices);
//                response.put("deviceCheckResult", deviceCheck);
//                response.put("connectedDevices", connectedDevices);
//                response.put("deviceCheckMessage", deviceCheck == 0 ? "Success" : morfinAuth.GetErrorMessage(deviceCheck));
//                
//                if (sdkInitialized && deviceInfo != null) {
//                    boolean isConnected = morfinAuth.IsDeviceConnected(DeviceModel.valueOf(deviceInfo.Model));
//                    response.put("deviceStatus", isConnected ? "CONNECTED" : "DISCONNECTED");
//                }
//            } catch (Exception e) {
//                response.put("deviceStatus", "UNKNOWN");
//                response.put("deviceCheckError", e.getMessage());
//            }
//        }
//        
//        response.put("success", true);
//        return ResponseEntity.ok(response);
//    }
//    
//    @PostMapping("/reinitialize")
//    public ResponseEntity<Map<String, Object>> reinitialize() {
//        Map<String, Object> response = new HashMap<>();
//        
//        try {
//            sdkInitialized = false;
//            deviceConnected = false;
//            initializationError = null;
//            
//            // Re-check device connection
//            checkDeviceConnection();
//            
//            response.put("success", sdkInitialized);
//            response.put("message", sdkInitialized ? "Device reinitialized successfully" : "Reinitialization failed");
//            response.put("sdkInitialized", sdkInitialized);
//            
//        } catch (Exception e) {
//            response.put("success", false);
//            response.put("message", "Reinitialization error: " + e.getMessage());
//        }
//        
//        return ResponseEntity.ok(response);
//    }
//    
//    @PostMapping("/test")
//    public ResponseEntity<Map<String, Object>> testDevice() {
//        Map<String, Object> response = new HashMap<>();
//        
//        if (!checkSDKInitialized()) {
//            return createErrorResponse("Device not initialized");
//        }
//        
//        try {
//            // Simple test - try to get device info
//            boolean isConnected = morfinAuth.IsDeviceConnected(DeviceModel.MFS500);
//            String sdkVersion = morfinAuth.GetSDKVersion();
//            
//            response.put("success", true);
//            response.put("deviceConnected", isConnected);
//            response.put("sdkVersion", sdkVersion);
//            response.put("message", "Device test successful - " + (isConnected ? "Connected" : "Disconnected"));
//            
//        } catch (Exception e) {
//            response.put("success", false);
//            response.put("message", "Device test failed: " + e.getMessage());
//        }
//        
//        return ResponseEntity.ok(response);
//    }
//    
//    @PostMapping("/clear-template")
//    public ResponseEntity<Map<String, Object>> clearTemplate() {
//        savedTemplate = null;
//        lastCapturedImage = null;
//        Map<String, Object> response = new HashMap<>();
//        response.put("success", true);
//        response.put("message", "Saved template cleared successfully");
//        return ResponseEntity.ok(response);
//    }
//
//    // CORRECTED CALLBACK METHODS
//    @Override 
//    public void OnPreview(int errorCode, int quality, byte[] image) {
//        System.out.println("OnPreview - Error: " + errorCode + ", Quality: " + quality);
//        // This is called for AutoCapture when preview is available
//    }
//    
//    @Override 
//    public void OnComplete(int errorCode, int quality, int nfiq) {
//        System.out.println("OnComplete - Error: " + errorCode + ", Quality: " + quality + ", NFIQ: " + nfiq);
//        // This is called for StartCapture when complete
//        if (captureFuture != null && !captureFuture.isDone()) {
//            if (errorCode != 0) {
//                captureFuture.complete(new CaptureResult(errorCode, 0, 0, null, null));
//                return;
//            }
//            
//            try {
//                int[] templateLen = new int[]{3000};
//                byte[] template = new byte[templateLen[0]];
//                int ret = morfinAuth.GetTemplate(template, templateLen, TemplateFormat.FMR_V2011);
//                
//                int[] imageLen = new int[]{(deviceInfo.Width * deviceInfo.Height + 5000)};
//                byte[] imageData = new byte[imageLen[0]];
//                int imageRet = morfinAuth.GetImage(imageData, imageLen, 0, ImageFormat.BMP);
//                
//                captureFuture.complete(new CaptureResult(ret, quality, nfiq, 
//                    ret == 0 ? Arrays.copyOf(template, templateLen[0]) : null,
//                    imageRet == 0 ? Arrays.copyOf(imageData, imageLen[0]) : null));
//            } catch (Exception e) {
//                captureFuture.completeExceptionally(e);
//            }
//        }
//    }
//    
//    @Override 
//    public void OnFingerPostionDetection(int errorCode, FingerPostion fingerPosition) {
//        System.out.println("OnFingerPostionDetection: " + errorCode + " - " + fingerPosition);
//    }
//    
// // Add these methods to your existing FingerprintController
//
//    @Autowired
//    private FingerprintRepository fingerprintRepository;
//
//    @PostMapping("/register")
//    public ResponseEntity<Map<String, Object>> registerFingerprint(@RequestBody Map<String, String> body) {
//        
//        // CORRECT Java way to create HashMap
//        Map<String, Object> response = new HashMap<>();
//
//        String employeeId = body.get("employeeId");
//        String templateBase64 = body.get("template");  // We send "template" from Angular
//
//        if (employeeId == null || employeeId.trim().isEmpty()) {
//            response.put("success", false);
//            response.put("message", "Employee ID is required");
//            return ResponseEntity.badRequest().body(response);
//        }
//
//        if (templateBase64 == null || templateBase64.trim().isEmpty()) {
//            response.put("success", false);
//            response.put("message", "No fingerprint template received");
//            return ResponseEntity.badRequest().body(response);
//        }
//
//        try {
//            // Clean and decode Base64 safely
//            String cleanBase64 = templateBase64.replaceAll("\\s+", ""); // Remove spaces, newlines
//            byte[] templateData = Base64.getDecoder().decode(cleanBase64);
//
//            // Basic validation
//            if (templateData.length < 200) {
//                throw new IllegalArgumentException("Fingerprint template too small");
//            }
//
//            // Save to database
//            Fingerprint fingerprint = fingerprintRepository.findByEmployeeId(employeeId)
//                .orElse(new Fingerprint());
//
//            fingerprint.setEmployeeId(employeeId.trim().toUpperCase());
//            fingerprint.setTemplateData(templateData);
//            fingerprint.setUpdatedAt(LocalDateTime.now());
//
//            fingerprintRepository.save(fingerprint);
//
//            response.put("success", true);
//            response.put("message", "Fingerprint registered successfully for " + employeeId);
//            return ResponseEntity.ok(response);
//
//        } catch (IllegalArgumentException e) {
//            response.put("success", false);
//            response.put("message", "Invalid Base64 data: " + e.getMessage());
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            response.put("success", false);
//            response.put("message", "Server error: " + e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//        }
//    }
//
//    @PostMapping("/verify-login")
//    public ResponseEntity<Map<String, Object>> verifyFingerprintForLogin(@RequestBody Map<String, String> request) {
//        Map<String, Object> response = new HashMap<>();
//        
//        if (!checkSDKInitialized()) {
//            return createErrorResponse("Fingerprint device not ready.");
//        }
//        
//        String employeeId = request.get("employeeId");
//        if (employeeId == null || employeeId.trim().isEmpty()) {
//            return createErrorResponse("Employee ID is required.");
//        }
//        
//        try {
//            // Get stored template
//            Optional<Fingerprint> fingerprintOpt = fingerprintRepository.findByEmployeeId(employeeId);
//            if (!fingerprintOpt.isPresent()) {
//                response.put("success", false);
//                response.put("message", "No fingerprint registered for this user.");
//                response.put("registered", false);
//                return ResponseEntity.ok(response);
//            }
//            
//            byte[] storedTemplate = fingerprintOpt.get().getTemplateData();
//            
//            // Capture new fingerprint for verification
//            int[] qualityArray = new int[1], nfiq = new int[1];
//            System.out.println("Verifying fingerprint for login: " + employeeId);
//            
//            int ret = morfinAuth.AutoCapture(60, 15000, qualityArray, nfiq);
//            
//            if (ret != 0) {
//                return createErrorResponse("Capture failed: " + morfinAuth.GetErrorMessage(ret));
//            }
//            
//            // Get verification template
//            int[] templateLen = new int[]{3000};
//            byte[] verifyTemplate = new byte[templateLen[0]];
//            int templateRet = morfinAuth.GetTemplate(verifyTemplate, templateLen, TemplateFormat.FMR_V2011);
//            
//            if (templateRet != 0) {
//                return createErrorResponse("Failed to get verification template: " + morfinAuth.GetErrorMessage(templateRet));
//            }
//            
//            // Match templates
//            int[] score = new int[1];
//            int matchRet = morfinAuth.MatchTemplate(
//                Arrays.copyOf(verifyTemplate, templateLen[0]), 
//                storedTemplate, 
//                score, 
//                TemplateFormat.FMR_V2011
//            );
//            
//            if (matchRet != 0) {
//                return createErrorResponse("Template matching failed: " + morfinAuth.GetErrorMessage(matchRet));
//            }
//            
//            boolean matched = score[0] >= MATCH_THRESHOLD;
//            
//            response.put("success", true);
//            response.put("matched", matched);
//            response.put("score", score[0]);
//            response.put("threshold", MATCH_THRESHOLD);
//            response.put("registered", true);
//            response.put("message", matched ? 
//                "Fingerprint verified successfully!" : 
//                "Fingerprint verification failed.");
//            
//            System.out.println("Login verification - Employee: " + employeeId + 
//                ", Score: " + score[0] + ", Matched: " + matched);
//            
//        } catch (Exception e) {
//            response.put("success", false);
//            response.put("message", "Verification error: " + e.getMessage());
//        }
//        
//        return ResponseEntity.ok(response);
//    }
//
//    @GetMapping("/check-registration/{employeeId}")
//    public ResponseEntity<Map<String, Object>> checkRegistration(@PathVariable String employeeId) {
//        Map<String, Object> response = new HashMap<>();
//        boolean registered = fingerprintRepository.existsByEmployeeId(employeeId);
//        
//        response.put("success", true);
//        response.put("registered", registered);
//        response.put("employeeId", employeeId);
//        
//        return ResponseEntity.ok(response);
//    }
//
//    @DeleteMapping("/delete/{employeeId}")
//    public ResponseEntity<Map<String, Object>> deleteFingerprint(@PathVariable String employeeId) {
//        Map<String, Object> response = new HashMap<>();
//        
//        try {
//            fingerprintRepository.deleteByEmployeeId(employeeId);
//            response.put("success", true);
//            response.put("message", "Fingerprint deleted successfully for: " + employeeId);
//        } catch (Exception e) {
//            response.put("success", false);
//            response.put("message", "Delete error: " + e.getMessage());
//        }
//        
//        return ResponseEntity.ok(response);
//    }
//    
// // Add this alternative login method for fingerprint
//    @RequestMapping(value = "/login-fingerprint", method = RequestMethod.POST)
//    @ResponseBody
//    public ResponseEntity<?> loginWithFingerprint(@RequestBody Map<String, String> request) {
//        try {
//            String employeeId = request.get("username"); // From Angular: { username: "10018" }
//
//            if (employeeId == null || employeeId.trim().isEmpty()) {
//                Map<String, Object> err = new HashMap<>();
//                err.put("error", "Employee ID is required");
//                return ResponseEntity.badRequest().body(err);
//            }
//
//            // FIX: Create correct payload for verifyFingerprintForLogin
//            Map<String, String> verifyPayload = new HashMap<>();
//            verifyPayload.put("employeeId", employeeId.trim().toUpperCase());
//
//            // Now call verify with correct key
//            ResponseEntity<Map<String, Object>> verifyResp = verifyFingerprintForLogin(verifyPayload);
//            Map<String, Object> verifyResult = verifyResp.getBody();
//
//            if (verifyResult == null || !Boolean.TRUE.equals(verifyResult.get("success"))) {
//                Map<String, Object> err = new HashMap<>();
//                err.put("error", verifyResult != null ? verifyResult.get("message") : "Verification failed");
//                return ResponseEntity.badRequest().body(err);
//            }
//
//            if (!Boolean.TRUE.equals(verifyResult.get("matched"))) {
//                Map<String, Object> err = new HashMap<>();
//                err.put("error", "Fingerprint did not match");
//                err.put("score", verifyResult.get("score"));
//                return ResponseEntity.badRequest().body(err);
//            }
//
//            // SUCCESS — Generate Token & Login
//            Optional<usermaintenance> empOpt = usermaintenanceRepository.findByEmpIdOrUserId(employeeId);
//            Optional<TraineeMaster> traineeOpt = Optional.empty();
//
//            if (!empOpt.isPresent() && employeeId.toUpperCase().startsWith("WS")) {
//                traineeOpt = traineemasterRepository.findByTrngidOrUserId(employeeId.toUpperCase());
//            }
//
//            if (!empOpt.isPresent() && !traineeOpt.isPresent()) {
//                Map<String, Object> err = new HashMap<>();
//                err.put("error", "User not found");
//                return ResponseEntity.badRequest().body(err);
//            }
//
//            Map<String, Object> response = new HashMap<>();
//            String finalEmployeeId, role, name, email, reportTo = null, managerName = null;
//
//            if (empOpt.isPresent()) {
//                usermaintenance emp = empOpt.get();
//                finalEmployeeId = emp.getEmpid();
//                name = emp.getFirstname();
//                email = emp.getEmailid();
//                reportTo = emp.getRepoteTo();
//                role = userRoleMaintenanceRepository.findByRoleid(emp.getRoleid())
//                        .map(UserRoleMaintenance::getRolename).orElse("USER");
//            } else {
//                TraineeMaster t = traineeOpt.get();
//                finalEmployeeId = t.getTrngid();
//                name = t.getFirstname();
//                email = t.getEmailid();
//                reportTo = t.getRepoteTo();
//                role = userRoleMaintenanceRepository.findByRoleid(t.getRoleid())
//                        .map(UserRoleMaintenance::getRolename).orElse("TRAINEE");
//            }
//
//            if (reportTo != null && !reportTo.isEmpty()) {
//                usermaintenance mgr = usermaintenanceRepository.findByEmpid(reportTo);
//                if (mgr != null) {
//                    managerName = mgr.getFirstname() + (mgr.getLastname() != null ? " " + mgr.getLastname() : "");
//                }
//            }
//
//            String token = generateToken(finalEmployeeId, role);
//
//            response.put("message", "Fingerprint login successful!");
//            response.put("token", token);
//            response.put("employeeId", finalEmployeeId);
//            response.put("username", name);
//            response.put("email", email);
//            response.put("role", role);
//            response.put("reportTo", reportTo);
//            response.put("managerName", managerName);
//            response.put("loginMethod", "fingerprint");
//
//            return ResponseEntity.ok(response);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            Map<String, Object> err = new HashMap<>();
//            err.put("error", "Login failed: " + e.getMessage());
//            return ResponseEntity.status(500).body(err);
//        }
//    }
// 	
//	
//	public String generateToken(String employeeId, String role) {
//		// Set expiration date for token (1 hour from now)
//		long expirationTime = 1000 * 60 * 60; // 1 hour in milliseconds
//		Date expirationDate = new Date(System.currentTimeMillis() + expirationTime);
//
//		// Generate a secure signing key for HS256 algorithm (256 bits)
//		SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256); // Secure key for HS256
//
//		// Generate JWT token using the employeeId and role
//		return Jwts.builder().setSubject(employeeId).claim("role", role) // You can add more claims as needed
//				.setIssuedAt(new Date()).setExpiration(expirationDate).signWith(secretKey) // Use the generated secret
//																							// key
//				.compact();
//	}
//
//}