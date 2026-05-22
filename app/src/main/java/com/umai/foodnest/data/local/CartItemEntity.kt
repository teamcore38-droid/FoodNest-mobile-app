package com.umai.foodnest.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey val foodId: Int,
    val restaurantId: Int,
    val name: String,
    val price: Double,
    val imageUrl: String,
    var quantity: Int,
    val category: String
)