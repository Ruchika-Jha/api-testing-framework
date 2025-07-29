package com.testlead.automation.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Utility class for JSON operations
 * This class goes in src/main/java
 */
@Slf4j
public class JsonUtils {
    
    private static final ObjectMapper objectMapper;
    
    static {
        objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
    
    /**
     * Convert object to JSON string
     */
    public static String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("Error converting object to JSON: {}", e.getMessage());
            throw new RuntimeException("JSON serialization failed", e);
        }
    }
    
    /**
     * Convert object to pretty JSON string
     */
    public static String toPrettyJson(Object object) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("Error converting object to pretty JSON: {}", e.getMessage());
            throw new RuntimeException("JSON pretty serialization failed", e);
        }
    }
    
    /**
     * Convert JSON string to object
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            log.error("Error converting JSON to object: {}", e.getMessage());
            throw new RuntimeException("JSON deserialization failed", e);
        }
    }
    
    /**
     * Convert JSON string to object using TypeReference
     */
    public static <T> T fromJson(String json, TypeReference<T> typeReference) {
        try {
            return objectMapper.readValue(json, typeReference);
        } catch (JsonProcessingException e) {
            log.error("Error converting JSON to object using TypeReference: {}", e.getMessage());
            throw new RuntimeException("JSON deserialization failed", e);
        }
    }
    
    /**
     * Convert JSON InputStream to object
     */
    public static <T> T fromJson(InputStream inputStream, Class<T> clazz) {
        try {
            return objectMapper.readValue(inputStream, clazz);
        } catch (IOException e) {
            log.error("Error converting JSON InputStream to object: {}", e.getMessage());
            throw new RuntimeException("JSON deserialization from InputStream failed", e);
        }
    }
    
    /**
     * Convert JSON string to Map
     */
    public static Map<String, Object> toMap(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
        } catch (JsonProcessingException e) {
            log.error("Error converting JSON to Map: {}", e.getMessage());
            throw new RuntimeException("JSON to Map conversion failed", e);
        }
    }
    
    /**
     * Convert JSON string to List
     */
    public static <T> List<T> toList(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, 
                objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (JsonProcessingException e) {
            log.error("Error converting JSON to List: {}", e.getMessage());
            throw new RuntimeException("JSON to List conversion failed", e);
        }
    }
    
    /**
     * Get JsonNode from JSON string
     */
    public static JsonNode getJsonNode(String json) {
        try {
            return objectMapper.readTree(json);
        } catch (JsonProcessingException e) {
            log.error("Error parsing JSON to JsonNode: {}", e.getMessage());
            throw new RuntimeException("JSON parsing failed", e);
        }
    }
    
    /**
     * Get value from JSON path
     */
    public static Object getValueFromPath(String json, String path) {
        try {
            JsonNode rootNode = objectMapper.readTree(json);
            String[] pathParts = path.split("\\.");
            JsonNode currentNode = rootNode;
            
            for (String part : pathParts) {
                if (currentNode.has(part)) {
                    currentNode = currentNode.get(part);
                } else {
                    log.warn("Path '{}' not found in JSON", path);
                    return null;
                }
            }
            
            return currentNode.isTextual() ? currentNode.asText() : currentNode;
        } catch (JsonProcessingException e) {
            log.error("Error getting value from JSON path '{}': {}", path, e.getMessage());
            throw new RuntimeException("JSON path extraction failed", e);
        }
    }
    
    /**
     * Check if JSON string is valid
     */
    public static boolean isValidJson(String json) {
        try {
            objectMapper.readTree(json);
            return true;
        } catch (JsonProcessingException e) {
            log.debug("Invalid JSON: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Merge two JSON objects
     */
    public static String mergeJson(String json1, String json2) {
        try {
            JsonNode node1 = objectMapper.readTree(json1);
            JsonNode node2 = objectMapper.readTree(json2);
            
            JsonNode merged = merge(node1, node2);
            return objectMapper.writeValueAsString(merged);
        } catch (JsonProcessingException e) {
            log.error("Error merging JSON objects: {}", e.getMessage());
            throw new RuntimeException("JSON merge failed", e);
        }
    }
    
    /**
     * Helper method to merge JsonNode objects
     */
    private static JsonNode merge(JsonNode mainNode, JsonNode updateNode) {
        updateNode.fieldNames().forEachRemaining(fieldName -> {
            JsonNode jsonNode = updateNode.get(fieldName);
            if (mainNode instanceof com.fasterxml.jackson.databind.node.ObjectNode) {
                if (jsonNode.isObject()) {
                    JsonNode existingNode = mainNode.get(fieldName);
                    if (existingNode != null && existingNode.isObject()) {
                        merge(existingNode, jsonNode);
                    } else {
                        ((com.fasterxml.jackson.databind.node.ObjectNode) mainNode).set(fieldName, jsonNode);
                    }
                } else {
                    ((com.fasterxml.jackson.databind.node.ObjectNode) mainNode).set(fieldName, jsonNode);
                }
            }
        });
        return mainNode;
    }
    
    /**
     * Remove field from JSON
     */
    public static String removeField(String json, String fieldName) {
        try {
            JsonNode node = objectMapper.readTree(json);
            if (node instanceof com.fasterxml.jackson.databind.node.ObjectNode) {
                ((com.fasterxml.jackson.databind.node.ObjectNode) node).remove(fieldName);
            }
            return objectMapper.writeValueAsString(node);
        } catch (JsonProcessingException e) {
            log.error("Error removing field '{}' from JSON: {}", fieldName, e.getMessage());
            throw new RuntimeException("JSON field removal failed", e);
        }
    }
    
    /**
     * Update field value in JSON
     */
    public static String updateField(String json, String fieldName, Object newValue) {
        try {
            JsonNode node = objectMapper.readTree(json);
            if (node instanceof com.fasterxml.jackson.databind.node.ObjectNode) {
                JsonNode valueNode = objectMapper.valueToTree(newValue);
                ((com.fasterxml.jackson.databind.node.ObjectNode) node).set(fieldName, valueNode);
            }
            return objectMapper.writeValueAsString(node);
        } catch (JsonProcessingException e) {
            log.error("Error updating field '{}' in JSON: {}", fieldName, e.getMessage());
            throw new RuntimeException("JSON field update failed", e);
        }
    }
    
    /**
     * Get ObjectMapper instance for advanced operations
     */
    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}