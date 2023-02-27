package com.example.onlypass

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import androidx.core.content.contentValuesOf
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

val DATABASE_NAME = "onlypass"
val TABLE_NAME = "password_info"
val COL_ID = "_idx"
val COL_WEBNAME = "web_name"
val COL_USEREMAIL = "user_email"
val COL_PASSWORD = "password"
val COL_WEBADDRESS = "web_address"
val COL_CATEGORY = "category"
val COL_DATE = "date"
val COL_MEMO = "memo"

class DBHelper (var context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        // 테이블 생성
        val createTable = "create table password_info (" +
                "_id integer primary key autoincrement," +
                "web_name, user_email, password, web_address, category, date, memo)"
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // 기존 테이블 제거후 다시 생성하기
        db?.execSQL("drop table password_info")
        onCreate(db)
    }

    fun insertData(passwordInfo:PasswordInfo) {
        val db = this.writableDatabase
        val cv = contentValuesOf()

        // 현재 시간 가져오기
        val longNow = System.currentTimeMillis()
        val tDate = Date(longNow)
        val tDateFormat = SimpleDateFormat("yyyy-MM-dd kk:mm:ss", Locale("ko", "KR"))
        val nowDate = tDateFormat.format(tDate)

        cv.put(COL_WEBNAME, passwordInfo.webName)
        cv.put(COL_USEREMAIL, passwordInfo.userEmail)

        val password = ChCrypto.aesEncrypt(passwordInfo.password, ChCrypto.SECRET_KEY)

        cv.put(COL_PASSWORD, password)
        cv.put(COL_WEBADDRESS, passwordInfo.webAddress)
        cv.put(COL_CATEGORY, passwordInfo.category)
        cv.put(COL_DATE, nowDate)
        cv.put(COL_MEMO, passwordInfo.memo)

        val result = db.insert(TABLE_NAME, null, cv)

        if (result == (-1).toLong())
            Toast.makeText(context, "Failed", Toast.LENGTH_LONG).show()
        else
            Toast.makeText(context, "Success", Toast.LENGTH_LONG).show()
    }

    fun readData():MutableList<PasswordInfo> {
        val list :MutableList<PasswordInfo> = ArrayList()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val result:Cursor = db.rawQuery(query, null)

        if(result.moveToFirst()) {
            do {
                val passwordInfo = PasswordInfo()
//                passwordInfo._idx = result.getInt(result.getColumnIndexOrThrow(COL_ID))
                passwordInfo.webName = result.getString(result.getColumnIndexOrThrow(COL_WEBNAME))
                passwordInfo.userEmail = result.getString(result.getColumnIndexOrThrow(COL_USEREMAIL))
                passwordInfo.password = result.getString(result.getColumnIndexOrThrow(COL_PASSWORD))
                passwordInfo.webAddress = result.getString(result.getColumnIndexOrThrow(COL_WEBADDRESS))
                passwordInfo.category = result.getString(result.getColumnIndexOrThrow(COL_CATEGORY))
                passwordInfo.date = result.getString(result.getColumnIndexOrThrow(COL_DATE))
                passwordInfo.memo = result.getString(result.getColumnIndexOrThrow(COL_MEMO))
                list.add(passwordInfo)
            } while (result.moveToNext())
        } else
            Toast.makeText(context, "There is no data.", Toast.LENGTH_LONG).show()

        result.close()
        db.close()
        return list
    }

    fun sortDateASC() : Cursor {
        val db = this.readableDatabase
        val query = "SELECT * FROM password_info order by date ASC"
        val cursor:Cursor = db.rawQuery(query, null)

        return cursor
    }

    fun sortTitleASC() : Cursor {
        val db = this.readableDatabase
        val query = "SELECT * FROM password_info order by web_name ASC"
        val cursor:Cursor = db.rawQuery(query, null)

        return cursor
    }

    fun sortDateDESC() : Cursor {
        val db = this.readableDatabase
        val query = "SELECT * FROM password_info order by date DESC"
        val cursor:Cursor = db.rawQuery(query, null)

        return cursor
    }
    fun search(query: String?) : Cursor {
        val db = this.readableDatabase
        val cursor:Cursor = db.rawQuery(query, null)

        return cursor
    }

}