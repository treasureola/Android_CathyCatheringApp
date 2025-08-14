package com.example.cathycatering

data class UserInfo(
    val userName: String,
    val userEmail: String,
    val userPhone: String,
){
    constructor(): this("","","")
}

