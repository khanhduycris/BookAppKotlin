package com.poly.mybookapp.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.poly.mybookapp.R
import com.poly.mybookapp.databinding.ActivityMainBinding
import com.poly.mybookapp.databinding.ActivityOderBinding
import kotlinx.android.synthetic.main.nav_header.*
import kotlinx.android.synthetic.main.nav_header.view.*

class NavHeader : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private lateinit var sto : FirebaseStorage
    private lateinit var currentUser: FirebaseUser
    private lateinit var navigationView: NavigationView
    private lateinit var handerView: View
    private lateinit var imgProfile: ImageView
    private lateinit var nameProfile: TextView
    private lateinit var emailProfile: TextView
//    lateinit var image : Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nav_header)

        imgProfile =  findViewById(R.id.imgProfile1)
        nameProfile = findViewById(R.id.tvNameProfile1)
        emailProfile = findViewById(R.id.tvEmailProfile1)

        auth = FirebaseAuth.getInstance()
        sto = FirebaseStorage.getInstance()
        currentUser = auth.currentUser!!
        loadUserInfo()

    upDateUser()

//        imgProfile.setOnClickListener {
//            chonanh()
//        }
    }

    private fun upDateUser() {
        navigationView = findViewById(R.id.nav_view)
        handerView = navigationView.getHeaderView(0)
    }

    private fun loadUserInfo(){
        val ref = FirebaseDatabase.getInstance()
            .getReference("user")
        ref.child(auth.uid!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val name = "${snapshot.child("name").value}"
//                    val phone = "${snapshot.child("phone").value}"
                    val email = "${snapshot.child("email").value}"

                    tvEmailProfile1.text = name
                    tvNameProfile1.text = email

                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

//    private fun chonanh() {
//        val intent = Intent()
//        intent.type = "image/*"
//        intent.action = Intent.ACTION_GET_CONTENT
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 100)
//    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if(requestCode == 100 && resultCode == RESULT_OK){
//            image = data?.data!!
//            imgProfile.setImageURI(image)
//        }
//    }
}