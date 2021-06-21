package com.example.jadoproject

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.jadoproject.databinding.FragmentMonthlyBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.prolificinteractive.materialcalendarview.CalendarDay

import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.DayOfWeek

import java.util.*
import kotlin.collections.ArrayList


class MonthlyFragment : Fragment() {
    private lateinit var binding : FragmentMonthlyBinding

    var simpleDateFormat : SimpleDateFormat = SimpleDateFormat("mmmm-yyyy", Locale.KOREA)
    var DateFormat : SimpleDateFormat = SimpleDateFormat("yyyy-mm-dd", Locale.KOREA)

    val FIREBASE_URL = "https://jadoproject-530a4-default-rtdb.asia-southeast1.firebasedatabase.app"
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance(FIREBASE_URL)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_monthly, container, false)


        setDate()
        calendarButton()
        firebaseConnet()

        return binding.root
    }


    val colorString : Array<String> = arrayOf("#eb346e")
    fun setDate()
    {
        binding.calendarView.state().edit()
            .isCacheCalendarPositionEnabled(false)
            .setCalendarDisplayMode(CalendarMode.MONTHS).commit()

        binding.calendarView.isDynamicHeightEnabled = true
       // binding.calendarView.setPadding(0,-20,0,30)

        //CalendarDay{2021-6-21}

        //1개 이상
        binding.calendarView.addDecorator(context?.let {

            EventDecorators(
                it, colorString,
                CalendarDay.today())
        })

        binding.calendarView.addDecorator(context?.let {

            EventDecorators(
                it, colorString,
                CalendarDay.from(2021,6,22))
        })

    }

    //캘린더 버튼
    fun calendarButton()
    {
        binding.calendarView.setOnDateChangedListener { widget, date, selected ->
            val selectDate = date
            Log.d("selectDate", selectDate.toString())
            //select date가 eventlist에 있으면 fragement이동

        }


    }

    fun firebaseConnet()
    {

        val myRef = database.getReference("User").child("ID").child("dayeon")


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