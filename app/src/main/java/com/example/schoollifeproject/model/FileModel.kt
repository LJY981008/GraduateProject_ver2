package com.example.schoollifeproject.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class FileModel(
    @Expose
    @SerializedName("fileName")
    private val fileName: String,
    @Expose
    @SerializedName("fileRealName")
    private val fileRealName: String,
    private val ext: String
) {
    fun getFileName():String {
        return fileName
    }
    fun getFileRealName():String {
        return fileRealName
    }
    fun getExt(): String {
        return ext
    }
}