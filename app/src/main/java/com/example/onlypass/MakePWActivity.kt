package com.example.onlypass

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.onlypass.databinding.ActivityMakePwactivityBinding

class MakePWActivity : AppCompatActivity() {
    lateinit var binding: ActivityMakePwactivityBinding
    lateinit var pref: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    var pwInput: Boolean = false
    var pwComplete: Boolean = false
    var pw: String = ""
    var pw2: String = ""

    fun alert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("지문 인식도 추가하시곘습니까?")
            .setMessage("핸드폰에 저장된 지문 인식도 추가하시겠습니까?")
            .setPositiveButton("확인",
                DialogInterface.OnClickListener { dialog, id ->
                    editor.putString("USING_BIO", "true")
                    editor.apply()
                    val returnIntent = Intent()
                    setResult(200, returnIntent)
                    finish()
                })
            .setNegativeButton("취소",
                DialogInterface.OnClickListener { dialog, id ->
                    editor.putString("USING_BIO", "false")
                    editor.apply()
                    val returnIntent = Intent()
                    setResult(400, returnIntent)
                    finish()
                })
        // 다이얼로그를 띄워주기
        builder.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMakePwactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        pref = getSharedPreferences("com.jck.onlypass", Context.MODE_PRIVATE)

//        pref = getPreferences(Context.MODE_PRIVATE)
        editor = pref.edit()


        binding.pw4.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {

                if(binding.pw1.text.length >= 1 && binding.pw2.text.length >= 1 && binding.pw3.text.length >= 1 && binding.pw4.text.length >= 1 ) {
                    binding.pw1.requestFocus()
                    if (!pwInput) {
                        pw =
                            binding.pw1.text.toString() + binding.pw2.text.toString() + binding.pw3.text.toString() + binding.pw4.text.toString()
                        pwInput = true
                        binding.title.setText("비밀번호 재설정")
                    } else {
                        pw2 = binding.pw1.text.toString() + binding.pw2.text.toString() + binding.pw3.text.toString() + binding.pw4.text.toString()
                        pwComplete = true
                    }
                    binding.pw1.setText("")
                    binding.pw2.setText("")
                    binding.pw3.setText("")
                    binding.pw4.setText("")
                }
                if(pwComplete) {
                    binding.title.setText("비밀번호")
                    if (pw.equals(pw2)) {
                        Toast.makeText(this@MakePWActivity, "비밀번호가 동일합니다.", Toast.LENGTH_SHORT).show()
                        editor.putString("USING_PW", "true")
                        editor.putString("PW", pw)
                        editor.apply()
                        alert()
                    } else {
                        Toast.makeText(this@MakePWActivity, "비밀번호가 동일하지 않습니다.", Toast.LENGTH_SHORT).show()
                        pwComplete = false
                        pwInput = false
                        binding.title.setText("비밀번호 설정")

                    }
                }
            }
        })
    }
}