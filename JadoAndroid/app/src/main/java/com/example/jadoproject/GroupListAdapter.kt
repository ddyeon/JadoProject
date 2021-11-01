package com.example.jadoproject

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.jadoproject.data.Group
import com.example.jadoproject.databinding.ItemGroupBinding

class GroupListAdapter(items : ArrayList<Group>) : RecyclerView.Adapter<GroupListAdapter.GroupHolder>() {

    private var item = items


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupHolder {

        val binding = DataBindingUtil.inflate<ItemGroupBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_group,
            parent,
            false)
        return GroupHolder(binding, LayoutInflater.from(parent.context).inflate(R.layout.item_group, parent, false)
        )
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

        fun bind(group: Group) {

            groupName.text = group.title
            groupGoal.text = group.goal

          /*  groupName.setOnClickListener {
                Log.d("error", "hi")
                Navigation.findNavController(binding.root).navigate(R.id.action_group_to_groupDetailFragment)
            }*/

        }



    }

}