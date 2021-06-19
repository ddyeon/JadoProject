package com.example.jadoproject

import android.database.DatabaseUtils
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.jadoproject.databinding.ActivityMainBinding
import com.example.jadoproject.databinding.FragmentWeekBinding
import com.example.jadoproject.databinding.ItemWeekBinding


class WeekFragment : Fragment() {
    private lateinit var binding: FragmentWeekBinding

    private var weekList  : ArrayList<ItemWeekBinding> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_week, container, false)


        weekList = arrayListOf(binding.include2, binding.include3,binding.include4, binding.include5, binding.include6,
        binding.include7, binding.include8)


        setOnClick()

        return binding.root
    }

    fun setOnClick()
    {
        for( i in 0 until weekList.size)
        {
            weekList[i].weekconst.setOnClickListener {
                parentFragmentManager.beginTransaction().replace(R.id.weekContainer,DailyFragment()).commit()
          /*      val action = WeekFragmentDirections.actionWeekFragmentToDailyFragment()
                findNavController().navigate(action)*/
            }
        }
    }


}