import java.util.*;
import java.util.stream.Collectors;

/**
 * ECommerceSystem - Main system managing products, customers, and orders
 * Includes recommendation engine and inventory management
 */
public class ECommerceSystem {
    
    private Map<String, Product> products;
    private Map<String, Customer> customers;
    private Map<String, Order> orders;
    private int orderCounter;
    
    public ECommerceSystem() {
        this.products = new HashMap<>();
        this.customers = new HashMap<>();
        this.orders = new HashMap<>();
        this.orderCounter = 1000;
    }
    
    // Product Management
    public void addProduct(Product product) {
        products.put(product.getProductId(), product);
    }
    
    public Product getProduct(String productId) {
        return products.get(productId);
    }
    
    public List<Product> getAllProducts() {
        return new ArrayList<>(products.values());
    }
    
    public List<Product> getProductsByCategory(String category) {
        return products.values().stream()
                      .filter(p -> p.getCategory().equalsIgnoreCase(category))
                      .collect(Collectors.toList());
    }
    
    public List<Product> searchProducts(String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        return products.values().stream()
                      .filter(p -> p.getName().toLowerCase().contains(lowerKeyword) ||
                                  p.getDescription().toLowerCase().contains(lowerKeyword) ||
                                  p.getCategory().toLowerCase().contains(lowerKeyword))
                      .collect(Collectors.toList());
    }
    
    public List<Product> getLowStockProducts() {
        return products.values().stream()
                      .filter(Product::isLowStock)
                      .collect(Collectors.toList());
    }
    
    // Customer Management
    public void addCustomer(Customer customer) {
        customers.put(customer.getCustomerId(), customer);
    }
    
    public Customer getCustomer(String customerId) {
        return customers.get(customerId);
    }
    
    public List<Customer> getAllCustomers() {
        return new ArrayList<>(customers.values());
    }
    
    // Shopping Cart Operations
    public boolean addToCart(String customerId, String productId, int quantity) {
        Customer customer = customers.get(customerId);
        Product product = products.get(productId);
        
        if (customer == null || product == null) {
            return false;
        }
        
        return customer.getCart().addItem(product, quantity);
    }
    
    public boolean removeFromCart(String customerId, String productId) {
        Customer customer = customers.get(customerId);
        Product product = products.get(productId);
        
        if (customer == null || product == null) {
            return false;
        }
        
        return customer.getCart().removeItem(product);
    }
    
    // Order Processing
    public OrderResult placeOrder(String customerId, String paymentMethod) {
        Customer customer = customers.get(customerId);
        
        if (customer == null) {
            return new OrderResult(false, "Customer not found", null);
        }
        
        ShoppingCart cart = customer.getCart();
        
        if (cart.isEmpty()) {
            return new OrderResult(false, "Cart is empty", null);
        }
        
        // Check stock availability
        for (Map.Entry<Product, Integer> entry : cart.getItems().entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            
            if (!product.isAvailable(quantity)) {
                return new OrderResult(false, 
                    "Insufficient stock for: " + product.getName(), null);
            }
        }
        
        // Create order
        String orderId = generateOrderId();
        Order order = new Order(
            orderId,
            customerId,
            cart.getItems(),
            cart.getTotal(),
            customer.getAddress(),
            paymentMethod
        );
        
        // Reduce stock
        for (Map.Entry<Product, Integer> entry : cart.getItems().entrySet()) {
            entry.getKey().reduceStock(entry.getValue());
        }
        
        // Save order
        orders.put(orderId, order);
        customer.addOrder(order);
        
        // Clear cart
        cart.clear();
        
        return new OrderResult(true, "Order placed successfully!", order);
    }
    
    private String generateOrderId() {
        return "ORD" + (orderCounter++);
    }
    
    public Order getOrder(String orderId) {
        return orders.get(orderId);
    }
    
    public List<Order> getAllOrders() {
        return new ArrayList<>(orders.values());
    }
    
    public List<Order> getOrdersByStatus(Order.OrderStatus status) {
        return orders.values().stream()
                    .filter(o -> o.getStatus() == status)
                    .collect(Collectors.toList());
    }
    
    public boolean updateOrderStatus(String orderId, Order.OrderStatus newStatus) {
        Order order = orders.get(orderId);
        if (order == null) {
            return false;
        }
        return order.updateStatus(newStatus);
    }
    
    public boolean cancelOrder(String orderId) {
        Order order = orders.get(orderId);
        if (order == null) {
            return false;
        }
        
        if (!order.canCancel()) {
            return false;
        }
        
        // Restore stock
        for (Map.Entry<Product, Integer> entry : order.getItems().entrySet()) {
            entry.getKey().increaseStock(entry.getValue());
        }
        
        return order.cancel();
    }
    
    // Review System
    public boolean addReview(String customerId, String productId, 
                            int rating, String comment) {
        Customer customer = customers.get(customerId);
        Product product = products.get(productId);
        
        if (customer == null || product == null) {
            return false;
        }
        
        // Check if customer purchased this product
        boolean purchased = customer.getPurchasedProducts().contains(productId);
        
        Review review = new Review(
            customerId,
            customer.getName(),
            rating,
            comment,
            purchased
        );
        
        product.addReview(review);
        return true;
    }
    
    // Recommendation Engine
    public List<Product> getRecommendations(String customerId, int limit) {
        Customer customer = customers.get(customerId);
        if (customer == null) {
            return new ArrayList<>();
        }
        
        // Get categories from customer's purchase history
        Set<String> purchasedCategories = new HashSet<>();
        for (String productId : customer.getPurchasedProducts()) {
            Product product = products.get(productId);
            if (product != null) {
                purchasedCategories.add(product.getCategory());
            }
        }
        
        // Recommend products from same categories that customer hasn't bought
        List<Product> recommendations = new ArrayList<>();
        Set<String> purchased = new HashSet<>(customer.getPurchasedProducts());
        
        for (Product product : products.values()) {
            if (!purchased.contains(product.getProductId()) &&
                purchasedCategories.contains(product.getCategory()) &&
                product.isInStock()) {
                recommendations.add(product);
            }
        }
        
        // Sort by rating
        recommendations.sort((p1, p2) -> 
            Double.compare(p2.getAverageRating(), p1.getAverageRating()));
        
        // If not enough, add popular products
        if (recommendations.size() < limit) {
            List<Product> popular = getTopRatedProducts(limit);
            for (Product product : popular) {
                if (!recommendations.contains(product) && 
                    !purchased.contains(product.getProductId())) {
                    recommendations.add(product);
                }
            }
        }
        
        return recommendations.stream().limit(limit).collect(Collectors.toList());
    }
    
    public List<Product> getTopRatedProducts(int limit) {
        return products.values().stream()
                      .filter(Product::isInStock)
                      .sorted((p1, p2) -> {
                          // Sort by rating, then by number of reviews
                          int ratingCompare = Double.compare(
                              p2.getAverageRating(), 
                              p1.getAverageRating()
                          );
                          if (ratingCompare != 0) return ratingCompare;
                          return Integer.compare(
                              p2.getTotalReviews(), 
                              p1.getTotalReviews()
                          );
                      })
                      .limit(limit)
                      .collect(Collectors.toList());
    }
    
    // Analytics
    public double getTotalRevenue() {
        return orders.values().stream()
                    .filter(o -> o.getStatus() != Order.OrderStatus.CANCELLED)
                    .mapToDouble(Order::getTotalAmount)
                    .sum();
    }
    
    public Map<String, Integer> getCategorySales() {
        Map<String, Integer> sales = new HashMap<>();
        
        for (Order order : orders.values()) {
            if (order.getStatus() != Order.OrderStatus.CANCELLED) {
                for (Map.Entry<Product, Integer> entry : order.getItems().entrySet()) {
                    String category = entry.getKey().getCategory();
                    int quantity = entry.getValue();
                    sales.put(category, sales.getOrDefault(category, 0) + quantity);
                }
            }
        }
        
        return sales;
    }
    
    public List<Product> getBestSellingProducts(int limit) {
        Map<Product, Integer> salesCount = new HashMap<>();
        
        for (Order order : orders.values()) {
            if (order.getStatus() != Order.OrderStatus.CANCELLED) {
                for (Map.Entry<Product, Integer> entry : order.getItems().entrySet()) {
                    Product product = entry.getKey();
                    int quantity = entry.getValue();
                    salesCount.put(product, 
                        salesCount.getOrDefault(product, 0) + quantity);
                }
            }
        }
        
        return salesCount.entrySet().stream()
                        .sorted((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()))
                        .limit(limit)
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toList());
    }
    
    // Result class
    public static class OrderResult {
        private boolean success;
        private String message;
        private Order order;
        
        public OrderResult(boolean success, String message, Order order) {
            this.success = success;
            this.message = message;
            this.order = order;
        }
        
        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public Order getOrder() { return order; }
        
        @Override
        public String toString() {
            return (success ? "✓ " : "✗ ") + message;
        }
    }
}
