package com.umai.foodnest.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.umai.foodnest.data.SampleData
import com.umai.foodnest.data.model.FoodItem
import com.umai.foodnest.data.model.Restaurant

class MenuViewModel : ViewModel() {
    private val _restaurant = MutableLiveData<Restaurant>()
    val restaurant: LiveData<Restaurant> = _restaurant

    private val _menuItems = MutableLiveData<List<FoodItem>>()
    val menuItems: LiveData<List<FoodItem>> = _menuItems

    private val _filteredItems = MutableLiveData<List<FoodItem>>()
    val filteredItems: LiveData<List<FoodItem>> = _filteredItems

    fun loadMenu(restaurantId: Int) {
        _restaurant.value = SampleData.restaurants.find { it.id == restaurantId }
        val items = SampleData.menuItems[restaurantId] ?: emptyList()
        _menuItems.value = items
        _filteredItems.value = items
    }

    fun filterByCategory(category: String) {
        _filteredItems.value = if (category == "All") _menuItems.value
        else _menuItems.value?.filter { it.category == category }
    }
}