package com.mulosbron.goldmarketcap.view

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import com.mulosbron.goldmarketcap.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.mulosbron.goldmarketcap.model.GoldPrice
import com.mulosbron.goldmarketcap.service.GoldPricesAPI
import com.mulosbron.goldmarketcap.service.QueryAPI

class AddTransactionActivity : FooterActivity() {

    private val baseURL = "https://goldmarketcap.xyz/"
    private lateinit var goldItemsListView: ListView
    private lateinit var searchEditText: EditText
    private lateinit var goldPrices: Map<String, GoldPrice>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_transaction)
        setupFooterNavigation()

        goldItemsListView = findViewById(R.id.lvGoldItems)
        searchEditText = findViewById(R.id.etSearchGold)

        loadApis()

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchGoldItems()
            }
        })

        goldItemsListView.setOnItemClickListener { adapterView, view, position, id ->
            val intent = Intent(this@AddTransactionActivity, AddProductActivity::class.java)
            intent.putExtra("goldName", adapterView.getItemAtPosition(position) as String)
            startActivity(intent)
        }
    }

    private fun getRetrofitInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun loadApis() {
        goldPrices = emptyMap()

        loadGoldTypes()
    }

    private fun loadGoldTypes() {
        val retrofit = getRetrofitInstance()
        val goldPricesAPI = retrofit.create(GoldPricesAPI::class.java)

        goldPricesAPI.getLatestGoldPrices().enqueue(object : Callback<Map<String, GoldPrice>> {
            override fun onResponse(
                call: Call<Map<String, GoldPrice>>,
                response: Response<Map<String, GoldPrice>>
            ) {
                if (response.isSuccessful) {
                    goldPrices = response.body() ?: emptyMap()
                    val goldNames = goldPrices.keys.toList()
                    val adapter = ArrayAdapter(this@AddTransactionActivity, android.R.layout.simple_list_item_1, goldNames)
                    goldItemsListView.adapter = adapter
                } else {
                    Toast.makeText(
                        this@AddTransactionActivity, "Failed to load gold prices",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<Map<String, GoldPrice>>, t: Throwable) {
                Toast.makeText(
                    this@AddTransactionActivity, "Error: ${t.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    private fun searchGoldItems() {
        val query = searchEditText.text.toString().trim()

        val queryAPI = Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(QueryAPI::class.java)

        if (query.isNotEmpty()) {
            queryAPI.searchGoldPriceNames(query).enqueue(object : Callback<List<String>> {
                override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                    if (response.isSuccessful) {
                        val goldNames = response.body() ?: emptyList()
                        val adapter = ArrayAdapter(this@AddTransactionActivity, android.R.layout.simple_list_item_1, goldNames)
                        goldItemsListView.adapter = adapter
                    } else {
                        Toast.makeText(
                            this@AddTransactionActivity, "Failed to load gold names",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<List<String>>, t: Throwable) {
                    Toast.makeText(
                        this@AddTransactionActivity, "Error: ${t.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        } else {
            loadGoldTypes()
        }
    }
}
