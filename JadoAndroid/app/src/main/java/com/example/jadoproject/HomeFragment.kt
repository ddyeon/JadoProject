package com.example.jadoproject

import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.jadoproject.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_home, container, false )

        var stoptime:Long=0

        binding.StartBtn.setOnClickListener {
            binding.StudyTimer.base=SystemClock.elapsedRealtime()+stoptime
            binding.StudyTimer.start()
            binding.StartandChange.visibility = View.GONE
            binding.cha.visibility = View.GONE
            binding.TimeTable.visibility =  View.VISIBLE
            binding.Stop.visibility = View.VISIBLE
        }

        binding.StopBtn.setOnClickListener{
            stoptime = binding.StudyTimer.base - SystemClock.elapsedRealtime()
            binding.StudyTimer.stop()
            binding.StartandChange.visibility = View.VISIBLE
            binding.cha.visibility = View.VISIBLE
            binding.TimeTable.visibility =  View.GONE
            binding.Stop.visibility = View.GONE
        }
        return binding.root
    }


}