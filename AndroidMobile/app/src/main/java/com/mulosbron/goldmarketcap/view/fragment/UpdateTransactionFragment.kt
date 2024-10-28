package com.mulosbron.goldmarketcap.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.mulosbron.goldmarketcap.R
import com.mulosbron.goldmarketcap.databinding.FragmentUpdateTransactionBinding
import com.mulosbron.goldmarketcap.model.Transaction
import com.mulosbron.goldmarketcap.model.TransactionUpdateModel
import com.mulosbron.goldmarketcap.service.ApiService

class UpdateTransactionFragment : Fragment() {

    private var _binding: FragmentUpdateTransactionBinding? = null
    private val binding get() = _binding!!
    private lateinit var apiService: ApiService
    private var transactionId: String? = null
    private var goldType: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUpdateTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        apiService = ApiService(this)

        transactionId = arguments?.getString("transactionId")
        goldType = arguments?.getString("goldType")

        transactionId?.let { id ->
            goldType?.let {
                apiService.getTransaction(id) { transaction ->
                    transaction?.let { populateFields(it) }
                }
            }
        }

        binding.rgUpdateBuySell.setOnCheckedChangeListener { _, _ ->
            updateButtonColors()
        }

        updateButtonColors()

        binding.btnUpdateTransaction.setOnClickListener {
            val transactionType = when (binding.rgUpdateBuySell.checkedRadioButtonId) {
                R.id.rbUpdateBuy -> "buy"
                R.id.rbUpdateSell -> "sell"
                else -> null
            }

            val updatedTransaction = TransactionUpdateModel(
                date = binding.etUpdateDate.text.toString().takeIf { it.isNotBlank() },
                transactionType = transactionType,
                amount = binding.etUpdateAmount.text.toString().toDoubleOrNull(),
                price = binding.etUpdatePrice.text.toString().toDoubleOrNull()
            )

            transactionId?.let { id ->
                goldType?.let { goldType ->
                    apiService.updateTransaction(id, updatedTransaction) {
                        Toast.makeText(context, "Transaction updated successfully", Toast.LENGTH_SHORT).show()
                        parentFragmentManager.popBackStack()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateButtonColors() {
        if (binding.rbUpdateBuy.isChecked) {
            setButtonStyle(binding.rbUpdateBuy, R.drawable.selected_radiobutton_background, R.color.selected_color)
            setButtonStyle(binding.rbUpdateSell, R.drawable.default_radiobutton_background, R.color.black)
        } else {
            setButtonStyle(binding.rbUpdateSell, R.drawable.selected_radiobutton_background, R.color.selected_color)
            setButtonStyle(binding.rbUpdateBuy, R.drawable.default_radiobutton_background, R.color.black)
        }
    }

    private fun setButtonStyle(button: RadioButton, backgroundRes: Int, textColorRes: Int) {
        button.setBackgroundResource(backgroundRes)
        button.setTextColor(resources.getColor(textColorRes, null))
    }

    private fun populateFields(transaction: Transaction) {
        binding.etUpdateDate.setText(transaction.date)
        when (transaction.transactionType) {
            "buy" -> binding.rgUpdateBuySell.check(R.id.rbUpdateBuy)
            "sell" -> binding.rgUpdateBuySell.check(R.id.rbUpdateSell)
        }
        binding.etUpdateAmount.setText(transaction.amount.toString())
        binding.etUpdatePrice.setText(transaction.price.toString())
    }
}
