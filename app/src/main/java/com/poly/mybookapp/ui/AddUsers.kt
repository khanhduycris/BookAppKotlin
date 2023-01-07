package com.poly.mybookapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.auth.User
import com.poly.mybookapp.R
import com.poly.mybookapp.databinding.ActivityAddUsersBinding
import com.poly.mybookapp.model.AddUserModel
import com.poly.mybookapp.model.UserModel

class AddUsers : AppCompatActivity() {
    private lateinit var binding: ActivityAddUsersBinding
    private lateinit var databaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnYes.setOnClickListener {
            val name = binding.edtFulName.text.toString()
            val phone = binding.edtPhone.text.toString()
            val address = binding.edtAddress.text.toString()


            databaseReference = FirebaseDatabase.getInstance().getReference().child("Users")
            val Users = AddUserModel(name, phone, address)
            databaseReference.child(name).setValue(Users).addOnSuccessListener {
                binding.edtFulName.text.clear()
                binding.edtPhone.text.clear()
                binding.edtAddress.text.clear()
            }.addOnFailureListener {
            }
        }
    }
}