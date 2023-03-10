package com.poly.mybookapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.poly.mybookapp.R
import com.poly.mybookapp.model.UserModel
import kotlinx.android.synthetic.main.activity_register.*

class Register : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var database: FirebaseDatabase

    private var emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        val emailRegister: EditText = findViewById(R.id.emailRegister)
        val passWordRegister: EditText = findViewById(R.id.passWordRegister)
        val nameRegister: EditText = findViewById(R.id.nameRegister)
        val phoneRegister: EditText = findViewById(R.id.phoneRegister)
        val addressRegister: EditText = findViewById(R.id.addressRegister)
        val comFirmPassWordRegister: EditText = findViewById(R.id.comfirmPassWordRegister)
        val ofLoginWith : TextView = findViewById(R.id.tvOfLoginWith)

        tvOfLoginWith.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }


        val btnRegister: Button = findViewById(R.id.btnRegister)

        btnRegister.setOnClickListener {
            val name = nameRegister.text.toString()
            val passWord = passWordRegister.text.toString()
            val email = emailRegister.text.toString()
            val cPassWord = comFirmPassWordRegister.text.toString()
            val phone = phoneRegister.text.toString()
            val address = addressRegister.text.toString()

            if (name.isEmpty() || address.isEmpty() || passWord.isEmpty() || email.isEmpty() || cPassWord.isEmpty() || phone.isEmpty()){
                if (name.isEmpty()){
                    btnRegister.error = "Vui l??ng nh???p t??n ..."
                }
                if (address.isEmpty()){
                    btnRegister.error = "Vui l??ng nh???p ?????a ch???  ..."
                }
                if (passWord.isEmpty()){
                    btnRegister.error = "Vui l??ng nh???p passrord ..."
                }
                if (cPassWord.isEmpty()){
                    btnRegister.error = "Vui l??ng nh???p l???i password ..."
                }
                if (phone.isEmpty()){
                    btnRegister.error = "Vui l??ng nh???p s??? ??i???n tho???i ..."
                }
                if (email.isEmpty()){
                    btnRegister.error = "Vui l??ng nh???p email ..."
                }
                Toast.makeText(this, "Vui l??ng nh???p chi ti???t h???p l??? ...", Toast.LENGTH_LONG).show()
            }else if (!email.matches(emailPattern.toRegex())){
                btnRegister.error = "Vui l??ng nh???p ?????a ch??? email h???p l??? ..."
                Toast.makeText(this, "Vui l??ng nh???p ?????a ch??? email h???p l??? ...", Toast.LENGTH_LONG).show()
            }else if (phone.length != 10){
                btnRegister.error = "Vui l??ng nh???p s??? ??i???n tho???i h???p l??? ..."
                Toast.makeText(this, "Vui l??ng nh???p s??? ??i???n tho???i h???p l??? ...", Toast.LENGTH_LONG).show()
            }else if (passWord.length < 6){
                btnRegister.error = "M???t kh???u ph???i c?? 6 ch??? s??? ..."
                Toast.makeText(this, "Vui l??ng nh???p l???i m???t kh???u ...", Toast.LENGTH_LONG).show()
            }else if (passWord != cPassWord){
                btnRegister.error = "M???t kh???u kh??ng h???p l???, vui l??ng th??? l???i"
                Toast.makeText(this, "M???t kh???u kh??ng h???p l???, vui l??ng th??? l???i", Toast.LENGTH_LONG).show()
            }else{
                auth.createUserWithEmailAndPassword(email, passWord)
                    .addOnCompleteListener {
                        if (it.isSuccessful){
                            val databaseRef = database.reference.child("user").child(name)

                            val users: UserModel = UserModel(name, email, phone, address, auth.currentUser!!.uid)

                            databaseRef.setValue(users).addOnCompleteListener {
                                if (it.isSuccessful){
                                    Toast.makeText(this, "????ng k?? th??nh c??ng ...", Toast.LENGTH_LONG).show()
                                }else{
                                    Toast.makeText(this, "???? x???y ra l???i vui l??ng th??? l???i ...", Toast.LENGTH_LONG).show()
                                }
                            }
                        }else{
                            Toast.makeText(this, "???? x???y ra l???i vui l??ng th??? l???i ...", Toast.LENGTH_LONG).show()
                        }
                    }
            }

        }
    }
}