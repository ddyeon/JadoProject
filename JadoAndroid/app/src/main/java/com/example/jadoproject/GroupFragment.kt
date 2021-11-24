package com.example.jadoproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jadoproject.data.Groups
import com.example.jadoproject.databinding.FragmentGroupBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import timber.log.Timber

class GroupFragment : Fragment() {

    private lateinit var binding : FragmentGroupBinding
    val FIREBASE_URL = "https://jadoproject-530a4-default-rtdb.asia-southeast1.firebasedatabase.app"
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance(FIREBASE_URL)
    private val gson = Gson()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_group, container, false)
        getFirebaseGroupData()
        setLister()
        return binding.root
    }

    private fun setAdapter(items: ArrayList<Groups>)
    {
        val adapter = GroupListAdapter(items)
        binding.groupListRecyclerview.adapter = adapter
        binding.groupListRecyclerview.layoutManager = LinearLayoutManager(context)
    }

    private fun setLister()
    {
        binding.addGroupBtn.setOnClickListener {
            val action = GroupFragmentDirections.actionGroupToGroupPlusFragment()
            it.findNavController().navigate(action)

        }
    }

    private fun getFirebaseGroupData()
    {
        val item = arrayListOf<Any?>()
        val dbRef = database.getReference("Group")
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach { it ->
                    item.add(it.value)
                }

                val jsonString = gson.toJson(item)
                val listType = object : TypeToken<ArrayList<Groups>>() {}.type
                val newList = gson.fromJson<ArrayList<Groups>>(jsonString, listType)

                Timber.d("newlist${newList}")
                setAdapter(newList)
            }

            override fun onCancelled(error: DatabaseError) {
               Timber.d("error${error}")
            }

        })
    }
}