package com.mulosbron.goldmarketcap.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mulosbron.goldmarketcap.R
import com.mulosbron.goldmarketcap.adapter.RecyclerViewAdapter
import com.mulosbron.goldmarketcap.model.GoldPrice
import com.mulosbron.goldmarketcap.service.GoldPricesAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), RecyclerViewAdapter.Listener {

    private lateinit var recyclerView: RecyclerView
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
        loadGoldPrices()
    }

    private fun getRetrofitInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun loadGoldPrices() {
        val retrofit = getRetrofitInstance()
        val goldPricesAPI = retrofit.create(GoldPricesAPI::class.java)

        goldPricesAPI.getLatestGoldPrices().enqueue(object : Callback<Map<String, GoldPrice>> {
            override fun onResponse(call: Call<Map<String, GoldPrice>>, response: Response<Map<String, GoldPrice>>) {
                if (response.isSuccessful) {
                    val goldPrices = response.body() ?: mapOf()
                    recyclerView.adapter = RecyclerViewAdapter(goldPrices, this@MainActivity)
                } else {
                    Toast.makeText(this@MainActivity, "Failed to load gold prices", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Map<String, GoldPrice>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onItemClick(goldType: String, goldPrice: GoldPrice) {
        Toast.makeText(this, "Clicked: $goldType - Buying: ${goldPrice.buyingPrice}, Selling: ${goldPrice.sellingPrice}", Toast.LENGTH_LONG).show()
    }
}