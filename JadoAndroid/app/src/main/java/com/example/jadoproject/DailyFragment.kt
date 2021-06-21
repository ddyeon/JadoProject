package com.example.jadoproject

import android.database.DatabaseUtils
import android.graphics.Color
import android.hardware.camera2.params.ColorSpaceTransform
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.jadoproject.databinding.FragmentDailyBinding
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate

class DailyFragment : Fragment() {

    private lateinit var binding : FragmentDailyBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_daily, container, false)

        drawChar()

        return binding.root
    }


    fun drawChar()
    {
        binding.piechart.setUsePercentValues(true)

        val pieList : ArrayList<PieEntry> = arrayListOf()
        pieList.add(PieEntry(25f,"Math", 0))
        pieList.add(PieEntry(30f,"Korean", 0))
        pieList.add(PieEntry(20f,"English", 0))
        pieList.add(PieEntry(25f,"Society", 0))

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


}