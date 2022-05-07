package com.example.schoollifeproject.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.schoollifeproject.model.Contacts
import com.example.schoollifeproject.databinding.ItemContactsBinding
import com.example.schoollifeproject.noticeActivity

class ContactsListAdapter(private val itemList: MutableList<Contacts>) :
    RecyclerView.Adapter<ContactsListAdapter.ContactsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        val binding =
            ItemContactsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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


    class ContactsViewHolder(private val binding: ItemContactsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Contacts) {
            binding.title.text = item.title
            binding.writer.text = item.writer
            binding.date.text = item.date
            binding.rootView.setOnClickListener {
                val intent = Intent(itemView.context, noticeActivity::class.java).apply {
                    putExtra("key", item.key)
                    putExtra("title", item.title)
                    putExtra("writer", item.writer)
                    putExtra("date", item.date)
                    putExtra("content", item.content)
                    putExtra("available", item.available)

                }
                startActivity(itemView.context, intent, null)

            }

        }
    }
}