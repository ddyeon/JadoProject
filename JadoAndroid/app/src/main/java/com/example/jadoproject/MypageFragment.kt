package com.example.jadoproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.jadoproject.data.Info
import com.example.jadoproject.databinding.FragmentMypageBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MypageFragment : Fragment() {

    private lateinit var binding : FragmentMypageBinding
    private val gson by lazy { Gson() }
    val FIREBASE_URL = "https://jadoproject-530a4-default-rtdb.asia-southeast1.firebasedatabase.app"
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance(FIREBASE_URL)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_mypage, container,false)

        binding.btnLogout.setOnClickListener {

            val loginIntent = Intent(activity, LoginActivity::class.java)
            loginIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(loginIntent)
        }

        binding.btnInfo.setOnClickListener {
            val intent = Intent(activity, JoinActivity::class.java)
            intent.putExtra("flag", "modify")
            startActivity(intent)
        }

        firebaseConnet()

        return binding.root
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

                binding.txtUsrName.setText(infoarray.name)

            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("connet", "fail")
                Log.d("eroor", error.toString())

            }


        })

    }


}