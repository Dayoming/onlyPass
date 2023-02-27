package com.example.onlypass

import android.content.ClipData
import android.content.ClipboardManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.example.onlypass.databinding.ActivityRegexPwBinding
import java.security.SecureRandom

class RegexPWActivity : AppCompatActivity() {
    lateinit var binding: ActivityRegexPwBinding
    val alphaSmall = charArrayOf('a', 'b', 'c', 'd','e','f','g', 'h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z')
    val alphaBig = charArrayOf('A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z')
    val numberArr = charArrayOf('0','1','2','3','4','5','6','7','8','9')
    val specialArr = charArrayOf('@','$','!','%','*','?','&')

    fun makeRegexPw(s: Int, e: Int): String {
        var len = 0
        var result = ""
        var RegexArray = charArrayOf()
        val secureRandom = SecureRandom()

        if(binding.cbSmall.isChecked) {
            RegexArray += alphaSmall
        }

        if(binding.cbBig.isChecked) {
            RegexArray += alphaBig
        }
        if(binding.cbNumber.isChecked) {
            RegexArray += numberArr
        }
        if(binding.cbSpecial.isChecked) {
            RegexArray += specialArr
        }
        if(s == e)
            len = s
        else
            len = secureRandom.nextInt(e-s)+s
        for(i in 1..len) {
            var index = secureRandom.nextInt(RegexArray.size)
            result += RegexArray[index]
        }

        return result
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegexPwBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var regexCheck = false
//        var start: String =
//        var special: String
//        var number: String

        setSupportActionBar(binding.regexToolBar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        // 뒤로가기 버튼 사용
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        // 뒤로가기 버튼 아이콘 변경
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back_arrow_white)


        binding.makeBtn.setOnClickListener {
            regexCheck = false
            var pwString: String = "^"
            var end: String = "["
            var startNumber = 0
            var endNumber = 0

            if(!(binding.startEdit.text.toString() == null && binding.endEdit.text.toString() == null)
                && !(binding.startEdit.text.toString() == "" && binding.endEdit.text.toString() == "")) {
                startNumber = Integer.parseInt(binding.startEdit.text.toString())
                endNumber = Integer.parseInt(binding.endEdit.text.toString())
            }


            if (!binding.cbSmall.isChecked && !binding.cbBig.isChecked && !binding.cbNumber.isChecked && !binding.cbSpecial.isChecked) {
                Toast.makeText(applicationContext, "최소 1개 이상의 체크 박스를 선택해주세요.", Toast.LENGTH_SHORT).show()
            } else if(startNumber <= 0 || startNumber > endNumber) {
                Toast.makeText(applicationContext, "유효한 숫자 범위를 작성해주세요.", Toast.LENGTH_SHORT).show()
            }
            else {
                if (binding.cbSmall.isChecked) {
                    pwString += "(?=.*[a-z])"
                    end += "a-z"
                }

                if (binding.cbBig.isChecked) {
                    pwString += "(?=.*[A-Z])"
                    end += "A-Z"
                }
                if (binding.cbNumber.isChecked) {
                    pwString += "(?=.*\\d)"
                    end += "\\d"
                }

                if (binding.cbSpecial.isChecked) {
                    pwString += "(?=.*[@$!%*?&])"
                    end += "@\$!%*?&"
                }
                end += "]"
                pwString += end
                if(startNumber == endNumber) {
                    pwString += "{${startNumber},}$"
                } else {
                    pwString += "{${startNumber},${endNumber}}$"
                }
//            Toast.makeText(applicationContext, pwString, Toast.LENGTH_SHORT)
//            val regex = Regex(pwString)
                val regex = Regex(pwString)
                for (i in 1..endNumber) {
                    val result = makeRegexPw(startNumber, endNumber)
                    Toast.makeText(applicationContext, result, Toast.LENGTH_SHORT)
                    if (regex.matches(result)) {
                        binding.pwTextView.setText(result)
                        regexCheck = true
                        break
                    }
                }
                if (!regexCheck) {
                    binding.pwTextView.setText("오류가 생겼습니다.\n잠시 뒤에 다시 시도해주세요.")
                }
            }
        }
        binding.copyBtn.setOnClickListener {
            val clipboard: ClipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clip: ClipData = ClipData.newPlainText("pw", binding.pwTextView.text)
            clipboard.setPrimaryClip(clip)

            Toast.makeText(getApplicationContext(), "클립보드에 복사되었습니다.", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}