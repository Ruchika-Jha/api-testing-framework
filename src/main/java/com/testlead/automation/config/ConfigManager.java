package com.testlead.automation.config;

import com.testlead.automation.utils.DataUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Configuration Manager for handling application properties
 * Singleton pattern implementation for global access
 */
public class ConfigManager {
    
    private static final Logger logger = LoggerFactory.getLogger(ConfigManager.class);
    private static ConfigManager instance;
    private Properties properties;
    private String currentEnvironment;
    
    // Private constructor for singleton pattern
    private ConfigManager() {
        loadConfiguration();
    }
    
    /**
     * Get singleton instance of ConfigManager
     */
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
    
    /**
     * Load configuration based on environment
     */
    private void loadConfiguration() {
        try {
            // Get environment from system property, default to 'qa'
            currentEnvironment = System.getProperty("env", "qa").toLowerCase();
            logger.info("Loading configuration for environment: {}", currentEnvironment);
            
            String configFile = String.format("config/%s.properties", currentEnvironment);
            
            // Load environment-specific properties
            if (DataUtils.fileExistsInClasspath(configFile)) {
                properties = DataUtils.readPropertiesFromClasspath(configFile);
                logger.info("Successfully loaded configuration from: {}", configFile);
            } else {
                logger.warn("Configuration file not found: {}, loading default qa.properties", configFile);
                properties = DataUtils.readPropertiesFromClasspath("config/qa.properties");
            }
            
            // Override with system properties if they exist
            overrideWithSystemProperties();
            
        } catch (Exception e) {
            logger.error("Error loading configuration", e);
            throw new RuntimeException("Failed to load configuration", e);
        }
    }
    
    /**
     * Override properties with system properties
     */
    private void overrideWithSystemProperties() {
        System.getProperties().forEach((key, value) -> {
            String keyStr = key.toString();
            if (keyStr.startsWith("test.") || keyStr.startsWith("api.") || keyStr.startsWith("base.")) {
                properties.setProperty(keyStr, value.toString());
                logger.debug("Overridden property {} with system property value", keyStr);
            }
        });
    }
    
    /**
     * Get property value
     */
    public String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            logger.warn("Property '{}' not found", key);
        }
        return value;
    }
    
    /**
     * Get property value with default
     */
    public String getProperty(String key, String defaultValue) {
        String value = properties.getProperty(key, defaultValue);
        if (value.equals(defaultValue)) {
            logger.debug("Using default value '{}' for property '{}'", defaultValue, key);
        }
        return value;
    }
    
    /**
     * Static method to get property - for convenience
     */
    public static String get(String key) {
        return getInstance().getProperty(key);
    }
    
    /**
     * Static method to get property with default - for convenience
     */
    public static String get(String key, String defaultValue) {
        return getInstance().getProperty(key, defaultValue);
    }
    
    /**
     * Get property as integer
     */
    public int getIntProperty(String key, int defaultValue) {
        try {
            String value = getProperty(key);
            return value != null ? Integer.parseInt(value) : defaultValue;
        } catch (NumberFormatException e) {
            logger.warn("Invalid integer value for property '{}', using default: {}", key, defaultValue);
            return defaultValue;
        }
    }
    
    /**
     * Static method to get integer property
     */
    public static int getInt(String key, int defaultValue) {
        return getInstance().getIntProperty(key, defaultValue);
    }
    
    /**
     * Get property as boolean
     */
    public boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = getProperty(key);
        if (value != null) {
            return Boolean.parseBoolean(value.trim().toLowerCase());
        }
        return defaultValue;
    }
    
    /**
     * Static method to get boolean property
     */
    public static boolean getBoolean(String key, boolean defaultValue) {
        return getInstance().getBooleanProperty(key, defaultValue);
    }
    
    /**
     * Get property as long
     */
    public long getLongProperty(String key, long defaultValue) {
        try {
            String value = getProperty(key);
            return value != null ? Long.parseLong(value) : defaultValue;
        } catch (NumberFormatException e) {
            logger.warn("Invalid long value for property '{}', using default: {}", key, defaultValue);
            return defaultValue;
        }
    }
    
    /**
     * Static method to get long property
     */
    public static long getLong(String key, long defaultValue) {
        return getInstance().getLongProperty(key, defaultValue);
    }
    
    /**
     * Get property as double
     */
    public double getDoubleProperty(String key, double defaultValue) {
        try {
            String value = getProperty(key);
            return value != null ? Double.parseDouble(value) : defaultValue;
        } catch (NumberFormatException e) {
            logger.warn("Invalid double value for property '{}', using default: {}", key, defaultValue);
            return defaultValue;
        }
    }
    
    /**
     * Static method to get double property
     */
    public static double getDouble(String key, double defaultValue) {
        return getInstance().getDoubleProperty(key, defaultValue);
    }
    
    /**
     * Set property value
     */
    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
        logger.debug("Set property '{}' to '{}'", key, value);
    }
    
    /**
     * Static method to set property
     */
    public static void set(String key, String value) {
        getInstance().setProperty(key, value);
    }
    
    /**
     * Get current environment
     */
    public String getCurrentEnvironment() {
        return currentEnvironment;
    }
    
    /**
     * Static method to get current environment
     */
    public static String getEnvironment() {
        return getInstance().getCurrentEnvironment();
    }
    
    /**
     * Check if property exists
     */
    public boolean hasProperty(String key) {
        return properties.containsKey(key);
    }
    
    /**
     * Static method to check if property exists
     */
    public static boolean has(String key) {
        return getInstance().hasProperty(key);
    }
    
    /**
     * Get all properties
     */
    public Properties getAllProperties() {
        return new Properties(properties);
    }
    
    /**
     * Reload configuration
     */
    public void reload() {
        logger.info("Reloading configuration...");
        loadConfiguration();
    }
    
    /**
     * Static method to reload configuration
     */
    public static void reloadConfig() {
        getInstance().reload();
    }
    
    /**
     * Get base URL for API
     */
    public String getBaseUrl() {
        return getProperty("base.url", "http://localhost:8080");
    }
    
    /**
     * Static method to get base URL
     */
    public static String baseUrl() {
        return getInstance().getBaseUrl();
    }
    
    /**
     * Get API timeout
     */
    public int getApiTimeout() {
        return getIntProperty("api.timeout", 30000);
    }
    
    /**
     * Static method to get API timeout
     */
    public static int apiTimeout() {
        return getInstance().getApiTimeout();
    }
    
    /**
     * Get retry count
     */
    public int getRetryCount() {
        return getIntProperty("api.retry.count", 3);
    }
    
    /**
     * Static method to get retry count
     */
    public static int retryCount() {
        return getInstance().getRetryCount();
    }
    
    /**
     * Get database URL
     */
    public String getDatabaseUrl() {
        return getProperty("db.url");
    }
    
    /**
     * Static method to get database URL
     */
    public static String databaseUrl() {
        return getInstance().getDatabaseUrl();
    }
    
    /**
     * Get database username
     */
    public String getDatabaseUsername() {
        return getProperty("db.username");
    }
    
    /**
     * Static method to get database username
     */
    public static String databaseUsername() {
        return getInstance().getDatabaseUsername();
    }
    
    /**
     * Get database password
     */
    public String getDatabasePassword() {
        return getProperty("db.password");
    }
    
    /**
     * Static method to get database password
     */
    public static String databasePassword() {
        return getInstance().getDatabasePassword();
    }
    
    /**
     * Print all configuration properties (for debugging)
     */
    public void printAllProperties() {
        logger.info("=== Configuration Properties ===");
        properties.forEach((key, value) -> {
            // Hide sensitive information
            String displayValue = key.toString().toLowerCase().contains("password") 
                || key.toString().toLowerCase().contains("secret")
                || key.toString().toLowerCase().contains("token") 
                ? "****" : value.toString();
            logger.info("{} = {}", key, displayValue);
        });
        logger.info("=== End Configuration Properties ===");
    }
    
    /**
     * Static method to print all properties
     */
    public static void printConfig() {
        getInstance().printAllProperties();
    }
    
    /**
     * Check if current environment is production
     */
    public boolean isProductionEnvironment() {
        return "prod".equalsIgnoreCase(currentEnvironment) || 
               "production".equalsIgnoreCase(currentEnvironment);
    }
    
    /**
     * Static method to check if current environment is production
     */
    public static boolean isProd() {
        return getInstance().isProductionEnvironment();
    }
    
    /**
     * Check if current environment is development
     */
    public boolean isDevelopmentEnvironment() {
        return "dev".equalsIgnoreCase(currentEnvironment) || 
               "development".equalsIgnoreCase(currentEnvironment);
    }
    
    /**
     * Static method to check if current environment is development
     */
    public static boolean isDev() {
        return getInstance().isDevelopmentEnvironment();
    }
    
    /**
     * Check if current environment is QA/test
     */
    public boolean isQaEnvironment() {
        return "qa".equalsIgnoreCase(currentEnvironment) || 
               "test".equalsIgnoreCase(currentEnvironment);
    }
    
    /**
     * Static method to check if current environment is QA/test
     */
    public static boolean isQa() {
        return getInstance().isQaEnvironment();
    }
    
    /**
     * Check if current environment is staging
     */
    public boolean isStagingEnvironment() {
        return "staging".equalsIgnoreCase(currentEnvironment) || 
               "stage".equalsIgnoreCase(currentEnvironment);
    }
    
    /**
     * Static method to check if current environment is staging
     */
    public static boolean isStaging() {
        return getInstance().isStagingEnvironment();
    }
    
    /**
     * Get API version
     */
    public String getApiVersion() {
        return getProperty("api.version", "v1");
    }
    
    /**
     * Static method to get API version
     */
    public static String apiVersion() {
        return getInstance().getApiVersion();
    }
    
    /**
     * Get request timeout in milliseconds
     */
    public int getRequestTimeout() {
        return getIntProperty("request.timeout", 30000);
    }
    
    /**
     * Static method to get request timeout
     */
    public static int requestTimeout() {
        return getInstance().getRequestTimeout();
    }
    
    /**
     * Get connection timeout in milliseconds
     */
    public int getConnectionTimeout() {
        return getIntProperty("connection.timeout", 10000);
    }
    
    /**
     * Static method to get connection timeout
     */
    public static int connectionTimeout() {
        return getInstance().getConnectionTimeout();
    }
    
    /**
     * Check if SSL verification is enabled
     */
    public boolean isSslVerificationEnabled() {
        return getBooleanProperty("ssl.verification.enabled", true);
    }
    
    /**
     * Static method to check if SSL verification is enabled
     */
    public static boolean sslVerificationEnabled() {
        return getInstance().isSslVerificationEnabled();
    }
    
    /**
     * Check if debug mode is enabled
     */
    public boolean isDebugMode() {
        return getBooleanProperty("debug.enabled", false);
    }
    
    /**
     * Static method to check if debug mode is enabled
     */
    public static boolean debugMode() {
        return getInstance().isDebugMode();
    }
    
    /**
     * Get parallel execution thread count
     */
    public int getParallelThreadCount() {
        return getIntProperty("parallel.thread.count", 5);
    }
    
    /**
     * Static method to get parallel thread count
     */
    public static int parallelThreadCount() {
        return getInstance().getParallelThreadCount();
    }
    
    /**
     * Get test data directory path
     */
    public String getTestDataDirectory() {
        return getProperty("testdata.directory", "testdata");
    }
    
    /**
     * Static method to get test data directory
     */
    public static String testDataDirectory() {
        return getInstance().getTestDataDirectory();
    }
    
    /**
     * Get reports directory path
     */
    public String getReportsDirectory() {
        return getProperty("reports.directory", "reports");
    }
    
    /**
     * Static method to get reports directory
     */
    public static String reportsDirectory() {
        return getInstance().getReportsDirectory();
    }
    
    /**
     * Get logs directory path
     */
    public String getLogsDirectory() {
        return getProperty("logs.directory", "logs");
    }
    
    /**
     * Static method to get logs directory
     */
    public static String logsDirectory() {
        return getInstance().getLogsDirectory();
    }
    
    /**
     * Get authentication token
     */
    public String getAuthToken() {
        return getProperty("auth.token");
    }
    
    /**
     * Static method to get authentication token
     */
    public static String authToken() {
        return getInstance().getAuthToken();
    }
    
    /**
     * Get authentication username
     */
    public String getAuthUsername() {
        return getProperty("auth.username");
    }
    
    /**
     * Static method to get authentication username
     */
    public static String authUsername() {
        return getInstance().getAuthUsername();
    }
    
    /**
     * Get authentication password
     */
    public String getAuthPassword() {
        return getProperty("auth.password");
    }
    
    /**
     * Static method to get authentication password
     */
    public static String authPassword() {
        return getInstance().getAuthPassword();
    }
    
    /**
     * Validate configuration - check for required properties
     */
    public void validateConfiguration() {
        logger.info("Validating configuration for environment: {}", currentEnvironment);
        
        List<String> missingProperties = new ArrayList<>();
        List<String> requiredProperties = getRequiredProperties();
        
        for (String property : requiredProperties) {
            if (!hasProperty(property) || getProperty(property) == null || getProperty(property).trim().isEmpty()) {
                missingProperties.add(property);
            }
        }
        
        if (!missingProperties.isEmpty()) {
            String errorMessage = String.format(
                "Configuration validation failed! Missing required properties: %s", 
                String.join(", ", missingProperties)
            );
            logger.error(errorMessage);
            throw new RuntimeException(errorMessage);
        }
        
        // Validate property formats
        validatePropertyFormats();
        
        logger.info("Configuration validation passed successfully");
    }
    
    /**
     * Static method to validate configuration
     */
    public static void validate() {
        getInstance().validateConfiguration();
    }
    
    /**
     * Get list of required properties based on environment
     */
    private List<String> getRequiredProperties() {
        List<String> required = new ArrayList<>();
        
        // Common required properties
        required.add("base.url");
        
        // Environment-specific required properties
        if (isProductionEnvironment()) {
            required.add("auth.token");
            // Add more production-specific required properties
        } else if (isDevelopmentEnvironment()) {
            // Add development-specific required properties
        } else {
            // QA/default required properties
        }
        
        return required;
    }
    
    /**
     * Validate property formats (URLs, numbers, etc.)
     */
    private void validatePropertyFormats() {
        // Validate base URL format
        String baseUrl = getProperty("base.url");
        if (baseUrl != null && !isValidUrl(baseUrl)) {
            throw new RuntimeException("Invalid base.url format: " + baseUrl);
        }
        
        // Validate timeout values are positive numbers
        validatePositiveInteger("api.timeout");
        validatePositiveInteger("request.timeout");
        validatePositiveInteger("connection.timeout");
        validatePositiveInteger("parallel.thread.count");
        validatePositiveInteger("api.retry.count");
        
        // Validate boolean properties
        validateBooleanProperty("debug.enabled");
        validateBooleanProperty("ssl.verification.enabled");
        
        logger.debug("Property format validation completed");
    }
    
    /**
     * Validate that a property is a positive integer
     */
    private void validatePositiveInteger(String propertyKey) {
        String value = getProperty(propertyKey);
        if (value != null) {
            try {
                int intValue = Integer.parseInt(value);
                if (intValue <= 0) {
                    throw new RuntimeException(String.format(
                        "Property '%s' must be a positive integer, got: %s", propertyKey, value));
                }
            } catch (NumberFormatException e) {
                throw new RuntimeException(String.format(
                    "Property '%s' must be a valid integer, got: %s", propertyKey, value));
            }
        }
    }
    
    /**
     * Validate that a property is a valid boolean
     */
    private void validateBooleanProperty(String propertyKey) {
        String value = getProperty(propertyKey);
        if (value != null) {
            String lowerValue = value.toLowerCase().trim();
            if (!lowerValue.equals("true") && !lowerValue.equals("false")) {
                throw new RuntimeException(String.format(
                    "Property '%s' must be 'true' or 'false', got: %s", propertyKey, value));
            }
        }
    }
    
    /**
     * Check if URL is valid
     */
    private boolean isValidUrl(String url) {
        try {
            new java.net.URL(url);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Validate specific property exists and is not empty
     */
    public boolean validateProperty(String key) {
        String value = getProperty(key);
        boolean isValid = value != null && !value.trim().isEmpty();
        if (!isValid) {
            logger.warn("Property validation failed for key: {}", key);
        }
        return isValid;
    }
    
    /**
     * Static method to validate specific property
     */
    public static boolean isPropertyValid(String key) {
        return getInstance().validateProperty(key);
    }
    
    /**
     * Validate that required properties for authentication are present
     */
    public void validateAuthConfiguration() {
        logger.info("Validating authentication configuration");
        
        List<String> missingAuthProps = new ArrayList<>();
        
        // Check for either token-based or username/password auth
        boolean hasToken = hasProperty("auth.token") && !getProperty("auth.token").trim().isEmpty();
        boolean hasCredentials = hasProperty("auth.username") && !getProperty("auth.username").trim().isEmpty()
                               && hasProperty("auth.password") && !getProperty("auth.password").trim().isEmpty();
        
        if (!hasToken && !hasCredentials) {
            throw new RuntimeException(
                "Authentication configuration missing! Please provide either 'auth.token' or both 'auth.username' and 'auth.password'");
        }
        
        logger.info("Authentication configuration validation passed");
    }
    
    /**
     * Static method to validate authentication configuration
     */
    public static void validateAuth() {
        getInstance().validateAuthConfiguration();
    }
    
    /**
     * Validate database configuration if database properties are present
     */
    public void validateDatabaseConfiguration() {
        if (hasProperty("db.url")) {
            logger.info("Validating database configuration");
            
            List<String> missingDbProps = new ArrayList<>();
            
            if (!validateProperty("db.url")) {
                missingDbProps.add("db.url");
            }
            if (!validateProperty("db.username")) {
                missingDbProps.add("db.username");
            }
            if (!validateProperty("db.password")) {
                missingDbProps.add("db.password");
            }
            
            if (!missingDbProps.isEmpty()) {
                throw new RuntimeException(
                    "Database configuration incomplete! Missing properties: " + String.join(", ", missingDbProps));
            }
            
            // Validate database URL format
            String dbUrl = getProperty("db.url");
            if (!dbUrl.startsWith("jdbc:")) {
                throw new RuntimeException("Invalid database URL format. Must start with 'jdbc:': " + dbUrl);
            }
            
            logger.info("Database configuration validation passed");
        }
    }
    
    /**
     * Static method to validate database configuration
     */
    public static void validateDatabase() {
        getInstance().validateDatabaseConfiguration();
    }
    
    /**
     * Get configuration summary for logging
     */
    public String getConfigurationSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("=== Configuration Summary ===\n");
        summary.append("Environment: ").append(currentEnvironment).append("\n");
        summary.append("Base URL: ").append(getProperty("base.url", "Not set")).append("\n");
        summary.append("API Timeout: ").append(getApiTimeout()).append("ms\n");
        summary.append("Debug Mode: ").append(isDebugMode()).append("\n");
        summary.append("SSL Verification: ").append(isSslVerificationEnabled()).append("\n");
        summary.append("Parallel Threads: ").append(getParallelThreadCount()).append("\n");
        summary.append("=== End Summary ===");
        return summary.toString();
    }
    
    /**
     * Static method to get configuration summary
     */
    public static String summary() {
        return getInstance().getConfigurationSummary();
    }
    
    /**
     * Log configuration summary
     */
    public void logConfigurationSummary() {
        logger.info(getConfigurationSummary());
    }
    
    /**
     * Static method to log configuration summary
     */
    public static void logSummary() {
        getInstance().logConfigurationSummary();
    }
    
    /**
     * Check if request/response logging is enabled
     */
    public boolean isRequestLoggingEnabled() {
        return getBooleanProperty("logging.enabled", true);
    }
    
    /**
     * Static method to check if logging is enabled
     */
    public static boolean isLoggingEnabled() {
        return getInstance().isRequestLoggingEnabled();
    }
    
    /**
     * Enable/disable request/response logging
     */
    public void setRequestLoggingEnabled(boolean enabled) {
        setProperty("logging.enabled", String.valueOf(enabled));
        logger.info("Request/Response logging {}", enabled ? "enabled" : "disabled");
    }
    
    /**
     * Static method to set logging enabled
     */
    public static void setLoggingEnabled(boolean enabled) {
        getInstance().setRequestLoggingEnabled(enabled);
    }
    
    /**
     * Check if verbose logging is enabled
     */
    public boolean isVerboseLoggingEnabled() {
        return getBooleanProperty("logging.verbose", false);
    }
    
    /**
     * Static method to check if verbose logging is enabled
     */
    public static boolean verboseLogging() {
        return getInstance().isVerboseLoggingEnabled();
    }
    
    /**
     * Enable/disable verbose logging
     */
    public void setVerboseLoggingEnabled(boolean enabled) {
        setProperty("logging.verbose", String.valueOf(enabled));
        logger.info("Verbose logging {}", enabled ? "enabled" : "disabled");
    }
    
    /**
     * Static method to set verbose logging
     */
    public static void setVerboseLogging(boolean enabled) {
        getInstance().setVerboseLoggingEnabled(enabled);
    }
    
    /**
     * Get log level
     */
    public String getLogLevel() {
        return getProperty("log.level", "INFO");
    }
    
    /**
     * Static method to get log level
     */
    public static String logLevel() {
        return getInstance().getLogLevel();
    }
    
    /**
     * Set log level
     */
    public void setLogLevel(String level) {
        setProperty("log.level", level.toUpperCase());
        logger.info("Log level set to: {}", level);
    }
    
    /**
     * Static method to set log level
     */
    public static void setLevel(String level) {
        getInstance().setLogLevel(level);
    }
}