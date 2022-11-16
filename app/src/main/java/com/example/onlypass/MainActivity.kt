package com.example.onlypass

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
        var binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mAdapter = MainRvAdapter(this, passwordList)

        binding.mRecyclerView.adapter = mAdapter

        val lm = LinearLayoutManager(this)
        binding.mRecyclerView.layoutManager = lm
        binding.mRecyclerView.setHasFixedSize(true)

    }
}