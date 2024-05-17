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
    private val averagePrices: Map<String, Double>,
    private val profits: Map<String, Double>,
    private val listener: Listener
) : RecyclerView.Adapter<PortfolioAdapter.ViewHolder>() {

    private val colors: Array<String> = arrayOf("#FFD700", "#FFDF00")

    interface Listener {
        fun onItemClick(goldType: String)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvGoldName: TextView = view.findViewById(R.id.tvGoldName)
        val tvAvgBuyPriceInt: TextView = view.findViewById(R.id.tvAvgBuyPrice)
        val tvProfitLossInt: TextView = view.findViewById(R.id.tvProfitLoss)

        fun bind(
            goldType: String,
            averagePrice: Double,
            profit: Double,
            colors: Array<String>,
            position: Int,
            listener: Listener
        ) {
            itemView.setOnClickListener {
                listener.onItemClick(goldType)
            }
            itemView.setBackgroundColor(Color.parseColor(colors[position % colors.size]))
            tvGoldName.text = goldType
            tvAvgBuyPriceInt.text =
                if (averagePrice < 0) "0" else String.format("%.0f", averagePrice)
            tvProfitLossInt.text = String.format("%.0f", profit)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_layout_portfolio, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val goldType = goldTypes[position]
        val averagePrice = averagePrices[goldType] ?: 0.0
        val profit = profits[goldType] ?: 0.0
        holder.bind(goldType, averagePrice, profit, colors, position, listener)
    }

    override fun getItemCount(): Int {
        return goldTypes.size
    }
}
