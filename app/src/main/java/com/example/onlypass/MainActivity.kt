package com.example.onlypass

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.onlypass.LoginActivity
import com.example.onlypass.RegexPWActivity
import com.example.onlypass.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var login: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(!login) {
            val intent = Intent(applicationContext, LoginActivity::class.java)
            finishAffinity()
            startActivity(intent)
        }

        binding.regexBtn.setOnClickListener {


            val nextIntent: Intent = Intent(applicationContext, ListActivity::class.java)
            startActivityForResult(nextIntent, 1)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 200) {
            login = true
            binding.loginText.setText("로그인 중")
            Toast.makeText(applicationContext,
                "접속 되었습니다.", Toast.LENGTH_SHORT)
                .show()
        }
    }
}