package com.poly.mybookapp.listener

import com.poly.mybookapp.model.BookModel

interface BookLoadListener {
    fun onBookLoadSuccess(bookModelList:List<BookModel>)
    fun onBookLoadFailed(message:String?)
}