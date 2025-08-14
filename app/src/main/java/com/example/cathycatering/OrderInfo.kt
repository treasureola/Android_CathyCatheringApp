package com.example.cathycatering

data class OrderInfo(
    val userEmail: String,
    val orderDate: String,
    val product: MutableList<CartItem>,
) {
    constructor() : this("", "", mutableListOf())
}

data class OrderList(
    val date: String,
){
    constructor(): this("")
}
