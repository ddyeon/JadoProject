package com.example.jadoproject

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jadoproject.data.Friend

import com.example.jadoproject.data.Group
import com.example.jadoproject.data.Info
import com.example.jadoproject.databinding.FragmentFriendBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FriendFragment : Fragment() {
    private lateinit var binding : FragmentFriendBinding
    private val gson by lazy { Gson() }
    val FIREBASE_URL = "https://jadoproject-530a4-default-rtdb.asia-southeast1.firebasedatabase.app"
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance(FIREBASE_URL)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_friend, container,false)

        //firebaseConnet()

        val item : ArrayList<Friend> = arrayListOf()


        binding.btnSearch.setOnClickListener {
            firebaseConnet()
            val searchId = binding.edtSearch.text.toString()
            //if (searchId == )
        }

      /*  binding.btnAdd.setOnClickListener {

        }*/

        return binding.root
    }


    fun firebaseConnet(){
        val myRef = database.getReference("User").child("dayeon").child("UserInfo")
        val userArray : ArrayList<Any?> = arrayListOf()
        var infoarray : Info = Info("","","","")

        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()) {
                    val friendInfo = dataSnapshot.value
                    Log.d("friendInfo", friendInfo.toString())
                    userArray.add(friendInfo)

                    val jsonString = gson.toJson(userArray)
                    val listType = object : TypeToken<ArrayList<Info>>() {}.type
                    val newList = gson.fromJson<ArrayList<Info>>(jsonString, listType)

                    Log.d("newlist", newList.toString())
                    infoarray = newList[0]
                    Log.d("email", infoarray.email.toString())
                    //getText(infoarray)
                    val adapter = FriendListAdapter(newList)
                    binding.freindListRecyclerview.adapter = adapter
                    binding.freindListRecyclerview.layoutManager = LinearLayoutManager(context)

                }



            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("connet", "fail")
                Log.d("eroor", error.toString())

            }


        })

    }

    fun getText(friendInfo : Info)
    {
        //binding.profileImg.seText(friendInfo.profileImg)
        //binding.userId.setText(friendInfo.email)

    }
}