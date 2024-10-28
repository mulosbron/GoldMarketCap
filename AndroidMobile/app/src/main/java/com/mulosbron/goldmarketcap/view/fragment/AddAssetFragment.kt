package com.mulosbron.goldmarketcap.view.fragment

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.mulosbron.goldmarketcap.R
import com.mulosbron.goldmarketcap.databinding.FragmentAddAssetBinding
import com.mulosbron.goldmarketcap.model.DailyGoldPrice
import com.mulosbron.goldmarketcap.model.Transaction
import com.mulosbron.goldmarketcap.service.ApiService
import com.mulosbron.goldmarketcap.view.MainActivity
import java.util.Date
import java.util.Locale

class AddAssetFragment : Fragment() {

    private var _binding: FragmentAddAssetBinding? = null
    private val binding get() = _binding!!
    private lateinit var apiService: ApiService

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddAssetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        apiService = ApiService(this)

        val goldName = arguments?.getString("goldName")

        updateButtonColors()

        goldName?.let {
            apiService.fetchGoldPrice(it, this::updatePrice)
        }

        binding.rgBuySell.setOnCheckedChangeListener { _, _ ->
            updateButtonColors()
            goldName?.let {
                apiService.fetchGoldPrice(it, this::updatePrice)
            }
        }

        binding.btnAddProduct.setOnClickListener {
            goldName?.let {
                createAndSendTransaction(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateButtonColors() {
        if (binding.rbBuy.isChecked) {
            setButtonStyle(binding.rbBuy, R.drawable.selected_radiobutton_background, R.color.selected_color)
            setButtonStyle(binding.rbSell, R.drawable.default_radiobutton_background, R.color.black)
        } else {
            setButtonStyle(binding.rbSell, R.drawable.selected_radiobutton_background, R.color.selected_color)
            setButtonStyle(binding.rbBuy, R.drawable.default_radiobutton_background, R.color.black)
        }
    }

    private fun setButtonStyle(button: RadioButton, backgroundRes: Int, textColorRes: Int) {
        button.setBackgroundResource(backgroundRes)
        button.setTextColor(resources.getColor(textColorRes, null))
    }

    private fun createAndSendTransaction(goldName: String) {
        val username = (activity as MainActivity?)?.getUsername()
        if (username != null) {
            try {
                val amount = binding.etAmount.text.toString().toDoubleOrNull()
                val price = binding.etPrice.text.toString().toDoubleOrNull()

                if (amount != null && price != null) {
                    val transaction = Transaction(
                        id = "",
                        date = SimpleDateFormat(
                            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                            Locale.getDefault()
                        ).format(Date()),
                        transactionType = if (binding.rbBuy.isChecked) "buy" else "sell",
                        amount = amount,
                        price = price
                    )
                    apiService.sendTransaction(username, goldName, transaction)
                } else {
                    Toast.makeText(context, "Please enter valid numbers", Toast.LENGTH_SHORT).show()
                }
            } catch (e: NumberFormatException) {
                Toast.makeText(context, "Please enter valid numbers", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "Username not found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updatePrice(goldPrice: DailyGoldPrice?) {
        goldPrice?.let {
            activity?.runOnUiThread {
                binding.etPrice.setText(
                    if (binding.rbBuy.isChecked) it.buyingPrice.toString() else it.sellingPrice.toString()
                )
            }
        }
    }
}
