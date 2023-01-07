package com.poly.mybookapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.poly.mybookapp.uibook.InfoBook
import com.poly.mybookapp.R
import com.poly.mybookapp.event.UpdateCartEvent
import com.poly.mybookapp.listener.BookClickListener
import com.poly.mybookapp.listener.BookLoadListener
import com.poly.mybookapp.listener.ICartLoadListener
import com.poly.mybookapp.model.BookModel
import com.poly.mybookapp.model.CartModel
import kotlinx.coroutines.*
import org.greenrobot.eventbus.EventBus
import java.util.*
import kotlin.collections.HashMap

class MyBookAdapter(
    private val context: Context,
    private val list: List<BookModel>,
    private val cartListener: ICartLoadListener
): RecyclerView.Adapter<MyBookAdapter.MyBookViewHolder>(){

    class MyBookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var imageView: ImageView? = null
        var tvName: TextView? = null
        var tvPrice: TextView? = null
        var tvThuongHieu: TextView? = null
        var tvTinhTrang: TextView? = null
        var tvNoiBan: TextView? = null
//        var btnInfoBook: Button? = null
        var btnDatHang: Button? = null

        private var clickListener:BookClickListener? = null

        fun setClickListener(clickListener: BookClickListener){
            this.clickListener = clickListener
        }
        init {
            imageView = itemView.findViewById(R.id.imageView) as ImageView
            tvName = itemView.findViewById(R.id.tvName) as TextView
//            tvThuongHieu = itemView.findViewById(R.id.tvThuongHieu)
//            tvNoiBan = itemView.findViewById(R.id.tvNoiBan)
//            tvTinhTrang = itemView.findViewById(R.id.tvTinhTrang)
            tvPrice = itemView.findViewById(R.id.tvPrice) as TextView
//            btnInfoBook = itemView.findViewById(R.id.infoBook)
            btnDatHang = itemView.findViewById(R.id.btnDatHang)

            btnDatHang!!.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            clickListener!!.onItemClickListener(v!!,adapterPosition)
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyBookViewHolder {
        return MyBookViewHolder(LayoutInflater.from(context)
            .inflate(R.layout.item_book,parent,false))
    }

    override fun onBindViewHolder(holder: MyBookViewHolder, position: Int) {
            Glide.with(context)
                .load(list[position].image)
                .into(holder.imageView!!)
            holder.tvName!!.text = StringBuilder().append(list[position].name)
//        holder.tvThuongHieu!!.text = StringBuilder().append(list[position].Trademark)
//        holder.tvTinhTrang!!.text = StringBuilder().append(list[position].Condition)
//        holder.tvNoiBan!!.text = StringBuilder().append(list[position].Forsaleat)
            holder.tvPrice!!.text = StringBuilder("$").append(list[position].price)

            holder.setClickListener(object : BookClickListener {
                override fun onItemClickListener(view: View, position: Int) {
                    addToCart(list[position])
                }

            })

            holder.imageView!!.setOnClickListener { v ->
                val intent = Intent(v.context, InfoBook::class.java)
                intent.putExtra("name", list[position].name)
//                intent.putExtra("Trademark", list[position].Trademark)
//                intent.putExtra("Condition", list[position].Condition)
//                intent.putExtra("Forsaleat", list[position].Forsaleat)
                intent.putExtra("price", list[position].price)
                intent.putExtra("image", list[position].image)
            }
    }

    private fun addToCart(bookModel: BookModel) {
        val userCart = FirebaseDatabase.getInstance()
            .getReference("Cart")
            .child("UNIQUE_USER_ID") // here is sumular user ID , you can user firebase auth uid here

        userCart.child(bookModel.key!!)
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){ //If item alrealy in cart, just update
                        val cartModel = snapshot.getValue(CartModel::class.java)
                        val updateData: MutableMap<String, Any> = HashMap()
                        cartModel!!.quantity = cartModel!!.quantity + 1
                        updateData["quantity"] = cartModel!!.quantity
                        updateData["totalPrice"] = cartModel!!.quantity * cartModel.price!!.toFloat()
                        userCart.child(bookModel.key!!)
                            .updateChildren(updateData)
                            .addOnSuccessListener {
                                EventBus.getDefault().postSticky(UpdateCartEvent())
                                cartListener.onLoadCartFailed("Success add to cart")
                            }.addOnFailureListener {e->
                                cartListener.onLoadCartFailed(e.message)
                            }
                    }else{
                        val cartModel = CartModel()
                        cartModel.key = bookModel.key
                        cartModel.name = bookModel.name
                        cartModel.image = bookModel.image
                        cartModel.price = bookModel.price
                        cartModel.quantity = 1
                        cartModel.totalPrice = bookModel.price!!.toFloat()

                        // them vao gio hang
                        userCart.child(bookModel.key!!)
                            .setValue(cartModel)
                            .addOnSuccessListener {
                                EventBus.getDefault().postSticky(UpdateCartEvent())
                            cartListener.onLoadCartFailed("Success add to cart")
                        }.addOnFailureListener {e->
                            cartListener.onLoadCartFailed(e.message)
                        }

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                   cartListener.onLoadCartFailed(error.message)
                }

            })
    }

    override fun getItemCount(): Int {
        return list.size
    }
}