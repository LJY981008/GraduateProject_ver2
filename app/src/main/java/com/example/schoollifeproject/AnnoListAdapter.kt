package com.example.schoollifeproject

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.schoollifeproject.databinding.AnnoContactsBinding
import com.example.schoollifeproject.databinding.ItemContactsBinding

class AnnoListAdapter(val itemList: List<AnnoContacts>) :
    RecyclerView.Adapter<AnnoListAdapter.ContactsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        TODO("Not yet implemented")
        val binding =
            AnnoContactsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return return ContactsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        TODO("Not yet implemented")
        val item = itemList[position]
        holder.apply {
            bind(item)
        }
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
        return itemList.size
    }

    class ContactsViewHolder(private val binding: AnnoContactsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: AnnoContacts) {
            binding.title.text = item.title
        }
    }
}
