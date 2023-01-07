package com.poly.mybookapp.listener

import com.poly.mybookapp.model.OderModel

interface OderLoadListener {
    fun onLoadOderSuccess(oderModelList: List<OderModel>)
    fun onLoadOderFailed(message: String?)
}