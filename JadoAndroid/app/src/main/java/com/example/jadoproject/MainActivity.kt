package com.example.jadoproject

import android.os.Build
import android.os.Bundle

import androidx.annotation.RequiresApi

import androidx.appcompat.app.AppCompatActivity

import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

import com.example.jadoproject.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val homeFragment = HomeFragment()
    private val weeklyFragment = WeeklyFragment()
    private val groupFragment = GroupFragment()
    private val mypageFragment = MypageFragment()



    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.bottomNavigationView.itemIconTintList = null




        bottomnavi()

    }







    fun bottomnavi()
    {
        binding.bottomNavigationView.run {
            setOnNavigationItemSelectedListener {
                when(it.itemId) {
                    R.id.home -> {
                        changeFragment(homeFragment)
                    }
                    R.id.week -> {
                        changeFragment(weeklyFragment)
                    }
                    R.id.group -> {
                        changeFragment(groupFragment)
                    }
                    R.id.mypage -> {
                        changeFragment(mypageFragment)
                    }
                }
                true
            }
            selectedItemId = R.id.home
        }
    }

    private fun changeFragment(fragment: Fragment) {
        val manager: FragmentManager = supportFragmentManager
        val ft: FragmentTransaction = manager.beginTransaction()
        ft.replace(R.id.fragmentContainerView2, fragment).commit()
    }




    }