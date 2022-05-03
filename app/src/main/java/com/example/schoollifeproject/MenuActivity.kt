package com.example.schoollifeproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import com.example.schoollifeproject.databinding.ActivityMenuBinding
import androidx.annotation.NonNull
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MenuActivity : AppCompatActivity() {
    private var flag = 0
    private var countKey: Int = 0
    private lateinit var userID: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val api = APIS_login.create()

        userID = intent.getStringExtra("ID").toString()
        val tvNick = binding.tvNick

        tvNick.setText(intent.getStringExtra("name") + "님 환영합니다")

        api.notice_key_search(1)
            .enqueue(object : Callback<PostModel> {
                override fun onResponse(
                    call: Call<PostModel>,
                    response: Response<PostModel>
                ) {

                    if (!response.body().toString().isEmpty()) {
                        //countKey = response.body()?.countKey!!
                    }
                    Log.d("키갯수", countKey.toString())
                }

                override fun onFailure(p0: Call<PostModel>, t: Throwable) {
                    Log.d("failedSearchKey", t.message.toString())
                }
            })

        binding.bottomNavigationView.run {

            val listFragment = ListFragment()
            val mindMapFragment = MindMapFragment()
            var bundle = Bundle()
            bundle.putString("ID", userID)

            setOnItemSelectedListener { item ->
                val transaction = supportFragmentManager.beginTransaction()
                when (item.itemId) {
                    R.id.mainMenu1 -> {
                        bundle.putInt("countKey", countKey)
                        listFragment.arguments = bundle
                        transaction.replace(R.id.frameLayout, listFragment)
                            .commitAllowingStateLoss()
                        true
                    }
                    R.id.mainMenu2 -> {
                        mindMapFragment.arguments = bundle
                        transaction.replace(R.id.frameLayout, mindMapFragment)
                            .commitAllowingStateLoss()
                        true
                    }
                    else -> {
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

}
