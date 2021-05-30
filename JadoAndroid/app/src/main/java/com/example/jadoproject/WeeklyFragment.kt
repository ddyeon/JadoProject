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
        
        binding.switchMultiButton.setOnSwitchListener { position, tabText ->
            when (position) {
                0 -> {
                    Navigation.findNavController(requireActivity(), R.id.weekContainer).navigate(R.id.action_weeklyFragment_to_dailyFragment)
                }
                1-> Navigation.findNavController(requireActivity(), R.id.weekContainer).navigate(R.id.action_weeklyFragment_to_weekFragment)
            }
        }

        return binding.root
    }

   
}