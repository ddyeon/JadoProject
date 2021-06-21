package com.example.jadoproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TodoListAdapter : RecyclerView.Adapter<TodoListAdapter.TodoHolder>() {

    private var item = ArrayList<String>()

    fun setItems(items: ArrayList<String>) {
        item.clear()
        item.addAll(items)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoHolder {
        return TodoHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_todolist, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return item.size
    }

    override fun onBindViewHolder(holder: TodoHolder, position: Int) {
        item?.let { it ->
            (holder as TodoHolder).bind(item[position])
        }


    }

    class TodoHolder(itemview : View) : RecyclerView.ViewHolder(itemview) {

        val item = itemView.findViewById<TextView>(R.id.todotext)

        fun bind(text: String) {

            item.text = text

        }


    }

}