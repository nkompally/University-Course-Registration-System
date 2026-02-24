import java.util.*;

/**
 * Customer - Represents a customer in the e-commerce system
 */
public class Customer {
    private String customerId;
    private String name;
    private String email;
    private String phone;
    private String address;
    private ShoppingCart cart;
    private List<Order> orderHistory;
    private Set<String> wishlist; // Product IDs
    
    public Customer(String customerId, String name, String email, 
                   String phone, String address) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.cart = new ShoppingCart(customerId);
        this.orderHistory = new ArrayList<>();
        this.wishlist = new HashSet<>();
    }
    
    public void addToWishlist(String productId) {
        wishlist.add(productId);
    }
    
    public boolean removeFromWishlist(String productId) {
        return wishlist.remove(productId);
    }
    
    public boolean isInWishlist(String productId) {
        return wishlist.contains(productId);
    }
    
    public void addOrder(Order order) {
        orderHistory.add(order);
    }
    
    public List<Order> getOrderHistory() {
        return new ArrayList<>(orderHistory);
    }
    
    public Order getOrder(String orderId) {
        return orderHistory.stream()
                          .filter(o -> o.getOrderId().equals(orderId))
                          .findFirst()
                          .orElse(null);
    }
    
    public List<Order> getOrdersByStatus(Order.OrderStatus status) {
        List<Order> result = new ArrayList<>();
        for (Order order : orderHistory) {
            if (order.getStatus() == status) {
                result.add(order);
            }
        }
        return result;
    }
    
    public double getTotalSpent() {
        return orderHistory.stream()
                          .filter(o -> o.getStatus() != Order.OrderStatus.CANCELLED)
                          .mapToDouble(Order::getTotalAmount)
                          .sum();
    }
    
    public int getTotalOrders() {
        return (int) orderHistory.stream()
                                 .filter(o -> o.getStatus() != Order.OrderStatus.CANCELLED)
                                 .count();
    }
    
    public List<String> getPurchasedProducts() {
        Set<String> products = new HashSet<>();
        for (Order order : orderHistory) {
            if (order.getStatus() == Order.OrderStatus.DELIVERED) {
                for (Product product : order.getItems().keySet()) {
                    products.add(product.getProductId());
                }
            }
        }
        return new ArrayList<>(products);
    }
    
    // Getters
    public String getCustomerId() { return customerId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public ShoppingCart getCart() { return cart; }
    public Set<String> getWishlist() { return new HashSet<>(wishlist); }
    
    // Setters
    public void setAddress(String address) { this.address = address; }
    public void setPhone(String phone) { this.phone = phone; }
    
    @Override
    public String toString() {
        return String.format("%s - %s\n" +
                           "  Email: %s | Phone: %s\n" +
                           "  Total Orders: %d | Total Spent: $%.2f",
                           customerId, name, email, phone, 
                           getTotalOrders(), getTotalSpent());
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return customerId.equals(customer.customerId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(customerId);
    }
}
