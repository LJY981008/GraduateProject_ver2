package com.example.schoollifeproject.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ItemInfo(@Expose
               @SerializedName("itemID")
               private var itemID: String,
               @Expose
               @SerializedName("itemContent")
               private var content: String,
               @Expose
               @SerializedName("itemCount")
               private var num: Int?,
               @Expose
               @SerializedName("noteContent")
               private var note: String?) {
    val parentID: String = itemID.split("_")[0]
    val childID: String = itemID.split("_")[0]

    fun getItemID(): String {
        return itemID
    }
    fun setItemID(itemID: String) {
        this.itemID = itemID
    }
    fun getContent(): String {
        return content
    }
    fun setContent(content: String) {
        this.content = content
    }
    fun getNote(): String? {
        return note
    }
    fun setNote(note: String) {
        this.note = note
    }
    fun getNum(): Int? {
        return num
    }
}