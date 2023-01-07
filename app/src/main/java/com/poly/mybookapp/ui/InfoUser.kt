package com.poly.mybookapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.poly.mybookapp.R
import kotlinx.android.synthetic.main.activity_info_user.*

class InfoUser : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private var namePattern = ""
//    private var phonePattern = "^[0-9]$"
    private var addressPattern = " "
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_user)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        val name:EditText = findViewById(R.id.edtName)
        val phone:EditText = findViewById(R.id.edtPhone)
        val address:EditText = findViewById(R.id.edtAddress)
        val btnYes:Button = findViewById(R.id.btnYes)

        btnYes.setOnClickListener {
            val name = name.text.toString()
            val phone = phone.text.toString()
            val address = address.text.toString()
//            val cPassWord = comFirmPassWordRegister.text.toString()
//            val phone = phoneRegister.text.toString()

            if (name.isEmpty()  || address.isEmpty() || phone.isEmpty()){
                if (name.isEmpty()){
                    btnYes.error = "Tên không được bỏ trống..."
                }
                if (phone.isEmpty()){
                    btnYes.error = "Số điện thoại không được bỏ trống .."
                }
                if (address.isEmpty()){
                    btnYes.error = "Địa chỉ không được bỏ trống ..."
                }
                Toast.makeText(this, "Vui lòng nhập chi tiết hợp lệ ...", Toast.LENGTH_LONG).show()
//            }else if (!name.matches(namePattern.toRegex())){
//                btnYes.error = "Vui lòng nhập tên hợp lệ ..."
//                Toast.makeText(this, "Vui lòng nhập địa chỉ email hợp lệ ...", Toast.LENGTH_LONG).show()
//            }else if (phone.length != 10){
//                btnYes.error = "Vui lòng nhập số điện thoại hợp lệ ..."
//                Toast.makeText(this, "Vui lòng nhập số điện thoại hợp lệ ...", Toast.LENGTH_LONG).show()
//            }else if (address.matches(addressPattern.toRegex())){
//                btnYes.error = "Mật khẩu phải có 6 chữ số ..."
//                Toast.makeText(this, "Vui lòng nhập lại mật khẩu ...", Toast.LENGTH_LONG).show()
//            }else{
//                auth.createUserWithEmailAndPassword(name, phone)
//                    .addOnCompleteListener {
//                        if (it.isSuccessful){
//                            val databaseRef = database.reference.child("user information").child(auth.currentUser!!.uid)
//
//                            val users: UserInformationModel = UserInformationModel(name, address, phone, auth.currentUser!!.uid)
//
//                            databaseRef.setValue(users).addOnCompleteListener {
//                                if (it.isSuccessful){
//                                    Toast.makeText(this, "Đăng kí thành công ...", Toast.LENGTH_LONG).show()
//                                }else{
//                                    Toast.makeText(this, "Đã xảy ra lỗi vui lòng thử lại ...", Toast.LENGTH_LONG).show()
//                                }
//                            }
//                        }else{
//                            Toast.makeText(this, "Đã xảy ra lỗi vui lòng thử lại ...", Toast.LENGTH_LONG).show()
//                        }
//                    }
            }
//
        }
    }
}