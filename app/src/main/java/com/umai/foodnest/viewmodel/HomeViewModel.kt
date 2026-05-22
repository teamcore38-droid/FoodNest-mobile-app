package com.umai.foodnest.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.umai.foodnest.data.SampleData
import com.umai.foodnest.data.model.Restaurant

class HomeViewModel : ViewModel() {

    private val _restaurants = MutableLiveData<List<Restaurant>>()
    val restaurants: LiveData<List<Restaurant>> = _restaurants

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init { loadRestaurants() }

    fun loadRestaurants(query: String = "", category: String = "All") {
        _isLoading.value = true
        var list = SampleData.restaurants
        if (query.isNotBlank())
            list = list.filter { it.name.contains(query, ignoreCase = true) }
        if (category != "All")
            list = list.filter { it.category == category }
        _restaurants.value = list
        _isLoading.value = false
    }
}