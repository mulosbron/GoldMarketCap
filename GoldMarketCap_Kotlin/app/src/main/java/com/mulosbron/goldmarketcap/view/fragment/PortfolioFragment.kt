package com.mulosbron.goldmarketcap.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mulosbron.goldmarketcap.R
import com.mulosbron.goldmarketcap.adapter.PortfolioAdapter
import com.mulosbron.goldmarketcap.service.ApiService
import com.mulosbron.goldmarketcap.view.MainActivity
import io.reactivex.disposables.CompositeDisposable

class PortfolioFragment : Fragment(), PortfolioAdapter.Listener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var tvTotalValue: TextView
    private lateinit var btnAddAsset: Button
    private lateinit var apiService: ApiService
    private var goldAssets: List<String> = listOf()
    private var averagePrices: Map<String, Double> = emptyMap()
    private var profits: Map<String, Double> = emptyMap()
    private var compositeDisposable: CompositeDisposable? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_portfolio, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.rvPortfolio)
        recyclerView.layoutManager = LinearLayoutManager(context)
        tvTotalValue = view.findViewById(R.id.tvTotalValue)
        btnAddAsset = view.findViewById(R.id.btnAddAsset)
        apiService = ApiService(this)
        compositeDisposable = CompositeDisposable()

        btnAddAsset.setOnClickListener {
            (activity as? MainActivity)?.replaceFragment(AddTransactionFragment())
        }
        apiService.fetchGoldTransactions { transactions ->
            val newGoldAssets = transactions.keys.toList()
            averagePrices = apiService.calculateAverageBuyingPrices(transactions)
            goldAssets = newGoldAssets
            apiService.fetchGoldPrices(compositeDisposable!!) { goldPrices ->
                profits = apiService.calculateProfits(goldPrices, transactions)
                val totalPortfolioValue =
                    apiService.calculateTotalPortfolioValue(goldPrices, transactions)
                tvTotalValue.text = String.format("%.0f", totalPortfolioValue)
                recyclerView.adapter = PortfolioAdapter(goldAssets, averagePrices, profits, this)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        compositeDisposable?.clear()
    }

    override fun onItemClick(goldAsset: String) {
        val bundle = Bundle()
        bundle.putString("goldAsset", goldAsset)
        val transactionFragment = TransactionFragment()
        transactionFragment.arguments = bundle
        (activity as? MainActivity)?.replaceFragment(transactionFragment)
    }
}
