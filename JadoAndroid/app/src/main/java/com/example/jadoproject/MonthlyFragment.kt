package com.example.jadoproject

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.jadoproject.databinding.FragmentMonthlyBinding
import com.github.sundeepk.compactcalendarview.domain.Event
import java.text.DateFormat
import java.text.SimpleDateFormat

import java.util.*


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

    fun setDate()
    {
        val c = Calendar.getInstance().time
        val df = SimpleDateFormat("yyyy-mm-dd")
        val formattedDate = df.format(c)

        binding.calanderView.setUseThreeLetterAbbreviation(true)

        val sdf = SimpleDateFormat("MMMM yyyy")

        val myCalendar = Calendar.getInstance()

        myCalendar.set(Calendar.YEAR, 2021)
        myCalendar.set(Calendar.MONTH, 6)
        myCalendar.set(Calendar.DAY_OF_MONTH, 17 )




        val event = Event(Color.RED, System.currentTimeMillis()+ 3600 * 1000, "Test")
        binding.calanderView.addEvent(event)



    }


}