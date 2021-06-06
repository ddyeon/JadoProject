package com.example.jadoproject

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.jadoproject.data.DateInfo
import com.example.jadoproject.data.ID
import com.example.jadoproject.data.Info
import com.example.jadoproject.data.Study
import com.example.jadoproject.databinding.FragmentMypageBinding
import com.google.firebase.FirebaseApp
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

      /*  val info : ID = ID(Info("dayeon", "123", "01083726110", "ekdus2574@gmail.com"),
            Study(DateInfo(3,100,))
        )
*/


        binding.cameraBtn.setOnClickListener {
            firebaseConnet()
            Log.d("click", "button")
            //database.reference.child("ID").setValue(info)

        }



       return binding.root
    }


    fun firebaseConnet()
    {

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