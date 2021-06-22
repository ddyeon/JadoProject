package com.example.jadoproject

import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer.OnChronometerTickListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jadoproject.data.StudyDate
import com.example.jadoproject.databinding.FragmentHomeBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val gson by lazy { Gson() }
    val FIREBASE_URL = "https://jadoproject-530a4-default-rtdb.asia-southeast1.firebasedatabase.app"
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance(FIREBASE_URL)

    private val adapter = TodoListAdapter()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_home, container, false)
         firebaseConnet()
         setAdapter()    


        var stoptime: Long = 0

        binding.StartBtn.setOnClickListener {
            binding.StudyTimer.base = SystemClock.elapsedRealtime() + stoptime
            binding.StudyTimer.start()
            binding.StartandChange.visibility = View.GONE
            binding.TimeTable.visibility = View.VISIBLE
            binding.cha.visibility = View.VISIBLE
            binding.Stop.visibility = View.VISIBLE
            database.reference.child("Camera").setValue("true")
        }

        binding.StopBtn.setOnClickListener {
            //stoptime = binding.StudyTimer.base - SystemClock.elapsedRealtime()
            binding.StudyTimer.stop()
            binding.StartandChange.visibility = View.VISIBLE
            binding.cha.visibility = View.GONE
            binding.TimeTable.visibility = View.VISIBLE
            binding.Stop.visibility = View.GONE
            database.reference.child("Camera").setValue("false")
            //database.reference.child("User").child("hyunji").child("Study").child("2021-06-23").child("total_time").setValue(binding.StudyTimer.text)
            getData()
        }


        var listItem: ArrayList<String> = arrayListOf()
        listItem.add("자두 완성 시키기");
        listItem.add("잠 자기");
        adapter.setItems(listItem)

        binding.saveButton.setOnClickListener(View.OnClickListener {
            if (binding.InputTodo.getText().toString().length > 0) { // 한글자 이상 입력 된다면 추가
                val inputStr: String = binding.InputTodo.getText().toString()
                listItem.add(inputStr)//변경 이후 arrayAdapter에게 변경되었음을 알림 (새로고침)
                adapter.setItems(listItem)

            }
        })

        binding.removeButton.setOnClickListener(View.OnClickListener {
            val inputStr: String = binding.InputTodo.getText().toString()
            listItem.remove(inputStr)//변경 이후 arrayAdapter에게 변경되었음을 알림 (새로고침)
            adapter.setItems(listItem)
        })

        binding.StudyTimer.setText("00:00:00")
        binding.StudyTimer.setOnChronometerTickListener(OnChronometerTickListener { chronometer ->
            val text = chronometer.text
            if (text.length == 5) {
                chronometer.text = "00:$text"
            } else if (text.length == 7) {
                chronometer.text = "0$text"
            }
        })

        return binding.root
    }


    fun firebaseConnet() {
        val myRef = database.getReference("Camera")


        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val cameraInfo = dataSnapshot.value
                Log.d("camera", cameraInfo.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("connet", "fail")
                Log.d("eroor", error.toString())
            }
        })


    }

    fun getData()
    {
        val studyRef = database.getReference("User").child("hyunji").child("Study").child("2021-06-23")

        val studyArray : ArrayList<Any?> = arrayListOf()
        var times : ArrayList<StudyDate> = arrayListOf()

        studyRef.addListenerForSingleValueEvent(object  : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                studyArray.add(snapshot.value)
                val jsonString = gson.toJson(studyArray)
                val listType = object : TypeToken<ArrayList<StudyDate>>() {}.type
                val newList = gson.fromJson<ArrayList<StudyDate>>(jsonString, listType)
                Log.d("studylist", newList.toString())
                times = newList
                binding.NotStudy.text = times[0].behavior_count.toString()
                binding.Totaltime.text = times[0].total_time


                val t_time = times[0].total_time.split(":")
                val b_time = times[0].behavior_time.split(":")

                val t_hour = t_time[0]
                val b_hour = b_time[0]

                val t_min = t_time[1]
                val b_min = b_time[1]

                val t_sec = t_time[2]
                val b_sec = b_time[2]

                val r_hour = t_hour.toInt() - b_hour.toInt()
                val r_min = t_min.toInt() - b_min.toInt()
                val r_sec = t_sec.toInt() - b_sec.toInt()

                val r_time = String.format("%02d", r_hour) +  ":" + String.format("%02d", r_min) +  ":" +  String.format("%02d", r_sec)
                Log.d("real_time", r_time)
                binding.Realtime.text = r_time

            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("connet", "fail")
                Log.d("eroor", error.toString())
            }

        })

    }

    private fun setAdapter() {
       binding.todorecyclerview.adapter = adapter
       binding.todorecyclerview.layoutManager = LinearLayoutManager(context)

    }

//
}