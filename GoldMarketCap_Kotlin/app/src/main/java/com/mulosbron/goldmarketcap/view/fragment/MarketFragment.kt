package com.mulosbron.goldmarketcap.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mulosbron.goldmarketcap.R
import com.mulosbron.goldmarketcap.adapter.RecyclerViewAdapter
import com.mulosbron.goldmarketcap.model.DailyPercentage
import com.mulosbron.goldmarketcap.model.GoldPrice
import com.mulosbron.goldmarketcap.service.ApiService
import io.reactivex.disposables.CompositeDisposable

class MarketFragment : Fragment(), RecyclerViewAdapter.Listener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var apiService: ApiService
    private var goldPrices: Map<String, GoldPrice> = emptyMap()
    private var dailyPercentages: Map<String, DailyPercentage> = emptyMap()
    private var compositeDisposable: CompositeDisposable? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_market, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.rvMarket)
        recyclerView.layoutManager = LinearLayoutManager(context)
        apiService = ApiService(this)
        compositeDisposable = CompositeDisposable()

        fetchMarketData()
    }

    override fun onDestroy() {
        super.onDestroy()

        compositeDisposable?.clear()
    }

    override fun onItemClick(goldType: String, goldPrice: GoldPrice) {
        Toast.makeText(
            requireContext(), "Clicked: $goldType - Buying: ${goldPrice.buyingPrice}, " +
                    "Selling: ${goldPrice.sellingPrice}", Toast.LENGTH_LONG
        ).show()
    }

    private fun fetchMarketData() {
        apiService.fetchGoldPrices(compositeDisposable!!) { newGoldPrices ->
            this.goldPrices = newGoldPrices
            setAdapter()
        }

        apiService.fetchDailyPercentages(compositeDisposable!!) { newDailyPercentages ->
            this.dailyPercentages = newDailyPercentages
            setAdapter()
        }
    }

    private fun setAdapter() {
        if (goldPrices.isNotEmpty() && dailyPercentages.isNotEmpty()) {
            recyclerView.adapter = RecyclerViewAdapter(goldPrices, dailyPercentages, this)
        }
    }
}
