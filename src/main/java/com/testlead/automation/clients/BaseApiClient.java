package com.testlead.automation.clients;

import com.testlead.automation.config.ConfigManager;
import com.testlead.automation.utils.RequestResponseLogger;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * Base API Client providing common functionality for all API clients
 */
@Slf4j
public abstract class BaseApiClient {
    
    protected ConfigManager config;
    protected RequestSpecification requestSpec;
    protected ResponseSpecification responseSpec;
    protected Map<String, String> defaultHeaders;
    
    public BaseApiClient() {
        this.config = ConfigManager.getInstance();
        this.defaultHeaders = new HashMap<>();
        initializeSpecs();
        setupDefaultHeaders();
    }
    
    /**
     * Initialize request and response specifications
     */
    private void initializeSpecs() {
        // Request Specification
        RequestSpecBuilder requestBuilder = new RequestSpecBuilder()
                .setBaseUri(config.getBaseUrl())
                .setContentType(ContentType.JSON)
                .addFilter(new RequestResponseLogger());
        
        // Add request timeout
        requestBuilder.setConfig(
            RestAssured.config()
                .httpClient(RestAssured.config().getHttpClientConfig()
                    .setParam("http.connection.timeout", config.getConnectionTimeout())
                    .setParam("http.socket.timeout", config.getRequestTimeout())
                )
        );
        
        // Conditional logging based on configuration
        if (config.isRequestLoggingEnabled()) {
            requestBuilder.log(LogDetail.ALL);
        }
        
        this.requestSpec = requestBuilder.build();
        
        // Response Specification
        ResponseSpecBuilder responseBuilder = new ResponseSpecBuilder();
        
        if (config.isResponseLoggingEnabled()) {
            responseBuilder.log(LogDetail.ALL);
        }
        
        this.responseSpec = responseBuilder.build();
    }
    
    /**
     * Setup default headers for all requests
     */
    private void setupDefaultHeaders() {
        defaultHeaders.put("Accept", "application/json");
        defaultHeaders.put("Content-Type", "application/json");
        
        // Add API key if available
        String apiKey = config.getApiKey();
        if (apiKey != null && !apiKey.trim().isEmpty()) {
            defaultHeaders.put("X-API-Key", apiKey);
        }
        
        // Add auth token if available
        String authToken = config.getAuthToken();
        if (authToken != null && !authToken.trim().isEmpty()) {
            defaultHeaders.put("Authorization", "Bearer " + authToken);
        }
    }
    
    /**
     * Get request specification with default headers
     */
    protected RequestSpecification getRequestSpec() {
        return RestAssured.given(requestSpec).headers(defaultHeaders);
    }
    
    /**
     * Get request specification with custom headers
     */
    protected RequestSpecification getRequestSpec(Map<String, String> customHeaders) {
        Map<String, String> allHeaders = new HashMap<>(defaultHeaders);
        if (customHeaders != null) {
            allHeaders.putAll(customHeaders);
        }
        return RestAssured.given(requestSpec).headers(allHeaders);
    }
    
    /**
     * GET request
     */
    protected Response get(String endpoint) {
        log.info("Executing GET request to: {}", endpoint);
        return getRequestSpec()
                .when()
                .get(endpoint)
                .then()
                .spec(responseSpec)
                .extract()
                .response();
    }
    
    /**
     * GET request with path parameters
     */
    protected Response get(String endpoint, Map<String, Object> pathParams) {
        log.info("Executing GET request to: {} with path params: {}", endpoint, pathParams);
        return getRequestSpec()
                .pathParams(pathParams)
                .when()
                .get(endpoint)
                .then()
                .spec(responseSpec)
                .extract()
                .response();
    }
    
    /**
     * GET request with query parameters
     */
    protected Response getWithQueryParams(String endpoint, Map<String, Object> queryParams) {
        log.info("Executing GET request to: {} with query params: {}", endpoint, queryParams);
        return getRequestSpec()
                .queryParams(queryParams)
                .when()
                .get(endpoint)
                .then()
                .spec(responseSpec)
                .extract()
                .response();
    }
    
    /**
     * POST request with body
     */
    protected Response post(String endpoint, Object body) {
        log.info("Executing POST request to: {}", endpoint);
        return getRequestSpec()
                .body(body)
                .when()
                .post(endpoint)
                .then()
                .spec(responseSpec)
                .extract()
                .response();
    }
    
    /**
     * POST request with body and custom headers
     */
    protected Response post(String endpoint, Object body, Map<String, String> headers) {
        log.info("Executing POST request to: {} with custom headers", endpoint);
        return getRequestSpec(headers)
                .body(body)
                .when()
                .post(endpoint)
                .then()
                .spec(responseSpec)
                .extract()
                .response();
    }
    
    /**
     * PUT request with body
     */
    protected Response put(String endpoint, Object body) {
        log.info("Executing PUT request to: {}", endpoint);
        return getRequestSpec()
                .body(body)
                .when()
                .put(endpoint)
                .then()
                .spec(responseSpec)
                .extract()
                .response();
    }
    
    /**
     * PUT request with path parameters
     */
    protected Response put(String endpoint, Object body, Map<String, Object> pathParams) {
        log.info("Executing PUT request to: {} with path params: {}", endpoint, pathParams);
        return getRequestSpec()
                .body(body)
                .pathParams(pathParams)
                .when()
                .put(endpoint)
                .then()
                .spec(responseSpec)
                .extract()
                .response();
    }
    
    /**
     * PATCH request with body
     */
    protected Response patch(String endpoint, Object body) {
        log.info("Executing PATCH request to: {}", endpoint);
        return getRequestSpec()
                .body(body)
                .when()
                .patch(endpoint)
                .then()
                .spec(responseSpec)
                .extract()
                .response();
    }
    
    /**
     * DELETE request
     */
    protected Response delete(String endpoint) {
        log.info("Executing DELETE request to: {}", endpoint);
        return getRequestSpec()
                .when()
                .delete(endpoint)
                .then()
                .spec(responseSpec)
                .extract()
                .response();
    }
    
    /**
     * DELETE request with path parameters
     */
    protected Response delete(String endpoint, Map<String, Object> pathParams) {
        log.info("Executing DELETE request to: {} with path params: {}", endpoint, pathParams);
        return getRequestSpec()
                .pathParams(pathParams)
                .when()
                .delete(endpoint)
                .then()
                .spec(responseSpec)
                .extract()
                .response();
    }
    
    /**
     * Update authorization header with Bearer token
     */
    public void setAuthToken(String token) {
        if (token != null && !token.trim().isEmpty()) {
            defaultHeaders.put("Authorization", "Bearer " + token);
            log.info("Authorization token updated");
        }
    }
    
    /**
     * Update API key header
     */
    public void setApiKey(String apiKey) {
        if (apiKey != null && !apiKey.trim().isEmpty()) {
            defaultHeaders.put("X-API-Key", apiKey);
            log.info("API key updated");
        }
    }
    
    /**
     * Add custom header
     */
    public void addHeader(String key, String value) {
        defaultHeaders.put(key, value);
        log.info("Custom header added: {} = {}", key, value);
    }
    
    /**
     * Remove header
     */
    public void removeHeader(String key) {
        defaultHeaders.remove(key);
        log.info("Header removed: {}", key);
    }
    
    /**
     * Clear all custom headers and reset to defaults
     */
    public void resetHeaders() {
        defaultHeaders.clear();
        setupDefaultHeaders();
        log.info("Headers reset to defaults");
    }
}