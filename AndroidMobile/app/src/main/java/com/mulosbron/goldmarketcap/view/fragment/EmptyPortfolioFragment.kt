package com.mulosbron.goldmarketcap.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.mulosbron.goldmarketcap.R
import com.mulosbron.goldmarketcap.view.MainActivity

class EmptyPortfolioFragment : Fragment() {

    private lateinit var btnAddTransaction: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_empty_portfolio, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnAddTransaction = view.findViewById(R.id.btnAddTransaction)

        btnAddTransaction.setOnClickListener {
            (activity as? MainActivity)?.replaceFragment(AddTransactionFragment())
        }
    }
}