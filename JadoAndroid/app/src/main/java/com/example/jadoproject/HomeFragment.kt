package com.example.jadoproject

import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jadoproject.databinding.FragmentHomeBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
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
            binding.cha.visibility = View.GONE
            binding.TimeTable.visibility = View.VISIBLE
            binding.Stop.visibility = View.VISIBLE
            database.reference.child("Camera").setValue("true")
        }

        binding.StopBtn.setOnClickListener {
            stoptime = binding.StudyTimer.base - SystemClock.elapsedRealtime()
            binding.StudyTimer.stop()
            binding.StartandChange.visibility = View.VISIBLE
            binding.cha.visibility = View.VISIBLE
            binding.TimeTable.visibility = View.GONE
            binding.Stop.visibility = View.GONE
            database.reference.child("Camera").setValue("false")
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

    private fun setAdapter() {

       binding.todorecyclerview.adapter = adapter
       binding.todorecyclerview.layoutManager = LinearLayoutManager(context)

    }
}