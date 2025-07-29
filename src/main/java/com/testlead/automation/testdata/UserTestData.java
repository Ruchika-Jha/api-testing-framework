package com.testlead.automation.testdata;

import com.testlead.automation.models.User;
import com.testlead.automation.utils.DataFaker;
import com.testlead.automation.utils.DataUtils;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Test data provider for User-related test scenarios
 */
public class UserTestData {
    
    private static final Logger logger = LoggerFactory.getLogger(UserTestData.class);
    private static final DataFaker dataFaker = DataFaker.getInstance();
    
    // Predefined test users for consistent testing
    private static final Map<String, User> PREDEFINED_USERS = new HashMap<>();
    
    static {
        initializePredefinedUsers();
    }
    
    /**
     * Initialize predefined test users
     */
    private static void initializePredefinedUsers() {
        // Valid test user
        PREDEFINED_USERS.put("valid_user", User.builder()
            .id(1L)
            .firstName("John")
            .lastName("Doe")
            .email("john.doe@testlead.com")
            .username("johndoe")
            .password("Password123!")
            .phone("+1-555-123-4567")
            .age(30)
            .address("123 Main St, New York, NY 10001")
            .isActive(true)
            .role("USER")
            .build());
        
        // Admin user
        PREDEFINED_USERS.put("admin_user", User.builder()
            .id(2L)
            .firstName("Admin")
            .lastName("User")
            .email("admin@testlead.com")
            .username("admin")
            .password("AdminPass123!")
            .phone("+1-555-999-0000")
            .age(35)
            .address("456 Admin Ave, Admin City, AC 12345")
            .isActive(true)
            .role("ADMIN")
            .build());
        
        // Inactive user
        PREDEFINED_USERS.put("inactive_user", User.builder()
            .id(3L)
            .firstName("Inactive")
            .lastName("User")
            .email("inactive@testlead.com")
            .username("inactive")
            .password("Inactive123!")
            .phone("+1-555-000-1111")
            .age(25)
            .address("789 Inactive Rd, Inactive Town, IT 54321")
            .isActive(false)
            .role("USER")
            .build());
        
        // User with special characters
        PREDEFINED_USERS.put("special_char_user", User.builder()
            .id(4L)
            .firstName("José")
            .lastName("García-Martinez")
            .email("jose.garcia@testlead.com")
            .username("jose_garcia")
            .password("José123!")
            .phone("+34-666-777-888")
            .age(28)
            .address("Calle Principal 123, Madrid, España")
            .isActive(true)
            .role("USER")
            .build());
    }
    
    /**
     * Get predefined user by type
     */
    public static User getPredefinedUser(String userType) {
        User user = PREDEFINED_USERS.get(userType);
        if (user == null) {
            logger.warn("Predefined user type '{}' not found, available types: {}", 
                userType, PREDEFINED_USERS.keySet());
            return getValidUser(); // Return default valid user
        }
        return user.toBuilder().build(); // Return a copy to avoid modification
    }
    
    /**
     * Get all predefined user types
     */
    public static Set<String> getPredefinedUserTypes() {
        return new HashSet<>(PREDEFINED_USERS.keySet());
    }
    
    /**
     * Generate a valid user with random data
     */
    public static User getValidUser() {
        return User.builder()
            .firstName(dataFaker.getFirstName())
            .lastName(dataFaker.getLastName())
            .email(dataFaker.getEmail())
            .username(dataFaker.getUsername())
            .password(dataFaker.getPassword())
            .phone(dataFaker.getPhoneNumber())
            .age(dataFaker.getAge(18, 80))
            .address(dataFaker.getAddress())
            .isActive(true)
            .role("USER")
            .build();
    }
    
    /**
     * Generate a user with invalid email
     */
    public static User getUserWithInvalidEmail() {
        return getValidUser().toBuilder()
            .email("invalid-email-format")
            .build();
    }
    
    /**
     * Generate a user with empty required fields
     */
    public static User getUserWithEmptyFields() {
        return User.builder()
            .firstName("")
            .lastName("")
            .email("")
            .username("")
            .password("")
            .build();
    }
    
    /**
     * Generate a user with null required fields
     */
    public static User getUserWithNullFields() {
        return User.builder()
            .firstName(null)
            .lastName(null)
            .email(null)
            .username(null)
            .password(null)
            .build();
    }
    
    /**
     * Generate a user with weak password
     */
    public static User getUserWithWeakPassword() {
        return getValidUser().toBuilder()
            .password("123")
            .build();
    }
    
    /**
     * Generate a user with long strings (boundary testing)
     */
    public static User getUserWithLongFields() {
        String longString = "a".repeat(300);
        return getValidUser().toBuilder()
            .firstName(longString)
            .lastName(longString)
            .email(longString + "@test.com")
            .username(longString)
            .address(longString)
            .build();
    }
    
    /**
     * Generate a user with special characters
     */
    public static User getUserWithSpecialCharacters() {
        return User.builder()
            .firstName("Test!@#$%^&*()")
            .lastName("User<>?:\"{}|")
            .email("special+chars@test-domain.com")
            .username("user_123-test")
            .password("Pass@123!")
            .phone("+1-(555)-123-4567")
            .age(25)
            .address("123 Main St. #456, Test City, TC 12345")
            .isActive(true)
            .role("USER")
            .build();
    }
    
    /**
     * Generate a minor user (age < 18)
     */
    public static User getMinorUser() {
        return getValidUser().toBuilder()
            .age(dataFaker.getAge(13, 17))
            .build();
    }
    
    /**
     * Generate a senior user (age > 65)
     */
    public static User getSeniorUser() {
        return getValidUser().toBuilder()
            .age(dataFaker.getAge(65, 95))
            .build();
    }
    
    /**
     * Generate admin user
     */
    public static User getAdminUser() {
        return getValidUser().toBuilder()
            .role("ADMIN")
            .build();
    }
    
    /**
     * Generate moderator user
     */
    public static User getModeratorUser() {
        return getValidUser().toBuilder()
            .role("MODERATOR")
            .build();
    }
    
    /**
     * Generate inactive user
     */
    public static User getInactiveUser() {
        return getValidUser().toBuilder()
            .isActive(false)
            .build();
    }
    
    /**
     * Generate multiple valid users
     */
    public static List<User> getMultipleValidUsers(int count) {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            users.add(getValidUser());
        }
        return users;
    }
    
    /**
     * Generate users with different roles
     */
    public static List<User> getUsersWithDifferentRoles() {
        List<User> users = new ArrayList<>();
        String[] roles = {"USER", "ADMIN", "MODERATOR", "GUEST"};
        
        for (String role : roles) {
            users.add(getValidUser().toBuilder().role(role).build());
        }
        return users;
    }
    
    /**
     * Generate users for pagination testing
     */
    public static List<User> getUsersForPagination(int totalUsers) {
        List<User> users = new ArrayList<>();
        for (int i = 1; i <= totalUsers; i++) {
            User user = getValidUser().toBuilder()
                .id((long) i)
                .username("user" + i)
                .email("user" + i + "@testlead.com")
                .build();
            users.add(user);
        }
        return users;
    }
    
    /**
     * Get user creation request data from JSON file
     */
    public static User getUserFromJsonFile(String fileName) {
        try {
            JsonNode jsonNode = DataUtils.readJsonFromClasspath("testdata/requests/" + fileName);
            return DataUtils.jsonToObject(jsonNode.toString(), User.class);
        } catch (Exception e) {
            logger.error("Error reading user data from file: {}", fileName, e);
            return getValidUser();
        }
    }
    
    /**
     * Get user update data
     */
    public static User getUserUpdateData(User originalUser) {
        return originalUser.toBuilder()
            .firstName(dataFaker.getFirstName())
            .lastName(dataFaker.getLastName())
            .phone(dataFaker.getPhoneNumber())
            .address(dataFaker.getAddress())
            .build();
    }
    
    /**
     * Get user data for performance testing
     */
    public static List<User> getUsersForPerformanceTesting(int count) {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            users.add(User.builder()
                .firstName("PerfTest" + i)
                .lastName("User" + i)
                .email("perftest" + i + "@testlead.com")
                .username("perfuser" + i)
                .password("PerfPass123!")
                .phone("+1-555-" + String.format("%07d", i))
                .age(25 + (i % 50))
                .address("Address " + i)
                .isActive(true)
                .role("USER")
                .build());
        }
        return users;
    }
    
    /**
     * Get user registration data with email verification
     */
    public static Map<String, Object> getUserRegistrationData() {
        User user = getValidUser();
        Map<String, Object> registrationData = new HashMap<>();
        registrationData.put("user", user);
        registrationData.put("confirmPassword", user.getPassword());
        registrationData.put("acceptTerms", true);
        registrationData.put("subscribeNewsletter", dataFaker.getBoolean()); // Fixed: using correct method
        return registrationData;
    }
    
    /**
     * Get user login credentials
     */
    public static Map<String, String> getUserLoginCredentials(String userType) {
        User user = getPredefinedUser(userType);
        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", user.getUsername());
        credentials.put("password", user.getPassword());
        return credentials;
    }
    
    /**
     * Get invalid login credentials
     */
    public static Map<String, String> getInvalidLoginCredentials() {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", "nonexistent_user");
        credentials.put("password", "wrong_password");
        return credentials;
    }
    
    /**
     * Get user profile update data
     */
    public static Map<String, Object> getUserProfileUpdateData() {
        Map<String, Object> updateData = new HashMap<>();
        updateData.put("firstName", dataFaker.getFirstName());
        updateData.put("lastName", dataFaker.getLastName());
        updateData.put("phone", dataFaker.getPhoneNumber());
        updateData.put("address", dataFaker.getAddress());
        updateData.put("bio", dataFaker.getText(10, 30)); // Fixed: using correct method
        return updateData;
    }
    
    /**
     * Get user search criteria
     */
    public static Map<String, Object> getUserSearchCriteria() {
        Map<String, Object> criteria = new HashMap<>();
        criteria.put("role", "USER");
        criteria.put("isActive", true);
        criteria.put("ageMin", 18);
        criteria.put("ageMax", 65);
        return criteria;
    }
    
    /**
     * Get user data for CSV export testing
     */
    public static List<User> getUsersForCsvExport() {
        return Arrays.asList(
            getPredefinedUser("valid_user"),
            getPredefinedUser("admin_user"),
            getPredefinedUser("inactive_user"),
            getValidUser(),
            getValidUser()
        );
    }
    
    /**
     * Print all predefined users (for debugging)
     */
    public static void printPredefinedUsers() {
        logger.info("=== Predefined Users ===");
        PREDEFINED_USERS.forEach((type, user) -> {
            logger.info("Type: {} | Username: {} | Email: {} | Role: {}", 
                type, user.getUsername(), user.getEmail(), user.getRole());
        });
        logger.info("=== End Predefined Users ===");
    }
}