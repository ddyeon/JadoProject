package com.example.jadoproject

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.jadoproject.data.weekdata
import com.example.jadoproject.databinding.FragmentWeekBinding
import com.example.jadoproject.databinding.ItemWeekBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


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

        val myRef = database.getReference("User").child("dayeon").child("Study")
        val studyDateList : ArrayList<Any?> = arrayListOf()
        var dateresult : ArrayList<weekdata> = arrayListOf()


        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.forEach {
                    studyDateList.add(dataSnapshot.value)
                }

                val jsonString = gson.toJson(studyDateList)
                Log.d("json", jsonString.toString())
                val listType = object : TypeToken<ArrayList<weekdata>>() {}.type
                val newList = gson.fromJson<ArrayList<weekdata>>(jsonString, listType)
                dateresult = newList
                timepercent(dateresult)

                Log.d("neslist",newList.toString())


            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("connet", "fail")
                Log.d("eroor", error.toString())

            }


        })


    }

    fun timepercent(dateArray: ArrayList<weekdata>)
    {
        val timesArray : ArrayList<String> = arrayListOf()
        Log.d("dateara", dateArray.toString())

       // dateArray[0]._0614.total_time

        timesArray.add(dateArray[0]._0614.total_time.substring(0,2))
        binding.include2.studytime.text = dateArray[0]._0614.total_time

        timesArray.add(dateArray[1]._0615.total_time.substring(0,2))
        binding.include3.studytime.text = dateArray[1]._0615.total_time

        timesArray.add(dateArray[2]._0616.total_time.substring(0,2))
        binding.include4.studytime.text = dateArray[2]._0616.total_time

        timesArray.add(dateArray[3]._0617.total_time.substring(0,2))
        binding.include5.studytime.text = dateArray[3]._0617.total_time

        timesArray.add(dateArray[4]._0618.total_time.substring(0,2))
        binding.include6.studytime.text = dateArray[4]._0618.total_time

        timesArray.add(dateArray[5]._0619.total_time.substring(0,2))
        binding.include7.studytime.text = dateArray[5]._0619.total_time

        timesArray.add(dateArray[6]._0620.total_time.substring(0,2))
        binding.include8.studytime.text = dateArray[6]._0620.total_time

        setProgress(timesArray)
    }

    fun setProgress(array : ArrayList<String>)
    {
        for(i in 0 until array.size)
        {
            weekList[i].progressBar.progress = array[i].toInt()
        }
    }


    //현재 시간
    fun calTotalTime(dateArray: ArrayList<weekdata>)
    {
        //06:11:55
        val f = SimpleDateFormat("HH:mm:ss")
        val totaltimes = f.format("00:00:00")






    }


}