package com.umai.foodnest.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.umai.foodnest.data.model.CartItem
import com.umai.foodnest.data.model.FoodItem

class CartViewModel : ViewModel() {
    private val _cartItems = MutableLiveData<MutableList<CartItem>>(mutableListOf())
    val cartItems: LiveData<MutableList<CartItem>> = _cartItems

    private val _cartTotal = MutableLiveData(0.0)
    val cartTotal: LiveData<Double> = _cartTotal

    private val _cartCount = MutableLiveData(0)
    val cartCount: LiveData<Int> = _cartCount

    fun addToCart(foodItem: FoodItem) {
        val list = _cartItems.value ?: mutableListOf()
        val existing = list.find { it.foodItem.id == foodItem.id }
        if (existing != null) existing.quantity++
        else list.add(CartItem(foodItem, 1))
        _cartItems.value = list
        updateTotals()
    }

    fun removeFromCart(foodItem: FoodItem) {
        val list = _cartItems.value ?: mutableListOf()
        list.removeAll { it.foodItem.id == foodItem.id }
        _cartItems.value = list
        updateTotals()
    }

    fun decreaseQuantity(foodItem: FoodItem) {
        val list = _cartItems.value ?: mutableListOf()
        val item = list.find { it.foodItem.id == foodItem.id }
        if (item != null) {
            if (item.quantity > 1) item.quantity--
            else list.remove(item)
        }
        _cartItems.value = list
        updateTotals()
    }

    fun clearCart() {
        _cartItems.value = mutableListOf()
        updateTotals()
    }

    private fun updateTotals() {
        val list = _cartItems.value ?: mutableListOf()
        _cartTotal.value = list.sumOf { it.totalPrice }
        _cartCount.value = list.sumOf { it.quantity }
    }

    fun getItemCount(foodItem: FoodItem): Int =
        _cartItems.value?.find { it.foodItem.id == foodItem.id }?.quantity ?: 0
}