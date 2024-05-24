package com.mulosbron.goldmarketcap.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mulosbron.goldmarketcap.R
import com.mulosbron.goldmarketcap.adapter.TransactionAdapter
import com.mulosbron.goldmarketcap.service.ApiService
import com.mulosbron.goldmarketcap.view.MainActivity
import io.reactivex.disposables.CompositeDisposable

class TransactionFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var apiService: ApiService
    private var compositeDisposable: CompositeDisposable? = null
    private lateinit var adapter: TransactionAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_transaction, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.rvTransactions)
        recyclerView.layoutManager = LinearLayoutManager(context)
        apiService = ApiService(this)
        compositeDisposable = CompositeDisposable()

        val goldAsset = arguments?.getString("goldAsset")
        if (goldAsset != null) {
            apiService.fetchGoldTransactions { transactions ->
                val transactionList = transactions[goldAsset] ?: listOf()
                adapter = TransactionAdapter(transactionList, apiService, goldAsset, { transactionId ->
                    apiService.deleteTransaction(goldAsset, transactionId) {
                        Toast.makeText(context, "Transaction deleted", Toast.LENGTH_SHORT).show()
                        fetchTransactions(goldAsset)
                    }
                }, { transactionId ->
                    val bundle = Bundle().apply {
                        putString("transactionId", transactionId)
                        putString("goldType", goldAsset)
                    }
                    val updateFragment = UpdateTransactionFragment().apply {
                        arguments = bundle
                    }
                    (activity as? MainActivity)?.replaceFragment(updateFragment)
                })
                recyclerView.adapter = adapter
            }
        }

        view.findViewById<Button>(R.id.btnDeleteGoldAsset).setOnClickListener {
            if (goldAsset != null) {
                apiService.deleteGoldType(goldAsset) {
                    Toast.makeText(context, "Gold asset deleted", Toast.LENGTH_SHORT).show()
                    (activity as? MainActivity)?.replaceFragment(PortfolioFragment())
                }
            }
        }
    }

    private fun fetchTransactions(goldAsset: String) {
        apiService.fetchGoldTransactions { transactions ->
            val transactionList = transactions[goldAsset] ?: listOf()
            adapter.updateTransactions(transactionList)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable?.clear()
    }
}
