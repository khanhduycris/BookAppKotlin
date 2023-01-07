package com.poly.mybookapp.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase
import com.poly.mybookapp.R
import com.poly.mybookapp.event.UpdateCartEvent
import com.poly.mybookapp.model.CartModel
import com.poly.mybookapp.uibook.Oder
import org.greenrobot.eventbus.EventBus
import java.util.logging.Handler

class MyCartAdapter(
    private val context: Context,
    private val cartModelList: List<CartModel>,
    ):RecyclerView.Adapter<MyCartAdapter.MyCartViewHolder>(){

    private lateinit var runnable: Runnable
    private lateinit var handler: android.os.Handler

        class MyCartViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView),
            View.OnClickListener {
            var imageView: ImageView? = null
            var btnMinus:ImageView? = null
            var btnDelete:ImageView? = null
            var btnPlus:ImageView? = null
            var tvName:TextView? = null
            var tvPrice:TextView? = null
            var tvQuantity:TextView? = null
//            var btnOder:Button? = null

            init {
                btnDelete = itemView.findViewById(R.id.btnDelete) as ImageView
                btnMinus = itemView.findViewById(R.id.btnMinus) as ImageView
                btnPlus = itemView.findViewById(R.id.btnPlus) as ImageView
                tvName = itemView.findViewById(R.id.tvName) as TextView
                tvPrice = itemView.findViewById(R.id.tvPrice) as TextView
                tvQuantity = itemView.findViewById(R.id.tvQuantity) as TextView
                imageView = itemView.findViewById(R.id.imageView) as ImageView
//                btnOder = itemView.findViewById(R.id.btnOder) as Button

            }

            override fun onClick(v: View?) {
                TODO("Not yet implemented")
            }


        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyCartViewHolder {
        return MyCartViewHolder(LayoutInflater.from(context)
            .inflate(R.layout.item_cart, parent, false))

    }

    override fun onBindViewHolder(holder: MyCartViewHolder, position: Int) {
        handler = android.os.Handler(Looper.getMainLooper())
        runnable = Runnable {  }
        var soLuong: Int? = null
        var cartItem = cartModelList.get(position)
        Glide.with(context)
            .load(cartModelList[position].image)
            .into(holder.imageView!!)

        holder.tvName!!.text = StringBuilder().append(cartModelList[position].name)
        holder.tvPrice!!.text = StringBuilder("$").append(cartModelList[position].price)
        holder.tvQuantity!!.text = StringBuilder("").append(cartModelList[position].quantity)
        soLuong = cartModelList.get(position).quantity





        //event
        holder.btnMinus!!.setOnClickListener {_ ->
            soLuong = soLuong!!+1
            runnable = Runnable {
                updateFirebase(cartItem)
            }
            minusCartItem(holder, cartModelList[position])
            onTouch()
        }

        holder.btnPlus!!.setOnClickListener {_ ->
            if (soLuong!! >= 1){
                soLuong = soLuong!!-1
            }else{
                soLuong = 0
            }
            runnable = Runnable {
                updateFirebase(cartItem)
            }
            plusCartItem(holder, cartModelList[position])
            onTouch()
        }

        holder

        holder.btnDelete!!.setOnClickListener {_ ->
            val dialog = AlertDialog.Builder(context)
                .setTitle("Delete item")
                .setMessage("Do you relly want to delete item")
                .setNegativeButton("CANCEL") {
                    dialog,_ -> dialog.dismiss()
                }
                .setNegativeButton("DELETE") {dialog, _ ->
                        notifyItemRemoved(position)
                    FirebaseDatabase.getInstance()
                        .getReference("Cart")
                        .child("UNIQUE_USER_ID")
                        .child(cartModelList[position].key!!)
                        .removeValue()
                        .addOnSuccessListener {
                            EventBus.getDefault().postSticky(UpdateCartEvent())
                        }

                }
                .create()
            dialog.show()
        }

//        holder.btnOder!!.setOnClickListener(View.OnClickListener { v ->
//            val builder = AlertDialog.Builder(context)
//            builder.setTitle("Do you want to order?")
////            builder.setMessage("We have a message")
//            //builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))
//
////            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
////                Toast.makeText(context,
////                    android.R.string.yes, Toast.LENGTH_SHORT).show()
////            }
//
//            builder.setNegativeButton(android.R.string.no) { dialog, which ->
//                Toast.makeText(context,
//                    android.R.string.no, Toast.LENGTH_SHORT).show()
//            }
//
////            builder.setNeutralButton("Yes") { dialog, which ->
////                Toast.makeText(context,
////                    "Oder successflly", Toast.LENGTH_SHORT).show()
////            }
//
//            builder.setNeutralButton("Yes") { dialog, which ->
//                Toast.makeText(context,
//                    "Oder Successlly", Toast.LENGTH_SHORT).show()
//                val intent = Intent(v.context, Oder::class.java)
//                intent.putExtra("name",cartModelList[position].name)
//                intent.putExtra("price",cartModelList[position].price)
//                intent.putExtra("image", cartModelList[position].image)
//                v.context.startActivity(intent)
//            }
//            builder.show()
//        })
    }

    private fun onTouch() {
        stopHander()
        starHander()
    }

    private fun starHander() {
        handler.postDelayed(runnable, 3000)
    }

    private fun stopHander() {
        handler.removeCallbacks(runnable)
    }

    private fun plusCartItem(holder: MyCartViewHolder, cartModel: CartModel) {
        cartModel.quantity += 1
        cartModel.totalPrice = cartModel.quantity * cartModel.price!!.toFloat()
        holder.tvQuantity!!.text = StringBuilder("").append(cartModel.quantity)

        updateFirebase(cartModel)
    }

    private fun minusCartItem(holder: MyCartViewHolder, cartModel: CartModel) {
        if (cartModel.quantity > 1){
            cartModel.quantity -= 1
            cartModel.totalPrice = cartModel.quantity * cartModel.price!!.toFloat()
            holder.tvQuantity!!.text = StringBuilder("").append(cartModel.quantity)

            updateFirebase(cartModel)
        }
    }

    private fun updateFirebase(cartModel: CartModel){
        FirebaseDatabase.getInstance()
            .getReference("Cart")
            .child("UNIQUE_USER_ID")
            .child(cartModel.key!!)
            .setValue(cartModel)
            .addOnSuccessListener {
                EventBus.getDefault().postSticky(UpdateCartEvent())
            }
    }

    override fun getItemCount(): Int {
        return cartModelList.size
    }
}

