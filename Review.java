import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Review - Represents a customer review for a product
 */
public class Review {
    private String customerId;
    private String customerName;
    private int rating; // 1-5 stars
    private String comment;
    private LocalDateTime reviewDate;
    private boolean verified; // Verified purchase
    
    public Review(String customerId, String customerName, int rating, 
                  String comment, boolean verified) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.rating = Math.max(1, Math.min(5, rating)); // Clamp between 1-5
        this.comment = comment;
        this.reviewDate = LocalDateTime.now();
        this.verified = verified;
    }
    
    public String getCustomerId() { return customerId; }
    public String getCustomerName() { return customerName; }
    public int getRating() { return rating; }
    public String getComment() { return comment; }
    public LocalDateTime getReviewDate() { return reviewDate; }
    public boolean isVerified() { return verified; }
    
    @Override
    public String toString() {
        String verifiedBadge = verified ? " [Verified Purchase]" : "";
        String stars = "⭐".repeat(rating);
        String date = reviewDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"));
        
        return String.format("%s%s - %s\n" +
                           "%s\n" +
                           "by %s on %s",
                           stars, verifiedBadge, comment,
                           "-".repeat(50), customerName, date);
    }
}
