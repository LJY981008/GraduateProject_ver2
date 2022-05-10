package com.example.schoollifeproject.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.schoollifeproject.databinding.ContactsMainBoardBinding
import com.example.schoollifeproject.NoticeActivity
import com.example.schoollifeproject.model.InfoListModel

/**
 * 공부게시판 RecyclerView Adapter
 * 작성자 : 박동훈
 * */
class InfoListAdapter(private val itemList: List<InfoListModel>) :
    RecyclerView.Adapter<InfoListAdapter.InfoViewHolder>() {
    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoViewHolder {
        val binding =
            ContactsMainBoardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InfoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InfoViewHolder, position: Int) {
        val item = itemList[position]
        holder.apply {
            bind(item)
        }
    }

    class InfoViewHolder(private val binding: ContactsMainBoardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        //메인 메뉴 공지사항에 등록될 text, listener
        fun bind(item: InfoListModel) {
            binding.title.text = item.getStudyTitle()
            //공지사항 내용 확인 클릭 리스너
            binding.rView.setOnClickListener {
                val intent = Intent(itemView.context, NoticeActivity::class.java).apply {
                    putExtra("key", item.getStudyKey())
                    putExtra("title", item.getStudyTitle())
                    putExtra("writer", item.getStudyWriter())
                    putExtra("date", item.getStudyDate())
                    putExtra("content", item.getStudyContent())
                }
                startActivity(itemView.context, intent, null)
            }
        }
    }
}
