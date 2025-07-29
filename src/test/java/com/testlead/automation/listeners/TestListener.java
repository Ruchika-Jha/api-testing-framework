package com.testlead.automation.listeners;

import io.qameta.allure.AllureLifecycle;
import lombok.extern.slf4j.Slf4j;
import org.testng.*;

/**
 * TestNG Listener for enhanced reporting and logging
 * This class goes in src/test/java
 */
@Slf4j
public class TestListener implements ITestListener, ISuiteListener, IInvokedMethodListener {
    
    private long suiteStartTime;
    private long testStartTime;
    
    // =============== SUITE LEVEL METHODS ===============
    
    @Override
    public void onStart(ISuite suite) {
        suiteStartTime = System.currentTimeMillis();
        log.info("===============================================");
        log.info("SUITE STARTED: {}", suite.getName());
        log.info("===============================================");
    }
    
    @Override
    public void onFinish(ISuite suite) {
        long duration = System.currentTimeMillis() - suiteStartTime;
        log.info("===============================================");
        log.info("SUITE FINISHED: {}", suite.getName());
        log.info("Total Suite Duration: {} ms ({} seconds)", duration, duration / 1000);
        log.info("===============================================");
    }
    
    // =============== TEST LEVEL METHODS ===============
    
    @Override
    public void onTestStart(ITestResult result) {
        testStartTime = System.currentTimeMillis();
        String testName = getTestName(result);
        log.info("🚀 STARTING TEST: {}", testName);
        log.info("Test Class: {}", result.getTestClass().getName());
        log.info("Test Method: {}", result.getMethod().getMethodName());
        
        // Log test parameters if any
        Object[] parameters = result.getParameters();
        if (parameters != null && parameters.length > 0) {
            log.info("Test Parameters: {}", java.util.Arrays.toString(parameters));
        }
    }
    
    @Override
    public void onTestSuccess(ITestResult result) {
        long duration = System.currentTimeMillis() - testStartTime;
        String testName = getTestName(result);
        log.info("✅ TEST PASSED: {} (Duration: {} ms)", testName, duration);
    }
    
    @Override
    public void onTestFailure(ITestResult result) {
        long duration = System.currentTimeMillis() - testStartTime;
        String testName = getTestName(result);
        Throwable throwable = result.getThrowable();
        
        log.error("❌ TEST FAILED: {} (Duration: {} ms)", testName, duration);
        log.error("Failure Reason: {}", throwable.getMessage());
        log.error("Stack Trace: ", throwable);
        
        // Add failure information to Allure
        attachFailureInfoToAllure(result);
    }
    
    @Override
    public void onTestSkipped(ITestResult result) {
        String testName = getTestName(result);
        Throwable throwable = result.getThrowable();
        
        log.warn("⏭️ TEST SKIPPED: {}", testName);
        if (throwable != null) {
            log.warn("Skip Reason: {}", throwable.getMessage());
        }
    }
    
    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        String testName = getTestName(result);
        log.warn("⚠️ TEST FAILED BUT WITHIN SUCCESS PERCENTAGE: {}", testName);
    }
    
    // =============== INVOKED METHOD METHODS ===============
    
    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        if (method.isTestMethod()) {
            log.debug("Before test method invocation: {}", method.getTestMethod().getMethodName());
        } else if (method.isConfigurationMethod()) {
            log.debug("Before configuration method: {}", method.getTestMethod().getMethodName());
        }
    }
    
    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        if (method.isTestMethod()) {
            log.debug("After test method invocation: {}", method.getTestMethod().getMethodName());
        } else if (method.isConfigurationMethod()) {
            log.debug("After configuration method: {}", method.getTestMethod().getMethodName());
            
            // Log configuration method failures
            if (testResult.getStatus() == ITestResult.FAILURE) {
                log.error("Configuration method failed: {}", method.getTestMethod().getMethodName());
                log.error("Failure reason: {}", testResult.getThrowable().getMessage());
            }
        }
    }
    
    // =============== HELPER METHODS ===============
    
    private String getTestName(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        Object[] parameters = result.getParameters();
        
        if (parameters != null && parameters.length > 0) {
            testName += "(" + java.util.Arrays.toString(parameters) + ")";
        }
        
        return testName;
    }
    
    private void attachFailureInfoToAllure(ITestResult result) {
        try {
            // Get Allure lifecycle
            AllureLifecycle lifecycle = io.qameta.allure.Allure.getLifecycle();
            
            // Create failure details
            String failureDetails = createFailureDetails(result);
            
            // Attach failure details
            lifecycle.addAttachment(
                "Failure Details",
                "text/plain",
                ".txt",
                failureDetails.getBytes()
            );
            
        } catch (Exception e) {
            log.warn("Failed to attach failure info to Allure: {}", e.getMessage());
        }
    }
    
    private String createFailureDetails(ITestResult result) {
        StringBuilder details = new StringBuilder();
        
        details.append("=== TEST FAILURE DETAILS ===\n");
        details.append("Test Name: ").append(getTestName(result)).append("\n");
        details.append("Test Class: ").append(result.getTestClass().getName()).append("\n");
        details.append("Failure Time: ").append(new java.util.Date()).append("\n");
        details.append("Duration: ").append(System.currentTimeMillis() - testStartTime).append(" ms\n");
        
        Throwable throwable = result.getThrowable();
        if (throwable != null) {
            details.append("Exception Type: ").append(throwable.getClass().getSimpleName()).append("\n");
            details.append("Exception Message: ").append(throwable.getMessage()).append("\n");
            details.append("Stack Trace:\n");
            details.append(getStackTrace(throwable));
        }
        
        return details.toString();
    }
    
    private String getStackTrace(Throwable throwable) {
        java.io.StringWriter sw = new java.io.StringWriter();
        java.io.PrintWriter pw = new java.io.PrintWriter(sw);
        throwable.printStackTrace(pw);
        return sw.toString();
    }
    
    // =============== STATISTICS LOGGING ===============
    
    @Override
    public void onStart(ITestContext context) {
        log.info("Test Context Started: {}", context.getName());
        log.info("Total tests to run: {}", context.getAllTestMethods().length);
    }
    
    @Override
    public void onFinish(ITestContext context) {
        log.info("=== TEST EXECUTION SUMMARY ===");
        log.info("Test Context: {}", context.getName());
        log.info("Tests Passed: {}", context.getPassedTests().size());
        log.info("Tests Failed: {}", context.getFailedTests().size());
        log.info("Tests Skipped: {}", context.getSkippedTests().size());
        log.info("Total Duration: {} ms", 
            context.getEndDate().getTime() - context.getStartDate().getTime());
        log.info("===============================");
    }
}