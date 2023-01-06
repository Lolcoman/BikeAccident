package com.example.bikeaccident

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bikeaccident.databinding.ItemBinding

class AccidentAdapter(private val accident: MutableList<Accident>) : RecyclerView.Adapter<AccidentAdapter.ViewHolder>() {

    private lateinit var binding: ItemBinding

    inner class ViewHolder(itemView: ItemBinding) : RecyclerView.ViewHolder(itemView.root) {
        fun bind(item: Accident){
            binding.apply {
                itemTextView.text = item.id.toString()
                itemTextView1.text = item.year.toString()
                itemTextView2.text = item.alc
                itemTextView3.text = item.place
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = ItemBinding.inflate(inflater,parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        holder.bind(accident[position])
    }

    override fun getItemCount() = accident.size

}
