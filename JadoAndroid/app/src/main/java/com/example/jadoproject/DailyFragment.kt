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

        if(selectDay != null)
            binding.dailydate.text = textday
        else
            binding.dailydate.text = CalendarDay.today().toString()

        val totaldate = selectDay?.substring(12,17) + "0" + selectDay?.substring(17,21)
        Log.d("selectDay", totaldate)
        firebaseConnet(totaldate)

        drawChar()

        return binding.root
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

    fun drawChar()
    {
        binding.piechart.setUsePercentValues(true)

        val pieList : ArrayList<PieEntry> = arrayListOf()
        pieList.add(PieEntry(55f,"Math", 0))
        pieList.add(PieEntry(30f,"Korean", 0))
        pieList.add(PieEntry(20f,"English", 0))

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

        val studySubject : subjects = studyDate.subjects
        val f = SimpleDateFormat("HH:mm:ss", Locale.KOREA)
        val totlas = f.parse(studyDate.total_time)
        val beh = f.parse(studyDate.behavior_time)

        val diff = totlas.time - beh.time
        val sec = diff / 1000
        val min = diff / 1000/60
        val hour = diff /1000/60/60
        Log.d("sec", "$min")

      /*  val totalTime: Date = f.parse(studyDate.total_time)
        val mathTime: Date = f.parse(studySubject.Math)
        val koreanTime: Date = f.parse(studySubject.Korean)
        val englishTime: Date = f.parse(studySubject.English)


        val diffmath = (abs(mathTime.time) / abs(totalTime.time))
        val diffkorea = (abs(koreanTime.time) / abs(totalTime.time))
       val diffenglish = (abs(englishTime.time) / abs(totalTime.time))


        Log.d("timeeee","math${diffmath}korea${diffkorea}english${diffenglish}")
*/
        //Log.d("diff", "math${diffmath}korea${diffkorea}english${diffenglish}")

    }


}