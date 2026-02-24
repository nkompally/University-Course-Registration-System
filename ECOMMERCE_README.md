# E-Commerce System

A comprehensive online shopping platform built with Java, featuring shopping cart management, order processing with state tracking, product reviews, inventory management, and a recommendation engine.

## Overview

This project simulates a complete e-commerce system where customers can browse products, manage shopping carts, place orders, track shipments, write reviews, and receive personalized recommendations. The system includes order state management, inventory tracking with low-stock alerts, and analytics for administrators.

## Features

### Customer Features
- Browse products by category or search by keyword
- View detailed product information with reviews and ratings
- Shopping cart management (add, remove, update quantities)
- Order placement with multiple payment methods
- Order tracking with status history
- Write product reviews (verified purchase badge for bought items)
- Personalized product recommendations
- Wishlist functionality
- Order history with filtering

### Product Management
- Product catalog with categories
- Stock management with availability checking
- Low stock alerts (configurable threshold)
- Review and rating system
- Average rating calculation
- Product search and filtering

### Order Processing
- State-based order workflow (Pending → Confirmed → Processing → Shipped → Delivered)
- State transition validation (prevents invalid status changes)
- Order cancellation (only for non-shipped orders)
- Stock reduction on purchase
- Stock restoration on cancellation
- Complete order history tracking

### Admin Features
- View all orders with status filtering
- Update order status
- View low stock products
- Sales analytics (revenue, category sales)
- Best selling products report

### Recommendation Engine
- Category-based recommendations
- Personalized suggestions based on purchase history
- Top-rated products
- Excludes already purchased items

## New Concepts (Beyond University Registration System)

1. **Shopping Cart State Management**
   - Persistent cart per customer
   - Quantity updates
   - Price calculations with discounts

2. **Order State Machine**
   - Valid state transitions
   - Status history tracking
   - Terminal states (delivered, cancelled)

3. **Inventory Management**
   - Stock reduction/restoration
   - Availability checking
   - Low stock alerts

4. **Review & Rating System**
   - Verified purchase badges
   - Average rating calculation
   - Review history

5. **Recommendation Engine**
   - Category-based filtering
   - Purchase history analysis
   - Rating-based sorting

6. **Business Analytics**
   - Revenue tracking
   - Category sales analysis
   - Best sellers identification

## Project Structure

- `Product.java` - Product model with inventory and reviews
- `Review.java` - Customer review with rating and verification
- `ShoppingCart.java` - Cart management with pricing
- `Order.java` - Order model with state machine
- `Customer.java` - Customer with cart and order history
- `ECommerceSystem.java` - Core system with business logic
- `ECommerceApp.java` - Main application with interactive menu

## Getting Started

### Requirements

- Java JDK 8 or higher
- No external dependencies required

### Compilation

Compile all files together:

```bash
javac *.java
```

### Running

```bash
java ECommerceApp
```

## How to Use

### For Customers

1. **Login**
   - Use demo accounts: C001 (Alice), C002 (Bob), C003 (Carol)

2. **Browse Products**
   - View all products
   - Filter by category
   - Search by keyword
   - View top-rated items

3. **Shopping**
   - Add items to cart
   - Update quantities
   - View cart total
   - Apply discounts

4. **Checkout**
   - Review cart
   - Select payment method
   - Place order
   - Receive order confirmation

5. **Track Orders**
   - View order history
   - Track order status
   - View status history

6. **Write Reviews**
   - Rate products 1-5 stars
   - Add written comments
   - Get verified badge for purchases

7. **Get Recommendations**
   - Personalized based on your purchases
   - Category-based suggestions

### For Admins

1. **Order Management**
   - View all orders
   - Update order status
   - Track order pipeline

2. **Inventory**
   - Monitor low stock
   - View product availability

3. **Analytics**
   - View total revenue
   - Category sales breakdown
   - Best selling products

## Sample Usage Flow

**Scenario 1: Customer Shopping**
```
1. Login as C001 (Alice)
2. Browse Electronics category
3. View iPhone 14 Pro details
4. Add to cart (quantity: 1)
5. View cart (shows total)
6. Checkout with Credit Card
7. Order placed successfully!
8. Track order status
```

**Scenario 2: Admin Processing**
```
1. Access Admin Menu
2. View all pending orders
3. Update order ORD1000 to CONFIRMED
4. Update to PROCESSING
5. Update to SHIPPED
6. Customer can now track shipment
```

**Scenario 3: Reviews & Recommendations**
```
1. Login as C001
2. Write review for purchased laptop
3. Rate 5 stars with comment
4. View recommendations
5. System suggests similar electronics
```

## Technical Implementation

### Order State Machine

The system implements a proper state machine for orders:

```
PENDING → CONFIRMED → PROCESSING → SHIPPED → DELIVERED
    ↓          ↓            ↓
CANCELLED  CANCELLED    CANCELLED
```

**State Transition Rules:**
- Can only cancel before shipping
- Cannot reverse from DELIVERED
- Status history tracks all changes

**Code Example:**
```java
public boolean updateStatus(OrderStatus newStatus) {
    if (!isValidTransition(status, newStatus)) {
        return false;
    }
    this.status = newStatus;
    // Log history
    return true;
}
```

### Shopping Cart Management

Cart persists across sessions and manages:
- Product-quantity mapping
- Subtotal calculation
- Discount application
- Stock availability checking

**Code Example:**
```java
public boolean addItem(Product product, int quantity) {
    if (!product.isAvailable(quantity)) {
        return false;
    }
    items.put(product, items.getOrDefault(product, 0) + quantity);
    return true;
}
```

### Recommendation Algorithm

Simple but effective recommendation engine:

1. **Identify purchase history** - Get categories customer bought
2. **Find similar products** - Match same categories
3. **Filter purchased** - Exclude already bought items
4. **Sort by rating** - Prioritize highly rated
5. **Fill with popular** - Add top-rated if needed

**Code Example:**
```java
public List<Product> getRecommendations(String customerId, int limit) {
    // Get purchased categories
    Set<String> categories = getPurchasedCategories(customerId);
    
    // Find matching products
    List<Product> recommendations = products.stream()
        .filter(p -> categories.contains(p.getCategory()))
        .filter(p -> !customer.hasPurchased(p))
        .sorted(byRating())
        .limit(limit)
        .collect(toList());
    
    return recommendations;
}
```

### Inventory Management

Automatic stock management with:
- Reduction on purchase
- Restoration on cancellation
- Low stock alerts
- Availability checking

**Code Example:**
```java
public void reduceStock(int quantity) {
    if (quantity <= stockQuantity) {
        stockQuantity -= quantity;
        if (isLowStock()) {
            // Trigger alert
        }
    }
}
```

## Data Structures Used

- **HashMap** - Fast product/customer/order lookup (O(1))
- **ArrayList** - Order history, reviews, recommendations
- **HashSet** - Wishlist, purchased products tracking
- **LinkedHashMap** - Preserve cart item order

## Key Algorithms

### 1. Average Rating Calculation
```
Average = Sum of all ratings / Number of reviews
```

### 2. Order Total with Discount
```
Subtotal = Σ (Product Price × Quantity)
Discount = Subtotal × (Discount % / 100)
Total = Subtotal - Discount
```

### 3. Recommendation Scoring
```
Score = (Average Rating × 10) + (Number of Reviews)
Sort products by score descending
```

## Testing Scenarios

### Test 1: Complete Purchase Flow
1. Login as C001
2. Add P001 (Laptop) to cart
3. Add P003 (Headphones) to cart
4. View cart (verify totals)
5. Checkout
6. Verify stock reduced
7. Verify order created

### Test 2: Order State Transitions
1. Place order (status: PENDING)
2. Admin confirms (status: CONFIRMED)
3. Admin processes (status: PROCESSING)
4. Admin ships (status: SHIPPED)
5. Admin delivers (status: DELIVERED)
6. Try to cancel (should fail)

### Test 3: Stock Management
1. Product has 15 units
2. Customer A buys 10 units
3. Stock now 5 units (LOW STOCK alert)
4. Customer B tries to buy 10 (should fail)
5. Customer A cancels order
6. Stock restored to 15 units

### Test 4: Review System
1. Customer buys product
2. Customer writes review (verified badge)
3. Another customer writes review (no badge)
4. View product (shows average rating)

### Test 5: Recommendations
1. Customer buys Electronics items
2. View recommendations
3. System suggests other Electronics
4. Excludes already purchased items

## Extensions

Potential enhancements:

- Payment gateway integration
- User authentication (passwords, sessions)
- Image upload for products
- Advanced search (filters, price range, ratings)
- Shipping cost calculation
- Coupon/promo code system
- Multiple addresses per customer
- Order returns and refunds
- Email notifications
- Product comparisons
- Seller dashboard
- Admin panel with charts
- Database persistence
- RESTful API
- Frontend (React/Angular)

## Learning Objectives

This project demonstrates:

- State machine implementation
- Complex data relationships
- Business logic validation
- Recommendation algorithms
- Inventory management
- Order processing pipeline
- Review aggregation
- Sales analytics
- Role-based functionality
- Real-world e-commerce concepts

## Comparison with University System

| Feature | University System | E-Commerce System |
|---------|------------------|-------------------|
| Core Entity | Course/Student | Product/Customer/Order |
| Main Operation | Registration | Purchase |
| State Management | Simple (enrolled/not) | Complex (order states) |
| Inventory | N/A | Stock tracking |
| Reviews | N/A | Rating system |
| Recommendations | Prerequisites | Purchase-based |
| Analytics | GPA | Sales/Revenue |
| Business Rules | Prerequisites, conflicts | Stock, state transitions |

## License

This is an educational project created for learning purposes.
