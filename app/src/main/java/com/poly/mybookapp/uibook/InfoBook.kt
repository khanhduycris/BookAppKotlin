package com.poly.mybookapp.uibook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.poly.mybookapp.R
import com.poly.mybookapp.databinding.ActivityInfoBookBinding
import kotlinx.android.synthetic.main.activity_cart.*

class InfoBook : AppCompatActivity() {
    private lateinit var binding: ActivityInfoBookBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        binding.btnDatHang.setOnClickListener {
//            Toast.makeText(this, "Đặt hàng thành công", Toast.LENGTH_LONG).show()
//        }

        binding.btnBack.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        val name : TextView = findViewById(R.id.tvTenSach)
        val price: TextView = findViewById(R.id.tvGia)
        val thuongHieu: TextView = findViewById(R.id.tvThuongHieu)
        val tinhTrang: TextView = findViewById(R.id.tvTinhTrang)
        val noiBan: TextView = findViewById(R.id.tvNoiBan)
        val image: ImageView = findViewById(R.id.imgBook)

        val bundle : Bundle?= intent.extras
        val tenSach = bundle!!.getString("name")
        val mThuongHieu = bundle!!.getString("Trademark")
        val mTinhTrang = bundle!!.getString("Condition")
        val mNoiBan = bundle!!.getString("Forsaleat")
        val giaSach = bundle.getString("price")
        val anh = bundle.getString("image")
        name.text = tenSach
        thuongHieu.text = mThuongHieu
        tinhTrang.text = mTinhTrang
        noiBan.text = mNoiBan
        price.text = giaSach
        Glide.with(this)
            .load(anh)
            .into(image)
    }
}