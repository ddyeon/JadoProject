package com.example.jadoproject

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.RecyclerView
import com.example.jadoproject.data.Groups
import com.example.jadoproject.databinding.ItemGroupBinding

class GroupListAdapter(items: ArrayList<Groups>) : RecyclerView.Adapter<GroupListAdapter.GroupHolder>() {

    private var item = items


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupHolder {

        val binding = DataBindingUtil.inflate<ItemGroupBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_group,
            parent,
            false)
        return GroupHolder(binding, LayoutInflater.from(parent.context).inflate(R.layout.item_group, parent, false))
    }

    override fun getItemCount(): Int {
        return item.size
    }

    override fun onBindViewHolder(holder: GroupHolder, position: Int) {
        item?.let { it ->
            (holder).bind(item[position])
        }


    }

    class GroupHolder(private val binding: ItemGroupBinding, itemview: View) : RecyclerView.ViewHolder(itemview) {

        val groupName = itemView.findViewById<TextView>(R.id.group_name)
        val groupGoal = itemView.findViewById<TextView>(R.id.group_goal)

        fun bind(group: Groups) {

            groupName.text = group.groupname
            groupGoal.text = group.goal

            binding.groupCardView.setOnClickListener {
                Navigation.findNavController(binding.root).navigate(R.id.action_group_to_groupDetailFragment)
            }

        }



    }

}