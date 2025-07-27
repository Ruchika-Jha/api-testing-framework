package com.testlead.automation.utils;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import lombok.extern.slf4j.Slf4j;

/**
 * Custom filter to log requests and responses for better debugging and reporting
 * Note: Allure attachments are handled separately in test classes to keep framework clean
 */
@Slf4j
public class RequestResponseLogger implements Filter {

    @Override
    public Response filter(FilterableRequestSpecification requestSpec, 
                          FilterableResponseSpecification responseSpec, 
                          FilterContext ctx) {
        
        // Log request details
        logRequest(requestSpec);
        
        // Execute the request
        Response response = ctx.next(requestSpec, responseSpec);
        
        // Log response details
        logResponse(response);
        
        return response;
    }
    
    private void logRequest(FilterableRequestSpecification requestSpec) {
        log.info("=== REQUEST DETAILS ===");
        log.info("Method: {}", requestSpec.getMethod());
        log.info("URI: {}", requestSpec.getURI());
        
        if (requestSpec.getHeaders() != null && requestSpec.getHeaders().size() > 0) {
            log.info("Headers: {}", requestSpec.getHeaders());
        }
        
        if (requestSpec.getQueryParams() != null && !requestSpec.getQueryParams().isEmpty()) {
            log.info("Query Parameters: {}", requestSpec.getQueryParams());
        }
        
        if (requestSpec.getPathParams() != null && !requestSpec.getPathParams().isEmpty()) {
            log.info("Path Parameters: {}", requestSpec.getPathParams());
        }
        
        if (requestSpec.getBody() != null) {
            log.info("Request Body: {}", (Object) requestSpec.getBody());
        }
        
        log.info("=========================");
    }
    
    private void logResponse(Response response) {
        log.info("=== RESPONSE DETAILS ===");
        log.info("Status Code: {}", response.getStatusCode());
        log.info("Status Line: {}", response.getStatusLine());
        log.info("Response Time: {} ms", response.getTime());
        
        if (response.getHeaders() != null && response.getHeaders().size() > 0) {
            log.info("Response Headers: {}", response.getHeaders());
        }
        
        String responseBody = response.getBody().asString();
        if (responseBody != null && !responseBody.trim().isEmpty()) {
            log.info("Response Body: {}", responseBody);
        }
        
        log.info("==========================");
    }
}