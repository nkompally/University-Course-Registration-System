# E-Commerce System

A Java app that simulates an online store. You can browse products, add things to a cart, place orders, track them, leave reviews, and get recommendations. There's also an admin side for managing orders and checking inventory.

## Running it

Java 8+ is all you need.

```bash
javac *.java
java ECommerceApp
```

To log in, use one of the demo accounts: `C001`, `C002`, or `C003`. You can also browse and search products without logging in.

## Files

- `Product.java` — products, stock, reviews
- `Review.java` — ratings and comments
- `ShoppingCart.java` — cart logic
- `Order.java` — orders and status tracking
- `Customer.java` — customer data and history
- `ECommerceSystem.java` — where all the business logic lives
- `ECommerceApp.java` — the menu you actually interact with

## How orders move through the system

Once you place an order it goes: Pending → Confirmed → Processing → Shipped → Delivered. You can cancel up until it ships, but after that you're locked out. Delivered is the end of the line.

## A few other things

Stock goes down when you order and comes back if you cancel. Anything at 10 units or below shows as low stock.

Reviews work for anyone, but if you actually bought the product yours gets marked as a verified purchase.

Recommendations are based on what categories you've ordered from before — it finds other in-stock stuff from those same categories and sorts by rating.

Admins can pull up revenue totals, see which categories are selling most, and view a best-sellers list.
