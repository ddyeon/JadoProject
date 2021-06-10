package com.example.jadoproject

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.jadoproject.databinding.FragmentMonthlyBinding
import com.prolificinteractive.materialcalendarview.CalendarDay

import com.prolificinteractive.materialcalendarview.CalendarMode
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.DayOfWeek

import java.util.*
import kotlin.collections.ArrayList


class MonthlyFragment : Fragment() {
    private lateinit var binding : FragmentMonthlyBinding

    var simpleDateFormat : SimpleDateFormat = SimpleDateFormat("mmmm-yyyy", Locale.KOREA)
    var DateFormat : SimpleDateFormat = SimpleDateFormat("yyyy-mm-dd", Locale.KOREA)



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_monthly, container, false)


        setDate()

        return binding.root
    }

    //캘린더 버튼
    fun calendarButton()
    {

    }

    val colorString : Array<String> = arrayOf("#eb346e")
    fun setDate()
    {
        binding.calendarView.state().edit()
            .isCacheCalendarPositionEnabled(false)
            .setCalendarDisplayMode(CalendarMode.MONTHS).commit()

        binding.calendarView.isDynamicHeightEnabled = true
       // binding.calendarView.setPadding(0,-20,0,30)

        binding.calendarView.addDecorator(context?.let {
            EventDecorators(
                it, colorString,
                CalendarDay.today())
        })


    }




}