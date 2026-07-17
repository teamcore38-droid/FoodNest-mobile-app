package com.umai.foodnest.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.umai.foodnest.data.SampleData
import com.umai.foodnest.data.model.Restaurant

class HomeViewModel : ViewModel() {
    private val _restaurants = MutableLiveData<List<Restaurant>>()
    val restaurants: LiveData<List<Restaurant>> = _restaurants

    init { loadRestaurants() }

    fun loadRestaurants(query: String = "", category: String = "All") {
        var list = SampleData.restaurants
        if (query.isNotBlank())
            list = list.filter {
                it.name.contains(query, true) ||
                        it.category.contains(query, true) ||
                        it.description.contains(query, true)
            }
        if (category != "All")
            list = list.filter { it.category == category }
        _restaurants.value = list
    }
}