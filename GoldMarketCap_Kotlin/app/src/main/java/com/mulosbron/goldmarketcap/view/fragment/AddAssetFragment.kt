package com.mulosbron.goldmarketcap.view.fragment

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.mulosbron.goldmarketcap.R
import com.mulosbron.goldmarketcap.model.GoldPrice
import com.mulosbron.goldmarketcap.model.Transaction
import com.mulosbron.goldmarketcap.view.MainActivity
import com.mulosbron.goldmarketcap.view.APIService
import java.util.Date
import java.util.Locale

class AddAssetFragment : Fragment() {

    private lateinit var btnBuy: RadioButton
    private lateinit var etAmount: EditText
    private lateinit var etPrice: EditText
    private lateinit var apiService: APIService

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_asset, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        apiService = APIService(this)

        btnBuy = view.findViewById(R.id.btnBuy)
        etAmount = view.findViewById(R.id.etAmount)
        etPrice = view.findViewById(R.id.etPrice)

        val goldName = arguments?.getString("goldName")
        goldName?.let {
            apiService.fetchGoldPrice(it, this::updateUI)
        }

        view.findViewById<Button>(R.id.btnAddProduct).setOnClickListener {
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
                    date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).format(Date()),
                    transactionType = if (btnBuy.isChecked) "buy" else "sell",
                    amount = etAmount.text.toString().toInt(),
                    price = etPrice.text.toString().toInt()
                )
                apiService.sendTransaction(username, goldName, transaction)
            } catch (e: NumberFormatException) {
                Toast.makeText(context, "Please enter valid numbers.", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "Username not found, please log in again.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(goldPrice: GoldPrice) {
        etPrice.post {
            etPrice.setText(if (btnBuy.isChecked) goldPrice.buyingPrice.toString() else goldPrice.sellingPrice.toString())
        }
    }
}


