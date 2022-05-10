package com.example.schoollifeproject

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.schoollifeproject.adapter.AnnoListAdapter
import com.example.schoollifeproject.adapter.SugListAdapter
import com.example.schoollifeproject.databinding.ActivityMenuBinding
import com.example.schoollifeproject.fragment.AnnoListFragment
import com.example.schoollifeproject.fragment.FreeListFragment
import com.example.schoollifeproject.fragment.MapListFragment
import com.example.schoollifeproject.fragment.MindMapFragment
import com.example.schoollifeproject.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * 로그인 후 메뉴 Activity
 * 작성자 : 이준영, 박동훈
 */
class MenuActivity : AppCompatActivity() {
    private val annoContactslist: MutableList<Notice> = mutableListOf()
    private val sugContactslist: MutableList<MapModel> = mutableListOf()

    private val annoAdapter = AnnoListAdapter(annoContactslist)
    private val sugAdapter = SugListAdapter(sugContactslist)

    private lateinit var userID: String
    private lateinit var userName: String
    private var loginCK: Int = 0

    private lateinit var binding: ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val api = APIS.create()

        val dividerItemDecoration = DividerItemDecoration(applicationContext, RecyclerView.VERTICAL)
        binding.annoRecycler.addItemDecoration(dividerItemDecoration)
        binding.sugRecycler.addItemDecoration(dividerItemDecoration)
        binding.freeRecycler.addItemDecoration(dividerItemDecoration)
        binding.infoRecycler.addItemDecoration(dividerItemDecoration)

        binding.annoRecycler.adapter = annoAdapter
        binding.sugRecycler.adapter = sugAdapter

        userID = intent.getStringExtra("ID").toString()
        userName = intent.getStringExtra("name").toString()
        loginCK = intent.getIntExtra("loginCheck", 0)

        val annoText = binding.annoPost
        val sugText = binding.sugPost
        annoText.setOnClickListener {
            val transaction = supportFragmentManager.beginTransaction()
            val AnnoListFragment = AnnoListFragment()
            transaction.replace(R.id.frameLayout, AnnoListFragment.newInstance(userID))
                .commitAllowingStateLoss()
            menuMainVisible(false)
        }
        sugText.setOnClickListener {
            val transaction = supportFragmentManager.beginTransaction()
            val MapListFragment = MapListFragment()
            transaction.replace(R.id.frameLayout, MapListFragment.newInstance(userID))
                .commitAllowingStateLoss()
            menuMainVisible(false)
        }

        /**
         * 메인메뉴의 공지사항 DB 불러오기
         * */
        //type 0 = 일반 포스팅, type 1 = 공지 포스팅
        api.notice_load(1).enqueue(
            object : Callback<List<Notice>> {
            override fun onResponse(
                call: Call<List<Notice>>,
                response: Response<List<Notice>>
            ) {
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
                    annoContactslist.add(contacts)
                    annoAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<List<Notice>>, t: Throwable) {}

        })

        api.map_list(1).enqueue(
            object : Callback<List<MapModel>> {
                override fun onResponse(
                    call: Call<List<MapModel>>,
                    response: Response<List<MapModel>>
                ) {
                    for (i in response.body()!!) {
                        val contacts = (
                                MapModel(
                                    i.getMapID(),
                                    i.getMapHits(),
                                    i.getMapRecommend()
                                )
                                )
                        sugContactslist.add(contacts)
                        sugAdapter.notifyDataSetChanged()
                    }
                }

                override fun onFailure(call: Call<List<MapModel>>, t: Throwable) {
                }

            })

        /**
         * 프래그먼트 하단바
         * 메뉴1 - 메인화면
         * 메뉴2 - 로드맵제작(비회원 이용불가)
         * 메뉴3 - 게시판
         * 메뉴4 - 로드맵게시판
         * */
        binding.bottomNavigationView.run {
            val mindMapFragment = MindMapFragment()
            val freeListFragment = FreeListFragment()
            val mapListFragment = MapListFragment()

            setOnItemSelectedListener { item ->
                val transaction = supportFragmentManager.beginTransaction()
                val frameLayout = supportFragmentManager.findFragmentById(R.id.frameLayout)
                when (item.itemId) {
                    R.id.mainMenu1 -> {
                        if (frameLayout != null) {
                            removeFragment()
                        }
                        menuMainVisible(true)
                        true
                    }
                    R.id.mainMenu2 -> {
                        if (userID == "비회원")
                            failDialog()
                        else {
                            transaction.replace(
                                R.id.frameLayout,
                                mindMapFragment.newInstance(userID, userID)
                            )
                                .commitAllowingStateLoss()
                            menuMainVisible(false)
                        }
                        true
                    }
                    R.id.mainMenu3 -> {
                        transaction.replace(R.id.frameLayout, freeListFragment.newInstance(userID))
                            .commitAllowingStateLoss()
                        menuMainVisible(false)
                        true
                    }
                    else -> {
                        transaction.replace(R.id.frameLayout, mapListFragment.newInstance(userID))
                            .commitAllowingStateLoss()
                        menuMainVisible(false)
                        true
                    }
                }

            }
        }
    }

    private fun removeFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        val frameLayout = supportFragmentManager.findFragmentById(R.id.frameLayout)
        transaction.remove(frameLayout!!)
        transaction.commit()
    }

    private fun failDialog() {
        var dialog = AlertDialog.Builder(this)
        dialog.setTitle("오류")
        dialog.setMessage("비회원은 이용할 수 없는 기능입니다.")
        val dialog_listener = DialogInterface.OnClickListener { dialog, which -> }
        dialog.setPositiveButton("확인", dialog_listener)
        dialog.show()
    }

    private fun menuMainVisible(b: Boolean) {
        if (b) {
            binding.annoLayout.visibility = View.VISIBLE
            binding.freeLayout.visibility = View.VISIBLE
            binding.sugLayout.visibility = View.VISIBLE
            binding.infoLayout.visibility = View.VISIBLE
        } else {
            binding.annoLayout.visibility = View.GONE
            binding.freeLayout.visibility = View.GONE
            binding.sugLayout.visibility = View.GONE
            binding.infoLayout.visibility = View.GONE
        }
    }
}
