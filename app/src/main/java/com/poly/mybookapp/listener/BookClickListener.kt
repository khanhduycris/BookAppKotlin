package com.poly.mybookapp.listener

import android.view.View
import java.text.FieldPosition

interface BookClickListener {
    fun onItemClickListener(view: View, position: Int)
}