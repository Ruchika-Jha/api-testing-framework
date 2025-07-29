package com.testlead.automation.clients;

import com.testlead.automation.base.BaseApiClient;
import com.testlead.automation.models.User;
import io.restassured.response.Response;
import java.util.Map;

import static io.restassured.RestAssured.given;

/**
 * API client for User operations
 */
public class UserApiClient extends BaseApiClient {
    
    /**
     * Create a new user
     */
    public Response createUser(User user) {
        logger.info("Creating user with email: {}", user.getEmail());
        
        return given(getRequestSpec())
            .body(user)
            .when()
            .post("/users");
    }
    
    /**
     * Get user by ID
     */
    public Response getUserById(Long userId) {
        logger.info("Getting user by ID: {}", userId);
        
        return given(getRequestSpec())
            .pathParam("id", userId)
            .when()
            .get("/users/{id}");
    }
    
    /**
     * Update user
     */
    public Response updateUser(Long userId, User user) {
        logger.info("Updating user with ID: {}", userId);
        
        return given(getRequestSpec())
            .pathParam("id", userId)
            .body(user)
            .when()
            .put("/users/{id}");
    }
    
    /**
     * Delete user
     */
    public Response deleteUser(Long userId) {
        logger.info("Deleting user with ID: {}", userId);
        
        return given(getRequestSpec())
            .pathParam("id", userId)
            .when()
            .delete("/users/{id}");
    }
    
    /**
     * User login
     */
    public Response login(Map<String, String> credentials) {
        logger.info("Attempting login for user: {}", credentials.get("email"));
        
        return given(getRequestSpec())
            .body(credentials)
            .when()
            .post("/auth/login");
    }
    
    /**
     * User logout
     */
    public Response logout() {
        logger.info("Attempting logout");
        
        return given(getRequestSpec())
            .when()
            .post("/auth/logout");
    }
    
    /**
     * Get user profile
     */
    public Response getUserProfile(Long userId) {
        logger.info("Getting user profile for ID: {}", userId);
        
        return given(getRequestSpec())
            .pathParam("id", userId)
            .when()
            .get("/users/{id}/profile");
    }
    
    /**
     * Get all users
     */
    public Response getAllUsers() {
        logger.info("Getting all users");
        
        return given(getRequestSpec())
            .when()
            .get("/users");
    }
    
    /**
     * Get users with pagination
     */
    public Response getUsers(int page, int size) {
        logger.info("Getting users - Page: {}, Size: {}", page, size);
        
        return given(getRequestSpec())
            .queryParam("page", page)
            .queryParam("size", size)
            .when()
            .get("/users");
    }
}