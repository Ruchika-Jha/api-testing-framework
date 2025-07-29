package com.testlead.automation.base;

import com.testlead.automation.config.ConfigManager;
import com.testlead.automation.utils.RequestResponseLogger;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base API client with common functionality
 */
public abstract class BaseApiClient {
    
    protected static final Logger logger = LoggerFactory.getLogger(BaseApiClient.class);
    protected String authToken;
    
    static {
        // Set base URI from configuration
        RestAssured.baseURI = ConfigManager.baseUrl();
    }
    
    /**
     * Get request specification with common settings
     */
    protected RequestSpecification getRequestSpec() {
        RequestSpecBuilder builder = new RequestSpecBuilder();
        
        // Set base URI
        builder.setBaseUri(ConfigManager.baseUrl());
        
        // Set content type
        builder.setContentType(ContentType.JSON);
        builder.setAccept(ContentType.JSON);
        
        // Add authentication header if token exists
        if (authToken != null && !authToken.trim().isEmpty()) {
            builder.addHeader("Authorization", "Bearer " + authToken);
        }
        
        // Add common headers
        builder.addHeader("User-Agent", "API-Testing-Framework/1.0");
        
        // Build the specification
        RequestSpecification spec = builder.build();
        
        // Add request/response logging filter if enabled
        if (ConfigManager.isLoggingEnabled()) {
            spec.filter(new RequestResponseLogger());
        }
        
        return spec;
    }
    
    /**
     * Set authentication token
     */
    public void setAuthToken(String token) {
        this.authToken = token;
        logger.info("Auth token set for API client");
    }
    
    /**
     * Clear authentication token
     */
    public void clearAuthToken() {
        this.authToken = null;
        logger.info("Auth token cleared");
    }
    
    /**
     * Get current authentication token
     */
    public String getAuthToken() {
        return this.authToken;
    }
    
    /**
     * Enable logging for this client
     */
    public void enableLogging() {
        ConfigManager.setLoggingEnabled(true);
        logger.info("API logging enabled");
    }
    
    /**
     * Disable logging for this client
     */
    public void disableLogging() {
        ConfigManager.setLoggingEnabled(false);
        logger.info("API logging disabled");
    }
}