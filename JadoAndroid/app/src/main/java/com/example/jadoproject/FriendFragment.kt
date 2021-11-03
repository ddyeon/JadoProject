package com.example.jadoproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jadoproject.data.Friend
import com.example.jadoproject.data.Group
import com.example.jadoproject.databinding.FragmentFriendBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson

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

        val item : ArrayList<Friend> = arrayListOf()
        val dayeon : Friend = Friend("ddyeon")
        val yooh : Friend = Friend("yuyu")
        val suzy : Friend = Friend("suzy")

        item.add(dayeon)
        item.add(yooh)
        item.add(suzy)

        setAdapter(item)

        binding.btnSearch.setOnClickListener {

        }

        return binding.root
    }


    private fun setAdapter(items : ArrayList<Friend>)
    {
        val adapter = FriendListAdapter(items)
        binding.freindListRecyclerview.adapter = adapter
        binding.freindListRecyclerview.layoutManager = LinearLayoutManager(context)
    }
}