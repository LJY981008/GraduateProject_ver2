package com.example.schoollifeproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.schoollifeproject.adapter.NoteReadListAdapter
import com.example.schoollifeproject.databinding.ActivityNoticeBinding
import com.example.schoollifeproject.model.APIS_login
import com.example.schoollifeproject.model.Contacts
import com.example.schoollifeproject.model.NoteReadContacts
import com.example.schoollifeproject.model.PostModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class noticeActivity : AppCompatActivity() {
    private var readContactsList: MutableList<NoteReadContacts> = mutableListOf()
    private val readAdapter = NoteReadListAdapter(readContactsList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityNoticeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val api = APIS_login.create()
        binding.noteReadRecyclerView.adapter = readAdapter

        val key = intent.getIntExtra("key", 0)
        val title = intent.getStringExtra("title")

        //해당 글의 키를 가져와 표시
        api.notice_open(
            key
        ).enqueue(object : Callback<PostModel> {
            override fun onResponse(call: Call<PostModel>, response: Response<PostModel>) {
                //작성되어있던 댓글 불러오기
                //작성자 표시
                //작성일 표시
                val contacts =
                    NoteReadContacts(
                        title,
                        response.body()?.noticeName,
                        response.body()?.noticeContents
                    )
                readContactsList.add(contacts)
                readAdapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<PostModel>, t: Throwable) {
                Log.d("onFailure", t.message.toString())
            }
        })
        //버튼 기능으로 댓글 작성 및 저장 최신화
    }
}