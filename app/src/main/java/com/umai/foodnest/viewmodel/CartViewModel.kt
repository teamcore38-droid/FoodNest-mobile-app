package com.umai.foodnest.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.umai.foodnest.data.local.AppDatabase
import com.umai.foodnest.data.model.FoodItem
import com.umai.foodnest.data.repository.CartRepository
import kotlinx.coroutines.launch

class CartViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = CartRepository(AppDatabase.getInstance(application))

    val cartItems = repo.getCartItems().asLiveData()
    val cartCount = repo.getCartCount().asLiveData()
    val cartTotal = repo.getCartTotal().asLiveData()

    fun addToCart(foodItem: FoodItem) = viewModelScope.launch {
        repo.addToCart(foodItem)
    }

    fun removeFromCart(foodItem: FoodItem) = viewModelScope.launch {
        repo.removeFromCart(foodItem)
    }

    fun clearCart() = viewModelScope.launch { repo.clearCart() }
}