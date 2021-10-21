package com.example.frontend.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.frontend.R
import com.example.frontend.models.Client

class ClientAdapter : RecyclerView.Adapter<ClientAdapter.ViewHolder>() {

    private var myList = emptyList<Client>()

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_layout, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        return myList.size
    }

    fun setData(newList: List<Client>){
        myList = newList
        notifyDataSetChanged()
    }

}