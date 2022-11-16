package com.example.onlypass

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MainRvAdapter(val context: Context, val passwordList: ArrayList<passwordInfo>) :
        RecyclerView.Adapter<MainRvAdapter.Holder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
                val view = LayoutInflater.from(context).inflate(R.layout.main_rv_item, parent, false)
                return Holder(view)
        }

        override fun onBindViewHolder(holder: Holder, position: Int) {
                holder?.bind(passwordList[position], context)
        }

        override fun getItemCount(): Int {
                return passwordList.size
        }

        inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
                val securityPhoto = itemView?.findViewById<ImageView>(R.id.securityPhoto)
                val webName = itemView?.findViewById<TextView>(R.id.webName)
                val userEmail = itemView?.findViewById<TextView>(R.id.userEmail)

                fun bind (passwordInfo: passwordInfo, context: Context) {
                        securityPhoto?.setImageResource(R.drawable.security)
                        /* dogPhoto의 setImageResource에 들어갈 이미지의 id를 파일명(String)으로 찾고,
                        이미지가 없는 경우 안드로이드 기본 아이콘을 표시한다.*/
                        /*if (dog.photo != "") {
                                val resourceId = context.resources.getIdentifier(dog.photo, "drawable", context.packageName)
                                dogPhoto?.setImageResource(resourceId)
                        } else {
                                dogPhoto?.setImageResource(R.mipmap.ic_launcher)
                        }*/
                        /* 나머지 TextView와 String 데이터를 연결한다. */
                        webName?.text = passwordInfo.webName
                        userEmail?.text = passwordInfo.userEmail
                }
        }


}