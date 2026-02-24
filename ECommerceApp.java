import java.util.*;

/**
 * ECommerceApp - Main application for the e-commerce system
 */
public class ECommerceApp {
    
    private static Scanner scanner = new Scanner(System.in);
    private static ECommerceSystem system = new ECommerceSystem();
    private static String currentCustomerId = null;
    
    public static void main(String[] args) {
        System.out.println("Initializing E-Commerce System...\n");
        setupDemoData();
        
        displayMainMenu();
    }
    
    private static void setupDemoData() {
        // Add products
        Product laptop = new Product("P001", "Dell XPS 15 Laptop", "Electronics",
            "Powerful laptop with Intel i7, 16GB RAM", 1299.99, 15, "SELLER001");
        system.addProduct(laptop);
        
        Product phone = new Product("P002", "iPhone 14 Pro", "Electronics",
            "Latest iPhone with A16 chip", 999.99, 25, "SELLER001");
        system.addProduct(phone);
        
        Product headphones = new Product("P003", "Sony WH-1000XM5", "Electronics",
            "Noise-cancelling wireless headphones", 349.99, 30, "SELLER002");
        system.addProduct(headphones);
        
        Product book1 = new Product("P004", "Clean Code", "Books",
            "A Handbook of Agile Software Craftsmanship", 42.99, 50, "SELLER003");
        system.addProduct(book1);
        
        Product book2 = new Product("P005", "Design Patterns", "Books",
            "Elements of Reusable Object-Oriented Software", 54.99, 40, "SELLER003");
        system.addProduct(book2);
        
        Product chair = new Product("P006", "Herman Miller Aeron", "Furniture",
            "Ergonomic office chair", 1395.00, 8, "SELLER004");
        system.addProduct(chair);
        
        Product desk = new Product("P007", "Standing Desk Pro", "Furniture",
            "Adjustable height standing desk", 599.99, 12, "SELLER004");
        system.addProduct(desk);
        
        Product watch = new Product("P008", "Apple Watch Series 9", "Electronics",
            "Fitness and health tracking", 429.99, 20, "SELLER001");
        system.addProduct(watch);
        
        // Add reviews to some products
        system.addReview("C001", "P001", 5, "Excellent laptop! Very fast and reliable.");
        system.addReview("C002", "P001", 4, "Great performance but a bit pricey.");
        system.addReview("C001", "P003", 5, "Best noise cancelling headphones!");
        system.addReview("C003", "P004", 5, "Must-read for every developer.");
        
        // Add customers
        Customer alice = new Customer("C001", "Alice Johnson", "alice@email.com",
            "555-0101", "123 Main St, City, State 12345");
        system.addCustomer(alice);
        
        Customer bob = new Customer("C002", "Bob Smith", "bob@email.com",
            "555-0102", "456 Oak Ave, City, State 12345");
        system.addCustomer(bob);
        
        Customer carol = new Customer("C003", "Carol Williams", "carol@email.com",
            "555-0103", "789 Pine Rd, City, State 12345");
        system.addCustomer(carol);
        
        System.out.println("Demo data loaded successfully!\n");
        System.out.println("Available customers: C001 (Alice), C002 (Bob), C003 (Carol)");
        System.out.println("Login with a customer ID to start shopping.\n");
    }
    
    private static void displayMainMenu() {
        while (true) {
            System.out.println("\n" + "=".repeat(70));
            System.out.println("                   E-COMMERCE SYSTEM");
            System.out.println("=".repeat(70));
            
            if (currentCustomerId == null) {
                System.out.println("1. Login as Customer");
                System.out.println("2. View All Products");
                System.out.println("3. Search Products");
                System.out.println("4. Admin Menu");
                System.out.println("0. Exit");
            } else {
                Customer customer = system.getCustomer(currentCustomerId);
                System.out.println("Logged in as: " + customer.getName() + " (" + currentCustomerId + ")");
                System.out.println();
                System.out.println("1. Browse Products");
                System.out.println("2. Search Products");
                System.out.println("3. View Product Details");
                System.out.println("4. Add to Cart");
                System.out.println("5. View Cart");
                System.out.println("6. Checkout");
                System.out.println("7. My Orders");
                System.out.println("8. Track Order");
                System.out.println("9. Write Review");
                System.out.println("10. Recommendations for You");
                System.out.println("11. Logout");
                System.out.println("0. Exit");
            }
            
            System.out.println("=".repeat(70));
            System.out.print("Select an option: ");
            
            try {
                int choice = scanner.nextInt();
                scanner.nextLine();
                
                if (currentCustomerId == null) {
                    handleGuestMenu(choice);
                } else {
                    handleCustomerMenu(choice);
                }
                
            } catch (Exception e) {
                System.out.println("\nInvalid input. Please try again.");
                scanner.nextLine();
            }
        }
    }
    
    private static void handleGuestMenu(int choice) {
        switch (choice) {
            case 1:
                login();
                break;
            case 2:
                viewAllProducts();
                break;
            case 3:
                searchProducts();
                break;
            case 4:
                adminMenu();
                break;
            case 0:
                System.out.println("\nThank you for visiting!");
                System.exit(0);
            default:
                System.out.println("\nInvalid option.");
        }
    }
    
    private static void handleCustomerMenu(int choice) {
        switch (choice) {
            case 1:
                browseProducts();
                break;
            case 2:
                searchProducts();
                break;
            case 3:
                viewProductDetails();
                break;
            case 4:
                addToCart();
                break;
            case 5:
                viewCart();
                break;
            case 6:
                checkout();
                break;
            case 7:
                viewOrders();
                break;
            case 8:
                trackOrder();
                break;
            case 9:
                writeReview();
                break;
            case 10:
                viewRecommendations();
                break;
            case 11:
                logout();
                break;
            case 0:
                System.out.println("\nThank you for shopping with us!");
                System.exit(0);
            default:
                System.out.println("\nInvalid option.");
        }
    }
    
    private static void login() {
        System.out.print("\nEnter Customer ID (e.g., C001): ");
        String customerId = scanner.nextLine().toUpperCase();
        
        Customer customer = system.getCustomer(customerId);
        if (customer == null) {
            System.out.println("\nCustomer not found!");
        } else {
            currentCustomerId = customerId;
            System.out.println("\nWelcome, " + customer.getName() + "!");
        }
    }
    
    private static void logout() {
        System.out.println("\nLogged out successfully.");
        currentCustomerId = null;
    }
    
    private static void viewAllProducts() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("                      ALL PRODUCTS");
        System.out.println("=".repeat(70) + "\n");
        
        for (Product product : system.getAllProducts()) {
            System.out.println(product);
            System.out.println();
        }
    }
    
    private static void browseProducts() {
        System.out.println("\nBrowse by:");
        System.out.println("1. All Products");
        System.out.println("2. By Category");
        System.out.println("3. Top Rated");
        System.out.print("Choice: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        switch (choice) {
            case 1:
                viewAllProducts();
                break;
            case 2:
                viewByCategory();
                break;
            case 3:
                viewTopRated();
                break;
        }
    }
    
    private static void viewByCategory() {
        System.out.print("\nEnter category (Electronics, Books, Furniture): ");
        String category = scanner.nextLine();
        
        List<Product> products = system.getProductsByCategory(category);
        
        if (products.isEmpty()) {
            System.out.println("\nNo products found in this category.");
        } else {
            System.out.println("\n" + "=".repeat(70));
            System.out.println("              PRODUCTS IN " + category.toUpperCase());
            System.out.println("=".repeat(70) + "\n");
            
            for (Product product : products) {
                System.out.println(product);
                System.out.println();
            }
        }
    }
    
    private static void viewTopRated() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("                    TOP RATED PRODUCTS");
        System.out.println("=".repeat(70) + "\n");
        
        List<Product> products = system.getTopRatedProducts(10);
        for (Product product : products) {
            System.out.println(product);
            System.out.println();
        }
    }
    
    private static void searchProducts() {
        System.out.print("\nEnter search keyword: ");
        String keyword = scanner.nextLine();
        
        List<Product> results = system.searchProducts(keyword);
        
        if (results.isEmpty()) {
            System.out.println("\nNo products found.");
        } else {
            System.out.println("\n" + "=".repeat(70));
            System.out.println("                    SEARCH RESULTS");
            System.out.println("=".repeat(70) + "\n");
            
            for (Product product : results) {
                System.out.println(product);
                System.out.println();
            }
        }
    }
    
    private static void viewProductDetails() {
        System.out.print("\nEnter Product ID: ");
        String productId = scanner.nextLine().toUpperCase();
        
        Product product = system.getProduct(productId);
        if (product == null) {
            System.out.println("\nProduct not found!");
        } else {
            System.out.println("\n" + product.getDetailedInfo());
        }
    }
    
    private static void addToCart() {
        System.out.print("\nEnter Product ID: ");
        String productId = scanner.nextLine().toUpperCase();
        
        System.out.print("Enter Quantity: ");
        int quantity = scanner.nextInt();
        scanner.nextLine();
        
        if (system.addToCart(currentCustomerId, productId, quantity)) {
            System.out.println("\n✓ Added to cart!");
        } else {
            System.out.println("\n✗ Could not add to cart. Check stock availability.");
        }
    }
    
    private static void viewCart() {
        Customer customer = system.getCustomer(currentCustomerId);
        System.out.println("\n" + customer.getCart());
    }
    
    private static void checkout() {
        Customer customer = system.getCustomer(currentCustomerId);
        
        if (customer.getCart().isEmpty()) {
            System.out.println("\nYour cart is empty!");
            return;
        }
        
        System.out.println("\n" + customer.getCart());
        
        System.out.print("\nConfirm order? (yes/no): ");
        String confirm = scanner.nextLine().toLowerCase();
        
        if (!confirm.equals("yes") && !confirm.equals("y")) {
            System.out.println("\nOrder cancelled.");
            return;
        }
        
        System.out.print("Payment method (Credit Card/Debit Card/PayPal): ");
        String paymentMethod = scanner.nextLine();
        
        ECommerceSystem.OrderResult result = system.placeOrder(currentCustomerId, paymentMethod);
        
        System.out.println("\n" + result);
        
        if (result.isSuccess()) {
            System.out.println("\nOrder ID: " + result.getOrder().getOrderId());
        }
    }
    
    private static void viewOrders() {
        Customer customer = system.getCustomer(currentCustomerId);
        List<Order> orders = customer.getOrderHistory();
        
        if (orders.isEmpty()) {
            System.out.println("\nNo orders yet.");
            return;
        }
        
        System.out.println("\n" + "=".repeat(70));
        System.out.println("                      YOUR ORDERS");
        System.out.println("=".repeat(70) + "\n");
        
        for (Order order : orders) {
            System.out.printf("%s | Status: %s | Total: $%.2f | Items: %d\n",
                order.getOrderId(),
                order.getStatus(),
                order.getTotalAmount(),
                order.getTotalItems());
        }
    }
    
    private static void trackOrder() {
        System.out.print("\nEnter Order ID: ");
        String orderId = scanner.nextLine().toUpperCase();
        
        Order order = system.getOrder(orderId);
        if (order == null) {
            System.out.println("\nOrder not found!");
        } else {
            System.out.println("\n" + order.getTrackingInfo());
        }
    }
    
    private static void writeReview() {
        System.out.print("\nEnter Product ID: ");
        String productId = scanner.nextLine().toUpperCase();
        
        System.out.print("Rating (1-5): ");
        int rating = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Comment: ");
        String comment = scanner.nextLine();
        
        if (system.addReview(currentCustomerId, productId, rating, comment)) {
            System.out.println("\n✓ Review added successfully!");
        } else {
            System.out.println("\n✗ Could not add review.");
        }
    }
    
    private static void viewRecommendations() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("                 RECOMMENDED FOR YOU");
        System.out.println("=".repeat(70) + "\n");
        
        List<Product> recommendations = system.getRecommendations(currentCustomerId, 5);
        
        if (recommendations.isEmpty()) {
            System.out.println("No recommendations available yet.");
            System.out.println("Purchase some products to get personalized recommendations!");
        } else {
            for (Product product : recommendations) {
                System.out.println(product);
                System.out.println();
            }
        }
    }
    
    private static void adminMenu() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("                     ADMIN MENU");
        System.out.println("=".repeat(70));
        System.out.println("1. View All Orders");
        System.out.println("2. Update Order Status");
        System.out.println("3. View Low Stock Products");
        System.out.println("4. Sales Analytics");
        System.out.println("5. Best Selling Products");
        System.out.println("0. Back");
        System.out.println("=".repeat(70));
        System.out.print("Select: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        switch (choice) {
            case 1:
                viewAllOrders();
                break;
            case 2:
                updateOrderStatus();
                break;
            case 3:
                viewLowStock();
                break;
            case 4:
                viewAnalytics();
                break;
            case 5:
                viewBestSellers();
                break;
        }
    }
    
    private static void viewAllOrders() {
        List<Order> orders = system.getAllOrders();
        
        System.out.println("\n" + "=".repeat(70));
        System.out.println("                      ALL ORDERS");
        System.out.println("=".repeat(70) + "\n");
        
        for (Order order : orders) {
            System.out.printf("%s | Customer: %s | Status: %s | Total: $%.2f\n",
                order.getOrderId(),
                order.getCustomerId(),
                order.getStatus(),
                order.getTotalAmount());
        }
    }
    
    private static void updateOrderStatus() {
        System.out.print("\nEnter Order ID: ");
        String orderId = scanner.nextLine().toUpperCase();
        
        System.out.println("\nNew Status:");
        System.out.println("1. CONFIRMED");
        System.out.println("2. PROCESSING");
        System.out.println("3. SHIPPED");
        System.out.println("4. DELIVERED");
        System.out.print("Choice: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        Order.OrderStatus newStatus = null;
        switch (choice) {
            case 1: newStatus = Order.OrderStatus.CONFIRMED; break;
            case 2: newStatus = Order.OrderStatus.PROCESSING; break;
            case 3: newStatus = Order.OrderStatus.SHIPPED; break;
            case 4: newStatus = Order.OrderStatus.DELIVERED; break;
        }
        
        if (newStatus != null && system.updateOrderStatus(orderId, newStatus)) {
            System.out.println("\n✓ Order status updated!");
        } else {
            System.out.println("\n✗ Could not update status.");
        }
    }
    
    private static void viewLowStock() {
        List<Product> lowStock = system.getLowStockProducts();
        
        System.out.println("\n" + "=".repeat(70));
        System.out.println("                  LOW STOCK PRODUCTS");
        System.out.println("=".repeat(70) + "\n");
        
        if (lowStock.isEmpty()) {
            System.out.println("No low stock items.");
        } else {
            for (Product product : lowStock) {
                System.out.println(product);
                System.out.println();
            }
        }
    }
    
    private static void viewAnalytics() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("                   SALES ANALYTICS");
        System.out.println("=".repeat(70) + "\n");
        
        System.out.printf("Total Revenue: $%.2f\n", system.getTotalRevenue());
        System.out.printf("Total Orders: %d\n\n", system.getAllOrders().size());
        
        System.out.println("Sales by Category:");
        Map<String, Integer> sales = system.getCategorySales();
        for (Map.Entry<String, Integer> entry : sales.entrySet()) {
            System.out.printf("  %s: %d units\n", entry.getKey(), entry.getValue());
        }
    }
    
    private static void viewBestSellers() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("                  BEST SELLING PRODUCTS");
        System.out.println("=".repeat(70) + "\n");
        
        List<Product> bestSellers = system.getBestSellingProducts(5);
        for (Product product : bestSellers) {
            System.out.println(product);
            System.out.println();
        }
    }
}
