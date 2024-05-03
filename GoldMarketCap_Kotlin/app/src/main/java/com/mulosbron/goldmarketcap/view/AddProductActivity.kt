package com.mulosbron.goldmarketcap.view

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import com.mulosbron.goldmarketcap.R
import com.mulosbron.goldmarketcap.model.GoldPrice
import com.mulosbron.goldmarketcap.model.Transaction
import com.mulosbron.goldmarketcap.model.TransactionResponse
import com.mulosbron.goldmarketcap.service.GoldPricesAPI
import com.mulosbron.goldmarketcap.service.PortfolioAPI
import com.mulosbron.goldmarketcap.service.UserAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class AddProductActivity : FooterActivity() {

    private val baseURL = "https://goldmarketcap.xyz/"
    private lateinit var btnBuy: RadioButton
    private lateinit var etAmount: EditText
    private lateinit var etPrice: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)
        setupFooterNavigation()

        btnBuy = findViewById(R.id.btnBuy)
        etAmount = findViewById(R.id.etAmount)
        etPrice = findViewById(R.id.etPrice)

        val goldName = intent.getStringExtra("goldName")
        if (goldName != null) {
            fetchGoldPrice(goldName)
        }

        val btnAddProduct: Button = findViewById(R.id.btnAddProduct)
        btnAddProduct.setOnClickListener {
            if (goldName != null) {
                sendTransaction(goldName)
            }
        }
    }

    private fun fetchGoldPrice(productName: String) {
        val goldPricesAPI = Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GoldPricesAPI::class.java)

        goldPricesAPI.getGoldPrice(productName).enqueue(object : Callback<GoldPrice> {
            override fun onResponse(call: Call<GoldPrice>, response: Response<GoldPrice>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        updateUI(it)
                    }
                } else {
                    Toast.makeText(this@AddProductActivity, "Failed to fetch gold price", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<GoldPrice>, t: Throwable) {
                Toast.makeText(this@AddProductActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun updateUI(goldPrice: GoldPrice) {
        findViewById<RadioGroup>(R.id.buySellToggleGroup).setOnCheckedChangeListener { group, checkedId ->
            val priceEditText = findViewById<EditText>(R.id.etPrice)
            if (checkedId == R.id.btnBuy) {
                priceEditText.setText(goldPrice.buyingPrice.toString())
            } else if (checkedId == R.id.btnSell) {
                priceEditText.setText(goldPrice.sellingPrice.toString())
            }
        }
    }

    private fun sendTransaction(productName: String) {
        // TEST
        val username = "user556008"
        val transaction = Transaction(
            id = "0",
            date = "0",
            transactionType = if (btnBuy.isChecked) "buy" else "sell",
            amount = etAmount.text.toString().toInt(),
            price = etPrice.text.toString().toInt()
        )

        val portfolioAPI = Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PortfolioAPI::class.java)

        portfolioAPI.addTransaction(username, productName, transaction).enqueue(object : Callback<TransactionResponse> {
            override fun onResponse(call: Call<TransactionResponse>, response: Response<TransactionResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@AddProductActivity, response.body()?.message, Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@AddProductActivity, "Failed to add transaction", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<TransactionResponse>, t: Throwable) {
                Toast.makeText(this@AddProductActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}