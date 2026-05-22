package com.umai.foodnest.data.repository

import com.umai.foodnest.data.local.AppDatabase
import com.umai.foodnest.data.local.CartItemEntity
import com.umai.foodnest.data.model.CartItem
import com.umai.foodnest.data.model.FoodItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CartRepository(private val db: AppDatabase) {

    fun getCartItems(): Flow<List<CartItem>> =
        db.cartDao().getAllItems().map { entities ->
            entities.map { it.toCartItem() }
        }

    fun getCartCount(): Flow<Int> = db.cartDao().getCartCount()

    fun getCartTotal(): Flow<Double> =
        db.cartDao().getCartTotal().map { it ?: 0.0 }

    suspend fun addToCart(foodItem: FoodItem) {
        db.cartDao().insertItem(foodItem.toEntity())
    }

    suspend fun removeFromCart(foodItem: FoodItem) {
        db.cartDao().deleteItem(foodItem.toEntity())
    }

    suspend fun updateQuantity(entity: CartItemEntity) {
        db.cartDao().updateItem(entity)
    }

    suspend fun clearCart() = db.cartDao().clearCart()

    private fun CartItemEntity.toCartItem() = CartItem(
        FoodItem(foodId, restaurantId, name, "", price, imageUrl, category),
        quantity
    )

    private fun FoodItem.toEntity() = CartItemEntity(
        id, restaurantId, name, price, imageUrl, 1, category
    )
}