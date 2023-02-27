package com.example.onlypass

import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.onlypass.databinding.ActivityListBinding
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions

class ListActivity : AppCompatActivity() {
    lateinit var binding: ActivityListBinding
    private val db = DBHelper(this)

    lateinit var passwordRecycle: RecyclerView

    var sort: String = "desc"

    var searchText: String = ""

    var passwordList = arrayListOf<PasswordInfo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.mainToolBar)

        // Toolbar에 표시되는 제목의 표시 유무, custom한 툴바 이름 출력
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // DB에서 데이터를 불러오고 passwordList에 저장
        read()

        val mAdapter = MainRvAdapter(this, passwordList)

        binding.mRecyclerView.adapter = mAdapter

        val lm = LinearLayoutManager(this)
        binding.mRecyclerView.layoutManager = lm
        binding.mRecyclerView.setHasFixedSize(true)

        passwordRecycle = findViewById(R.id.mRecyclerView)

        if (sort == "desc")
            refreshListDesc()
        else if (sort == "asc")
            refreshListAsc()
        else
            refreshTitleAsc()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 200) {
            read()
        }
    }

    @SuppressLint("ResourceType")
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        val search = menu!!.findItem(R.id.menu_search_btn)
        val searchView: SearchView = search.actionView as SearchView


        searchView.queryHint = "Search"
        // 서치뷰 클릭시 힌트 설정

        // 서치뷰의 x버튼 클릭했을때
        searchView.setOnCloseListener {
            searchView.clearFocus()
            // 포커스 제거
            false
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    // 텍스트를 입력시 searchQuery 함수 호출
                    searchQuery(newText)
                    searchText = newText

                    /*
                    searchText를 사용하는 이유는
                    검색 후 메모를 삭제시 리스트를 유지하기 위함임
                    */
                }
                return true
            }

        })
        return true
//        return super.onCreateOptionsMenu(menu)
    }

    @SuppressLint("Recycle")
    fun searchQuery(query: String?) {
        val sql = "select * from password_info " +
                "where user_email Like " + "'%" + query + "%'" + "or web_name Like " + "'%" + query + "%'" + "order by date DESC"
        /*
        "select * from noteData where content Like '%query%' or title Like '%query%' order by time DESC"
        select 컬럼 from 테이블 | *는 모든 컬럼을 의미함
        where 조건
        content 컬럼내에서 Like(포함하는것) | title 컬럼도 동일함
        입력값이 사과일때
        %query면 썩은사과, 파인사과 등
        query%면 사과가격, 사과하세요 등
        %query%면 황금사과가격, 빨리사과하세요 등
        order by 정렬 | time 컬럼을 기준으로 DESC 내림차순
        order by를 사용하지 않거나 order by time ASC로 하면 오름차순
         */

        val cursor: Cursor = db.search(sql)

        val intent = Intent(this@ListActivity, PasswordSearchAdater::class.java)
        intent.putExtra("sql", sql)
        // 인텐트에 sql문구를 담음

        val recordCount = cursor.count
        // 갯수
        val adapter = PasswordSearchAdater(intent)
        val recyclerView = findViewById<RecyclerView>(R.id.mRecyclerView)

        for (i in 0 until recordCount) {
            cursor.moveToNext()
            val web_name = cursor.getString(1)
            val user_email = cursor.getString(2)
            adapter.addItem(PasswordInfo(web_name, user_email))
            // 어댑터에 아이템 추가
        }

        recyclerView.adapter = adapter
        // 리사이클뷰 어댑터 설정

        cursor.close()


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.menu_make_regex -> {
                val intent = Intent(applicationContext, RegexPWActivity::class.java)
                startActivity(intent)
            }
            R.id.menuPasswordSave -> {
                val intent = Intent(applicationContext, AddActivity::class.java)
                finishAffinity()
                startActivity(intent)
                return true
            }
            R.id.menu_sort_btn -> {
                if (sort == "desc") {
                    Toast.makeText(this, "오래된 순", Toast.LENGTH_SHORT).show()
                    sort = "asc"
                    refreshListAsc()
                } else if (sort == "asc") {
                    Toast.makeText(this, "최근 저장순", Toast.LENGTH_SHORT).show()
                    sort = "title"
                    refreshListDesc()
                } else {
                    Toast.makeText(this, "이름 순", Toast.LENGTH_SHORT).show()
                    sort = "desc"
                    refreshTitleAsc()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun read(){
        val data : MutableList<PasswordInfo> = db.readData()
        if(data.isNotEmpty())
            for(i in 0 until data.size){
                passwordList.add(data[i])
            }
    }

    fun refreshListAsc() {
        val list = ArrayList<PasswordInfo>()

        val cursor: Cursor = db.sortDateASC()

        for (i in 0 until cursor.count) {
            // count가 100이면 0 부터 99까지

            cursor.moveToNext() // 커서 이동
            val webName = cursor.getString(1)
            val userEmail = cursor.getString(2)
            list.add(PasswordInfo(webName, userEmail))
            // 리스트에 DB에 담긴 내용 추가
        }
        cursor.close()
        // 커서를 닫음

        passwordRecycle.adapter = MainRvAdapter(this, list) //리사이클러뷰 어댑터 할당
    }

    fun refreshTitleAsc() {
        val list = ArrayList<PasswordInfo>()

        val cursor: Cursor = db.sortTitleASC()

        for (i in 0 until cursor.count) {
            // count가 100이면 0 부터 99까지

            cursor.moveToNext() // 커서 이동
            val webName = cursor.getString(1)
            val userEmail = cursor.getString(2)
            list.add(PasswordInfo(webName, userEmail))
            // 리스트에 DB에 담긴 내용 추가
        }
        cursor.close()
        // 커서를 닫음

        passwordRecycle.adapter = MainRvAdapter(this, list) //리사이클러뷰 어댑터 할당
    }

    fun refreshListDesc() {
        val list = ArrayList<PasswordInfo>()

        val cursor: Cursor = db.sortDateDESC()

        for (i in 0 until cursor.count) {
            // count가 100이면 0 부터 99까지

            cursor.moveToNext() // 커서 이동
            val webName = cursor.getString(1)
            val userEmail = cursor.getString(2)
            list.add(PasswordInfo(webName, userEmail))
            // 리스트에 DB에 담긴 내용 추가
        }
        cursor.close()
        // 커서를 닫음

        passwordRecycle.adapter = MainRvAdapter(this, list) //리사이클러뷰 어댑터 할당
    }

}