package com.example.schoollifeproject.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.schoollifeproject.databinding.ContactsItemFileBinding
import com.example.schoollifeproject.model.FileContacts
import com.example.schoollifeproject.model.NoteReadContacts

class ItemFileAdapter(private val itemList: MutableList<FileContacts>) :
    RecyclerView.Adapter<ItemFileAdapter.ItemFileViewHolder>() {
    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemFileViewHolder {
        val binding =
            ContactsItemFileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemFileViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemFileViewHolder, position: Int) {
        val item = itemList[position]
        holder.apply {
            bind(item)
        }
    }

    class ItemFileViewHolder(private val binding: ContactsItemFileBinding) :
        RecyclerView.ViewHolder(binding.root) {
        //게시글에 등록될 Text
        fun bind(item: FileContacts) {
            binding.fileNameText.text = item.text
        }
    }

}
