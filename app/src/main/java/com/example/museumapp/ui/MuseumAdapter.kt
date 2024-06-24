package com.example.museumapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.museumapp.R

class MuseumAdapter(private val museums: List<Map<String, String>>) : RecyclerView.Adapter<MuseumAdapter.MuseumViewHolder>() {

    class MuseumViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.museum_name)
        val addressTextView: TextView = view.findViewById(R.id.museum_address)
        val themeTextView: TextView = view.findViewById(R.id.museum_theme)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MuseumViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.museum_item, parent, false)
        return MuseumViewHolder(view)
    }

    override fun onBindViewHolder(holder: MuseumViewHolder, position: Int) {
        val museum = museums[position]
        holder.nameTextView.text = museum["name"]
        holder.addressTextView.text = museum["address"]
        holder.themeTextView.text = museum["theme"]
    }

    override fun getItemCount() = museums.size
}