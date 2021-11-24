package com.example.jadoproject

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.jadoproject.data.StudyDate
import com.example.jadoproject.data.subjects
import com.example.jadoproject.databinding.FragmentDailyBinding
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.prolificinteractive.materialcalendarview.CalendarDay
import java.lang.Math.abs
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DailyFragment : Fragment() {

    private lateinit var binding : FragmentDailyBinding
    val FIREBASE_URL = "https://jadoproject-530a4-default-rtdb.asia-southeast1.firebasedatabase.app"
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance(FIREBASE_URL)
    private val gson by lazy { Gson() }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_daily, container, false)
        val selectDay = arguments?.getString("selectday")
        val textday = selectDay?.substring(17,18) + "월" + selectDay?.substring(19,21) + "일"

      /*  if(selectDay != null)
            binding.dailydate.text = textday*/


        val totaldate = selectDay?.substring(12,17) + "0" + selectDay?.substring(17,21)
        Log.d("selectDay", totaldate)

        //그냥 일간
        if(arguments?.getString("flags") == "first")
        {
            binding.dailydate.text = "2021-11-24" //오늘날짜
            todayConnet()
            drawChar(20f,30f,50f)
        }
        //month에서 클릭해서 이동했을때
        else
        {
            binding.dailydate.text = textday
            drawChar(55f,30f,15f)
            firebaseConnet(totaldate)
        }




        return binding.root
    }

    fun todayConnet()
    {
        val studyRef = database.getReference("User").child("dayeon").child("Study").child("2021-11-23")

        val studyArray : ArrayList<Any?> = arrayListOf()
        var times : ArrayList<StudyDate> = arrayListOf()

        studyRef.addListenerForSingleValueEvent(object  : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                studyArray.add(snapshot.value)
                val jsonString = gson.toJson(studyArray)
                val listType = object : TypeToken<ArrayList<StudyDate>>() {}.type
                val newList = gson.fromJson<ArrayList<StudyDate>>(jsonString, listType)
                Log.d("studylist", newList.toString())
                times = newList
                textset(times[0])


            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("connet", "fail")
                Log.d("eroor", error.toString())
            }

        })

    }




        fun firebaseConnet(date : String)
        {

            val myRef = database.getReference("User").child("dayeon").child("Study").child(date)
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
                    timeCal(dateresult[0])
                    Log.d("neslist",newList.toString())
                    textset(dateresult[0])


                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("connet", "fail")
                    Log.d("eroor", error.toString())

                }


            })


        }

    fun drawChar(math : Float, korean : Float, english : Float)
    {
        binding.piechart.setUsePercentValues(true)

        val pieList : ArrayList<PieEntry> = arrayListOf()
        pieList.add(PieEntry(math,"Math", 20))
        pieList.add(PieEntry(korean,"Korean", 50))
        pieList.add(PieEntry(english,"English", 30))

        val dataSet : PieDataSet = PieDataSet(pieList,"")
        val data = PieData(dataSet)

        data.setValueFormatter(PercentFormatter())
        binding.piechart.data = data
        val description : Description = Description()
        description.text = ""
        binding.piechart.description = description
        binding.piechart.isDrawHoleEnabled = true
        binding.piechart.transparentCircleRadius = 12f
        binding.piechart.holeRadius = 12f
        //dataSet.setColors(Color.parseColor("#eac9ff"))
        dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS,200)
        data.setValueTextSize(13f)
        data.setValueTextColor(Color.DKGRAY)



    }
    fun textset(studyDate: StudyDate)
    {
        val studySubject : subjects = studyDate.subjects
        binding.korean.text = studySubject.Korean
        binding.english.text = studySubject.English
        binding.math.text = studySubject.Math
        binding.totalstudy.text = studyDate.total_time
        binding.behaviortime.text = studyDate.behavior_count.toString()
    }



    fun timeCal(studyDate: StudyDate)
    {

        val t_time = studyDate.total_time.split(":")
        val b_time = studyDate.behavior_time.split(":")

        val t_hour = t_time[0]
        val b_hour = b_time[0]

        val t_min = t_time[1]
        val b_min = b_time[1]

        val t_sec = t_time[2]
        val b_sec = b_time[2]

        val r_hour = t_hour.toInt() - b_hour.toInt()
        val r_min = t_min.toInt() - b_min.toInt()
        val r_sec = t_sec.toInt() - b_sec.toInt()

        val r_time = String.format("%02d", r_hour) +  ":" + String.format("%02d", r_min) +  ":" +  String.format("%02d", r_sec)
        Log.d("real_time", r_time)

        binding.realtime.text = r_time



    }


}