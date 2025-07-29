package com.testlead.automation.testdata;

import com.testlead.automation.models.Product;
import com.testlead.automation.utils.DataFaker;
import com.testlead.automation.utils.DataUtils;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * Test data provider for Product-related test scenarios
 * COMPLETELY ERROR-FREE VERSION - NO BUILDERS
 */
public class ProductTestData {
    
    private static final Logger logger = LoggerFactory.getLogger(ProductTestData.class);
    private static final DataFaker dataFaker = DataFaker.getInstance();
    
    // Predefined test products for consistent testing
    private static final Map<String, Product> PREDEFINED_PRODUCTS = new HashMap<>();
    
    static {
        initializePredefinedProducts();
    }
    
    /**
     * Initialize predefined test products
     */
    private static void initializePredefinedProducts() {
        // Valid electronics product
        Product electronicsProduct = new Product();
        electronicsProduct.setId(1L);
        electronicsProduct.setName("Samsung Galaxy Smartphone");
        electronicsProduct.setDescription("Latest Samsung Galaxy smartphone with advanced features");
        electronicsProduct.setPrice(new BigDecimal("699.99"));
        electronicsProduct.setCategory("Electronics");
        electronicsProduct.setSku("ELEC-SAMSUNG-001");
        electronicsProduct.setQuantity(50);
        electronicsProduct.setIsActive(true);
        electronicsProduct.setTags(new String[]{"smartphone", "electronics", "samsung"});
        PREDEFINED_PRODUCTS.put("electronics_product", electronicsProduct);
        
        // Valid book product
        Product bookProduct = new Product();
        bookProduct.setId(2L);
        bookProduct.setName("Java Programming Guide");
        bookProduct.setDescription("Comprehensive guide to Java programming");
        bookProduct.setPrice(new BigDecimal("49.99"));
        bookProduct.setCategory("Books");
        bookProduct.setSku("BOOK-JAVA-001");
        bookProduct.setQuantity(100);
        bookProduct.setIsActive(true);
        bookProduct.setTags(new String[]{"book", "programming", "java"});
        PREDEFINED_PRODUCTS.put("book_product", bookProduct);
        
        // Valid clothing product
        Product clothingProduct = new Product();
        clothingProduct.setId(3L);
        clothingProduct.setName("Cotton T-Shirt");
        clothingProduct.setDescription("100% cotton comfortable t-shirt");
        clothingProduct.setPrice(new BigDecimal("19.99"));
        clothingProduct.setCategory("Clothing");
        clothingProduct.setSku("CLOTH-TSHIRT-001");
        clothingProduct.setQuantity(200);
        clothingProduct.setIsActive(true);
        clothingProduct.setTags(new String[]{"clothing", "cotton", "tshirt"});
        PREDEFINED_PRODUCTS.put("clothing_product", clothingProduct);
        
        // Expensive product
        Product expensiveProduct = new Product();
        expensiveProduct.setId(4L);
        expensiveProduct.setName("Premium Laptop");
        expensiveProduct.setDescription("High-end laptop for professionals");
        expensiveProduct.setPrice(new BigDecimal("2999.99"));
        expensiveProduct.setCategory("Electronics");
        expensiveProduct.setSku("ELEC-LAPTOP-PREM");
        expensiveProduct.setQuantity(10);
        expensiveProduct.setIsActive(true);
        expensiveProduct.setTags(new String[]{"laptop", "premium", "electronics"});
        PREDEFINED_PRODUCTS.put("expensive_product", expensiveProduct);
    }
    
    /**
     * Get predefined product by type
     */
    public static Product getPredefinedProduct(String productType) {
        Product product = PREDEFINED_PRODUCTS.get(productType);
        if (product == null) {
            logger.warn("Predefined product type '{}' not found, available types: {}", 
                productType, PREDEFINED_PRODUCTS.keySet());
            return getValidProduct();
        }
        
        // Create a copy to avoid modification
        Product copy = new Product();
        copy.setId(product.getId());
        copy.setName(product.getName());
        copy.setDescription(product.getDescription());
        copy.setPrice(product.getPrice());
        copy.setCategory(product.getCategory());
        copy.setSku(product.getSku());
        copy.setQuantity(product.getQuantity());
        copy.setIsActive(product.getIsActive());
        if (product.getTags() != null) {
            copy.setTags(Arrays.copyOf(product.getTags(), product.getTags().length));
        }
        
        return copy;
    }
    
    /**
     * Get all predefined product types
     */
    public static Set<String> getPredefinedProductTypes() {
        return new HashSet<>(PREDEFINED_PRODUCTS.keySet());
    }
    
    /**
     * Generate a valid product with random data
     */
    public static Product getValidProduct() {
        Product product = new Product();
        product.setName(dataFaker.getProductName());
        product.setDescription(dataFaker.getDescription());
        product.setPrice(new BigDecimal(dataFaker.getPrice()).setScale(2, RoundingMode.HALF_UP));
        product.setCategory(dataFaker.getCategory());
        product.setSku(generateSKU());
        product.setQuantity(dataFaker.getRandomNumber(1, 1000));
        product.setIsActive(true);
        product.setTags(generateTags());
        
        return product;
    }
    
    /**
     * Generate random SKU
     */
    private static String generateSKU() {
        return "SKU-" + dataFaker.getRandomAlphanumeric(8).toUpperCase();
    }
    
    /**
     * Generate random tags
     */
    private static String[] generateTags() {
        String[] possibleTags = {"new", "sale", "popular", "featured", "limited", "bestseller"};
        int tagCount = dataFaker.getRandomNumber(1, 4);
        
        Set<String> selectedTags = new HashSet<>();
        for (int i = 0; i < tagCount; i++) {
            String randomTag = possibleTags[dataFaker.getRandomNumber(0, possibleTags.length - 1)];
            selectedTags.add(randomTag);
        }
        
        return selectedTags.toArray(new String[0]);
    }
    
    /**
     * Get multiple valid products for bulk operations
     */
    public static List<Product> getMultipleValidProducts(int count) {
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Product product = getValidProduct();
            product.setName(product.getName() + " " + (i + 1));
            product.setSku(product.getSku() + "-" + (i + 1));
            products.add(product);
        }
        return products;
    }
    
    /**
     * Get product with specific category
     */
    public static Product getProductWithCategory(String category) {
        Product product = getValidProduct();
        product.setCategory(category);
        product.setName(category + " Product");
        return product;
    }
    
    /**
     * Get invalid product for error testing
     */
    public static Product getInvalidProduct() {
        Product product = new Product();
        product.setName(""); // Empty name - should be invalid
        product.setDescription("");
        product.setPrice(null); // Null price - should be invalid
        product.setCategory("");
        product.setSku("");
        product.setQuantity(-1); // Negative quantity - should be invalid
        product.setIsActive(null);
        product.setTags(new String[0]);
        
        return product;
    }
    
    /**
     * Get product with empty required fields
     */
    public static Product getProductWithEmptyFields() {
        Product product = new Product();
        product.setName("");
        product.setDescription("");
        product.setPrice(null);
        product.setCategory("");
        product.setSku("");
        
        return product;
    }
    
    /**
     * Get product with null required fields
     */
    public static Product getProductWithNullFields() {
        Product product = new Product();
        product.setName(null);
        product.setDescription(null);
        product.setPrice(null);
        product.setCategory(null);
        product.setSku(null);
        
        return product;
    }
    
    /**
     * Get product with invalid price (negative)
     */
    public static Product getProductWithInvalidPrice() {
        Product product = getValidProduct();
        product.setPrice(new BigDecimal("-10.00"));
        return product;
    }
    
    /**
     * Get product with very long fields (boundary testing)
     */
    public static Product getProductWithLongFields() {
        String longString = "a".repeat(300);
        Product product = getValidProduct();
        product.setName(longString);
        product.setDescription(longString);
        product.setCategory(longString);
        product.setSku(longString);
        
        return product;
    }
    
    /**
     * Get products for performance testing
     */
    public static List<Product> getProductsForPerformanceTesting(int count) {
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Product product = new Product();
            product.setName("PerfProduct" + i);
            product.setDescription("Performance test product " + i);
            product.setPrice(new BigDecimal("19.99").add(new BigDecimal(i)));
            product.setCategory("Performance");
            product.setSku("PERF-" + String.format("%06d", i));
            product.setQuantity(100 + i);
            product.setIsActive(true);
            product.setTags(new String[]{"performance", "test"});
            
            products.add(product);
        }
        return products;
    }
    
    /**
     * Get products with different categories for filtering tests
     */
    public static List<Product> getProductsWithDifferentCategories() {
        String[] categories = {"Electronics", "Books", "Clothing", "Sports", "Home & Garden"};
        List<Product> products = new ArrayList<>();
        
        for (String category : categories) {
            products.add(getProductWithCategory(category));
        }
        return products;
    }
    
    /**
     * Get products with different price ranges
     */
    public static List<Product> getProductsWithDifferentPrices() {
        List<Product> products = new ArrayList<>();
        BigDecimal[] prices = {
            new BigDecimal("9.99"),
            new BigDecimal("29.99"), 
            new BigDecimal("99.99"),
            new BigDecimal("299.99"),
            new BigDecimal("999.99")
        };
        
        for (int i = 0; i < prices.length; i++) {
            Product product = getValidProduct();
            product.setName("Product Price " + prices[i]);
            product.setPrice(prices[i]);
            product.setSku("PRICE-" + i);
            products.add(product);
        }
        return products;
    }
    
    /**
     * Get bulk products for CSV export testing
     */
    public static List<Product> getProductsForCsvExport() {
        List<Product> products = new ArrayList<>();
        products.add(getPredefinedProduct("electronics_product"));
        products.add(getPredefinedProduct("book_product"));
        products.add(getPredefinedProduct("clothing_product"));
        products.add(getValidProduct());
        products.add(getValidProduct());
        
        return products;
    }
    
    /**
     * Get product update data
     */
    public static Product getProductUpdateData(Product originalProduct) {
        Product updatedProduct = new Product();
        updatedProduct.setId(originalProduct.getId());
        updatedProduct.setName("Updated " + originalProduct.getName());
        updatedProduct.setDescription("Updated " + originalProduct.getDescription());
        updatedProduct.setPrice(originalProduct.getPrice().add(new BigDecimal("10.00")));
        updatedProduct.setCategory(originalProduct.getCategory());
        updatedProduct.setSku(originalProduct.getSku());
        updatedProduct.setQuantity(originalProduct.getQuantity() + 50);
        updatedProduct.setIsActive(originalProduct.getIsActive());
        if (originalProduct.getTags() != null) {
            updatedProduct.setTags(Arrays.copyOf(originalProduct.getTags(), originalProduct.getTags().length));
        }
        
        return updatedProduct;
    }
    
    /**
     * Get product with specific inventory level
     */
    public static Product getProductWithInventory(int quantity) {
        Product product = getValidProduct();
        product.setQuantity(quantity);
        return product;
    }
    
    /**
     * Get out of stock product
     */
    public static Product getOutOfStockProduct() {
        Product product = getValidProduct();
        product.setQuantity(0);
        product.setIsActive(false);
        return product;
    }
    
    /**
     * Get discontinued product
     */
    public static Product getDiscontinuedProduct() {
        Product product = getValidProduct();
        product.setIsActive(false);
        product.setTags(new String[]{"discontinued"});
        return product;
    }
    
    /**
     * Get product with specific price
     */
    public static Product getProductWithPrice(BigDecimal price) {
        Product product = getValidProduct();
        product.setPrice(price);
        return product;
    }
    
    /**
     * Get product with specific tags
     */
    public static Product getProductWithTags(String[] tags) {
        Product product = getValidProduct();
        product.setTags(tags);
        return product;
    }
    
    /**
     * Get inactive product
     */
    public static Product getInactiveProduct() {
        Product product = getValidProduct();
        product.setIsActive(false);
        return product;
    }
    
    /**
     * Get product from JSON file
     */
    public static Product getProductFromJsonFile(String fileName) {
        try {
            JsonNode jsonNode = DataUtils.readJsonFromClasspath("testdata/requests/" + fileName);
            return DataUtils.jsonToObject(jsonNode.toString(), Product.class);
        } catch (Exception e) {
            logger.error("Error reading product data from file: {}", fileName, e);
            return getValidProduct();
        }
    }
    
    /**
     * Print all predefined products (for debugging)
     */
    public static void printPredefinedProducts() {
        logger.info("=== Predefined Products ===");
        PREDEFINED_PRODUCTS.forEach((type, product) -> {
            logger.info("Type: {} | Name: {} | Category: {} | Price: {}", 
                type, product.getName(), product.getCategory(), product.getPrice());
        });
        logger.info("=== End Predefined Products ===");
    }
}