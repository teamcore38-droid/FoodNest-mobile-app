package com.umai.foodnest.data

import com.umai.foodnest.data.model.*

object SampleData {

    val restaurants = listOf(
        Restaurant(1, "KFC", 4.8f, 20, 200.0,
            "https://via.placeholder.com/400x200/FF6B35/white?text=KFC", "Fast Food"),
        Restaurant(2, "Barista", 4.5f, 18, 150.0,
            "https://via.placeholder.com/400x200/8B4513/white?text=Barista", "Cafe"),
        Restaurant(3, "Pizza Hut", 4.3f, 30, 250.0,
            "https://via.placeholder.com/400x200/CC0000/white?text=Pizza+Hut", "Pizza"),
        Restaurant(4, "McDonald's", 4.6f, 15, 180.0,
            "https://via.placeholder.com/400x200/FFC300/black?text=McD", "Fast Food"),
        Restaurant(5, "Subway", 4.2f, 25, 120.0,
            "https://via.placeholder.com/400x200/00843D/white?text=Subway", "Sandwiches")
    )

    val menuItems = mapOf(
        1 to listOf(
            FoodItem(1, 1, "Zinger Burger", "Crispy chicken fillet burger", 1450.0,
                "https://via.placeholder.com/300x200/FF6B35/white?text=Zinger", "Burger"),
            FoodItem(2, 1, "Double Decker Burger", "Double patty delight", 2250.0,
                "https://via.placeholder.com/300x200/FF8C00/white?text=DDouble", "Burger"),
            FoodItem(3, 1, "Cheese Burger", "Classic with cheese", 1400.0,
                "https://via.placeholder.com/300x200/FFD700/black?text=Cheese", "Burger"),
            FoodItem(4, 1, "Hot Wings (6pc)", "Spicy wings", 1200.0,
                "https://via.placeholder.com/300x200/FF4500/white?text=Wings", "Chicken"),
            FoodItem(5, 1, "French Fries", "Crispy golden fries", 600.0,
                "https://via.placeholder.com/300x200/F4D03F/black?text=Fries", "Sides")
        ),
        2 to listOf(
            FoodItem(6, 2, "Cappuccino", "Rich espresso with milk foam", 750.0,
                "https://via.placeholder.com/300x200/6F4E37/white?text=Cappuccino", "Coffee"),
            FoodItem(7, 2, "Latte", "Smooth milk coffee", 850.0,
                "https://via.placeholder.com/300x200/8B7355/white?text=Latte", "Coffee"),
            FoodItem(8, 2, "Muffin", "Blueberry muffin", 450.0,
                "https://via.placeholder.com/300x200/4169E1/white?text=Muffin", "Bakery")
        )
    )

    val offers = listOf(
        Offer(1, "10% OFF", "First order at Barista", "BAR10", 10,
            "https://via.placeholder.com/300x150/8B4513/white?text=10+OFF", OfferType.ALL),
        Offer(2, "Free Delivery", "Orders above LKR 2000", "FREEDEL", 0,
            "https://via.placeholder.com/300x150/4CAF50/white?text=Free+Delivery", OfferType.ALL),
        Offer(3, "50% OFF", "Flash sale today only", "FLASH50", 50,
            "https://via.placeholder.com/300x150/FF6B35/white?text=50+OFF", OfferType.FLASH)
    )

    val notifications = listOf(
        AppNotification(1, "Order on the way!", "Your KFC order #1024 is being delivered", type = "order"),
        AppNotification(2, "50% off at Barista!", "Valid today only. Code: BAR50", type = "promo"),
        AppNotification(3, "Order Delivered", "Order #1020 delivered. Rate your experience!", type = "order"),
        AppNotification(4, "New restaurant near you!", "Pizza Hut opened in Kandy Road", type = "system"),
        AppNotification(5, "Payment Successful", "LKR 3,150 paid for order #1022", type = "order")
    )

    val sampleOrders = listOf(
        Order("1024", "KFC",
            listOf(CartItem(menuItems[1]!![0], 1), CartItem(menuItems[1]!![2], 1)),
            OrderStatus.ON_THE_WAY, 2850.0, 200.0, 3050.0,
            "No. 20, Galle Road, Colombo 03", "Cash on Delivery",
            System.currentTimeMillis() - 3600000),
        Order("1023", "Barista",
            listOf(CartItem(menuItems[2]!![0], 2), CartItem(menuItems[2]!![2], 1)),
            OrderStatus.DELIVERED, 1950.0, 150.0, 2100.0,
            "120 Main Street", "Credit Card",
            System.currentTimeMillis() - 86400000)
    )

    val adminStats = AdminStats(
        totalOrders = 48,
        revenue = 152000.0,
        pending = 12,
        cancelled = 3,
        avgOrderValue = 2880.0
    )
}