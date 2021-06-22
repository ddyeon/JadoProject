package com.example.jadoproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.jadoproject.databinding.FragmentMypageBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MypageFragment : Fragment() {

    private lateinit var binding : FragmentMypageBinding

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

    fun firebaseConnet()
    {

        val myRef = database.getReference("User").child("dayeon")


        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val userInfo = dataSnapshot.value
                Log.d("userInfo", userInfo.toString())



            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("connet", "fail")
                Log.d("eroor", error.toString())

            }


        })


    }
}