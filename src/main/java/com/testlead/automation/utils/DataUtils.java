package com.testlead.automation.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
// Removed Commons IO dependency - using built-in Java APIs
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

/**
 * Utility class for handling data operations like file reading, writing, and data manipulation
 */
public class DataUtils {
    
    private static final Logger logger = LoggerFactory.getLogger(DataUtils.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * Read file content as string from classpath
     */
    public static String readFileFromClasspath(String filePath) {
        try (InputStream inputStream = DataUtils.class.getClassLoader().getResourceAsStream(filePath)) {
            if (inputStream == null) {
                throw new FileNotFoundException("File not found in classpath: " + filePath);
            }
            // Using built-in Java method instead of Commons IO
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            logger.error("Error reading file from classpath: {}", filePath, e);
            throw new RuntimeException("Failed to read file: " + filePath, e);
        }
    }
    
    /**
     * Read file content as string from file system
     */
    public static String readFileFromPath(String filePath) {
        try {
            Path path = Paths.get(filePath);
            byte[] bytes = Files.readAllBytes(path);
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            logger.error("Error reading file from path: {}", filePath, e);
            throw new RuntimeException("Failed to read file: " + filePath, e);
        }
    }
    
    /**
     * Write string content to file
     */
    public static void writeStringToFile(String content, String filePath) {
        try {
            Path path = Paths.get(filePath);
            // Create directories if they don't exist
            Files.createDirectories(path.getParent());
            byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
            Files.write(path, bytes, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            logger.info("Successfully wrote content to file: {}", filePath);
        } catch (IOException e) {
            logger.error("Error writing to file: {}", filePath, e);
            throw new RuntimeException("Failed to write to file: " + filePath, e);
        }
    }
    
    /**
     * Append string content to file
     */
    public static void appendStringToFile(String content, String filePath) {
        try {
            Path path = Paths.get(filePath);
            Files.createDirectories(path.getParent());
            byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
            Files.write(path, bytes, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            logger.info("Successfully appended content to file: {}", filePath);
        } catch (IOException e) {
            logger.error("Error appending to file: {}", filePath, e);
            throw new RuntimeException("Failed to append to file: " + filePath, e);
        }
    }
    
    /**
     * Read JSON file and return as JsonNode
     */
    public static JsonNode readJsonFromClasspath(String filePath) {
        try {
            String jsonContent = readFileFromClasspath(filePath);
            return objectMapper.readTree(jsonContent);
        } catch (Exception e) {
            logger.error("Error reading JSON from classpath: {}", filePath, e);
            throw new RuntimeException("Failed to read JSON file: " + filePath, e);
        }
    }
    
    /**
     * Read JSON file from file system and return as JsonNode
     */
    public static JsonNode readJsonFromPath(String filePath) {
        try {
            String jsonContent = readFileFromPath(filePath);
            return objectMapper.readTree(jsonContent);
        } catch (Exception e) {
            logger.error("Error reading JSON from path: {}", filePath, e);
            throw new RuntimeException("Failed to read JSON file: " + filePath, e);
        }
    }
    
    /**
     * Convert object to JSON string
     */
    public static String objectToJson(Object object) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (Exception e) {
            logger.error("Error converting object to JSON", e);
            throw new RuntimeException("Failed to convert object to JSON", e);
        }
    }
    
    /**
     * Convert JSON string to object
     */
    public static <T> T jsonToObject(String jsonString, Class<T> clazz) {
        try {
            return objectMapper.readValue(jsonString, clazz);
        } catch (Exception e) {
            logger.error("Error converting JSON to object", e);
            throw new RuntimeException("Failed to convert JSON to object", e);
        }
    }
    
    /**
     * Read properties file from classpath
     */
    public static Properties readPropertiesFromClasspath(String filePath) {
        Properties properties = new Properties();
        try (InputStream inputStream = DataUtils.class.getClassLoader().getResourceAsStream(filePath)) {
            if (inputStream == null) {
                throw new FileNotFoundException("Properties file not found in classpath: " + filePath);
            }
            properties.load(inputStream);
            return properties;
        } catch (IOException e) {
            logger.error("Error reading properties from classpath: {}", filePath, e);
            throw new RuntimeException("Failed to read properties file: " + filePath, e);
        }
    }
    
    /**
     * Read properties file from file system
     */
    public static Properties readPropertiesFromPath(String filePath) {
        Properties properties = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
            properties.load(fileInputStream);
            return properties;
        } catch (IOException e) {
            logger.error("Error reading properties from path: {}", filePath, e);
            throw new RuntimeException("Failed to read properties file: " + filePath, e);
        }
    }
    
    /**
     * Write properties to file
     */
    public static void writePropertiesToFile(Properties properties, String filePath, String comments) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {
            properties.store(fileOutputStream, comments);
            logger.info("Successfully wrote properties to file: {}", filePath);
        } catch (IOException e) {
            logger.error("Error writing properties to file: {}", filePath, e);
            throw new RuntimeException("Failed to write properties file: " + filePath, e);
        }
    }
    
    /**
     * Read CSV file and return as List of Maps
     */
    public static List<Map<String, String>> readCsvFromClasspath(String filePath) {
        try {
            String csvContent = readFileFromClasspath(filePath);
            return parseCsvContent(csvContent);
        } catch (Exception e) {
            logger.error("Error reading CSV from classpath: {}", filePath, e);
            throw new RuntimeException("Failed to read CSV file: " + filePath, e);
        }
    }
    
    /**
     * Read CSV file from file system and return as List of Maps
     */
    public static List<Map<String, String>> readCsvFromPath(String filePath) {
        try {
            String csvContent = readFileFromPath(filePath);
            return parseCsvContent(csvContent);
        } catch (Exception e) {
            logger.error("Error reading CSV from path: {}", filePath, e);
            throw new RuntimeException("Failed to read CSV file: " + filePath, e);
        }
    }
    
    /**
     * Parse CSV content and return as List of Maps
     */
    private static List<Map<String, String>> parseCsvContent(String csvContent) {
        List<Map<String, String>> result = new ArrayList<>();
        String[] lines = csvContent.split("\n");
        
        if (lines.length == 0) {
            return result;
        }
        
        // Get headers from first line
        String[] headers = lines[0].split(",");
        for (int i = 0; i < headers.length; i++) {
            headers[i] = headers[i].trim().replace("\"", "");
        }
        
        // Parse data rows
        for (int i = 1; i < lines.length; i++) {
            String[] values = lines[i].split(",");
            Map<String, String> row = new HashMap<>();
            
            for (int j = 0; j < headers.length && j < values.length; j++) {
                row.put(headers[j], values[j].trim().replace("\"", ""));
            }
            result.add(row);
        }
        
        return result;
    }
    
    /**
     * Write CSV data to file
     */
    public static void writeCsvToFile(List<Map<String, String>> data, String filePath) {
        if (data.isEmpty()) {
            logger.warn("No data to write to CSV file: {}", filePath);
            return;
        }
        
        try {
            StringBuilder csvContent = new StringBuilder();
            
            // Write headers
            Set<String> headers = data.get(0).keySet();
            csvContent.append(String.join(",", headers)).append("\n");
            
            // Write data rows
            for (Map<String, String> row : data) {
                List<String> values = new ArrayList<>();
                for (String header : headers) {
                    values.add("\"" + row.getOrDefault(header, "") + "\"");
                }
                csvContent.append(String.join(",", values)).append("\n");
            }
            
            writeStringToFile(csvContent.toString(), filePath);
        } catch (Exception e) {
            logger.error("Error writing CSV to file: {}", filePath, e);
            throw new RuntimeException("Failed to write CSV file: " + filePath, e);
        }
    }
    
    /**
     * Check if file exists in classpath
     */
    public static boolean fileExistsInClasspath(String filePath) {
        try (InputStream inputStream = DataUtils.class.getClassLoader().getResourceAsStream(filePath)) {
            return inputStream != null;
        } catch (IOException e) {
            return false;
        }
    }
    
    /**
     * Check if file exists in file system
     */
    public static boolean fileExistsInPath(String filePath) {
        return Files.exists(Paths.get(filePath));
    }
    
    /**
     * Create directory if it doesn't exist
     */
    public static void createDirectoryIfNotExists(String directoryPath) {
        try {
            Path path = Paths.get(directoryPath);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
                logger.info("Created directory: {}", directoryPath);
            }
        } catch (IOException e) {
            logger.error("Error creating directory: {}", directoryPath, e);
            throw new RuntimeException("Failed to create directory: " + directoryPath, e);
        }
    }
    
    /**
     * Delete file if it exists
     */
    public static boolean deleteFileIfExists(String filePath) {
        try {
            Path path = Paths.get(filePath);
            boolean deleted = Files.deleteIfExists(path);
            if (deleted) {
                logger.info("Deleted file: {}", filePath);
            }
            return deleted;
        } catch (IOException e) {
            logger.error("Error deleting file: {}", filePath, e);
            return false;
        }
    }
    
    /**
     * Get file extension
     */
    public static String getFileExtension(String filePath) {
        int lastDotIndex = filePath.lastIndexOf('.');
        if (lastDotIndex > 0) {
            return filePath.substring(lastDotIndex + 1);
        }
        return "";
    }
    
    /**
     * Replace placeholders in string with values from map
     */
    public static String replacePlaceholders(String template, Map<String, String> replacements) {
        String result = template;
        for (Map.Entry<String, String> entry : replacements.entrySet()) {
            String placeholder = "${" + entry.getKey() + "}";
            result = result.replace(placeholder, entry.getValue());
        }
        return result;
    }
    
    /**
     * Generate random string
     */
    public static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder result = new StringBuilder();
        
        for (int i = 0; i < length; i++) {
            result.append(characters.charAt(random.nextInt(characters.length())));
        }
        
        return result.toString();
    }
    
    /**
     * Generate timestamp string
     */
    public static String generateTimestamp() {
        return String.valueOf(System.currentTimeMillis());
    }
    
    /**
     * Get current date time string
     */
    public static String getCurrentDateTime() {
        return java.time.LocalDateTime.now().toString();
    }
}