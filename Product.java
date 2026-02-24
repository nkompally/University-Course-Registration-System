import java.util.*;

/**
 * Product - Represents a product in the e-commerce system
 */
public class Product {
    private String productId;
    private String name;
    private String category;
    private String description;
    private double price;
    private int stockQuantity;
    private String sellerId;
    private List<Review> reviews;
    private int lowStockThreshold;
    
    public Product(String productId, String name, String category, 
                   String description, double price, int stockQuantity, String sellerId) {
        this.productId = productId;
        this.name = name;
        this.category = category;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.sellerId = sellerId;
        this.reviews = new ArrayList<>();
        this.lowStockThreshold = 10;
    }
    
    public boolean isInStock() {
        return stockQuantity > 0;
    }
    
    public boolean isAvailable(int quantity) {
        return stockQuantity >= quantity;
    }
    
    public boolean isLowStock() {
        return stockQuantity <= lowStockThreshold && stockQuantity > 0;
    }
    
    public void reduceStock(int quantity) {
        if (quantity <= stockQuantity) {
            stockQuantity -= quantity;
        }
    }
    
    public void increaseStock(int quantity) {
        stockQuantity += quantity;
    }
    
    public void addReview(Review review) {
        reviews.add(review);
    }
    
    public double getAverageRating() {
        if (reviews.isEmpty()) {
            return 0.0;
        }
        
        double total = reviews.stream()
                             .mapToDouble(Review::getRating)
                             .sum();
        return total / reviews.size();
    }
    
    public int getTotalReviews() {
        return reviews.size();
    }
    
    public List<Review> getReviews() {
        return new ArrayList<>(reviews);
    }
    
    // Getters and setters
    public String getProductId() { return productId; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public int getStockQuantity() { return stockQuantity; }
    public String getSellerId() { return sellerId; }
    public int getLowStockThreshold() { return lowStockThreshold; }
    
    public void setPrice(double price) { this.price = price; }
    public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }
    public void setLowStockThreshold(int threshold) { this.lowStockThreshold = threshold; }
    
    @Override
    public String toString() {
        String stockStatus = !isInStock() ? " [OUT OF STOCK]" : 
                           isLowStock() ? " [LOW STOCK]" : "";
        
        return String.format("%s - %s%s\n" +
                           "  Price: $%.2f | Stock: %d | Rating: %.1f ⭐ (%d reviews)\n" +
                           "  Category: %s",
                           productId, name, stockStatus, price, stockQuantity,
                           getAverageRating(), getTotalReviews(), category);
    }
    
    public String getDetailedInfo() {
        StringBuilder info = new StringBuilder();
        info.append(toString()).append("\n");
        info.append("  Description: ").append(description).append("\n");
        info.append("  Seller ID: ").append(sellerId).append("\n");
        
        if (!reviews.isEmpty()) {
            info.append("\n  Recent Reviews:\n");
            int count = Math.min(3, reviews.size());
            for (int i = reviews.size() - 1; i >= reviews.size() - count; i--) {
                Review review = reviews.get(i);
                info.append("    ").append(review.getRating()).append("⭐ - ")
                    .append(review.getComment()).append("\n");
            }
        }
        
        return info.toString();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return productId.equals(product.productId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(productId);
    }
}
