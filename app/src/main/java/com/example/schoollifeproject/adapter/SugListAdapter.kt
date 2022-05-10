package com.example.schoollifeproject.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.schoollifeproject.databinding.ContactsSugBinding
import com.example.schoollifeproject.model.MapModel

/**
 * 메인메뉴 추천맵 RecyclerView Adapter
 * */

class SugListAdapter(private val itemList: List<MapModel>) :
    RecyclerView.Adapter<SugListAdapter.SugViewHolder>() {
    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SugViewHolder {
        val binding =
            ContactsSugBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SugViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SugViewHolder, position: Int) {
        val item = itemList[position]
        holder.apply {
            bind(item)
        }
    }


    class SugViewHolder(private val binding: ContactsSugBinding) :
        RecyclerView.ViewHolder(binding.root) {
        //추천맵에 등록될 text, listener
        fun bind(item: MapModel) {
            binding.title.text = "${item.getMapID()}님의 로드맵"
        }
    }
}
