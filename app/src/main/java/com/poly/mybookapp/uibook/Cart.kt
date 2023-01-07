package com.poly.mybookapp.uibook

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.poly.mybookapp.R
import com.poly.mybookapp.adapter.MyCartAdapter
import com.poly.mybookapp.listener.ICartLoadListener
import com.poly.mybookapp.model.AddUserModel
import com.poly.mybookapp.model.CartModel
import com.poly.mybookapp.ui.AddUsers
import com.poly.mybookapp.ui.InfoUser
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.activity_main.*


class Cart : AppCompatActivity(), ICartLoadListener {
    var sum = 0.0
    lateinit var btnOder: Button
    var cartLoadListener:ICartLoadListener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        init()
        loadCartFromFirebase()

        btnOder = findViewById(R.id.btnOder)
        btnOder.setOnClickListener {
            val dialog: AlertDialog.Builder = AlertDialog.Builder(this@Cart)
            dialog.setCancelable(false)
            dialog.setTitle(" ")
            dialog.setMessage("Bạn có chắc chắn đặt hàng ...")
            dialog.setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, id ->
                var intent : Intent = Intent(this, Oder::class.java)
                intent.putExtra("sum1", sum)
                Log.d("TAG", "onCreate: cart"+sum)
                Toast.makeText(this, "Đặt hàng thành công ..", Toast.LENGTH_LONG).show()
                startActivity(intent)
            })
                .setNegativeButton("Cancel ", DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()
                    //Action for "Cancel".
                })

            val alert: AlertDialog = dialog.create()
            alert.show()
        }

    }

    private fun loadCartFromFirebase(){
        val cartModels : MutableList<CartModel> = ArrayList()
        FirebaseDatabase.getInstance()
            .getReference("Cart")
            .child("UNIQUE_USER_ID")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (cartSnapshot in snapshot.children){
                        val cartModel = cartSnapshot.getValue(CartModel :: class.java)
                        cartModel!!.key = cartSnapshot.key
                        cartModels.add(cartModel)
                    }
                    cartLoadListener!!.onLoadCartSuccess(cartModels)
                }

                override fun onCancelled(error: DatabaseError) {
                    cartLoadListener!!.onLoadCartFailed(error.message)
                }

            })
    }

    private fun init(){
        cartLoadListener = this
        val layoutManager = LinearLayoutManager(this)
        rcv_cart!!.layoutManager = layoutManager
        rcv_cart!!.addItemDecoration(DividerItemDecoration(this, layoutManager.orientation))
        btnBack!!.setOnClickListener { finish() }
    }

    override fun onLoadCartSuccess(cartModelList: List<CartModel>) {
        for (cartModel in cartModelList!!){
            sum += cartModel!!.totalPrice
//            Log.d("TAG", "onLoadCartSuccess: "+sum)
        }
        tvPrice .text = StringBuilder("$").append(sum)
        val adapter = MyCartAdapter(this,cartModelList)
        rcv_cart!!.adapter = adapter

    }

    override fun onLoadCartFailed(message: String?) {
        Snackbar.make(mainLayout, message!!, Snackbar.LENGTH_LONG).show()
    }
}