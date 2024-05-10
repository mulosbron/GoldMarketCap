package com.mulosbron.goldmarketcap.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mulosbron.goldmarketcap.R

class PortfolioAdapter(
    private val goldTypes: List<String>,
    private val listener: Listener
) : RecyclerView.Adapter<PortfolioAdapter.ViewHolder>() {

    private val colors: Array<String> = arrayOf("#FFD700", "#FFDF00") // Gold colors for alternating rows

    interface Listener {
        fun onItemClick(goldType: String)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvGoldName: TextView = view.findViewById(R.id.tvGoldName)
        val tvAvgBuyPriceInt: TextView = view.findViewById(R.id.tvAvgBuyPriceInt)
        val tvProfitLossInt: TextView = view.findViewById(R.id.tvProfitLossInt)

        fun bind(goldType: String, colors: Array<String>, position: Int, listener: Listener) {
            itemView.setOnClickListener {
                listener.onItemClick(goldType)
            }
            itemView.setBackgroundColor(Color.parseColor(colors[position % colors.size])) // Set background color
            tvGoldName.text = goldType
            tvAvgBuyPriceInt.text = "0" // Placeholder text
            tvProfitLossInt.text = "0" // Placeholder text
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_layout_portfolio, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(goldTypes[position], colors, position, listener)
    }

    override fun getItemCount(): Int {
        return goldTypes.size
    }
}

