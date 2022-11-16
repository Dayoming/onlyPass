package com.example.onlypass

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper (context: Context?,
                name: String?, // 데이터 베이스 이름
                factory: SQLiteDatabase.CursorFactory? = null, // null을 기본 값으로 함
                version: Int // 데이터 베이스 버전
) : SQLiteOpenHelper(context, name, factory, version) {

    override fun onCreate(db: SQLiteDatabase?) {
        // 테이블 생성
        val memoSQL = "create table password_info (" +
                "_id integer primary key autoincrement," +
                "web_name, password, web_address, category, date, memo)"
        db?.execSQL(memoSQL)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // 기존 테이블 제거후 다시 생성하기
        db?.execSQL("drop table password_info")
        onCreate(db)
    }
}