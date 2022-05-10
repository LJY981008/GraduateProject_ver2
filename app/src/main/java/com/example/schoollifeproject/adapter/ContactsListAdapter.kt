package com.example.schoollifeproject.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.schoollifeproject.model.Contacts
import com.example.schoollifeproject.databinding.ContactsListItemBinding
import com.example.schoollifeproject.NoticeActivity
import com.example.schoollifeproject.model.Bbs

/**
 * 게시판 RecyclerView Adapter
 * */
class ContactsListAdapter(private val itemList: MutableList<Bbs>) :
    RecyclerView.Adapter<ContactsListAdapter.ContactsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        val binding =
            ContactsListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        val item = itemList[position]
        holder.apply {
            bind(item)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }


    class ContactsViewHolder(private val binding: ContactsListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        //게시판에 등록될 text, listener
        fun bind(item: Bbs) {
            binding.title.text = item.getBbsTitle()
            binding.writer.text = item.getBbsWriter()
            binding.date.text = item.getBbsDate()
            //게시글 내용확인 클릭리스너
            binding.rootView.setOnClickListener {
                val intent = Intent(itemView.context, NoticeActivity::class.java).apply {
                    putExtra("key", item.getBbsKey())
                    putExtra("title", item.getBbsTitle())
                    putExtra("writer", item.getBbsWriter())
                    putExtra("date", item.getBbsDate())
                    putExtra("content", item.getBbsContent())

                }
                startActivity(itemView.context, intent, null)
            }
        }
    }
}