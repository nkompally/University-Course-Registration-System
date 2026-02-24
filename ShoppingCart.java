import java.util.*;

/**
 * ShoppingCart - Manages items in a customer's cart
 */
public class ShoppingCart {
    private String customerId;
    private Map<Product, Integer> items; // Product -> Quantity
    private double discountPercent;
    
    public ShoppingCart(String customerId) {
        this.customerId = customerId;
        this.items = new HashMap<>();
        this.discountPercent = 0.0;
    }
    
    public boolean addItem(Product product, int quantity) {
        if (!product.isAvailable(quantity)) {
            return false;
        }
        
        items.put(product, items.getOrDefault(product, 0) + quantity);
        return true;
    }
    
    public boolean removeItem(Product product) {
        return items.remove(product) != null;
    }
    
    public boolean updateQuantity(Product product, int newQuantity) {
        if (newQuantity <= 0) {
            return removeItem(product);
        }
        
        if (!product.isAvailable(newQuantity)) {
            return false;
        }
        
        items.put(product, newQuantity);
        return true;
    }
    
    public void clear() {
        items.clear();
    }
    
    public boolean isEmpty() {
        return items.isEmpty();
    }
    
    public int getTotalItems() {
        return items.values().stream().mapToInt(Integer::intValue).sum();
    }
    
    public double getSubtotal() {
        return items.entrySet().stream()
                   .mapToDouble(e -> e.getKey().getPrice() * e.getValue())
                   .sum();
    }
    
    public double getDiscountAmount() {
        return getSubtotal() * (discountPercent / 100);
    }
    
    public double getTotal() {
        return getSubtotal() - getDiscountAmount();
    }
    
    public void applyDiscount(double percent) {
        this.discountPercent = Math.max(0, Math.min(100, percent));
    }
    
    public Map<Product, Integer> getItems() {
        return new HashMap<>(items);
    }
    
    public String getCustomerId() { return customerId; }
    public double getDiscountPercent() { return discountPercent; }
    
    @Override
    public String toString() {
        if (isEmpty()) {
            return "Cart is empty";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("=".repeat(70)).append("\n");
        sb.append("                         SHOPPING CART\n");
        sb.append("=".repeat(70)).append("\n\n");
        
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
        sb.append(String.format("%60s $%11.2f\n", "Subtotal:", getSubtotal()));
        
        if (discountPercent > 0) {
            sb.append(String.format("%60s -$%10.2f\n", 
                                   String.format("Discount (%.0f%%):", discountPercent),
                                   getDiscountAmount()));
        }
        
        sb.append(String.format("%60s $%11.2f\n", "TOTAL:", getTotal()));
        sb.append("=".repeat(70)).append("\n");
        
        return sb.toString();
    }
    
    private String truncate(String str, int length) {
        return str.length() > length ? str.substring(0, length - 3) + "..." : str;
    }
}
