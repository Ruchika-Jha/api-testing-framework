package com.testlead.automation.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * User model class representing user entity
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("firstName")
    private String firstName;
    
    @JsonProperty("lastName")
    private String lastName;
    
    @JsonProperty("email")
    private String email;
    
    @JsonProperty("username")
    private String username;
    
    @JsonProperty("password")
    private String password;
    
    @JsonProperty("phone")
    private String phone;
    
    @JsonProperty("age")
    private Integer age;
    
    @JsonProperty("address")
    private String address;
    
    @JsonProperty("isActive")
    private Boolean isActive;
    
    @JsonProperty("role")
    private String role;
    
    @JsonProperty("createdAt")
    private LocalDateTime createdAt;
    
    @JsonProperty("updatedAt")
    private LocalDateTime updatedAt;
    
    @JsonProperty("lastLoginAt")
    private LocalDateTime lastLoginAt;
    
    @JsonProperty("profilePicture")
    private String profilePicture;
    
    @JsonProperty("bio")
    private String bio;
    
    // Default constructor
    public User() {}
    
    // Builder constructor
    private User(Builder builder) {
        this.id = builder.id;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.email = builder.email;
        this.username = builder.username;
        this.password = builder.password;
        this.phone = builder.phone;
        this.age = builder.age;
        this.address = builder.address;
        this.isActive = builder.isActive;
        this.role = builder.role;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
        this.lastLoginAt = builder.lastLoginAt;
        this.profilePicture = builder.profilePicture;
        this.bio = builder.bio;
    }
    
    // Getters
    public Long getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getPhone() { return phone; }
    public Integer getAge() { return age; }
    public String getAddress() { return address; }
    public Boolean getIsActive() { return isActive; }
    public Boolean isActive() { return isActive; }
    public String getRole() { return role; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public LocalDateTime getLastLoginAt() { return lastLoginAt; }
    public String getProfilePicture() { return profilePicture; }
    public String getBio() { return bio; }
    
    // Setters
    public void setId(Long id) { this.id = id; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setEmail(String email) { this.email = email; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setAge(Integer age) { this.age = age; }
    public void setAddress(String address) { this.address = address; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    public void setActive(Boolean active) { this.isActive = active; }
    public void setRole(String role) { this.role = role; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public void setLastLoginAt(LocalDateTime lastLoginAt) { this.lastLoginAt = lastLoginAt; }
    public void setProfilePicture(String profilePicture) { this.profilePicture = profilePicture; }
    public void setBio(String bio) { this.bio = bio; }
    
    // Utility methods
    public String getFullName() {
        return (firstName != null ? firstName : "") + " " + (lastName != null ? lastName : "");
    }
    
    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(role);
    }
    
    public boolean isModerator() {
        return "MODERATOR".equalsIgnoreCase(role);
    }
    
    public boolean isUser() {
        return "USER".equalsIgnoreCase(role);
    }
    
    public boolean isValidEmail() {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$");
    }
    
    public boolean hasRequiredFields() {
        return firstName != null && !firstName.trim().isEmpty() &&
               lastName != null && !lastName.trim().isEmpty() &&
               email != null && !email.trim().isEmpty() &&
               username != null && !username.trim().isEmpty() &&
               password != null && !password.trim().isEmpty();
    }
    
    // Builder pattern
    public static Builder builder() {
        return new Builder();
    }
    
    public Builder toBuilder() {
        return new Builder()
            .id(this.id)
            .firstName(this.firstName)
            .lastName(this.lastName)
            .email(this.email)
            .username(this.username)
            .password(this.password)
            .phone(this.phone)
            .age(this.age)
            .address(this.address)
            .isActive(this.isActive)
            .role(this.role)
            .createdAt(this.createdAt)
            .updatedAt(this.updatedAt)
            .lastLoginAt(this.lastLoginAt)
            .profilePicture(this.profilePicture)
            .bio(this.bio);
    }
    
    public static class Builder {
        private Long id;
        private String firstName;
        private String lastName;
        private String email;
        private String username;
        private String password;
        private String phone;
        private Integer age;
        private String address;
        private Boolean isActive;
        private String role;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private LocalDateTime lastLoginAt;
        private String profilePicture;
        private String bio;
        
        public Builder id(Long id) { this.id = id; return this; }
        public Builder firstName(String firstName) { this.firstName = firstName; return this; }
        public Builder lastName(String lastName) { this.lastName = lastName; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public Builder username(String username) { this.username = username; return this; }
        public Builder password(String password) { this.password = password; return this; }
        public Builder phone(String phone) { this.phone = phone; return this; }
        public Builder age(Integer age) { this.age = age; return this; }
        public Builder address(String address) { this.address = address; return this; }
        public Builder isActive(Boolean isActive) { this.isActive = isActive; return this; }
        public Builder active(Boolean active) { this.isActive = active; return this; }
        public Builder role(String role) { this.role = role; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Builder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }
        public Builder lastLoginAt(LocalDateTime lastLoginAt) { this.lastLoginAt = lastLoginAt; return this; }
        public Builder profilePicture(String profilePicture) { this.profilePicture = profilePicture; return this; }
        public Builder bio(String bio) { this.bio = bio; return this; }
        
        public User build() {
            return new User(this);
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
               Objects.equals(email, user.email) &&
               Objects.equals(username, user.username);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, email, username);
    }
    
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", phone='" + phone + '\'' +
                ", age=" + age +
                ", isActive=" + isActive +
                ", role='" + role + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
    
    // toString without sensitive data
    public String toStringSafe() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", phone='" + phone + '\'' +
                ", age=" + age +
                ", isActive=" + isActive +
                ", role='" + role + '\'' +
                '}';
    }
}