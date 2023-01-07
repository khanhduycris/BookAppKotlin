package com.poly.mybookapp.ui

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.poly.mybookapp.R
import com.poly.mybookapp.databinding.ActivityProfileBinding
import com.poly.mybookapp.model.UserModel

class Profile : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var sto : FirebaseStorage
    private lateinit var imgLogout : ImageView
    private lateinit var binding: ActivityProfileBinding
    lateinit var image : Uri


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imgProfile.setOnClickListener {
            chonanh()
        }

    imgLogout = findViewById(R.id.img_logout)

    auth = FirebaseAuth.getInstance()
    sto = FirebaseStorage.getInstance()
    loadUserInfo()

        imgLogout.setOnClickListener {
            val dialog = layoutInflater.inflate(R.layout.custom_dialog, null)
            val myDialog = Dialog(this)
            myDialog.setContentView(dialog)

            myDialog.setCancelable(true)
            myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            myDialog.show()

            val btnYes = dialog.findViewById<Button>(R.id.btnYes)
            btnYes.setOnClickListener {
                startActivity(Intent(this, LoginActivity::class.java))
                Toast.makeText(this, "Đăng xuất thành công", Toast.LENGTH_LONG).show()
            }
            val btnNo = dialog.findViewById<Button>(R.id.btnNo)
            btnNo.setOnClickListener {
                myDialog.dismiss()
            }
        }

    }

    private fun chonanh() {
//        val intent = Intent()
//        intent.type = "iamge/*"
//        intent.action = Intent.ACTION_GET_CONTENT
//
//        startActivityForResult(intent, 100)

        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 100);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 100 && resultCode == RESULT_OK){
            image = data?.data!!
            binding.imgProfile.setImageURI(image)
        }
    }

    private fun loadUserInfo(){
        val ref = FirebaseDatabase.getInstance()
            .getReference("user")
        ref.child(auth.uid!!)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val name = "${snapshot.child("name").value}"
                    val phone = "${snapshot.child("phone").value}"
                    val email = "${snapshot.child("email").value}"

                    binding.tvName.text = name
                    binding.tvEmail.text = email
                    binding.tvPhone.text = phone

                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }
}