package com.example.schoollifeproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.schoollifeproject.databinding.ActivityMenuBinding
import androidx.annotation.NonNull
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MenuActivity : AppCompatActivity() {
    private var flag = 0
    private var countKey: Int = 0
    private lateinit var id: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val api = APIS_login.create()

        id = intent.getStringExtra("ID").toString()
        val btnMain = binding.btnMain
        val btnNotice = binding.btnNotice
        val btnMap = binding.btnMap
        val btnSetting = binding.btnSetting
        val tvNick = binding.tvNick

        tvNick.setText(intent.getStringExtra("name") + "님 환영합니다")

        api.notice_key_search(1)
            .enqueue(object : Callback<PostModel> {
                override fun onResponse(
                    call: Call<PostModel>,
                    response: Response<PostModel>
                ) {

                    if (!response.body().toString().isEmpty()) {
                        countKey = response.body()?.countKey!!
                    }
                    Log.d("키갯수", countKey.toString())
                }

                override fun onFailure(p0: Call<PostModel>, t: Throwable) {
                    Log.d("failedSearchKey", t.message.toString())
                }
            })


        btnMain.setOnClickListener {
            flag = 0
            removeFragment()
        }
        btnNotice.setOnClickListener {
            flag = 1
            switchFragment()

        }

        btnMap.setOnClickListener {
            val intent = Intent(this, MindMapActivity::class.java)
            intent.putExtra("ID", id)
            startActivity(intent)
        }

    }

    private fun switchFragment() {
        var listFragment = ListFragment()
        var bundle = Bundle()
        bundle.putString("ID", id)
        bundle.putInt("countKey", countKey)
        listFragment.arguments = bundle





        val transaction = supportFragmentManager.beginTransaction()
        when (flag) {
            1 -> {

                transaction.replace(R.id.frameLayout, listFragment)
                transaction.commit()
            }
           /* 2 -> {
                transaction.replace(R.id.frameLayout, FragmentB())
            }
            3 -> {
                transaction.replace(R.id.frameLayout, FragmentA())
            }
            4 -> {

            }*/
        }


    }

    private fun removeFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        val frameLayout = supportFragmentManager.findFragmentById(R.id.frameLayout)
        transaction.remove(frameLayout!!)
        transaction.commit()
    }

}
