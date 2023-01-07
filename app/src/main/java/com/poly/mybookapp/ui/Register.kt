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
                    btnRegister.error = "Vui lòng nhập tên ..."
                }
                if (address.isEmpty()){
                    btnRegister.error = "Vui lòng nhập địa chỉ  ..."
                }
                if (passWord.isEmpty()){
                    btnRegister.error = "Vui lòng nhập passrord ..."
                }
                if (cPassWord.isEmpty()){
                    btnRegister.error = "Vui lòng nhập lại password ..."
                }
                if (phone.isEmpty()){
                    btnRegister.error = "Vui lòng nhập số điện thoại ..."
                }
                if (email.isEmpty()){
                    btnRegister.error = "Vui lòng nhập email ..."
                }
                Toast.makeText(this, "Vui lòng nhập chi tiết hợp lệ ...", Toast.LENGTH_LONG).show()
            }else if (!email.matches(emailPattern.toRegex())){
                btnRegister.error = "Vui lòng nhập địa chỉ email hợp lệ ..."
                Toast.makeText(this, "Vui lòng nhập địa chỉ email hợp lệ ...", Toast.LENGTH_LONG).show()
            }else if (phone.length != 10){
                btnRegister.error = "Vui lòng nhập số điện thoại hợp lệ ..."
                Toast.makeText(this, "Vui lòng nhập số điện thoại hợp lệ ...", Toast.LENGTH_LONG).show()
            }else if (passWord.length < 6){
                btnRegister.error = "Mật khẩu phải có 6 chữ số ..."
                Toast.makeText(this, "Vui lòng nhập lại mật khẩu ...", Toast.LENGTH_LONG).show()
            }else if (passWord != cPassWord){
                btnRegister.error = "Mật khẩu không hợp lệ, vui lòng thử lại"
                Toast.makeText(this, "Mật khẩu không hợp lệ, vui lòng thử lại", Toast.LENGTH_LONG).show()
            }else{
                auth.createUserWithEmailAndPassword(email, passWord)
                    .addOnCompleteListener {
                        if (it.isSuccessful){
                            val databaseRef = database.reference.child("user").child(name)

                            val users: UserModel = UserModel(name, email, phone, address, auth.currentUser!!.uid)

                            databaseRef.setValue(users).addOnCompleteListener {
                                if (it.isSuccessful){
                                    Toast.makeText(this, "Đăng kí thành công ...", Toast.LENGTH_LONG).show()
                                }else{
                                    Toast.makeText(this, "Đã xảy ra lỗi vui lòng thử lại ...", Toast.LENGTH_LONG).show()
                                }
                            }
                        }else{
                            Toast.makeText(this, "Đã xảy ra lỗi vui lòng thử lại ...", Toast.LENGTH_LONG).show()
                        }
                    }
            }

        }
    }
}