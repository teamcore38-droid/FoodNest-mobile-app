package com.umai.foodnest.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.umai.foodnest.data.SampleData
import com.umai.foodnest.data.model.Order
import com.umai.foodnest.data.model.OrderStatus

class OrderViewModel : ViewModel() {
    private val _orders = MutableLiveData<List<Order>>()
    val orders: LiveData<List<Order>> = _orders

    private val _currentOrder = MutableLiveData<Order>()
    val currentOrder: LiveData<Order> = _currentOrder

    init { loadOrders() }

    fun loadOrders() { _orders.value = SampleData.sampleOrders }

    fun setCurrentOrder(order: Order) { _currentOrder.value = order }

    fun placeOrder(order: Order) {
        val list = _orders.value?.toMutableList() ?: mutableListOf()
        list.add(0, order)
        _orders.value = list
        _currentOrder.value = order
    }
}