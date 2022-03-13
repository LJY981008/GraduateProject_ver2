package com.example.schoollifeproject
import androidx.appcompat.app.AlertDialog
import android.content.DialogInterface

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.schoollifeproject.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import org.json.JSONException

import org.json.JSONObject




class MainActivity : AppCompatActivity() {

    val TAG: String = "MainActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        val api = APIS.create()
        setContentView(binding.root)



        val btnLogin = binding.btnLogin
        val btnRegister = binding.btnRegister

        btnLogin.setOnClickListener {
            Log.e("click", "11")
            val id = binding.editId.text.toString()
            val pw = binding.editPw.text.toString()

            val intent = Intent(this, MenuActivity::class.java)


            api.post_users(
                id
            ).enqueue(object : Callback<PostModel> {

                override fun onResponse(
                    call: Call<PostModel>,
                    response: Response<PostModel>
                ) {
                    Log.d("dbTestNoBody",response.toString())
                    Log.d("dbTestBody",response.body().toString())
                    if(!response.body().toString().isEmpty()) {
                        if(pw.equals( response.body()?.userPassword.toString())) {
                            intent.putExtra("name", response.body()?.userName.toString())
                            startActivity(intent)
                        }
                        else{
                            failDialog()
                        }
                    }
                }

                override fun onFailure(p0: Call<PostModel>, t: Throwable) {
                    Log.d("failedDBopen", t.message.toString())
                    failDialog()
                }
            })
        }

        btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)

        }


    }

    fun failDialog(){
        var dialog = AlertDialog.Builder(this)

         dialog.setTitle("로그인 실패")
         dialog.setMessage("아이디와 비밀번호를 확인해주세요")

         val dialog_listener = object: DialogInterface.OnClickListener{
           override fun onClick(dialog: DialogInterface?, which: Int) {
             when(which){
               DialogInterface.BUTTON_POSITIVE ->
               Log.d(TAG, "확인 버튼 클릭")
             }
           }
         }

         dialog.setPositiveButton("확인", dialog_listener)
         dialog.show()
    }
}