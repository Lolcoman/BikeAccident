package com.example.bikeaccident

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AccidentAdapter(private val accident: MutableList<Accident>) : RecyclerView.Adapter<AccidentAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val id: TextView = itemView.findViewById(R.id.itemTextView)
        val year: TextView = itemView.findViewById(R.id.itemTextView1)
        val alc: TextView = itemView.findViewById(R.id.itemTextView2)
        val place: TextView = itemView.findViewById(R.id.itemTextView3)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.id.text = accident[position].id.toString()
        holder.year.text = accident[position].year.toString()
        holder.alc.text = accident[position].alc
        holder.place.text = accident[position].place
    }

    override fun getItemCount() = accident.size

}
