package com.example.jadoproject

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.DataBindingUtil.setContentView
import com.example.jadoproject.databinding.FragmentHomeAfterBinding


class HomeAfterFragment : Fragment(){

    private var time = 0
    //private var timerTask: Timer ?= null

    var flag = true

    private lateinit var binding : FragmentHomeAfterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_home_after, container, false)
        return binding.root
    }

//
//        binding.Stop_Btn.setOnClicklistener(View.OnClickListener {
//            endRecord()
//        })endRecord


    }


