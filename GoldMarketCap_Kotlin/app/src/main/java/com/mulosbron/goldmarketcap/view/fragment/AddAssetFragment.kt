package com.mulosbron.goldmarketcap.view.fragment

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.mulosbron.goldmarketcap.R
import com.mulosbron.goldmarketcap.model.GoldPrice
import com.mulosbron.goldmarketcap.model.Transaction
import com.mulosbron.goldmarketcap.service.ApiService
import com.mulosbron.goldmarketcap.view.MainActivity
import java.util.Date
import java.util.Locale

class AddAssetFragment : Fragment() {

    private lateinit var rgBuySell: RadioGroup
    private lateinit var btnBuy: RadioButton
    private lateinit var etAmount: EditText
    private lateinit var etPrice: EditText
    private lateinit var btnAddAsset: Button
    private lateinit var apiService: ApiService

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_asset, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rgBuySell = view.findViewById(R.id.rgBuySell)
        btnBuy = view.findViewById(R.id.rbBuy)
        etAmount = view.findViewById(R.id.etAmount)
        etPrice = view.findViewById(R.id.etPrice)
        btnAddAsset = view.findViewById(R.id.btnAddProduct)
        apiService = ApiService(this)

        val goldName = arguments?.getString("goldName")

        rgBuySell.setOnCheckedChangeListener { _, _ ->
            goldName?.let {
                apiService.fetchGoldPrice(it, this::updatePrice)
            }
        }

        btnAddAsset.setOnClickListener {
            goldName?.let {
                createAndSendTransaction(it)
            }
        }
    }


    private fun createAndSendTransaction(goldName: String) {
        val username = (activity as MainActivity?)?.getUsername()
        if (username != null) {
            try {
                val transaction = Transaction(
                    id = "",
                    date = SimpleDateFormat(
                        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                        Locale.getDefault()
                    ).format(Date()),
                    transactionType = if (btnBuy.isChecked) "buy" else "sell",
                    amount = etAmount.text.toString().toDouble(),
                    price = etPrice.text.toString().toDouble()
                )
                apiService.sendTransaction(username, goldName, transaction)
            } catch (e: NumberFormatException) {
                Toast.makeText(context, "Please enter valid numbers", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "Username not found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updatePrice(goldPrice: GoldPrice) {
        etPrice.post {
            etPrice.setText(if (btnBuy.isChecked) goldPrice.buyingPrice.toString() else goldPrice.sellingPrice.toString())
        }
    }
}


