package com.example.schoollifeproject

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.schoollifeproject.databinding.ActivityMainBinding
import com.example.schoollifeproject.model.APIS
import com.example.schoollifeproject.model.PostModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    val TAG: String = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                    applicationContext, Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ), 2
                )
            }
        }

        val binding = ActivityMainBinding.inflate(layoutInflater)
        val api = APIS.create()
        setContentView(binding.root)

        val btnLogin = binding.btnLogin
        val btnRegister = binding.btnRegister
        val btnNonLogin = binding.btnNonlogin


        btnLogin.setOnClickListener {
            Log.e("click", "11")
            val id = binding.editId.text.toString()
            val pw = binding.editPw.text.toString()

            val intent = Intent(this, MenuActivity::class.java)


            api.login_users(
                id
            ).enqueue(object : Callback<PostModel> {

                override fun onResponse(
                    call: Call<PostModel>,
                    response: Response<PostModel>
                ) {
                    Log.d("dbTestNoBody", response.toString())
                    Log.d("dbTestBody", response.body().toString())
                    if (!response.body().toString().isEmpty()) {
                        if (response.body()?.error.toString() == "error") {
                            failDialog("isLogin")
                        } else if (pw == response.body()?.userPassword.toString()) {
                            intent.putExtra("ID", response.body()?.userID.toString())
                            intent.putExtra("name", response.body()?.userName.toString())
                            startActivity(intent)
                        } else {
                            failDialog("fail")
                        }
                    }
                }

                override fun onFailure(p0: Call<PostModel>, t: Throwable) {
                    Log.d("failedDBopen", t.message.toString())
                    failDialog("fail")
                }
            })
        }

        btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        btnNonLogin.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            intent.putExtra("ID", "비회원")
            intent.putExtra("loginCheck", 1)
            startActivity(intent)

        }
    }

    fun failDialog(error: String) {
        var dialog = AlertDialog.Builder(this)

        dialog.setTitle("로그인실패")
        if (error == "isLogin") {
            dialog.setMessage("다른 브라우저에서 로그아웃해주세요")
        } else {
            dialog.setMessage("아이디와 비밀번호를 확인해주세요")
        }
        val dialog_listener = object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                when (which) {
                    DialogInterface.BUTTON_POSITIVE ->
                        Log.d(TAG, "확인 버튼 클릭")
                }
            }
        }

        dialog.setPositiveButton("확인", dialog_listener)
        dialog.show()
    }
}