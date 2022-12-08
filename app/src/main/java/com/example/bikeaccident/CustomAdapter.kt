package com.example.bikeaccident

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView

internal class CustomAdapter(private var itemsList: List<String>) :
    RecyclerView.Adapter<CustomAdapter.MyViewHolder>() {
    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var itemTextView: TextView = view.findViewById(R.id.itemTextView)
        var itemTextView1: TextView = view.findViewById(R.id.itemTextView1)
//        var itemTextView2: TextView = view.findViewById(R.id.itemTextView2)
//        var itemTextView3: TextView = view.findViewById(R.id.itemTextView3)
    }
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item, parent, false)
        return MyViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//        for (i in 1 until itemsList.size) {
//            val item = itemsList[position]
//            val year = itemsList[position + i]
//            println(item)
//            println(year)
////        val alc = itemsList[position]
////        val place = itemsList[position]
//            holder.itemTextView.text = item
//            holder.itemTextView1.text = year
////        holder.itemTextView2.text = alc
////        holder.itemTextView3.text = place
//        }
        val item = itemsList[position]
        val year = itemsList[position]
        holder.itemTextView.text = item
        holder.itemTextView1.text = year
    }
    override fun getItemCount(): Int {
        return itemsList.size
    }
}