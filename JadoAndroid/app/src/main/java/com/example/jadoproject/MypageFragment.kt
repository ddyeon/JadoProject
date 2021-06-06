package com.example.jadoproject

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.jadoproject.databinding.FragmentMypageBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MypageFragment : Fragment() {

    private lateinit var binding : FragmentMypageBinding



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_mypage, container,false)



        binding.cameraBtn.setOnClickListener {
            firebaseConnet()
            Log.d("click", "button")
            //FirebaseDatabase.getInstance().reference.push().setValue("A" + 1);

        }



       return binding.root
    }


    fun firebaseConnet()
    {
         val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val myRef = database.getReference("Camera")


        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val cameraInfo = dataSnapshot.value
                Log.d("camera", cameraInfo.toString())



            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("connet", "fail")
                Log.d("eroor", error.toString())

            }


        })


    }

}