package com.poly.mybookapp.uibook

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.poly.mybookapp.R
import com.poly.mybookapp.adapter.MyOderAdapter
import com.poly.mybookapp.databinding.ActivityOderBinding
import com.poly.mybookapp.listener.OderLoadListener
import com.poly.mybookapp.model.BookModel
import com.poly.mybookapp.model.OderModel
import com.poly.mybookapp.ui.AddUsers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_oder.*

class Oder : AppCompatActivity(), OderLoadListener {

    private lateinit var oderLoadListener: OderLoadListener
    lateinit var tvTongTien: TextView
    private lateinit var auth : FirebaseAuth
    private lateinit var sto : FirebaseStorage
    private lateinit var btnBack: ImageView
    private lateinit var btnSua: Button
    private lateinit var binding: ActivityOderBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        sto = FirebaseStorage.getInstance()
        loadUserInfo()

//        btnSua = findViewById(R.id.btnSua)
//        btnSua.setOnClickListener {
//            val intent = Intent(this, AddUsers::class.java)
//            startActivity(intent)
//        }


        btnBack = findViewById(R.id.btnBackOder)
        btnBack.setOnClickListener {
           val intent = Intent(this, Cart::class.java)
            startActivity(intent)
        }
        tvTongTien = findViewById(R.id.tvPrice)

        val mIntent = intent
        val sum: Double = mIntent.getDoubleExtra("sum1", 0.0)
        Log.d("TAG", "onCreate: order" + sum )
        tvTongTien .text = StringBuilder("$").append(sum)
        init()
        loadOderFromFirebase()


    }

    private fun loadUserInfo(){
        val ref = FirebaseDatabase.getInstance()
            .getReference("user")
        ref.child(auth.uid!!)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val name = "${snapshot.child("name").value}"
                    val phone = "${snapshot.child("phone").value}"
                    val address = "${snapshot.child("address").value}"

                    binding.nameDonHang.text = name
                    binding.phoneDonHang.text = phone
                    binding.diachiDonHang.text = address

                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    private fun loadOderFromFirebase(){
        val oderModels : MutableList<OderModel> = ArrayList()
        FirebaseDatabase.getInstance().getReference("Cart").child("UNIQUE_USER_ID")
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        for (oderSnapshot in snapshot.children){
                            val oderModel = oderSnapshot.getValue(OderModel::class.java)
                            oderModel!!.key = oderSnapshot.key
                            oderModels.add(oderModel)
                        }
                        oderLoadListener.onLoadOderSuccess(oderModels)
                    }else{
                        oderLoadListener.onLoadOderFailed("Oder items not exits")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    oderLoadListener.onLoadOderFailed(error.message)
                }

            })
    }
    private fun init(){
        oderLoadListener = this

        val gridLayoutManager = GridLayoutManager(this, 1)
        rcv_oder.layoutManager = gridLayoutManager
    }

    override fun onLoadOderSuccess(oderModelList: List<OderModel>) {
        val adapter = MyOderAdapter(this, oderModelList)
        rcv_oder.adapter = adapter
    }

    override fun onLoadOderFailed(message: String?) {
        Snackbar.make(mainLayout,message!!, Snackbar.LENGTH_LONG).show()
    }

}
