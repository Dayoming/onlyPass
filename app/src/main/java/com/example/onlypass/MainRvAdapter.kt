package com.example.onlypass

import android.app.Activity
import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.w3c.dom.Text
import javax.crypto.SecretKey

class MainRvAdapter(val context: Context, val passwordList: ArrayList<PasswordInfo>) :

        RecyclerView.Adapter<MainRvAdapter.Holder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
                val view = LayoutInflater.from(context).inflate(R.layout.main_rv_item, parent, false)
                return Holder(view)
        }

        override fun onBindViewHolder(holder: Holder, position: Int) {

                val database: SQLiteDatabase
                val dbHelper = DBHelper(context)
                database = dbHelper.writableDatabase
                var cursor: Cursor = database.rawQuery(
                        "select * from password_info order by date DESC",
                        null
                )

                var favIconUrl: HashMap<String, String> = HashMap<String, String>()

                while (cursor.moveToNext()) {
                        // favIconUrl.add("https://" + cursor.getString(4) + "/favicon.ico")
                        favIconUrl.put(cursor.getString(1), "https://" + cursor.getString(4) + "/favicon.ico")
                }

//                var favIconUrl: ArrayList<String> = arrayListOf()
//
//                while (cursor.moveToNext()) {
//                        favIconUrl.add("https://" + cursor.getString(4) + "/favicon.ico")
//                }
//
                cursor.close()
//
//                holder?.bind(passwordList[position], context, favIconUrl[position])

                holder?.bind(passwordList[position], context, favIconUrl) }

        override fun getItemCount(): Int {
                return passwordList.size
        }

        inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
                private var view: View = itemView
                //val securityPhoto = itemView?.findViewById<ImageView>(R.id.securityPhoto)
                val webName = itemView?.findViewById<TextView>(R.id.webName)
                val userEmail = itemView?.findViewById<TextView>(R.id.userEmail)
                val favIcon = itemView?.findViewById<ImageView>(R.id.securityPhoto)

                fun bind(passwordInfo: PasswordInfo, context: Context, favList: HashMap<String,String>) {

                        /* 나머지 TextView와 String 데이터를 연결한다. */
                        webName?.text = passwordInfo.webName
                        userEmail?.text = passwordInfo.userEmail
                        Picasso.get().load(favList.get(passwordInfo.webName)).placeholder(R.drawable.security).into(favIcon)

                        view.setOnClickListener { itemView ->
                                val database: SQLiteDatabase
                                val context = itemView.context
                                val dbHelper = DBHelper(itemView.context)
                                database = dbHelper.writableDatabase
                                val sort = (itemView.context as ListActivity).sort
                                val alertDialog = AlertDialog.Builder(context)
                                var cursor: Cursor
                                val position = adapterPosition
                                println("---------------------$position------------------")
                                cursor = if (sort == "desc") {
                                        database.rawQuery(
                                                "select * from password_info order by date DESC",
                                                null
                                        )
                                } else {
                                        database.rawQuery(
                                                "select * from password_info order by date ASC",
                                                null
                                        )
                                }

                                cursor.move(position + 1)

                                var clipboard: ClipboardManager = context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                                var cursorText = cursor.getString(3)
                                cursorText = ChCrypto.aesDecrypt(cursorText, ChCrypto.SECRET_KEY)
                                val clip = ClipData.newPlainText("pw", cursorText)
                                clipboard.setPrimaryClip(clip)

                                Toast.makeText(itemView.context, "클립보드에 복사되었습니다.", Toast.LENGTH_SHORT).show()
                                alertDialog.setMessage("해당 페이지로 이동하시겠습니까?")
                                alertDialog.setPositiveButton("예") { _, _ ->
                                        val address = "http://" + cursor.getString(4)
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(address))
                                        context.startActivity(intent)
                                        cursor.close()
                                }

                                alertDialog.setNegativeButton("취소") { _, _ -> }//취소 버튼
                                alertDialog.create().show()
                                false
                                }
                        // 메모 삭제 -> 롱 클릭
                        view.setOnLongClickListener { itemView ->
                                val database: SQLiteDatabase
                                val context = itemView.context
                                val dbHelper = DBHelper(itemView.context)
                                database = dbHelper.writableDatabase
                                val sort = (itemView.context as ListActivity).sort

                                val alertDialog = AlertDialog.Builder(context)
                                var cursor: Cursor
                                alertDialog.setMessage("삭제하시겠습니까?")
                                alertDialog.setPositiveButton("예") { _, _ ->
                                        val position = adapterPosition
                                        println("---------------------$position------------------")
                                        cursor = if (sort == "desc") {
                                                database.rawQuery(
                                                        "select * from password_info order by date DESC",
                                                        null
                                                )
                                        } else {
                                                database.rawQuery(
                                                        "select * from password_info order by date ASC",
                                                        null
                                                )
                                        }
                                        cursor.move(position + 1)
                                        Toast.makeText(itemView.context, "삭제되었습니다.", Toast.LENGTH_SHORT).show()
                                        database.delete("password_info", "_id=?",
                                                arrayOf(cursor.getInt(0).toString())
                                        )
                                        cursor.close()
                                        if (sort == "desc")
                                                (itemView.context as ListActivity).refreshListDesc()
                                        else
                                                (itemView.context as ListActivity).refreshListAsc()
                                }

                                alertDialog.setNegativeButton("취소") { _, _ -> }//취소 버튼
                                alertDialog.create().show()
                                false
                        }
                }
        }

        fun crawlingLogin(url: String) {

//                val webDriverID = "webdriver.chrome.driver"
//                val webDriverPath = "C:/Users/jdayo/Downloads/chromedriver_win32"
//                System.setProperty(webDriverID, webDriverPath)
//
//                val options: ChromeOptions = ChromeOptions()
//                options.setBinary("")
//                options.addArguments("--start-maximized")
//                options.addArguments("--disable-popup-blocking")
//                options.addArguments("--disable-default-apps")
//
//                val driver: ChromeDriver = ChromeDriver(options)

//    driver.get("https://nid.naver.com/nidlogin.login")
//    driver.findElement(By.id("id")).sendKeys("test")
//    driver.findElement(By.id("pw")).sendKeys("1234")
//    driver.findElement(By.id("log.login")).click()

//                driver.get("https://www.naver.com")
//
//                driver.quit()

        }

}