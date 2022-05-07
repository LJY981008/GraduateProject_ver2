package com.example.schoollifeproject

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.view.isGone
import com.example.schoollifeproject.adapter.NoteReadListAdapter
import com.example.schoollifeproject.databinding.ActivityNoticeBinding
import com.example.schoollifeproject.model.*

class noticeActivity : AppCompatActivity() {
    private var readList: MutableList<NoteReadContacts> = mutableListOf()

    //private var commentContactsList: MutableList<NoteCommentContacts> = mutableListOf()
    private val readAdapter = NoteReadListAdapter(readList)
    //private val commentAdapter = NoteCommentListAdapter(commentContactsList)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityNoticeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("HH시 mm분")
        val formatted = current.format(formatter)
*/
        val api = APIS.create()
        binding.noteReadRecyclerView.adapter = readAdapter
        //binding.commentRecyclerView.adapter = commentAdapter

        val btn_switch = binding.switchAvail

        val key = intent.getIntExtra("key", 0)
        val title = intent.getStringExtra("title").toString()
        val writer = intent.getStringExtra("writer").toString()
        val date = intent.getStringExtra("date").toString()
        val content = intent.getStringExtra("content").toString()
        val available = intent.getIntExtra("available", 0)
        if (writer == intent.getStringExtra("ID")) {
            btn_switch.visibility = View.VISIBLE
            btn_switch.isChecked = available == 1

        } else {
            btn_switch.visibility = View.GONE
        }

        val contact = (
                NoteReadContacts(
                    title,
                    writer,
                    date,
                    content,
                    available
                )
                )
        readList.add(contact)
        readAdapter.notifyDataSetChanged()

        /*btn_switch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (!isChecked) {
                btn_switch.isChecked = true
                available = 1
            } else {
                available = 0
                btn_switch.isChecked = false
            }
        }*/
        //여기에 api로 db수정


        //val views = 100
        //해당 글의 키를 가져와 표시


        //버튼 기능으로 댓글 작성 및 저장 최신화
        /*val btn_commitComment = binding.commentBtn
        var commentBlank = false

        btn_commitComment.setOnClickListener {
            val comment = binding.commentEdit.text.toString()
            Log.d("댓글", "눌림 : $comment")

            if (comment.isBlank()) {
                commentBlank = true
            }

            if (!commentBlank) {
                val contacts =
                    NoteCommentContacts(
                        writer,
                        formatted,
                        comment
                    )
                commentContactsList.add(contacts)
                commentAdapter.notifyDataSetChanged()
            }
        }*/

    }
}