package com.example.jadoproject

import android.database.DatabaseUtils
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.jadoproject.databinding.ActivityMainBinding
import com.example.jadoproject.databinding.FragmentWeekBinding


class WeekFragment : Fragment() {
    private lateinit var binding: FragmentWeekBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_week, container, false)

        return binding.root
    }


}