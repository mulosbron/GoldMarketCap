package com.mulosbron.goldmarketcap.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.mulosbron.goldmarketcap.R

class EmptyPortfolioActivity : FooterActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_empty_portfolio)
        setupFooterNavigation()

        val btnAddTransaction: Button = findViewById(R.id.btnAddTransaction)
        btnAddTransaction.setOnClickListener {
            openAddTransactionActivity()
        }
    }

    private fun openAddTransactionActivity() {
        val intent = Intent(this, AddTransactionActivity::class.java)
        startActivity(intent)
    }
}