package com.example.onlypass

import android.content.Intent
import android.database.sqlite.SQLiteOpenHelper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.onlypass.databinding.ActivityAddBinding
import java.util.*
import javax.crypto.Cipher

class AddActivity : AppCompatActivity() {

    lateinit var binding: ActivityAddBinding

    private val db = DBHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.addToolBar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        // 뒤로가기 버튼 사용
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        // 뒤로가기 버튼 아이콘 변경
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back_arrow_white)

        // 저장 버튼을 눌렀을 때
        binding.passwordAddBtn.setOnClickListener {
            insert()
            val returnIntent = Intent()
            setResult(200, returnIntent)
            finish()
        }
        // 카테고리 Spinner 항목 지정
//        var spinner: Spinner = findViewById<Spinner>(R.id.addCategory)
//        ArrayAdapter.createFromResource(
//            this,
//            R.array.category_array,
//            android.R.layout.simple_spinner_item
//        ).also { adapter ->
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
//            spinner.adapter = adapter
//        }
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

    private fun insert() {
        if(binding.addWebName.toString().isNotEmpty() && binding.addUserEmail.toString().isNotEmpty()
            && binding.addPassword.toString().isNotEmpty() && binding.addWebAddress.toString().isNotEmpty()) {
            val passwordInfo = PasswordInfo(binding.addWebName.text.toString(), binding.addUserEmail.text.toString(),
            binding.addPassword.text.toString(), binding.addWebAddress.text.toString(), "", "", binding.addMemo.text.toString())

            db.insertData(passwordInfo)

            startActivity(Intent(this, ListActivity::class.java))
            finish()

        } else {
            Toast.makeText(this, "메모를 제외한 모든 항목을 채워주세요.", Toast.LENGTH_LONG).show()
        }
    }


}