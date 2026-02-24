import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Order - Represents a customer order with state tracking
 */
public class Order {
    
    public enum OrderStatus {
        PENDING,
        CONFIRMED,
        PROCESSING,
        SHIPPED,
        DELIVERED,
        CANCELLED
    }
    
    private String orderId;
    private String customerId;
    private Map<Product, Integer> items;
    private double totalAmount;
    private OrderStatus status;
    private LocalDateTime orderDate;
    private LocalDateTime deliveryDate;
    private String shippingAddress;
    private String paymentMethod;
    private List<String> statusHistory;
    
    public Order(String orderId, String customerId, Map<Product, Integer> items,
                 double totalAmount, String shippingAddress, String paymentMethod) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.items = new HashMap<>(items);
        this.totalAmount = totalAmount;
        this.status = OrderStatus.PENDING;
        this.orderDate = LocalDateTime.now();
        this.shippingAddress = shippingAddress;
        this.paymentMethod = paymentMethod;
        this.statusHistory = new ArrayList<>();
        addStatusUpdate("Order created");
    }
    
    public boolean updateStatus(OrderStatus newStatus) {
        // Validate state transitions
        if (!isValidTransition(status, newStatus)) {
            return false;
        }
        
        this.status = newStatus;
        addStatusUpdate("Status changed to: " + newStatus);
        
        if (newStatus == OrderStatus.DELIVERED) {
            deliveryDate = LocalDateTime.now();
        }
        
        return true;
    }
    
    private boolean isValidTransition(OrderStatus from, OrderStatus to) {
        // Define valid state transitions
        switch (from) {
            case PENDING:
                return to == OrderStatus.CONFIRMED || to == OrderStatus.CANCELLED;
            case CONFIRMED:
                return to == OrderStatus.PROCESSING || to == OrderStatus.CANCELLED;
            case PROCESSING:
                return to == OrderStatus.SHIPPED || to == OrderStatus.CANCELLED;
            case SHIPPED:
                return to == OrderStatus.DELIVERED;
            case DELIVERED:
            case CANCELLED:
                return false; // Terminal states
            default:
                return false;
        }
    }
    
    private void addStatusUpdate(String update) {
        String timestamp = LocalDateTime.now().format(
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        statusHistory.add(timestamp + " - " + update);
    }
    
    public boolean canCancel() {
        return status == OrderStatus.PENDING || 
               status == OrderStatus.CONFIRMED ||
               status == OrderStatus.PROCESSING;
    }
    
    public boolean cancel() {
        if (canCancel()) {
            return updateStatus(OrderStatus.CANCELLED);
        }
        return false;
    }
    
    public int getTotalItems() {
        return items.values().stream().mapToInt(Integer::intValue).sum();
    }
    
    public List<String> getStatusHistory() {
        return new ArrayList<>(statusHistory);
    }
    
    // Getters
    public String getOrderId() { return orderId; }
    public String getCustomerId() { return customerId; }
    public Map<Product, Integer> getItems() { return new HashMap<>(items); }
    public double getTotalAmount() { return totalAmount; }
    public OrderStatus getStatus() { return status; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public LocalDateTime getDeliveryDate() { return deliveryDate; }
    public String getShippingAddress() { return shippingAddress; }
    public String getPaymentMethod() { return paymentMethod; }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=".repeat(70)).append("\n");
        sb.append("                          ORDER DETAILS\n");
        sb.append("=".repeat(70)).append("\n\n");
        
        sb.append(String.format("Order ID: %s\n", orderId));
        sb.append(String.format("Customer ID: %s\n", customerId));
        sb.append(String.format("Order Date: %s\n", 
                               orderDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm"))));
        sb.append(String.format("Status: %s\n", status));
        
        if (deliveryDate != null) {
            sb.append(String.format("Delivered: %s\n",
                                   deliveryDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm"))));
        }
        
        sb.append(String.format("Payment Method: %s\n\n", paymentMethod));
        
        sb.append("Items:\n");
        sb.append("-".repeat(70)).append("\n");
        sb.append(String.format("%-40s %8s %10s %12s\n", 
                               "Product", "Qty", "Price", "Subtotal"));
        sb.append("-".repeat(70)).append("\n");
        
        for (Map.Entry<Product, Integer> entry : items.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            double subtotal = product.getPrice() * quantity;
            
            sb.append(String.format("%-40s %8d $%9.2f $%11.2f\n",
                                   truncate(product.getName(), 40),
                                   quantity,
                                   product.getPrice(),
                                   subtotal));
        }
        
        sb.append("-".repeat(70)).append("\n");
        sb.append(String.format("%60s $%11.2f\n", "TOTAL:", totalAmount));
        
        sb.append("\nShipping Address:\n");
        sb.append("  ").append(shippingAddress).append("\n");
        
        sb.append("\n=".repeat(70)).append("\n");
        
        return sb.toString();
    }
    
    public String getTrackingInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("=".repeat(70)).append("\n");
        sb.append("                      ORDER TRACKING\n");
        sb.append("=".repeat(70)).append("\n\n");
        sb.append(String.format("Order ID: %s\n", orderId));
        sb.append(String.format("Current Status: %s\n\n", status));
        
        sb.append("Status History:\n");
        sb.append("-".repeat(70)).append("\n");
        for (String update : statusHistory) {
            sb.append("  ").append(update).append("\n");
        }
        sb.append("=".repeat(70)).append("\n");
        
        return sb.toString();
    }
    
    private String truncate(String str, int length) {
        return str.length() > length ? str.substring(0, length - 3) + "..." : str;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return orderId.equals(order.orderId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(orderId);
    }
}
