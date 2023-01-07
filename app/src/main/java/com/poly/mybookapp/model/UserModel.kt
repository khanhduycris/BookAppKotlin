package com.poly.mybookapp.model

data class UserModel(
    val name: String,
    val email: String,
    val phone: String,
    val address: String,
    val uid: String){
    constructor(): this("","","", "","")
}