package com.honeywell.orderapp.page

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.honeywell.orderapp.R
import kotlinx.android.synthetic.main.*
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginButton.setOnClickListener {
            var name = inputName.text.toString()
            var pwd = inputPwd.text.toString()
            if(name=="admin" && pwd=="123456")
            {
                val intent = Intent(this,FoodsManageActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}