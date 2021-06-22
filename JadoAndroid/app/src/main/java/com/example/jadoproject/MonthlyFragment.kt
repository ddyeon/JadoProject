package com.example.jadoproject

import android.os.Build
import android.os.Bundle
import android.os.Parcel
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.jadoproject.data.*
import com.example.jadoproject.databinding.FragmentMonthlyBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.prolificinteractive.materialcalendarview.CalendarDay

import com.prolificinteractive.materialcalendarview.CalendarMode
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

import java.util.*
import kotlin.collections.ArrayList


class MonthlyFragment : Fragment() {
    private lateinit var binding : FragmentMonthlyBinding

    var simpleDateFormat : SimpleDateFormat = SimpleDateFormat("mmmm-yyyy", Locale.KOREA)
    var DateFormat : SimpleDateFormat = SimpleDateFormat("yyyy-mm-dd", Locale.KOREA)
    private val gson by lazy { Gson() }

    companion object {
        val FIREBASE_URL = "https://jadoproject-530a4-default-rtdb.asia-southeast1.firebasedatabase.app"
        private val database: FirebaseDatabase = FirebaseDatabase.getInstance(FIREBASE_URL)

    }


    val dateKey : ArrayList<String> = arrayListOf()
    val colorString : Array<String> = arrayOf("#eb346e")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_monthly, container, false)


        calendarButton()
        firebaseConnet()

        return binding.root
    }





    //캘린더 버튼
    fun calendarButton()
    {
        binding.calendarView.setOnDateChangedListener { widget, date, selected ->
            val selectDate = date
            Log.d("selectDate", selectDate.toString())
            //select date가 eventlist에 있으면 fragement이동
            parentFragmentManager.beginTransaction().replace(R.id.weekContainer,DailyFragment().apply {
                arguments = Bundle().apply {
                    putString("selectday", selectDate.toString())
                    putString("flags", "second")
                }
            }).commit()


        }


    }

    fun firebaseConnet()
    {

        val myRef = database.getReference("User").child("dayeon").child("Study")
        val studyDateList : ArrayList<Any?> = arrayListOf()


        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.forEach{
                        val studyObj = it.value
                        studyDateList.add(studyObj)
                        dateKey.add(it.key.toString())

                }

                Log.d("studyDateList", studyDateList.toString())
                Log.d("dateKey", dateKey.toString())
                val jsonString = gson.toJson(studyDateList)
                val listType = object : TypeToken<ArrayList<StudyDate>>() {}.type
                val newList = gson.fromJson<ArrayList<StudyDate>>(jsonString, listType)
                Log.d("newlist", newList.toString())
                setDate(dateKey)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("connet", "fail")
                Log.d("eroor", error.toString())

            }


        })


    }

    fun setDate(dates : ArrayList<String>)
    {
        binding.calendarView.state().edit()
            .isCacheCalendarPositionEnabled(false)
            .setCalendarDisplayMode(CalendarMode.MONTHS).commit()

        binding.calendarView.isDynamicHeightEnabled = true
        // binding.calendarView.setPadding(0,-20,0,30)

        //CalendarDay{2021-6-21}

        //1개 이상
        var year = 0
        var month = 0
        var day = 0

        //공부한날 점찍히
        for(i in 0 until dates.size)
        {
            year = dates[i].substring(0,4).toInt()
            month = dates[i].substring(5,7).toInt()
            day = dates[i].substring(8,10).toInt()


            binding.calendarView.addDecorator(context?.let {

                EventDecorators(
                    it, colorString,
                    CalendarDay.from(year,month,day))
            })
        }



    }







}