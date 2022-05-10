package com.example.schoollifeproject.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.schoollifeproject.R
import com.example.schoollifeproject.adapter.AnnoListAdapter
import com.example.schoollifeproject.databinding.FragmentAnnoListBinding
import com.example.schoollifeproject.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * 공부게시판 Fragment
 * 작성자 : 박동훈
 */
class AnnoListFragment : Fragment() {
    private val TAG = this.javaClass.toString()
    private var annoList: MutableList<Notice> = mutableListOf()
    private val adapter = AnnoListAdapter(annoList)

    private lateinit var getResult: ActivityResultLauncher<Intent>
    private lateinit var binding: FragmentAnnoListBinding

    private val api = APIS.create()
    private lateinit var userID: String
    private var countKey: Int = 0

    fun newInstance(userID: String): AnnoListFragment {
        val args = Bundle()
        args.putString("userID", userID)

        val annoFragment = AnnoListFragment()
        annoFragment.arguments = args

        return annoFragment
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAnnoListBinding.inflate(inflater, container, false)

        val dividerItemDecoration = DividerItemDecoration(context, RecyclerView.VERTICAL)
        binding.recyclerView.addItemDecoration(dividerItemDecoration)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {})

        userID = arguments?.getString("ID").toString()

        //게시글 목록 호출
        api.notice_load(
            1       //type 0 = 일반 포스팅, type 1 = 공지 포스팅
        ).enqueue(object : Callback<List<Notice>> {
            override fun onResponse(
                call: Call<List<Notice>>,
                response: Response<List<Notice>>
            ) {
                val list = mutableListOf<Notice>()
                //공지사항의 개수만큼 호출, 연결
                for (i in response.body()!!) {
                    val contacts = (
                            Notice(
                                i.getNoticeKey(),
                                i.getNoticeTitle(),
                                i.getNoticeWriter(),
                                i.getNoticeDate(),
                                i.getNoticeContent(),
                                i.getNoticeAvailable()
                            )
                            )
                    list.add(contacts)
                    countKey++
                }
                annoList.clear()
                annoList.addAll(list)
                adapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<List<Notice>>, t: Throwable) {}

        })

        binding.annoView.setOnClickListener {
            val annoListFragment = AnnoListFragment()
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            Log.d("$TAG", "userIDSend: ${userID}")

            transaction?.replace(R.id.frameLayout, annoListFragment.newInstance(userID))
                ?.commitAllowingStateLoss()
        }

        binding.freeView.setOnClickListener {
            val freeListFragment = FreeListFragment()
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            Log.d("$TAG", "userIDSend: ${userID}")

            transaction?.replace(R.id.frameLayout, freeListFragment.newInstance(userID))
                ?.commitAllowingStateLoss()
        }

        binding.sugView.setOnClickListener {
            val mapListFragment = MapListFragment()
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            Log.d("$TAG", "userIDSend: ${userID}")

            transaction?.replace(R.id.frameLayout, mapListFragment.newInstance(userID))
                ?.commitAllowingStateLoss()
        }

        return binding.root
    }

    /**
     * RecyclerView에 포스팅할 아이템들 DB에서 호출
     * */
    private fun posting() {

    }
}