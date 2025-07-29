package com.testlead.automation.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Product model class representing a product entity
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {
    
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("description")
    private String description;
    
    @JsonProperty("price")
    private BigDecimal price;
    
    @JsonProperty("category")
    private String category;
    
    @JsonProperty("sku")
    private String sku;
    
    @JsonProperty("quantity")
    private Integer quantity;
    
    @JsonProperty("isActive")
    private Boolean isActive;
    
    @JsonProperty("tags")
    private String[] tags;
    
    @JsonProperty("createdAt")
    private String createdAt;
    
    @JsonProperty("updatedAt")
    private String updatedAt;
    
    @JsonProperty("manufacturer")
    private String manufacturer;
    
    @JsonProperty("weight")
    private Double weight;
    
    @JsonProperty("dimensions")
    private Dimensions dimensions;
    
    // Default constructor
    public Product() {
        this.isActive = true;
        this.quantity = 0;
    }
    
    // Constructor with required fields
    public Product(String name, String description, BigDecimal price, String category) {
        this();
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public String getSku() {
        return sku;
    }
    
    public void setSku(String sku) {
        this.sku = sku;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
    public String[] getTags() {
        return tags;
    }
    
    public void setTags(String[] tags) {
        this.tags = tags;
    }
    
    public String getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public String getManufacturer() {
        return manufacturer;
    }
    
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }
    
    public Double getWeight() {
        return weight;
    }
    
    public void setWeight(Double weight) {
        this.weight = weight;
    }
    
    public Dimensions getDimensions() {
        return dimensions;
    }
    
    public void setDimensions(Dimensions dimensions) {
        this.dimensions = dimensions;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id) &&
               Objects.equals(name, product.name) &&
               Objects.equals(sku, product.sku);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, name, sku);
    }
    
    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", category='" + category + '\'' +
                ", sku='" + sku + '\'' +
                ", quantity=" + quantity +
                ", isActive=" + isActive +
                ", manufacturer='" + manufacturer + '\'' +
                ", weight=" + weight +
                '}';
    }
    
    /**
     * Nested class for product dimensions
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Dimensions {
        @JsonProperty("length")
        private Double length;
        
        @JsonProperty("width")
        private Double width;
        
        @JsonProperty("height")
        private Double height;
        
        @JsonProperty("unit")
        private String unit;
        
        public Dimensions() {
            this.unit = "cm";
        }
        
        public Dimensions(Double length, Double width, Double height, String unit) {
            this.length = length;
            this.width = width;
            this.height = height;
            this.unit = unit;
        }
        
        // Getters and Setters
        public Double getLength() {
            return length;
        }
        
        public void setLength(Double length) {
            this.length = length;
        }
        
        public Double getWidth() {
            return width;
        }
        
        public void setWidth(Double width) {
            this.width = width;
        }
        
        public Double getHeight() {
            return height;
        }
        
        public void setHeight(Double height) {
            this.height = height;
        }
        
        public String getUnit() {
            return unit;
        }
        
        public void setUnit(String unit) {
            this.unit = unit;
        }
        
        @Override
        public String toString() {
            return "Dimensions{" +
                    "length=" + length +
                    ", width=" + width +
                    ", height=" + height +
                    ", unit='" + unit + '\'' +
                    '}';
        }
    }
}