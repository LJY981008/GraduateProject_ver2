package com.example.schoollifeproject.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MapModel (
    @Expose
    @SerializedName("userID")
    private var userID: String
){
    fun getUserID(): String{
        return userID
    }
}