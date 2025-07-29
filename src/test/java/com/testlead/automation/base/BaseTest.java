package com.testlead.automation.base;

import com.testlead.automation.config.ConfigManager;
import com.testlead.automation.utils.JsonUtils;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import static org.testng.Assert.*;

/**
 * Base Test Class for all API tests
 * Contains common setup, teardown, and utility methods
 * This class goes in src/test/java
 */
@Slf4j
public abstract class BaseTest {
    
    protected ConfigManager config;
    protected SoftAssert softAssert;
    
    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() {
        log.info("=== Test Suite Started ===");
        config = ConfigManager.getInstance();
        config.validateConfiguration();
        log.info("Running tests against environment: {}", config.getEnvironment());
        log.info("Base URL: {}", config.getBaseUrl());
    }
    
    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
        log.info("=== Test Suite Completed ===");
    }
    
    @BeforeClass(alwaysRun = true)
    public void beforeClass() {
        log.info("Starting test class: {}", this.getClass().getSimpleName());
    }
    
    @AfterClass(alwaysRun = true)
    public void afterClass() {
        log.info("Completed test class: {}", this.getClass().getSimpleName());
    }
    
    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        softAssert = new SoftAssert();
        log.info("Starting test method");
    }
    
    @AfterMethod(alwaysRun = true)
    public void afterMethod() {
        if (softAssert != null) {
            softAssert.assertAll();
        }
        log.info("Completed test method");
    }
    
    // =============== ASSERTION METHODS ===============
    
    @Step("Verify response status code is {expectedStatusCode}")
    public void verifyStatusCode(Response response, int expectedStatusCode) {
        int actualStatusCode = response.getStatusCode();
        assertEquals(actualStatusCode, expectedStatusCode, 
            String.format("Expected status code %d but got %d. Response: %s", 
                expectedStatusCode, actualStatusCode, response.getBody().asString()));
        log.info("Status code verification passed: {}", actualStatusCode);
    }
    
    @Step("Verify response status code is {expectedStatusCode} (Soft Assert)")
    public void verifySoftStatusCode(Response response, int expectedStatusCode) {
        int actualStatusCode = response.getStatusCode();
        softAssert.assertEquals(actualStatusCode, expectedStatusCode, 
            String.format("Expected status code %d but got %d", expectedStatusCode, actualStatusCode));
        log.info("Soft status code verification: expected {}, actual {}", expectedStatusCode, actualStatusCode);
    }
    
    @Step("Verify response contains field: {fieldPath}")
    public void verifyResponseContainsField(Response response, String fieldPath) {
        Object fieldValue = response.jsonPath().get(fieldPath);
        assertNotNull(fieldValue, String.format("Field '%s' is missing in response", fieldPath));
        log.info("Field '{}' exists with value: {}", fieldPath, fieldValue);
    }
    
    @Step("Verify response field value: {fieldPath} = {expectedValue}")
    public void verifyResponseFieldValue(Response response, String fieldPath, Object expectedValue) {
        Object actualValue = response.jsonPath().get(fieldPath);
        assertEquals(actualValue, expectedValue, 
            String.format("Field '%s' expected: %s, actual: %s", fieldPath, expectedValue, actualValue));
        log.info("Field '{}' verification passed: {}", fieldPath, actualValue);
    }
    
    @Step("Verify response field value: {fieldPath} = {expectedValue} (Soft Assert)")
    public void verifySoftResponseFieldValue(Response response, String fieldPath, Object expectedValue) {
        Object actualValue = response.jsonPath().get(fieldPath);
        softAssert.assertEquals(actualValue, expectedValue, 
            String.format("Field '%s' expected: %s, actual: %s", fieldPath, expectedValue, actualValue));
        log.info("Soft field '{}' verification: expected {}, actual {}", fieldPath, expectedValue, actualValue);
    }
    
    @Step("Verify response time is less than {maxTimeMs} ms")
    public void verifyResponseTime(Response response, long maxTimeMs) {
        long actualTime = response.getTime();
        assertTrue(actualTime < maxTimeMs, 
            String.format("Response time %d ms exceeded maximum allowed time %d ms", actualTime, maxTimeMs));
        log.info("Response time verification passed: {} ms", actualTime);
    }
    
    @Step("Verify response body is not empty")
    public void verifyResponseBodyNotEmpty(Response response) {
        String responseBody = response.getBody().asString();
        assertNotNull(responseBody, "Response body is null");
        assertFalse(responseBody.trim().isEmpty(), "Response body is empty");
        log.info("Response body is not empty");
    }
    
    @Step("Verify response header: {headerName} = {expectedValue}")
    public void verifyResponseHeader(Response response, String headerName, String expectedValue) {
        String actualValue = response.getHeader(headerName);
        assertEquals(actualValue, expectedValue, 
            String.format("Header '%s' expected: %s, actual: %s", headerName, expectedValue, actualValue));
        log.info("Header '{}' verification passed: {}", headerName, actualValue);
    }
    
    @Step("Verify response contains error message: {expectedMessage}")
    public void verifyErrorMessage(Response response, String expectedMessage) {
        String responseBody = response.getBody().asString();
        assertTrue(responseBody.contains(expectedMessage), 
            String.format("Response does not contain expected error message: %s. Actual response: %s", 
                expectedMessage, responseBody));
        log.info("Error message verification passed: {}", expectedMessage);
    }
    
    // =============== UTILITY METHODS ===============
    
    @Step("Parse response to object of type: {clazz}")
    public <T> T parseResponseToObject(Response response, Class<T> clazz) {
        try {
            return JsonUtils.fromJson(response.getBody().asString(), clazz);
        } catch (Exception e) {
            log.error("Failed to parse response to object of type: {}", clazz.getSimpleName(), e);
            throw new RuntimeException("Response parsing failed", e);
        }
    }
    
    @Step("Log response details")
    public void logResponse(Response response) {
        log.info("Response Status: {}", response.getStatusCode());
        log.info("Response Time: {} ms", response.getTime());
        log.info("Response Body: {}", response.getBody().asString());
    }
    
    @Attachment(value = "Response Body", type = "application/json")
    public String attachResponseBody(Response response) {
        return response.getBody().asString();
    }
    
    @Attachment(value = "Request/Response Details", type = "text/plain")
    public String attachRequestResponseDetails(String method, String endpoint, Object requestBody, Response response) {
        StringBuilder details = new StringBuilder();
        details.append("=== REQUEST DETAILS ===\n");
        details.append("Method: ").append(method).append("\n");
        details.append("Endpoint: ").append(endpoint).append("\n");
        if (requestBody != null) {
            details.append("Request Body: ").append(JsonUtils.toJson(requestBody)).append("\n");
        }
        details.append("\n=== RESPONSE DETAILS ===\n");
        details.append("Status Code: ").append(response.getStatusCode()).append("\n");
        details.append("Response Time: ").append(response.getTime()).append(" ms\n");
        details.append("Response Body: ").append(response.getBody().asString()).append("\n");
        return details.toString();
    }
    
    // =============== DATA GENERATION HELPERS ===============
    
    protected String generateUniqueId() {
        return "test_" + System.currentTimeMillis();
    }
    
    protected String generateRandomEmail() {
        return "test_" + System.currentTimeMillis() + "@automation.com";
    }
    
    protected String generateRandomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < length; i++) {
            result.append(chars.charAt((int) (Math.random() * chars.length())));
        }
        return result.toString();
    }
    
    // =============== WAIT UTILITIES ===============
    
    protected void waitForSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
            log.info("Waited for {} seconds", seconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Wait interrupted", e);
        }
    }
    
    // =============== ENVIRONMENT HELPERS ===============
    
    protected boolean isProductionEnvironment() {
        return config.isProductionEnvironment();
    }
    
    protected void skipIfProduction(String reason) {
        if (isProductionEnvironment()) {
            throw new org.testng.SkipException("Skipping test in production environment: " + reason);
        }
    }
}