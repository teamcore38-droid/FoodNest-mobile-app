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

    private val _orderStatus = MutableLiveData<OrderStatus>()
    val orderStatus: LiveData<OrderStatus> = _orderStatus

    init { _orders.value = SampleData.sampleOrders }

    fun loadOrders() { _orders.value = SampleData.sampleOrders }

    fun setCurrentOrder(order: Order) {
        _currentOrder.value = order
        _orderStatus.value = order.status
    }

    fun getActiveOrders() = _orders.value?.filter {
        it.status != OrderStatus.DELIVERED && it.status != OrderStatus.CANCELLED
    } ?: emptyList()

    fun getPastOrders() = _orders.value?.filter {
        it.status == OrderStatus.DELIVERED || it.status == OrderStatus.CANCELLED
    } ?: emptyList()
}