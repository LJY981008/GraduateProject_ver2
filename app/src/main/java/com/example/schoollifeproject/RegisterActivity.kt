package com.example.schoollifeproject

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.schoollifeproject.databinding.ActivityRegisterBinding
import com.example.schoollifeproject.model.APIS_login
import com.example.schoollifeproject.model.PostModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.util.*

class RegisterActivity : AppCompatActivity() {
    val TAG: String = "Register"
    var isExistBlank = false
    var isPWSame = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val api_register = APIS_login.create()
        val df = DecimalFormat("000000")

        val btnRegister = binding.btnRegister


        btnRegister.setOnClickListener {
            Log.d(TAG, "회원가입 버튼 클릭")

            val id = binding.editId.text.toString()
            val pw = binding.editPw.text.toString()
            val pw_re = binding.editPwRe.text.toString()
            val name = binding.editName.text.toString()
            val email = binding.editEmail.text.toString()
            //텍스트를 채우지 않았을 때
            if (id.isBlank() || pw.isBlank() || pw_re.isBlank() || name.isBlank() || email.isBlank() ) {
                isExistBlank = true
            } else {
                if (pw == pw_re) isPWSame = true
            }

            if (!isExistBlank && isPWSame) {
                Toast.makeText(this, "가입성공", Toast.LENGTH_SHORT).show()

                //쉐어드에 저장(이 부분에 회원가입 정보 서버전송 코드 작성)
                api_register.register_users(
                    id, pw, name, email
                ).enqueue(object : Callback<PostModel> {
                    override fun onResponse(call: Call<PostModel>, response: Response<PostModel>) {
                        Log.d("onRespon","리스폰 성공")
                        if(response.body()?.error.toString().equals("ok")) {
                            dialog("success")
                        } else{
                           dialog("id same")
                        }
                    }
                    override fun onFailure(call: Call<PostModel>, t: Throwable) {
                        Log.d("onFailure", "리스폰 실패 : " + t)

                   }
                })
                Log.d("endapi","api끝")

            } else {
                if (isExistBlank) {
                    dialog("blank")
                } else if (!isPWSame) {
                    dialog("not same")
                }
            }
        }
    }


    //회원가입 오류시 띄우는 다이얼로그
    fun dialog(type: String) {
        val dialog = AlertDialog.Builder(this)

        if(type.equals("blank")){
            dialog.setTitle("회원가입 실패")
            dialog.setMessage("입력란을 모두 작성해주세요.(공백을 제외해주세요)")
        } else if(type.equals("not same")){
            dialog.setTitle("회원가입 실패")
            dialog.setMessage("비밀번호를 정확히 입력해주세요.")
        } else if(type.equals("id same")){
            dialog.setTitle("회원가입 실패")
            dialog.setMessage("중복된 아이디가 존재합니다.")
        } else if(type.equals("success")){
            dialog.setTitle("회원가입 성공")
            dialog.setMessage("환영합니다.")
        }

        val dialog_litener = object: DialogInterface.OnClickListener{
            override fun onClick(p0: DialogInterface?, p1: Int) {
                if(type.equals("success")){
                    finish()
                }
            }
        }

        dialog.setPositiveButton("확인", dialog_litener)
        dialog.show()
    }

}