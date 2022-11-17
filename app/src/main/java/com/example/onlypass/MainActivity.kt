package com.example.onlypass

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.onlypass.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    var passwordList = arrayListOf<passwordInfo>(
        passwordInfo(1,"Instagram", "dayo___mi", "1234",
            "www.instagram.com", "SNS", "2022-11-16", "SNS WebService"),
        passwordInfo(2, "Stove", "jdayoung1204@naver.com", "4567",
        "www.stove.com", "Game", "2022-11-17", "6789")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.mainToolBar)

        // Toolbar에 표시되는 제목의 표시 유무, custom한 툴바 이름 출력
        supportActionBar?.setDisplayShowTitleEnabled(false)


        val mAdapter = MainRvAdapter(this, passwordList)

        binding.mRecyclerView.adapter = mAdapter

        val lm = LinearLayoutManager(this)
        binding.mRecyclerView.layoutManager = lm
        binding.mRecyclerView.setHasFixedSize(true)

    }

    // 메뉴 바
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.menuPasswordSave -> {
                val intent = Intent(applicationContext, AddActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}