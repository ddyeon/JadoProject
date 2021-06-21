package com.example.jadoproject

import android.database.DatabaseUtils
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.jadoproject.data.StudyDate
import com.example.jadoproject.databinding.ActivityMainBinding
import com.example.jadoproject.databinding.FragmentWeekBinding
import com.example.jadoproject.databinding.ItemWeekBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class WeekFragment : Fragment() {
    private lateinit var binding: FragmentWeekBinding

    val FIREBASE_URL = "https://jadoproject-530a4-default-rtdb.asia-southeast1.firebasedatabase.app"
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance(FIREBASE_URL)

    private var weekList  : ArrayList<ItemWeekBinding> = arrayListOf()

    private val gson by lazy { Gson() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_week, container, false)


        weekList = arrayListOf(binding.include2, binding.include3,binding.include4, binding.include5, binding.include6,
        binding.include7, binding.include8)


        setOnClick()
        firebaseConnet()

        return binding.root
    }

    fun setOnClick()
    {
        for( i in 0 until weekList.size)
        {
            weekList[i].weekconst.setOnClickListener {
                parentFragmentManager.beginTransaction().replace(R.id.weekContainer,DailyFragment()).commit()
          /*      val action = WeekFragmentDirections.actionWeekFragmentToDailyFragment()
                findNavController().navigate(action)*/
            }
        }
    }

    fun firebaseConnet()
    {
        val myRef = database.getReference("User").child("dayeon").child("Study").child("2021-06-14").child("total_time")
        val studyDateList : ArrayList<Any?> = arrayListOf()
        var dateresult : ArrayList<StudyDate> = arrayListOf()


        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {


                studyDateList.add(dataSnapshot.value)

                val jsonString = gson.toJson(studyDateList)
                Log.d("json", jsonString.toString())
                val listType = object : TypeToken<ArrayList<StudyDate>>() {}.type
                val newList = gson.fromJson<ArrayList<StudyDate>>(jsonString, listType)
                dateresult = newList

                Log.d("neslist",newList.toString())


            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("connet", "fail")
                Log.d("eroor", error.toString())

            }


        })


    }


}