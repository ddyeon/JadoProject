package com.example.jadoproject

import android.app.Activity
import android.graphics.Path
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.example.jadoproject.databinding.FragmentWeeklyBinding
import lib.kingja.switchbutton.SwitchMultiButton

class WeeklyFragment : Fragment() {
    
    private lateinit var binding : FragmentWeeklyBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_weekly, container, false)

        if(binding.switchMultiButton.selectedTab == 2)
            parentFragmentManager.beginTransaction().replace(R.id.weekContainer,MonthlyFragment()).commit()

        binding.switchMultiButton.setOnSwitchListener { position, tabText ->
            when (position) {
                0 -> {
                    parentFragmentManager.beginTransaction().replace(R.id.weekContainer,DailyFragment()).commit()


                }
                1->  {
                    parentFragmentManager.beginTransaction().replace(R.id.weekContainer,WeekFragment()).commit()
                }
                //month로 이동
                2 ->
                {
                    parentFragmentManager.beginTransaction().replace(R.id.weekContainer,MonthlyFragment()).commit()
                }
            }
        }

        return binding.root
    }

   
}