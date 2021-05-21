package com.example.jadoproject

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.jadoproject.databinding.ActivityMainBinding
import java.io.*
import java.lang.Exception
import java.net.Socket

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val homeFragment = HomeFragment()
    private val weeklyFragment = WeeklyFragment()
    private val groupFragment = GroupFragment()
    private val mypageFragment = MypageFragment()

    private val html = ""
    private var mHandler: Handler? = null

    private var socket: Socket? = null

    private val networkReader: BufferedReader? = null
    private val networkWriter: PrintWriter? = null

    private var dos: DataOutputStream? = null
    private var dis: DataInputStream? = null

    private val ip = "192.168.0.20" // IP 번호

    private val port = 9999 // port 번호


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

       /* binding.include.connectionBtn.setOnClickListener {
            connectionSocket()
        }*/


        bottomnavi()

    }

    private fun connectionSocket()
    {
        mHandler = Handler()
        Log.d("connect", "연결하는중")

        val checkUpdate : Thread = object : Thread() {
            override fun run() {
                super.run()
                //ip받기

                try {
                    socket = Socket(ip,port)
                    Log.d("서버 접속 완료", "서버 접속됨")

                }
                catch (e1 : IOException) {
                    Log.w("서버접속못함", "서버접속못함")
                    e1.printStackTrace();
                }

                Log.w("edit 넘어가야 할 값 : ","안드로이드에서 서버로 연결요청")


                try {
                    dos = DataOutputStream(socket?.getOutputStream())
                    dis  = DataInputStream(socket?.getInputStream())

                    dos!!.writeUTF("안드로이드에서 서버로 연결요청")
                }catch (e1 : IOException) {
                    e1.printStackTrace();
                    Log.w("버퍼", "버퍼생성 잘못됨")
                }
                Log.w("버퍼","버퍼생성 잘됨")

                while (true)
                {
                    try {
                        var line = ""
                        var line2 = 0

                        while (true)
                        {
                            line2 = dis?.read()!!

                            if(line2 > 0)
                            {
                                Log.w("------서버에서 받아온 값 ", "" + line2);
                                dos?.writeUTF("하나 받았습니다. : " + line2);
                                dos?.flush();
                            }
                            if(line2 == 99) {
                                Log.w("------서버에서 받아온 값 ", "" + line2);
                                socket?.close();
                                break;
                            }
                        }
                    } catch (e : Exception) {

                    }

                }
            }

        }
        checkUpdate.start()
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