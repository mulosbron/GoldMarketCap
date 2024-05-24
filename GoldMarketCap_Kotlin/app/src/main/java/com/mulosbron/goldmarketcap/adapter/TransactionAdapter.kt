package com.mulosbron.goldmarketcap.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mulosbron.goldmarketcap.R
import com.mulosbron.goldmarketcap.model.Transaction
import com.mulosbron.goldmarketcap.service.ApiService

class TransactionAdapter(
    private var transactions: List<Transaction>,
    private val apiService: ApiService,
    private val goldAsset: String,
    private val onDeleteTransaction: (String) -> Unit,
    private val onUpdateTransaction: (String) -> Unit
) : RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    private val colors: Array<String> = arrayOf("#FFD700", "#FFDF00")

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val transactionTypeTextView: TextView = view.findViewById(R.id.tvTransactionType)
        val amountTextView: TextView = view.findViewById(R.id.tvAmount)
        val priceTextView: TextView = view.findViewById(R.id.tvPrice)
        val dateTextView: TextView = view.findViewById(R.id.tvDate)
        val deleteButton: Button = view.findViewById(R.id.btnDeleteTransaction)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_layout_transaction, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val transaction = transactions[position]
        holder.transactionTypeTextView.text = transaction.transactionType
        holder.amountTextView.text = transaction.amount.toString()
        holder.priceTextView.text = transaction.price.toString()
        holder.dateTextView.text = transaction.date
        holder.itemView.setBackgroundColor(Color.parseColor(colors[position % colors.size]))

        holder.itemView.setOnClickListener {
            onUpdateTransaction(transaction.id)
        }

        holder.deleteButton.setOnClickListener {
            onDeleteTransaction(transaction.id)
        }
    }

    override fun getItemCount(): Int {
        return transactions.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateTransactions(newTransactions: List<Transaction>) {
        transactions = newTransactions
        notifyDataSetChanged()
    }
}
