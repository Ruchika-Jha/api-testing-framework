package com.testlead.automation.constants;

/**
 * API Endpoints constants
 * This class goes in src/main/java
 */
public class ApiEndpoints {
    
    // Base API paths
    public static final String API_V1 = "/api/v1";
    public static final String API_V2 = "/api/v2";
    
    // =============== AUTHENTICATION ENDPOINTS ===============
    public static final String AUTH_LOGIN = API_V1 + "/auth/login";
    public static final String AUTH_LOGOUT = API_V1 + "/auth/logout";
    public static final String AUTH_REFRESH = API_V1 + "/auth/refresh";
    public static final String AUTH_VERIFY_TOKEN = API_V1 + "/auth/verify";
    public static final String AUTH_FORGOT_PASSWORD = API_V1 + "/auth/forgot-password";
    public static final String AUTH_RESET_PASSWORD = API_V1 + "/auth/reset-password";
    public static final String AUTH_CHANGE_PASSWORD = API_V1 + "/auth/change-password";
    public static final String AUTH_2FA_ENABLE = API_V1 + "/auth/2fa/enable";
    public static final String AUTH_2FA_DISABLE = API_V1 + "/auth/2fa/disable";
    public static final String AUTH_2FA_VERIFY = API_V1 + "/auth/2fa/verify";
    public static final String AUTH_REGISTER = "/auth/register";
    public static final String AUTH_VERIFY_EMAIL = "/auth/verify-email";

    // Health and monitoring endpoints
    public static final String HEALTH_CHECK = "/health";
    public static final String HEALTH_DETAILED = "/health/detailed";
    public static final String METRICS = "/metrics";
    public static final String INFO = "/info";
    
    // =============== USER ENDPOINTS ===============
    public static final String USERS = API_V1 + "/users";
    public static final String USER_BY_ID = API_V1 + "/users/{userId}";
    public static final String USER_BY_USERNAME = API_V1 + "/users/username/{username}";
    public static final String USER_SEARCH = API_V1 + "/users/search";
    public static final String USER_PROFILE = API_V1 + "/users/{userId}/profile";
    public static final String USER_PROFILE_IMAGE = API_V1 + "/users/{userId}/profile/image";
    public static final String USER_ACTIVATE = API_V1 + "/users/{userId}/activate";
    public static final String USER_DEACTIVATE = API_V1 + "/users/{userId}/deactivate";
    public static final String USER_CHANGE_PASSWORD = API_V1 + "/users/{userId}/password";
    public static final String USER_RESET_PASSWORD = API_V1 + "/users/reset-password";
    public static final String USER_CHECK_USERNAME = API_V1 + "/users/check/username";
    public static final String USER_CHECK_EMAIL = API_V1 + "/users/check/email";
    public static final String USER_VALIDATE = API_V1 + "/users/validate";
    public static final String USER_BULK_CREATE = API_V1 + "/users/bulk/create";
    public static final String USER_BULK_UPDATE = API_V1 + "/users/bulk/update";
    public static final String USER_BULK_DELETE = API_V1 + "/users/bulk/delete";
    
    // =============== PRODUCT ENDPOINTS ===============
    public static final String PRODUCTS = API_V1 + "/products";
    public static final String PRODUCT_BY_ID = API_V1 + "/products/{productId}";
    public static final String PRODUCT_BY_SKU = API_V1 + "/products/sku/{sku}";
    public static final String PRODUCT_SEARCH = API_V1 + "/products/search";
    public static final String PRODUCT_CATEGORIES = API_V1 + "/products/categories";
    public static final String PRODUCT_BY_CATEGORY = API_V1 + "/products/category/{categoryId}";
    public static final String PRODUCT_INVENTORY = API_V1 + "/products/{productId}/inventory";
    public static final String PRODUCT_REVIEWS = API_V1 + "/products/{productId}/reviews";
    public static final String PRODUCT_IMAGES = API_V1 + "/products/{productId}/images";
    public static final String PRODUCT_BULK_CREATE = API_V1 + "/products/bulk/create";
    public static final String PRODUCT_BULK_UPDATE = API_V1 + "/products/bulk/update";
    public static final String PRODUCT_BULK_DELETE = API_V1 + "/products/bulk/delete";
    public static final String PRODUCTS_SEARCH = "/products/search";
    public static final String PRODUCTS_BULK = "/products/bulk";
    public static final String PRODUCT_ACTIVATE = "/products/{id}/activate";
    public static final String PRODUCT_DEACTIVATE = "/products/{id}/deactivate";
    public static final String PRODUCTS_FEATURED = "/products/featured";
    
    // =============== ORDER ENDPOINTS ===============
    public static final String ORDERS = API_V1 + "/orders";
    public static final String ORDER_BY_ID = API_V1 + "/orders/{orderId}";
    public static final String ORDER_BY_USER = API_V1 + "/orders/user/{userId}";
    public static final String ORDER_SEARCH = API_V1 + "/orders/search";
    public static final String ORDER_STATUS = API_V1 + "/orders/{orderId}/status";
    public static final String ORDER_CANCEL = API_V1 + "/orders/{orderId}/cancel";
    public static final String ORDER_REFUND = API_V1 + "/orders/{orderId}/refund";
    public static final String ORDER_ITEMS = API_V1 + "/orders/{orderId}/items";
    public static final String ORDER_HISTORY = API_V1 + "/orders/history";
    
    // =============== CATEGORY ENDPOINTS ===============
    public static final String CATEGORIES = API_V1 + "/categories";
    public static final String CATEGORY_BY_ID = API_V1 + "/categories/{categoryId}";
    public static final String CATEGORY_TREE = API_V1 + "/categories/tree";
    public static final String CATEGORY_PRODUCTS = API_V1 + "/categories/{categoryId}/products";
    
    // =============== INVENTORY ENDPOINTS ===============
    public static final String INVENTORY = API_V1 + "/inventory";
    public static final String INVENTORY_BY_PRODUCT = API_V1 + "/inventory/product/{productId}";
    public static final String INVENTORY_UPDATE = API_V1 + "/inventory/{productId}/update";
    public static final String INVENTORY_BULK_UPDATE = API_V1 + "/inventory/bulk/update";
    public static final String INVENTORY_LOW_STOCK = API_V1 + "/inventory/low-stock";
    
    // =============== CART ENDPOINTS ===============
    public static final String CART = API_V1 + "/cart";
    public static final String CART_BY_USER = API_V1 + "/cart/user/{userId}";
    public static final String CART_ADD_ITEM = API_V1 + "/cart/add";
    public static final String CART_REMOVE_ITEM = API_V1 + "/cart/remove";
    public static final String CART_UPDATE_ITEM = API_V1 + "/cart/update";
    public static final String CART_CLEAR = API_V1 + "/cart/clear";
    public static final String CART_CHECKOUT = API_V1 + "/cart/checkout";
    
    // =============== PAYMENT ENDPOINTS ===============
    public static final String PAYMENTS = API_V1 + "/payments";
    public static final String PAYMENT_BY_ID = API_V1 + "/payments/{paymentId}";
    public static final String PAYMENT_BY_ORDER = API_V1 + "/payments/order/{orderId}";
    public static final String PAYMENT_PROCESS = API_V1 + "/payments/process";
    public static final String PAYMENT_REFUND = API_V1 + "/payments/{paymentId}/refund";
    public static final String PAYMENT_METHODS = API_V1 + "/payments/methods";
    
    // =============== REVIEW ENDPOINTS ===============
    public static final String REVIEWS = API_V1 + "/reviews";
    public static final String REVIEW_BY_ID = API_V1 + "/reviews/{reviewId}";
    public static final String REVIEW_BY_PRODUCT = API_V1 + "/reviews/product/{productId}";
    public static final String REVIEW_BY_USER = API_V1 + "/reviews/user/{userId}";
    public static final String REVIEW_APPROVE = API_V1 + "/reviews/{reviewId}/approve";
    public static final String REVIEW_REJECT = API_V1 + "/reviews/{reviewId}/reject";
    
    // =============== NOTIFICATION ENDPOINTS ===============
    public static final String NOTIFICATIONS = API_V1 + "/notifications";
    public static final String NOTIFICATION_BY_ID = API_V1 + "/notifications/{notificationId}";
    public static final String NOTIFICATION_BY_USER = API_V1 + "/notifications/user/{userId}";
    public static final String NOTIFICATION_MARK_READ = API_V1 + "/notifications/{notificationId}/read";
    public static final String NOTIFICATION_MARK_ALL_READ = API_V1 + "/notifications/mark-all-read";
    public static final String NOTIFICATION_SEND = API_V1 + "/notifications/send";
    
    // =============== ADMIN ENDPOINTS ===============
    public static final String ADMIN_USERS = API_V1 + "/admin/users";
    public static final String ADMIN_PRODUCTS = API_V1 + "/admin/products";
    public static final String ADMIN_ORDERS = API_V1 + "/admin/orders";
    public static final String ADMIN_REPORTS = API_V1 + "/admin/reports";
    public static final String ADMIN_SETTINGS = API_V1 + "/admin/settings";
    public static final String ADMIN_LOGS = API_V1 + "/admin/logs";
    public static final String ADMIN_BACKUP = API_V1 + "/admin/backup";
    public static final String ADMIN_RESTORE = API_V1 + "/admin/restore";
    
    // =============== ANALYTICS ENDPOINTS ===============
    public static final String ANALYTICS_USERS = API_V1 + "/analytics/users";
    public static final String ANALYTICS_PRODUCTS = API_V1 + "/analytics/products";
    public static final String ANALYTICS_ORDERS = API_V1 + "/analytics/orders";
    public static final String ANALYTICS_REVENUE = API_V1 + "/analytics/revenue";
    public static final String ANALYTICS_DASHBOARD = API_V1 + "/analytics/dashboard";
    
    // =============== HEALTH CHECK ENDPOINTS ===============
    public static final String HEALTH = "/health";
    public static final String HEALTH_READY = "/health/ready";
    public static final String HEALTH_LIVE = "/health/live";
    public static final String HEALTH_METRICS = "/health/metrics";
    public static final String HEALTH_INFO = "/health/info";
    
    // =============== UTILITY ENDPOINTS ===============
    public static final String PING = "/ping";
    public static final String VERSION = "/version";
    public static final String DOCS = "/docs";
    public static final String SWAGGER = "/swagger-ui";
    
    
    // Private constructor to prevent instantiation
    private ApiEndpoints() {
        throw new AssertionError("Constants class should not be instantiated");
    }
}