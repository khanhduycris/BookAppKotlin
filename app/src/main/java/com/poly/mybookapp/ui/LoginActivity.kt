package com.poly.mybookapp.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.poly.mybookapp.R
import com.poly.mybookapp.uibook.MainActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private lateinit var progessbar: ProgressBar
    private var emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        val emailLogin: EditText = findViewById(R.id.EmailLogin)
        val passWordLogin: EditText = findViewById(R.id.PassWordLogin)
        val accountLogin: TextView = findViewById(R.id.tv_create_account)
        val btnLogin : Button = findViewById(R.id.btnLogin)

        progessbar = findViewById(R.id.progress_bar)

        accountLogin.setOnClickListener {
            startActivity(Intent(this, Register::class.java))
        }

        btnLogin.setOnClickListener {
//            progessbar.setVisibility(View.VISIBLE)
//            progessbar.setProgress(0, true)
//            progessbar.setVisibility(View.GONE)
//            progessbar.setProgress(0, false)
            val email = emailLogin.text.toString()
            val passWord = passWordLogin.text.toString()

            if (email.isEmpty() || passWord.isEmpty()){
                if (email.isEmpty()){
                    btnLogin.error = "Vui lòng nhập tên ..."
                }
                if (passWord.isEmpty()){
                    btnLogin.error = "Vui lòng nhập mật khẩu ..."
                }

                Toast.makeText(this, "Không hợp lệ vui lòng thử lại ...", Toast.LENGTH_LONG).show()
            }else if (!email.matches(emailPattern.toRegex())){
                btnLogin.error = "Email không hợp lệ ..."
                Toast.makeText(this, "Email không hợp lệ ...", Toast.LENGTH_LONG).show()
            }else if (passWord.length < 6){
                btnLogin.error = "Mật khẩu phải có 6 kí tự ..."
                Toast.makeText(this, "Mật khẩu phải có 6 kí tự ...", Toast.LENGTH_LONG).show()
            }else{
                auth.signInWithEmailAndPassword(email, passWord)
                    .addOnCompleteListener {
                        if (it.isSuccessful){
                            if (it.isSuccessful){
                                startActivity(Intent(this, MainActivity::class.java))
                            }else{
                                Toast.makeText(this, "Đã xảy ra lỗi vui lòng thử lại ...", Toast.LENGTH_LONG).show()
                            }
                        }else{
                            Toast.makeText(this, "Đã xảy ra lỗi vui lòng thử lại ...", Toast.LENGTH_LONG).show()
                        }
                    }
            }

        }

    }
}