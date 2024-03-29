package com.mulosbron.goldmarketcap.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mulosbron.goldmarketcap.R
import com.mulosbron.goldmarketcap.model.GoldPrice

class RecyclerViewAdapter(private val goldPricesMap: Map<String, GoldPrice>, private val listener: Listener) : RecyclerView.Adapter<RecyclerViewAdapter.RowHolder>() {

    private val colors: Array<String> = arrayOf("#7fffd4","#76eec6","#66cdaa","#458b74")
    private val goldPricesList = goldPricesMap.toList()

    interface Listener{
        fun onItemClick(goldType: String, goldPrice: GoldPrice)
    }

    class RowHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val textName: TextView = view.findViewById(R.id.text_name)
        private val textBuyingPrice: TextView = view.findViewById(R.id.text_buying_price)
        private val textSellingPrice: TextView = view.findViewById(R.id.text_selling_price)

        fun bind(goldType: String, goldPrice: GoldPrice, colors: Array<String>, position: Int, listener: Listener){
            itemView.setOnClickListener{
                listener.onItemClick(goldType, goldPrice)
            }
            itemView.setBackgroundColor(Color.parseColor(colors[position % 4]))
            textName.text = goldType
            textBuyingPrice.text = goldPrice.buyingPrice.toString()
            textSellingPrice.text = goldPrice.sellingPrice.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_layout, parent, false)
        return RowHolder(view)
    }

    override fun getItemCount(): Int{
        return goldPricesMap.size
    }

    override fun onBindViewHolder(holder: RowHolder, position: Int) {
        holder.bind(goldPricesList[position].first, goldPricesList[position].second, colors, position, listener)
    }
}
