package com.example.schoollifeproject

import android.app.Activity
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.schoollifeproject.databinding.ActivityWriteNoticeBinding
import com.example.schoollifeproject.model.APIS
import com.example.schoollifeproject.model.PostModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat

/**
 * 글작성 클릭 실행 Activity
 * */

class WriteNoticeActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityWriteNoticeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val api = APIS.create()
        val current =  System.currentTimeMillis()
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val date: String = formatter.format(current)
        val addNotice = binding.addNotice
        val btnCancel = binding.btnCancel

        btnCancel.setOnClickListener {
            finish()
        }

        addNotice.setOnClickListener {
            val editTitle = binding.editTitle.text.toString()
            val editContents = binding.editNotice.text.toString()
            val type = intent.getIntExtra("type", 1111)
            api.notice_save(
                type,
                editTitle,
                intent.getStringExtra("ID").toString(),
                date,
                editContents
            ).enqueue(object : Callback<PostModel> {
                override fun onResponse(call: Call<PostModel>, response: Response<PostModel>) {
                }

                override fun onFailure(p0: Call<PostModel>, t: Throwable) {
                }

            })
            Handler().postDelayed({
                setResult(Activity.RESULT_OK, intent)
                finish()
            },1000)
        }

    }
}