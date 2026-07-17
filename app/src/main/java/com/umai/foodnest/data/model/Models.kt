package com.umai.foodnest.data.model

data class Restaurant(
    val id: Int = 0,
    val name: String = "",
    val rating: Float = 0f,
    val deliveryTime: Int = 0,
    val deliveryFee: Double = 0.0,
    val imageUrl: String = "",
    val category: String = "",
    val isOpen: Boolean = true,
    val address: String = "",
    val totalReviews: Int = 0,
    val description: String = ""
)

data class FoodItem(
    val id: Int = 0,
    val restaurantId: Int = 0,
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val imageUrl: String = "",
    val category: String = "",
    val isPopular: Boolean = false
)

data class CartItem(
    val foodItem: FoodItem,
    var quantity: Int = 1
) {
    val totalPrice get() = foodItem.price * quantity
}

data class Order(
    val id: String = "",
    val restaurantName: String = "",
    val restaurantImage: String = "",
    val items: List<CartItem> = emptyList(),
    val status: OrderStatus = OrderStatus.CONFIRMED,
    val subtotal: Double = 0.0,
    val deliveryFee: Double = 200.0,
    val total: Double = 0.0,
    val deliveryAddress: String = "",
    val paymentMethod: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val estimatedArrival: Int = 30
) {
    val formattedTotal get() = "LKR %.2f".format(total)
    val formattedDate get() = java.text.SimpleDateFormat(
        "dd MMM yyyy", java.util.Locale.getDefault()
    ).format(java.util.Date(timestamp))
}

enum class OrderStatus { CONFIRMED, PREPARING, ON_THE_WAY, DELIVERED, CANCELLED }

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val profileImageUrl: String = ""
)

data class Offer(
    val id: Int = 0,
    val title: String = "",
    val description: String = "",
    val code: String = "",
    val discount: Int = 0,
    val imageUrl: String = "",
    val type: OfferType = OfferType.ALL,
    val bgColor: String = "#FF6B35"
)

enum class OfferType { ALL, COUPONS, FLASH }

data class AppNotification(
    val id: Int = 0,
    val title: String = "",
    val message: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false,
    val type: String = ""
)

data class Address(
    val id: Int = 0,
    val label: String = "",
    val fullAddress: String = "",
    val city: String = "",
    val isDefault: Boolean = false
)

data class AdminStats(
    val totalOrders: Int = 0,
    val revenue: Double = 0.0,
    val pending: Int = 0,
    val cancelled: Int = 0,
    val avgOrderValue: Double = 0.0
)