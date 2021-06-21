package com.example.jadoproject

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.jadoproject.data.Info
import com.example.jadoproject.databinding.ActivityJoinBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class JoinActivity: AppCompatActivity() {

    private lateinit var binding: ActivityJoinBinding
    private val gson by lazy { Gson() }
    // firebase 연동
    val FIREBASE_URL = "https://jadoproject-530a4-default-rtdb.asia-southeast1.firebasedatabase.app"
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance(FIREBASE_URL)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //상단바 숨김
        supportActionBar?.hide()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_join)


        val flag = intent.getStringExtra("flag")
        Log.d("flag", flag.toString())

        if (flag.toString() == "register") {
            Log.d("if", "0 IN!")

            binding.btnCancel.setOnClickListener {
                onBackPressed()
            }

        }

        if (flag.toString() == "modify") {
            Log.d("if", "1 IN!")

            firebaseConnet()

            binding.btnCancel.setOnClickListener {
                onBackPressed()
            }

            binding.btnOk.setOnClickListener {

                if (binding.txtJoinPwd.text.toString() == binding.txtJoinPwd2.text.toString()) {
                    val info : Info = Info(binding.txtJoinId.text.toString(), binding.txtJoinName.text.toString(),
                        binding.txtJoinPwd.text.toString(), binding.txtJoinNum.text.toString())

                    database.getReference("User").child("dayeon").child("UserInfo").setValue(info)

                    onBackPressed()
                }
                else if (binding.txtJoinPwd.text.toString() != binding.txtJoinPwd2.text.toString()){
                    Toast.makeText(this, "비밀번호 확인을 다시 해주세요", Toast.LENGTH_SHORT).show()
                }

            }

        }

    }

    fun firebaseConnet(){
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
                getText(infoarray)


            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("connet", "fail")
                Log.d("eroor", error.toString())

            }


        })

    }

    fun getText(userInfo : Info)
    {
        binding.txtJoinName.setText(userInfo.name)
        binding.txtJoinId.setText(userInfo.email)
        binding.txtJoinPwd.setText(userInfo.password)
        binding.txtJoinPwd2.setText(userInfo.password)
        binding.txtJoinNum.setText(userInfo.phoneNumber)

    }

}