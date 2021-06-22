package com.example.jadoproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.jadoproject.data.Info
import com.example.jadoproject.databinding.ActivityLoginBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val gson by lazy { Gson() }
    // firebase 연동
    val FIREBASE_URL = "https://jadoproject-530a4-default-rtdb.asia-southeast1.firebasedatabase.app"
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance(FIREBASE_URL)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 상단바 숨김
        supportActionBar?.hide()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        val myRef = database.getReference("User").child("dayeon").child("UserInfo")
        val userArray : ArrayList<Any?> = arrayListOf()
        var infoarray : Info = Info("","","","")


        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val userInfo = dataSnapshot.value
                Log.d("userInfo", userInfo.toString())
                userArray.add(userInfo)

                val jsonString = gson.toJson(userArray)
                val listType = object : TypeToken<ArrayList<Info>>() {}.type
                val newList = gson.fromJson<ArrayList<Info>>(jsonString, listType)

                Log.d("newlist", newList.toString())
                infoarray = newList[0]
                Log.d("email", infoarray.email.toString())

                checkInfo(infoarray.email, infoarray.password)

            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("connet", "fail")
                Log.d("eroor", error.toString())

            }


        })


        // 가입 버튼 클릭 시
        binding.btnJoin.setOnClickListener {

            // 가입 창으로 이동
            val intent = Intent(this, JoinActivity::class.java)
            intent.putExtra("flag", "register")
            startActivity(intent)
        }



    }

    fun checkInfo(email : String, passwd: String)
    {
        // 로그인 버튼 클릭 시
        binding.btnLogin.setOnClickListener {

            // 입력한 ID와 DB에 저장된 ID 같으면
            if (binding.txtLoginId.text.toString() == email) {

                // 입력한 PW와 DB에 저장된 PW 같으면
                if (binding.txtLoginPwd.text.toString() == passwd) {

                    // 홈으로 이동
                    val HomeIntent = Intent(this, MainActivity::class.java)
                    startActivity(HomeIntent)

                }

                // PW 다르면
                else {
                    Log.d("fail", binding.txtLoginPwd.text.toString())
                    Toast.makeText(this.applicationContext, "비밀번호가 틀렸습니다.", Toast.LENGTH_LONG).show()
                }

            }

            // ID 다르면
            else {
                Toast.makeText(this.applicationContext, "아이디가 틀렸습니다.", Toast.LENGTH_LONG).show()
            }



        }
    }



}