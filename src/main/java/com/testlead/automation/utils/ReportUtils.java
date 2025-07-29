package com.testlead.automation.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.testlead.automation.config.ConfigManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for test reporting
 */
public class ReportUtils {
    
    private static final Logger logger = LoggerFactory.getLogger(ReportUtils.class);
    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    private static final String REPORTS_PATH = "reports";
    
    /**
     * Initialize ExtentReports
     */
    public static void initializeReport() {
        if (extent == null) {
            createReportsDirectory();
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            String reportPath = REPORTS_PATH + "/TestReport_" + timestamp + ".html";
            
            ExtentSparkReporter htmlReporter = new ExtentSparkReporter(reportPath);
            htmlReporter.config().setDocumentTitle("API Test Automation Report");
            htmlReporter.config().setReportName("API Testing Results");
            htmlReporter.config().setTheme(Theme.STANDARD);
            htmlReporter.config().setEncoding("utf-8");
            
            extent = new ExtentReports();
            extent.attachReporter(htmlReporter);
            
            // System information
            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));
            extent.setSystemInfo("User", System.getProperty("user.name"));
            extent.setSystemInfo("Environment", ConfigManager.get("base.url", "Not specified"));
            
            logger.info("ExtentReports initialized. Report will be generated at: {}", reportPath);
        }
    }
    
    /**
     * Create test in report
     */
    public static void createTest(String testName, String description) {
        ExtentTest extentTest = extent.createTest(testName, description);
        test.set(extentTest);
        logger.debug("Test created in report: {}", testName);
    }
    
    /**
     * Log info message
     */
    public static void logInfo(String message) {
        if (test.get() != null) {
            test.get().log(Status.INFO, message);
        }
        logger.info(message);
    }
    
    /**
     * Log pass message
     */
    public static void logPass(String message) {
        if (test.get() != null) {
            test.get().log(Status.PASS, message);
        }
        logger.info("PASS: {}", message);
    }
    
    /**
     * Log fail message
     */
    public static void logFail(String message) {
        if (test.get() != null) {
            test.get().log(Status.FAIL, message);
        }
        logger.error("FAIL: {}", message);
    }
    
    /**
     * Log fail message with exception
     */
    public static void logFail(String message, Throwable exception) {
        if (test.get() != null) {
            test.get().log(Status.FAIL, message);
            test.get().log(Status.FAIL, exception);
        }
        logger.error("FAIL: {}", message, exception);
    }
    
    /**
     * Log warning message
     */
    public static void logWarning(String message) {
        if (test.get() != null) {
            test.get().log(Status.WARNING, message);
        }
        logger.warn("WARNING: {}", message);
    }
    
    /**
     * Log skip message
     */
    public static void logSkip(String message) {
        if (test.get() != null) {
            test.get().log(Status.SKIP, message);
        }
        logger.info("SKIP: {}", message);
    }
    
    /**
     * Add API request details to report
     */
    public static void logApiRequest(String method, String url, String requestBody, String headers) {
        if (test.get() != null) {
            StringBuilder apiDetails = new StringBuilder();
            apiDetails.append("<details><summary><b>API Request Details</b></summary>");
            apiDetails.append("<p><b>Method:</b> ").append(method).append("</p>");
            apiDetails.append("<p><b>URL:</b> ").append(url).append("</p>");
            
            if (headers != null && !headers.isEmpty()) {
                apiDetails.append("<p><b>Headers:</b></p>");
                apiDetails.append("<pre>").append(headers).append("</pre>");
            }
            
            if (requestBody != null && !requestBody.isEmpty()) {
                apiDetails.append("<p><b>Request Body:</b></p>");
                apiDetails.append("<pre>").append(requestBody).append("</pre>");
            }
            
            apiDetails.append("</details>");
            test.get().log(Status.INFO, apiDetails.toString());
        }
    }
    
    /**
     * Add API response details to report
     */
    public static void logApiResponse(int statusCode, String responseBody, long responseTime, String headers) {
        if (test.get() != null) {
            StringBuilder responseDetails = new StringBuilder();
            responseDetails.append("<details><summary><b>API Response Details</b></summary>");
            responseDetails.append("<p><b>Status Code:</b> ").append(statusCode).append("</p>");
            responseDetails.append("<p><b>Response Time:</b> ").append(responseTime).append(" ms</p>");
            
            if (headers != null && !headers.isEmpty()) {
                responseDetails.append("<p><b>Response Headers:</b></p>");
                responseDetails.append("<pre>").append(headers).append("</pre>");
            }
            
            if (responseBody != null && !responseBody.isEmpty()) {
                responseDetails.append("<p><b>Response Body:</b></p>");
                responseDetails.append("<pre>").append(responseBody).append("</pre>");
            }
            
            responseDetails.append("</details>");
            
            Status status = statusCode >= 200 && statusCode < 300 ? Status.PASS : Status.FAIL;
            test.get().log(status, responseDetails.toString());
        }
    }
    
    /**
     * Add screenshot to report
     */
    public static void addScreenshot(String screenshotPath) {
        if (test.get() != null) {
            try {
                test.get().addScreenCaptureFromPath(screenshotPath);
            } catch (Exception e) {
                logger.error("Failed to add screenshot to report", e);
            }
        }
    }
    
    /**
     * Assign category to test
     */
    public static void assignCategory(String category) {
        if (test.get() != null) {
            test.get().assignCategory(category);
        }
    }
    
    /**
     * Assign author to test
     */
    public static void assignAuthor(String author) {
        if (test.get() != null) {
            test.get().assignAuthor(author);
        }
    }
    
    /**
     * Flush and generate report
     */
    public static void flushReport() {
        if (extent != null) {
            extent.flush();
            logger.info("ExtentReports flushed and report generated");
        }
    }
    
    /**
     * Get current test instance
     */
    public static ExtentTest getCurrentTest() {
        return test.get();
    }
    
    /**
     * Remove current test from thread local
     */
    public static void removeTest() {
        test.remove();
    }
    
    /**
     * Create reports directory if it doesn't exist
     */
    private static void createReportsDirectory() {
        File reportsDir = new File(REPORTS_PATH);
        if (!reportsDir.exists()) {
            boolean created = reportsDir.mkdirs();
            if (created) {
                logger.info("Reports directory created: {}", REPORTS_PATH);
            } else {
                logger.warn("Failed to create reports directory: {}", REPORTS_PATH);
            }
        }
    }
    
    /**
     * Get reports directory path
     */
    public static String getReportsPath() {
        return REPORTS_PATH;
    }
}