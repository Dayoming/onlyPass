package com.example.onlypass

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.onlypass.databinding.ActivityLoginBinding
import java.util.concurrent.Executor

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    lateinit var pref: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    var login = false

    fun login() {
        login = true
        val intent = Intent(applicationContext, ListActivity::class.java)
        finishAffinity()
        startActivity(intent)
//        val returnIntent = Intent()
//        setResult(200, returnIntent)
//        finish()
    }
    fun makePW_Alert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("저장된 PIN이 없습니다.")
            .setMessage("PIN을 설정하러 가시겠습니까?\nPIN설정 이후에 지문인식으로도 로그인이 가능합니다.")
            .setPositiveButton("확인",
                DialogInterface.OnClickListener { dialog, id ->
                    val nextIntent: Intent = Intent(this, MakePWActivity::class.java)
                    startActivity(nextIntent)
                })
            .setNegativeButton("취소",
                DialogInterface.OnClickListener { dialog, id ->
                    System.exit(0)
                })
        // 다이얼로그를 띄워주기
        builder.show()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        pref = getSharedPreferences("com.jck.onlypass", Context.MODE_PRIVATE)
//        pref = getPreferences(Context.MODE_PRIVATE)
        editor = pref.edit()

        val using_pw = pref.getString("USING_PW", "false")
        if(using_pw == "false") {
            makePW_Alert()
        }

        binding.pw1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {

                if(binding.pw1.text.length >= 1) {
                    binding.pw2.requestFocus()

                }

//                if(binding.pw1.text.length >= 1 && binding.pw2.text.length >= 1 && binding.pw3.text.length >= 1 && binding.pw4.text.length >= 1 ) {
//                    Toast.makeText(applicationContext, "로그인 시도", Toast.LENGTH_SHORT)
//                    binding.pw1.setText("")
//                    binding.pw2.setText("")
//                    binding.pw3.setText("")
//                    binding.pw4.setText("")
//                }
            }
        })
        binding.pw2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {

                if(binding.pw2.text.length >= 1) {
                    binding.pw3.requestFocus()
                }
            }
        })
        binding.pw3.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {

                if(binding.pw3.text.length >= 1) {
                    binding.pw4.requestFocus()
                }
            }
        })
        binding.pw4.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                if(binding.pw1.text.length >= 1 && binding.pw2.text.length >= 1 && binding.pw3.text.length >= 1 && binding.pw4.text.length >= 1) {
                    binding.pw1.requestFocus()
                    val pw =
                        binding.pw1.text.toString() + binding.pw2.text.toString() + binding.pw3.text.toString() + binding.pw4.text.toString()
                    var inputData = pref.getString("PW", "")

                    if (pw == inputData) {
                        Toast.makeText(applicationContext, "로그인 성공", Toast.LENGTH_SHORT).show()
                        login()
                    } else {
                        Toast.makeText(applicationContext, "로그인 실패", Toast.LENGTH_SHORT).show()
                    }
                    binding.pw1.setText("")
                    binding.pw2.setText("")
                    binding.pw3.setText("")
                    binding.pw4.setText("")
                }
            }
        })

        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(
                        applicationContext,
                        "인증 취소: $errString", Toast.LENGTH_SHORT
                    )
                        .show()
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    login()
//                    val returnIntent = Intent()
//                    setResult(200, returnIntent)
//                    finish()
//                    Toast.makeText(applicationContext,
//                        "접속에 성공하였습니다.", Toast.LENGTH_SHORT)
//                        .show()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(
                        applicationContext, "잘못된 지문입니다.",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("지문인식")
            .setSubtitle("센서에 손가락을 올려 지문 인식을 해주세요.")
            .setNegativeButtonText("PIN을 이용하여 접속하기")
            .build()
        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                var using_bio = pref.getString("USING_BIO", "false")
                if(using_bio == "true") {
                    biometricPrompt.authenticate(promptInfo)
                }
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                Toast.makeText(applicationContext, "지문 인식을 지원하지 않습니다.", Toast.LENGTH_SHORT).show()
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                Toast.makeText(applicationContext, "현재 지문 인식을 사용할 수 없습니다.", Toast.LENGTH_SHORT)
                    .show()
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                Toast.makeText(applicationContext, "등록된 지문이 존재하지 않습니다.", Toast.LENGTH_SHORT).show()
            }
        }
        binding.bioLockBtn.setOnClickListener {
            var using_bio = pref.getString("USING_BIO", "false")
            if(using_bio == "true")
                biometricPrompt.authenticate(promptInfo)
            else {
                Toast.makeText(applicationContext, "저장된 지문이 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }
        binding.makePW.setOnClickListener {
            val nextIntent: Intent = Intent(this, MakePWActivity::class.java)
            startActivityForResult(nextIntent, 1)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 200) {
            Toast.makeText(applicationContext,
                "비밀번호 생성 완료.", Toast.LENGTH_SHORT)
                .show()
        }
    }
}