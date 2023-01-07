package com.poly.mybookapp.listener

import android.os.Message
import com.poly.mybookapp.model.CartModel

interface ICartLoadListener {
    fun onLoadCartSuccess(cartModelList: List<CartModel>)
    fun onLoadCartFailed(message: String?)
}