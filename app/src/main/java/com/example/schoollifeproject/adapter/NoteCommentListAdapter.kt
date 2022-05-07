package com.example.schoollifeproject.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.schoollifeproject.databinding.ContactsNoticeCommentBinding
import com.example.schoollifeproject.model.NoteCommentContacts
/**
 * 댓글작성 RecyclerView Adapter (미사용)
 * */
class NoteCommentListAdapter(private var itemList: List<NoteCommentContacts>) :
    RecyclerView.Adapter<NoteCommentListAdapter.NoteCommentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteCommentViewHolder {
        val binding =
            ContactsNoticeCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteCommentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteCommentViewHolder, position: Int) {
        val item = itemList[position]
        holder.apply {
            bind(item)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class NoteCommentViewHolder(private val binding: ContactsNoticeCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: NoteCommentContacts) {
            binding.commentTv.text = item.comment
            binding.commentWriter.text = item.writer
            binding.commentdate.text = item.time
        }
    }

}