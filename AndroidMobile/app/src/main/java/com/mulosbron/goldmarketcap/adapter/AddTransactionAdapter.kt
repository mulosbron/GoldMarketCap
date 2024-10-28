package com.mulosbron.goldmarketcap.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mulosbron.goldmarketcap.R

class AddTransactionAdapter(
    private val onItemClicked: (String) -> Unit
) : RecyclerView.Adapter<AddTransactionAdapter.ViewHolder>() {

    private var goldAssets: List<String> = listOf()

    @SuppressLint("NotifyDataSetChanged")
    fun updateGoldAssets(newGoldAssets: List<String>) {
        goldAssets = newGoldAssets
        notifyDataSetChanged()
    }

    class ViewHolder(view: View, private val onItemClicked: (String) -> Unit) : RecyclerView.ViewHolder(view) {
        private val goldNameTextView: TextView = view.findViewById(R.id.tvGoldName)

        fun bind(goldName: String) {
            goldNameTextView.text = goldName
            itemView.setOnClickListener {
                onItemClicked(goldName)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_layout_add_transaction, parent, false)
        return ViewHolder(view, onItemClicked)
    }

    override fun getItemCount(): Int {
        return goldAssets.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(goldAssets[position])
    }
}
