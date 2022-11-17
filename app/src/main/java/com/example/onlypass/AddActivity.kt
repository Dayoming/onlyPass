package com.example.onlypass

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import com.example.onlypass.databinding.ActivityAddBinding

class AddActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddBinding

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

}