package com.poly.mybookapp.model

data class AddUserModel(
    val name: String,
    val phone: String,
    val address: String){
    constructor(): this("","","")}
