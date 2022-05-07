package com.example.schoollifeproject

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.schoollifeproject.databinding.ActivityWriteNoticeBinding
import com.example.schoollifeproject.fragment.ListFragment
import com.example.schoollifeproject.model.APIS
import com.example.schoollifeproject.model.PostModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate

class WriteNoticeActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityWriteNoticeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val api = APIS.create()
        val date: String = LocalDate.now().toString()


        val editTitle = binding.editTitle.text.toString()
        val editContents = binding.editNotice.text.toString()
        val addNotice = binding.addNotice
        val available = 1
        val intent = Intent(this, ListFragment::class.java)
        addNotice.setOnClickListener {
            api.notice_save(
                editTitle,
                intent.getStringExtra("ID").toString(),
                date,
                editContents,
                available
            ).enqueue(object : Callback<PostModel> {
                override fun onResponse(call: Call<PostModel>, response: Response<PostModel>) {
                    Log.d("onResponse", "저장성공")
                    intent.apply {
                        putExtra("Key", response.body()?.key)
                        putExtra("Title", editTitle)
                        putExtra("Contents", editContents)
                        putExtra("Date", date)
                    }
                }

                override fun onFailure(p0: Call<PostModel>, t: Throwable) {
                    Log.d("onFailure", "저장실패 : " + t.message.toString())
                }

            })
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

    }
}