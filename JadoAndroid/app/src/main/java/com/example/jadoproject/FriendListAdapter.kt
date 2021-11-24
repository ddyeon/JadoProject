package com.example.jadoproject

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.jadoproject.data.Friend
import com.example.jadoproject.data.Info
import com.example.jadoproject.databinding.ItemFriendlistBinding
import com.example.jadoproject.databinding.ItemGroupBinding

class FriendListAdapter(items : ArrayList<Info>) : RecyclerView.Adapter<FriendListAdapter.FriendViewHolder>() {

    private var item = items


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {

        val binding = DataBindingUtil.inflate<ItemFriendlistBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_friendlist,
            parent,
            false)
        return FriendViewHolder(binding, LayoutInflater.from(parent.context).inflate(R.layout.item_friendlist, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return item.size
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        item?.let { it ->
            (holder).bind(item[position])
        }


    }

    class FriendViewHolder(private val binding: ItemFriendlistBinding, itemview: View) : RecyclerView.ViewHolder(itemview) {


        fun bind(friend: Info) {
            Log.d("info",friend.email)
            binding.name.text = friend.email
        }



    }

}