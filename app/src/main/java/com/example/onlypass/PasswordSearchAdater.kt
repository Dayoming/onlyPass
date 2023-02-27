package com.example.onlypass

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.onlypass.DBHelper
import com.example.onlypass.MainActivity
import com.example.onlypass.PasswordInfo
import com.example.onlypass.R
import java.util.*

class PasswordSearchAdater(intent: Intent) : RecyclerView.Adapter<PasswordSearchAdater.ViewHolder>() {

    //    NoteSearchAdapter는 NoteAdapter와 기본 구조는 같음.
    var items = ArrayList<PasswordInfo>()
    //    var mIntent = intent
    val sql = intent.extras!!.getString("sql").toString()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView: View = inflater.inflate(R.layout.main_rv_item, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.setItem(item)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun addItem(item: PasswordInfo?) {
        items.add(item!!)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var view: View = itemView
        fun setItem(item: PasswordInfo) {
            val webName: TextView = view.findViewById(R.id.webName)
            val userEmail: TextView = view.findViewById(R.id.userEmail)
            webName.text = item.webName
            userEmail.text = item.userEmail

            view.setOnClickListener { v ->
                val dbHelper = DBHelper(v.context)
                val database = dbHelper.writableDatabase

                val position = adapterPosition

                if (position != RecyclerView.NO_POSITION) {
                    Log.d("로그", position.toString() + "클릭")
                    Log.d("로그1", sql)
                    val cursor = database.rawQuery(sql, null)
                    //                        전달받은 쿼리문을 저장한 문자열 변수 sql을 사용
                    cursor.move(position + 1)
//                    val intent = Intent(v.context, EditActivity::class.java)
//                    intent.putExtra("title", cursor.getString(1))
//                    intent.putExtra("content", cursor.getString(2))
//                    intent.putExtra("position", cursor.getInt(0))
                    val context = v.context
//                    context.startActivity(intent)
                    (context as Activity).finish()
                }
            }


            view.setOnLongClickListener { v ->
                val database: SQLiteDatabase
                val context = v.context
                val dbHelper = DBHelper(v.context)
                database = dbHelper.writableDatabase

                val alertDialog = AlertDialog.Builder(context)
                var cursor: Cursor
                alertDialog.setMessage("삭제하시겠습니까?")
                alertDialog.setPositiveButton("예") { _, _ ->
                    val position = adapterPosition
                    cursor = database.rawQuery(sql, null)
                    database.rawQuery(sql, null)
                    cursor.move(position + 1)
                    Toast.makeText(v.context, "삭제되었습니다.", Toast.LENGTH_SHORT).show()
                    database.delete("noteData", "_id=?",
                        arrayOf(cursor.getInt(0).toString())
                    )
                    cursor.close()

                    (v.context as ListActivity).searchQuery((v.context as ListActivity).searchText)
                }

                alertDialog.setNegativeButton("취소") { _, _ -> }//취소 버튼
                alertDialog.create().show()
                false
            }
        }
    }

}