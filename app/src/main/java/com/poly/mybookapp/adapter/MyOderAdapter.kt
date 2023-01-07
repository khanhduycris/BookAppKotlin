package com.poly.mybookapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.poly.mybookapp.R
import com.poly.mybookapp.listener.ICartLoadListener
import com.poly.mybookapp.listener.OderLoadListener
import com.poly.mybookapp.model.OderModel
import org.w3c.dom.Text

class MyOderAdapter(
    private val context: Context,
    private val oderList: List<OderModel>,
): RecyclerView.Adapter<MyOderAdapter.MyOderViewHolder>(){

    class MyOderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

//        val name : TextView = itemView.findViewById(R.id.tvTenBook)
//        val quantyti: TextView = itemView.findViewById(R.id.tvTongTien)
//        val totalPrice: TextView = itemView.findViewById(R.id.tvSoluong)
//        val image: ImageView = itemView.findViewById(R.id.imageView)

        var imageView : ImageView? = null
        var quantyti : TextView? = null
        var totalPrice : TextView? = null
        var name : TextView? = null

        init {
            imageView = itemView.findViewById(R.id.imageOder) as ImageView
            name = itemView.findViewById(R.id.tvTenBook) as TextView
            totalPrice = itemView.findViewById(R.id.tvTongTien) as TextView
            quantyti = itemView.findViewById(R.id.tvSoluong) as TextView

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyOderViewHolder {
        return MyOderViewHolder(LayoutInflater.from(context)
            .inflate(R.layout.item_oder, parent, false))
//        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_oder, parent, false)
//        return MyOderViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyOderViewHolder, position: Int) {

//        val currentitem = oderList[position]
        Glide.with(context)
            .load(oderList[position].image)
            .into(holder.imageView!!)
        holder.name!!.text = StringBuilder().append(oderList[position].name)
        holder.quantyti!!.text = StringBuilder("").append(oderList[position].quantity)
        holder.totalPrice!!.text = StringBuilder("").append(oderList[position].totalPrice)
    }

    override fun getItemCount(): Int {
        return oderList.size
    }
}
