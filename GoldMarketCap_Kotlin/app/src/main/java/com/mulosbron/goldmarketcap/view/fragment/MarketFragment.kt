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
import com.mulosbron.goldmarketcap.model.GoldPrice
import com.mulosbron.goldmarketcap.model.DailyPercentage
import com.mulosbron.goldmarketcap.view.APIService

class MarketFragment : Fragment(), RecyclerViewAdapter.Listener {

    private lateinit var recyclerView: RecyclerView
    private var goldPrices: Map<String, GoldPrice> = emptyMap()
    private var dailyPercentages: Map<String, DailyPercentage> = emptyMap()
    private lateinit var apiService: APIService

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_market, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.rvMarket)
        recyclerView.layoutManager = LinearLayoutManager(context)
        apiService = APIService(this)
        fetchMarketData()
    }

    override fun onResume() {
        super.onResume()
        fetchMarketData()
    }

    override fun onItemClick(goldType: String, goldPrice: GoldPrice) {
        Toast.makeText(
            requireContext(), "Clicked: $goldType - Buying: ${goldPrice.buyingPrice}, " +
                    "Selling: ${goldPrice.sellingPrice}", Toast.LENGTH_LONG
        ).show()
    }

    private fun fetchMarketData() {
        apiService.fetchGoldPrices { newGoldPrices ->
            updateGoldPrices(newGoldPrices)
            setAdapter()
        }
        apiService.fetchDailyPercentages { newDailyPercentages ->
            updateDailyPercentages(newDailyPercentages)
            setAdapter()
        }
    }

    private fun updateGoldPrices(newGoldPrices: Map<String, GoldPrice>) {
        this.goldPrices = newGoldPrices
    }

    private fun updateDailyPercentages(newDailyPercentages: Map<String, DailyPercentage>) {
        this.dailyPercentages = newDailyPercentages
    }

    private fun setAdapter() {
        if (goldPrices.isNotEmpty() && dailyPercentages.isNotEmpty()) {
            recyclerView.adapter = RecyclerViewAdapter(goldPrices, dailyPercentages, this)
        }
    }
}
