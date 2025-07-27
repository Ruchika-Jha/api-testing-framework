package com.testlead.automation.config;

import lombok.extern.slf4j.Slf4j;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * Configuration Manager to handle all configuration properties
 * Supports environment-specific configurations
 */
@Slf4j
public class ConfigManager {
    
    private static ConfigManager instance;
    private Properties properties;
    private static final String DEFAULT_CONFIG_FILE = "config/qa.properties";
    
    private ConfigManager() {
        loadConfiguration();
    }
    
    public static ConfigManager getInstance() {
        if (instance == null) {
            synchronized (ConfigManager.class) {
                if (instance == null) {
                    instance = new ConfigManager();
                }
            }
        }
        return instance;
    }
    
    private void loadConfiguration() {
        properties = new Properties();
        String environment = System.getProperty("env", "qa");
        String configFile = String.format("config/%s.properties", environment);
        
        try {
            // Try to load from classpath first
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(configFile);
            
            if (inputStream == null) {
                // Fallback to default config
                inputStream = getClass().getClassLoader().getResourceAsStream(DEFAULT_CONFIG_FILE);
                log.warn("Config file {} not found, using default: {}", configFile, DEFAULT_CONFIG_FILE);
            }
            
            if (inputStream != null) {
                properties.load(inputStream);
                log.info("Configuration loaded successfully from: {}", configFile);
            } else {
                log.error("No configuration file found!");
                throw new RuntimeException("Configuration file not found: " + configFile);
            }
            
        } catch (Exception e) {
            log.error("Error loading configuration: ", e);
            throw new RuntimeException("Failed to load configuration", e);
        }
    }
    
    // Base URL methods
    public String getBaseUrl() {
        return getProperty("base.url");
    }
    
    public String getAuthUrl() {
        return getProperty("auth.url", getBaseUrl());
    }
    
    // Authentication
    public String getUsername() {
        return getProperty("auth.username");
    }
    
    public String getPassword() {
        return getProperty("auth.password");
    }
    
    public String getApiKey() {
        return getProperty("auth.api.key");
    }
    
    public String getAuthToken() {
        return getProperty("auth.token");
    }
    
    // Database configuration
    public String getDbUrl() {
        return getProperty("db.url");
    }
    
    public String getDbUsername() {
        return getProperty("db.username");
    }
    
    public String getDbPassword() {
        return getProperty("db.password");
    }
    
    // Timeouts
    public int getRequestTimeout() {
        return Integer.parseInt(getProperty("request.timeout", "30000"));
    }
    
    public int getConnectionTimeout() {
        return Integer.parseInt(getProperty("connection.timeout", "10000"));
    }
    
    // Retry configuration
    public int getMaxRetries() {
        return Integer.parseInt(getProperty("max.retries", "3"));
    }
    
    public int getRetryDelay() {
        return Integer.parseInt(getProperty("retry.delay", "1000"));
    }
    
    // Reporting
    public String getReportPath() {
        return getProperty("report.path", "target/reports");
    }
    
    public boolean isScreenshotEnabled() {
        return Boolean.parseBoolean(getProperty("screenshot.enabled", "true"));
    }
    
    // Logging
    public String getLogLevel() {
        return getProperty("log.level", "INFO");
    }
    
    public boolean isRequestLoggingEnabled() {
        return Boolean.parseBoolean(getProperty("log.requests", "true"));
    }
    
    public boolean isResponseLoggingEnabled() {
        return Boolean.parseBoolean(getProperty("log.responses", "true"));
    }
    
    // Environment specific
    public String getEnvironment() {
        return getProperty("environment", "qa");
    }
    
    public boolean isProductionEnvironment() {
        return "prod".equalsIgnoreCase(getEnvironment()) || 
               "production".equalsIgnoreCase(getEnvironment());
    }
    
    // Generic property getter with default value
    public String getProperty(String key, String defaultValue) {
        String value = System.getProperty(key);
        if (value == null) {
            value = properties.getProperty(key, defaultValue);
        }
        return value;
    }
    
    // Generic property getter
    public String getProperty(String key) {
        String value = System.getProperty(key);
        if (value == null) {
            value = properties.getProperty(key);
        }
        if (value == null) {
            log.warn("Property '{}' not found", key);
        }
        return value;
    }
    
    // Get all properties for debugging
    public Properties getAllProperties() {
        Properties allProps = new Properties();
        allProps.putAll(properties);
        
        // Add system properties
        System.getProperties().forEach((key, value) -> {
            if (properties.containsKey(key)) {
                allProps.put(key, value);
            }
        });
        
        return allProps;
    }
    
    // Validate required properties
    public void validateConfiguration() {
        String[] requiredProperties = {
            "base.url"
        };
        
        for (String property : requiredProperties) {
            if (getProperty(property) == null || getProperty(property).trim().isEmpty()) {
                throw new RuntimeException("Required property '" + property + "' is missing or empty");
            }
        }
        
        log.info("Configuration validation successful");
    }
}