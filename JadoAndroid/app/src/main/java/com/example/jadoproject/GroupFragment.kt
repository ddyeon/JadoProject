package com.example.jadoproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jadoproject.data.Group
import com.example.jadoproject.databinding.FragmentGroupBinding

class GroupFragment : Fragment() {

    private lateinit var binding : FragmentGroupBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_group, container, false)

        val item : ArrayList<Group> = arrayListOf()
        val groups : Group = Group("Java를 잡아", "1일 1알고리즘 풀기!!")

        item.add(groups)
        setAdapter(item)

        return binding.root
    }

    private fun setAdapter(items : ArrayList<Group>)
    {
        val adapter = GroupListAdapter(items)
        binding.groupListRecyclerview.adapter = adapter
        binding.groupListRecyclerview.layoutManager = LinearLayoutManager(context)
    }
}