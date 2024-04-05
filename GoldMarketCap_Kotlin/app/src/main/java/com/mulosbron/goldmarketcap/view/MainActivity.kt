package com.mulosbron.goldmarketcap.view

import android.os.Bundle
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mulosbron.goldmarketcap.R
import com.mulosbron.goldmarketcap.adapter.RecyclerViewAdapter
import com.mulosbron.goldmarketcap.model.GoldPrice
import com.mulosbron.goldmarketcap.model.DailyPercentage
import com.mulosbron.goldmarketcap.service.GoldPricesAPI
import com.mulosbron.goldmarketcap.service.DailyPercentagesAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : FooterActivity(), RecyclerViewAdapter.Listener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var goldPrices: Map<String, GoldPrice>
    private lateinit var dailyPercentages: Map<String, DailyPercentage>
    private val baseURL = "https://goldmarketcap.xyz/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        loadApis()
        setupFooterNavigation()
    }

    private fun getRetrofitInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun loadApis() {
        goldPrices = emptyMap()
        dailyPercentages = emptyMap()

        loadGoldPrices()
        loadDailyPercentages()
    }

    private fun loadGoldPrices() {
        val retrofit = getRetrofitInstance()
        val goldPricesAPI = retrofit.create(GoldPricesAPI::class.java)

        goldPricesAPI.getLatestGoldPrices().enqueue(object : Callback<Map<String, GoldPrice>> {
            override fun onResponse(call: Call<Map<String, GoldPrice>>,
                                    response: Response<Map<String, GoldPrice>>) {
                if (response.isSuccessful) {
                    goldPrices = response.body() ?: emptyMap()
                    if (dailyPercentages.isNotEmpty()) {
                        setAdapter()
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Failed to load gold prices",
                        Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Map<String, GoldPrice>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error: ${t.message}",
                    Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun loadDailyPercentages() {
        val retrofit = getRetrofitInstance()
        val dailyPercentagesAPI = retrofit.create(DailyPercentagesAPI::class.java)

        dailyPercentagesAPI.getLatestDailyPercentages().enqueue(object : Callback<Map<String, DailyPercentage>>
        {
            override fun onResponse(call: Call<Map<String, DailyPercentage>>,
                                    response: Response<Map<String, DailyPercentage>>) {
                if (response.isSuccessful) {
                    dailyPercentages = response.body() ?: emptyMap()
                    if (goldPrices.isNotEmpty()) {
                        setAdapter()
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Failed to load daily percentages",
                        Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Map<String, DailyPercentage>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error: ${t.message}",
                    Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun setAdapter() {
        if (goldPrices.isNotEmpty() && dailyPercentages.isNotEmpty()) {
            recyclerView.adapter = RecyclerViewAdapter(goldPrices, dailyPercentages, this)
        }
    }

    override fun onItemClick(goldType: String, goldPrice: GoldPrice) {
        Toast.makeText(this, "Clicked: $goldType - Buying: ${goldPrice.buyingPrice}, " +
                "Selling: ${goldPrice.sellingPrice}", Toast.LENGTH_LONG).show()
    }
}