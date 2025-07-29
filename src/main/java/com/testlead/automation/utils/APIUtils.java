package com.testlead.automation.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.testlead.automation.models.ApiResponse;
import io.restassured.http.Header;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;

/**
 * Utility class for API testing operations
 */
public class APIUtils {
    
    private static final Logger logger = LoggerFactory.getLogger(APIUtils.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Random random = new Random();
    
    /**
     * Convert RestAssured Response to ApiResponse object
     */
    public static ApiResponse<Object> convertToApiResponse(Response response) {
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        
        try {
            // Set basic response information
            apiResponse.setStatusCode(response.getStatusCode());
            apiResponse.setStatusMessage(response.getStatusLine());
            apiResponse.setResponseTime(response.getTime());
            apiResponse.setTimestamp(getCurrentTimestamp());
            
            // Convert headers
            Map<String, String> headerMap = new HashMap<>();
            if (response.getHeaders() != null) {
                for (Header header : response.getHeaders()) {
                    headerMap.put(header.getName(), header.getValue());
                }
            }
            apiResponse.setHeaders(headerMap);
            
            // Set response body
            String responseBody = "";
            try {
                responseBody = response.getBody().asString();
                apiResponse.setBody(responseBody);
                
                // Try to parse as JSON and set as data
                if (isValidJson(responseBody)) {
                    Object jsonData = objectMapper.readValue(responseBody, Object.class);
                    apiResponse.setData(jsonData);
                } else {
                    apiResponse.setData(responseBody);
                }
            } catch (Exception e) {
                logger.warn("Failed to process response body: {}", e.getMessage());
                apiResponse.setBody("");
                apiResponse.setErrorMessage("Failed to process response body: " + e.getMessage());
            }
            
            // Set success flag based on status code
            apiResponse.setSuccess(response.getStatusCode() >= 200 && response.getStatusCode() < 300);
            
            // Set error message for non-successful responses
            if (!apiResponse.isSuccess()) {
                apiResponse.setErrorMessage("HTTP " + response.getStatusCode() + ": " + response.getStatusLine());
            }
            
        } catch (Exception e) {
            logger.error("Failed to convert Response to ApiResponse: {}", e.getMessage(), e);
            apiResponse.setStatusCode(0);
            apiResponse.setSuccess(false);
            apiResponse.setErrorMessage("Failed to process response: " + e.getMessage());
        }
        
        return apiResponse;
    }
    
    /**
     * Convert RestAssured Response to typed ApiResponse
     */
    public static <T> ApiResponse<T> convertToApiResponse(Response response, Class<T> responseType) {
        ApiResponse<T> apiResponse = new ApiResponse<>();
        
        try {
            // Set basic response information
            apiResponse.setStatusCode(response.getStatusCode());
            apiResponse.setStatusMessage(response.getStatusLine());
            apiResponse.setResponseTime(response.getTime());
            apiResponse.setTimestamp(getCurrentTimestamp());
            
            // Convert headers
            Map<String, String> headerMap = new HashMap<>();
            if (response.getHeaders() != null) {
                for (Header header : response.getHeaders()) {
                    headerMap.put(header.getName(), header.getValue());
                }
            }
            apiResponse.setHeaders(headerMap);
            
            // Set response body
            String responseBody = response.getBody().asString();
            apiResponse.setBody(responseBody);
            
            // Try to parse as specific type
            if (isValidJson(responseBody)) {
                T typedData = objectMapper.readValue(responseBody, responseType);
                apiResponse.setData(typedData);
            }
            
            // Set success flag
            apiResponse.setSuccess(response.getStatusCode() >= 200 && response.getStatusCode() < 300);
            
            // Set error message for non-successful responses
            if (!apiResponse.isSuccess()) {
                apiResponse.setErrorMessage("HTTP " + response.getStatusCode() + ": " + response.getStatusLine());
            }
            
        } catch (Exception e) {
            logger.error("Failed to convert Response to typed ApiResponse: {}", e.getMessage(), e);
            apiResponse.setStatusCode(0);
            apiResponse.setSuccess(false);
            apiResponse.setErrorMessage("Failed to process response: " + e.getMessage());
        }
        
        return apiResponse;
    }
    
    /**
     * Generate random email address
     */
    public static String generateRandomEmail() {
        String[] domains = {"gmail.com", "yahoo.com", "outlook.com", "test.com"};
        String randomString = generateRandomString(8);
        String domain = domains[random.nextInt(domains.length)];
        return randomString.toLowerCase() + "@" + domain;
    }
    
    /**
     * Generate random string of specified length
     */
    public static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < length; i++) {
            result.append(characters.charAt(random.nextInt(characters.length())));
        }
        return result.toString();
    }
    
    /**
     * Generate random phone number
     */
    public static String generateRandomPhoneNumber() {
        return String.format("+1%d%d%d%d%d%d%d%d%d%d", 
            random.nextInt(10), random.nextInt(10), random.nextInt(10),
            random.nextInt(10), random.nextInt(10), random.nextInt(10),
            random.nextInt(10), random.nextInt(10), random.nextInt(10),
            random.nextInt(10));
    }
    
    /**
     * Generate random number within range
     */
    public static int generateRandomNumber(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }
    
    /**
     * Get current timestamp
     */
    public static String getCurrentTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
    
    /**
     * Get current timestamp with custom format
     */
    public static String getCurrentTimestamp(String format) {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(format));
    }
    
    /**
     * Validate JSON format
     */
    public static boolean isValidJson(String jsonString) {
        if (jsonString == null || jsonString.trim().isEmpty()) {
            return false;
        }
        try {
            objectMapper.readTree(jsonString);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Extract value from JSON response using JSONPath
     */
    public static String getJsonPathValue(String json, String jsonPath) {
        try {
            JsonNode rootNode = objectMapper.readTree(json);
            String[] pathParts = jsonPath.split("\\.");
            JsonNode currentNode = rootNode;
            
            for (String part : pathParts) {
                if (part.contains("[") && part.contains("]")) {
                    // Handle array access like "users[0]"
                    String arrayName = part.substring(0, part.indexOf("["));
                    int index = Integer.parseInt(part.substring(part.indexOf("[") + 1, part.indexOf("]")));
                    currentNode = currentNode.get(arrayName).get(index);
                } else {
                    currentNode = currentNode.get(part);
                }
                
                if (currentNode == null) {
                    return null;
                }
            }
            
            return currentNode.asText();
        } catch (Exception e) {
            logger.error("Failed to extract JSON path value: {}", e.getMessage());
            return null;
        }
    }
    
    /**
     * Create query string from parameters map
     */
    public static String createQueryString(Map<String, String> params) {
        if (params == null || params.isEmpty()) {
            return "";
        }
        
        StringBuilder queryString = new StringBuilder("?");
        params.forEach((key, value) -> {
            if (queryString.length() > 1) {
                queryString.append("&");
            }
            queryString.append(key).append("=").append(value);
        });
        
        return queryString.toString();
    }
    
    /**
     * Wait for specified milliseconds
     */
    public static void waitFor(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("Thread was interrupted during wait");
        }
    }
    
    /**
     * Retry mechanism for API calls
     */
    public static <T> T retryOperation(Supplier<T> operation, int maxRetries, long delayMs) {
        Exception lastException = null;
        
        for (int i = 0; i < maxRetries; i++) {
            try {
                return operation.get();
            } catch (Exception e) {
                lastException = e;
                logger.warn("Operation failed on attempt {} of {}: {}", i + 1, maxRetries, e.getMessage());
                
                if (i < maxRetries - 1) {
                    waitFor(delayMs);
                }
            }
        }
        
        throw new RuntimeException("Operation failed after " + maxRetries + " attempts", lastException);
    }
    
    /**
     * Mask sensitive data in logs
     */
    public static String maskSensitiveData(String data, String... sensitiveFields) {
        if (data == null || data.isEmpty()) {
            return data;
        }
        
        String maskedData = data;
        for (String field : sensitiveFields) {
            // Simple regex to mask common sensitive fields
            String pattern = "\"" + field + "\"\\s*:\\s*\"[^\"]*\"";
            maskedData = maskedData.replaceAll(pattern, "\"" + field + "\":\"***MASKED***\"");
        }
        return maskedData;
    }
    
    /**
     * Extract specific field from API response
     */
    public static <T> T extractFieldFromResponse(ApiResponse<?> response, String fieldName, Class<T> fieldType) {
        try {
            if (response.getData() != null) {
                String json = objectMapper.writeValueAsString(response.getData());
                JsonNode rootNode = objectMapper.readTree(json);
                JsonNode fieldNode = rootNode.get(fieldName);
                
                if (fieldNode != null) {
                    return objectMapper.convertValue(fieldNode, fieldType);
                }
            }
        } catch (Exception e) {
            logger.error("Failed to extract field '{}' from response: {}", fieldName, e.getMessage());
        }
        return null;
    }
    
    /**
     * Check if response contains specific field
     */
    public static boolean responseContainsField(ApiResponse<?> response, String fieldName) {
        try {
            if (response.getData() != null) {
                String json = objectMapper.writeValueAsString(response.getData());
                JsonNode rootNode = objectMapper.readTree(json);
                return rootNode.has(fieldName);
            }
        } catch (Exception e) {
            logger.error("Failed to check field '{}' in response: {}", fieldName, e.getMessage());
        }
        return false;
    }
    
    /**
     * Pretty print JSON string
     */
    public static String prettyPrintJson(String json) {
        try {
            Object jsonObject = objectMapper.readValue(json, Object.class);
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
        } catch (Exception e) {
            logger.warn("Failed to pretty print JSON: {}", e.getMessage());
            return json;
        }
    }
}