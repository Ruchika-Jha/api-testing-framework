package com.testlead.automation.utils;

import net.datafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Utility class for generating fake test data using DataFaker library
 */
public class DataFaker {
    
    private static final Logger logger = LoggerFactory.getLogger(DataFaker.class);
    private static DataFaker instance;
    private final Faker faker;
    private final Random random;
    private final Locale locale; // Add locale field to track it
    
    private DataFaker() {
        this.locale = Locale.getDefault();
        this.faker = new Faker();
        this.random = new Random();
    }
    
    /**
     * Get singleton instance
     */
    public static DataFaker getInstance() {
        if (instance == null) {
            synchronized (DataFaker.class) {
                if (instance == null) {
                    instance = new DataFaker();
                }
            }
        }
        return instance;
    }
    
    /**
     * Get DataFaker instance with specific locale
     */
    public static DataFaker getInstance(Locale locale) {
        return new DataFaker(locale);
    }
    
    private DataFaker(Locale locale) {
        this.locale = locale;
        this.faker = new Faker(locale);
        this.random = new Random();
    }
    
    // Personal Information Methods
    public String getFirstName() {
        return faker.name().firstName();
    }
    
    public String getLastName() {
        return faker.name().lastName();
    }
    
    public String getFullName() {
        return faker.name().fullName();
    }
    
    public String getUsername() {
        return faker.name().username();
    }
    
    public String getEmail() {
        return faker.internet().emailAddress();
    }
    
    public String getEmail(String domain) {
        return faker.internet().emailAddress(domain);
    }
    
    public String getPassword() {
        return faker.internet().password(8, 16, true, true, true);
    }
    
    public String getPassword(int minLength, int maxLength) {
        return faker.internet().password(minLength, maxLength, true, true, true);
    }
    
    public String getStrongPassword() {
        return faker.internet().password(12, 20, true, true, true);
    }
    
    // Phone and Contact Information
    public String getPhoneNumber() {
        return faker.phoneNumber().phoneNumber();
    }
    
    public String getCellPhone() {
        return faker.phoneNumber().cellPhone();
    }
    
    // Address Information
    public String getAddress() {
        return faker.address().fullAddress();
    }
    
    public String getStreetAddress() {
        return faker.address().streetAddress();
    }
    
    public String getCity() {
        return faker.address().city();
    }
    
    public String getState() {
        return faker.address().state();
    }
    
    public String getZipCode() {
        return faker.address().zipCode();
    }
    
    public String getCountry() {
        return faker.address().country();
    }
    
    // Age and Date Methods (Custom implementations)
    public int getAge(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
    
    public int getAge() {
        return getAge(18, 80);
    }
    
    public boolean getBoolean() {
        return random.nextBoolean();
    }
    
    public String getText(int minWords, int maxWords) {
        int wordCount = ThreadLocalRandom.current().nextInt(minWords, maxWords + 1);
        return faker.lorem().sentence(wordCount);
    }
    
    public String getText(int characterCount) {
        return faker.lorem().characters(characterCount);
    }
    
    public String getParagraph() {
        return faker.lorem().paragraph();
    }
    
    public String getSentence() {
        return faker.lorem().sentence();
    }
    
    // Business Information
    public String getCompanyName() {
        return faker.company().name();
    }
    
    public String getJobTitle() {
        return faker.job().title();
    }
    
    public String getDepartment() {
        return faker.job().field();
    }
    
    // Internet and Technology
    public String getUrl() {
        return faker.internet().url();
    }
    
    public String getDomainName() {
        return faker.internet().domainName();
    }
    
    public String getIpAddress() {
        return faker.internet().ipV4Address();
    }
    
    public String getMacAddress() {
        return faker.internet().macAddress();
    }
    
    // Financial Information
    public String getCreditCardNumber() {
        return faker.finance().creditCard();
    }
    
    public String getIban() {
        return faker.finance().iban();
    }
    
    public String getBic() {
        return faker.finance().bic();
    }
    
    // Numbers and Measurements
    public int getRandomInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
    
    public int getRandomNumber(int min, int max) {
        return getRandomInt(min, max); // Alias for getRandomInt
    }
    
    public long getRandomLong(long min, long max) {
        return ThreadLocalRandom.current().nextLong(min, max + 1);
    }
    
    public double getRandomDouble(double min, double max) {
        return ThreadLocalRandom.current().nextDouble(min, max);
    }
    
    public String getRandomString(int length) {
        return faker.lorem().characters(length);
    }
    
    public String getRandomAlphanumeric(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < length; i++) {
            result.append(characters.charAt(random.nextInt(characters.length())));
        }
        return result.toString();
    }
    
    // Product Information
    public String getProductName() {
        return faker.commerce().productName();
    }
    
    public String getDescription() {
        return faker.lorem().paragraph(2); // Generate a 2-paragraph description
    }
    
    public String getProductDescription() {
        return getDescription();
    }
    
    public String getShortDescription() {
        return faker.lorem().sentence(10); // Short 10-word description
    }
    
    public String getBrand() {
        return faker.commerce().brand();
    }
    
    public String getColor() {
        return faker.color().name();
    }
    
    public String getPrice() {
        return faker.commerce().price();
    }
    
    public String getCategory() {
        String[] categories = {
            "Electronics", "Clothing", "Books", "Home & Garden", "Sports", 
            "Automotive", "Health & Beauty", "Toys & Games", "Food & Beverage",
            "Office Supplies", "Pet Supplies", "Jewelry", "Music", "Movies"
        };
        return getRandomFromArray(categories);
    }
    
    public String getSku() {
        return "SKU-" + getRandomAlphanumeric(8).toUpperCase();
    }
    
    public String getProductCode() {
        return "PROD-" + getRandomAlphanumeric(6).toUpperCase();
    }
    
    public String[] getProductTags() {
        String[] possibleTags = {"new", "sale", "popular", "featured", "limited", "bestseller", "trending", "recommended"};
        int tagCount = getRandomNumber(1, 4);
        String[] selectedTags = new String[tagCount];
        
        for (int i = 0; i < tagCount; i++) {
            selectedTags[i] = getRandomFromArray(possibleTags);
        }
        return selectedTags;
    }
    
    public double getPriceAsDouble(double min, double max) {
        return getRandomDouble(min, max);
    }
    
    // Date and Time
    public LocalDateTime getFutureDateTime() {
        return LocalDateTime.now().plusDays(getRandomInt(1, 365));
    }
    
    public LocalDateTime getPastDateTime() {
        return LocalDateTime.now().minusDays(getRandomInt(1, 365));
    }
    
    public LocalDateTime getRandomDateTime() {
        return getBoolean() ? getFutureDateTime() : getPastDateTime();
    }
    
    // Collection Methods
    public <T> T getRandomFromList(List<T> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(random.nextInt(list.size()));
    }
    
    public <T> T getRandomFromArray(T[] array) {
        if (array == null || array.length == 0) {
            return null;
        }
        return array[random.nextInt(array.length)];
    }
    
    // UUID and IDs
    public String getUuid() {
        return faker.internet().uuid();
    }
    
    public String getRandomId() {
        return getRandomAlphanumeric(10);
    }
    
    // Special Characters and Edge Cases
    public String getStringWithSpecialChars() {
        String[] specialStrings = {
            "Test!@#$%^&*()",
            "User<>?:\"{}|",
            "Name with spaces",
            "unicode-ñáéíóú",
            "test_with_underscores",
            "test-with-hyphens",
            "TEST_ALL_CAPS",
            "test.with.dots"
        };
        return getRandomFromArray(specialStrings);
    }
    
    public String getEmptyString() {
        return "";
    }
    
    public String getWhitespaceString() {
        return "   ";
    }
    
    public String getLongString(int length) {
        return faker.lorem().characters(length);
    }
    
    // Validation Helpers
    public String getInvalidEmail() {
        String[] invalidEmails = {
            "invalid-email",
            "@domain.com",
            "user@",
            "user@@domain.com",
            "user..double.dot@domain.com",
            "user@domain",
            "user@.com"
        };
        return getRandomFromArray(invalidEmails);
    }
    
    public String getWeakPassword() {
        String[] weakPasswords = {
            "123",
            "password",
            "abc",
            "12345678",
            "qwerty"
        };
        return getRandomFromArray(weakPasswords);
    }
    
    // Role and Status
    public String getRandomRole() {
        String[] roles = {"USER", "ADMIN", "MODERATOR", "GUEST", "PREMIUM"};
        return getRandomFromArray(roles);
    }
    
    public String getRandomStatus() {
        String[] statuses = {"ACTIVE", "INACTIVE", "PENDING", "SUSPENDED", "DELETED"};
        return getRandomFromArray(statuses);
    }
    
    // API Testing Specific
    public String getApiKey() {
        return "api_" + getRandomAlphanumeric(32);
    }
    
    public String getBearerToken() {
        return "Bearer " + getRandomAlphanumeric(40);
    }
    
    public String getJsonWebToken() {
        String header = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9";
        String payload = faker.internet().uuid().replace("-", "");
        String signature = faker.internet().uuid().replace("-", "");
        return header + "." + payload + "." + signature;
    }
    
    // Utility Methods
    public void setSeed(long seed) {
        random.setSeed(seed);
    }
    
    public Faker getFaker() {
        return faker;
    }
    
    public Random getRandom() {
        return random;
    }
    
    public Locale getLocale() {
        return locale;
    }
    
    // Log current instance info
    public void logInstanceInfo() {
        logger.info("DataFaker instance created with locale: {}", locale);
    }
}