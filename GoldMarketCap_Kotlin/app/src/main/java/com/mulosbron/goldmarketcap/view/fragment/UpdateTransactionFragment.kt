package com.mulosbron.goldmarketcap.view.fragment

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
import com.mulosbron.goldmarketcap.model.Transaction
import com.mulosbron.goldmarketcap.model.TransactionUpdateModel
import com.mulosbron.goldmarketcap.service.ApiService
import com.mulosbron.goldmarketcap.view.MainActivity

class UpdateTransactionFragment : Fragment() {

    private lateinit var apiService: ApiService
    private lateinit var etUpdateDate: EditText
    private lateinit var rgUpdateTransactionType: RadioGroup
    private lateinit var rbUpdateBuy: RadioButton
    private lateinit var rbUpdateSell: RadioButton
    private lateinit var etUpdateAmount: EditText
    private lateinit var etUpdatePrice: EditText
    private lateinit var btnUpdate: Button

    private var transactionId: String? = null
    private var goldType: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_update_transaction, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        apiService = ApiService(this)

        etUpdateDate = view.findViewById(R.id.etUpdateDate)
        rgUpdateTransactionType = view.findViewById(R.id.rgUpdateBuySell)
        rbUpdateBuy = view.findViewById(R.id.rbUpdateBuy)
        rbUpdateSell = view.findViewById(R.id.rbUpdateSell)
        etUpdateAmount = view.findViewById(R.id.etUpdateAmount)
        etUpdatePrice = view.findViewById(R.id.etUpdatePrice)
        btnUpdate = view.findViewById(R.id.btnUpdateTransaction)

        transactionId = arguments?.getString("transactionId")
        goldType = arguments?.getString("goldType")

        transactionId?.let { id ->
            goldType?.let {
                apiService.getTransaction(id) { transaction ->
                    transaction?.let { populateFields(it) }
                }
            }
        }

        rgUpdateTransactionType.setOnCheckedChangeListener { _, _ ->
            updateButtonColors()
        }

        updateButtonColors()

        btnUpdate.setOnClickListener {
            val transactionType = when (rgUpdateTransactionType.checkedRadioButtonId) {
                R.id.rbUpdateBuy -> "buy"
                R.id.rbUpdateSell -> "sell"
                else -> null
            }

            val updatedTransaction = TransactionUpdateModel(
                date = etUpdateDate.text.toString().takeIf { it.isNotBlank() },
                transactionType = transactionType,
                amount = etUpdateAmount.text.toString().toDoubleOrNull(),
                price = etUpdatePrice.text.toString().toDoubleOrNull()
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

    private fun updateButtonColors() {
        if (rbUpdateBuy.isChecked) {
            rbUpdateBuy.setBackgroundResource(R.drawable.selected_radiobutton_background)
            rbUpdateBuy.setTextColor(resources.getColor(R.color.selected_color))
            rbUpdateSell.setBackgroundResource(R.drawable.default_radiobutton_background)
            rbUpdateSell.setTextColor(resources.getColor(R.color.black))
        } else {
            rbUpdateSell.setBackgroundResource(R.drawable.selected_radiobutton_background)
            rbUpdateSell.setTextColor(resources.getColor(R.color.selected_color))
            rbUpdateBuy.setBackgroundResource(R.drawable.default_radiobutton_background)
            rbUpdateBuy.setTextColor(resources.getColor(R.color.black))
        }
    }

    private fun populateFields(transaction: Transaction) {
        etUpdateDate.setText(transaction.date)
        when (transaction.transactionType) {
            "buy" -> rgUpdateTransactionType.check(R.id.rbUpdateBuy)
            "sell" -> rgUpdateTransactionType.check(R.id.rbUpdateSell)
        }
        etUpdateAmount.setText(transaction.amount.toString())
        etUpdatePrice.setText(transaction.price.toString())
    }
}
