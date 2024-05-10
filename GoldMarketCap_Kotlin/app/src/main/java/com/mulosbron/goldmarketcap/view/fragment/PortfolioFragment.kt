package com.mulosbron.goldmarketcap.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mulosbron.goldmarketcap.R
import com.mulosbron.goldmarketcap.adapter.PortfolioAdapter
import com.mulosbron.goldmarketcap.view.APIService
import com.mulosbron.goldmarketcap.view.MainActivity

class PortfolioFragment : Fragment(), PortfolioAdapter.Listener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var btnAddTransaction: Button
    private var goldAssets: List<String> = listOf()
    private lateinit var apiService: APIService

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_portfolio, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.rvPortfolio)
        recyclerView.layoutManager = LinearLayoutManager(context)

        apiService = APIService(this)
        apiService.fetchGoldTransactions() { newGoldTypes ->
            updateGoldAssets(newGoldTypes)
        }

        btnAddTransaction = view.findViewById(R.id.btnSendInstructions)
        btnAddTransaction.setOnClickListener {
            (activity as? MainActivity)?.replaceFragment(AddTransactionFragment())
        }
    }

    override fun onItemClick(goldType: String) {
        Toast.makeText(context, "Clicked: $goldType", Toast.LENGTH_LONG).show()
    }

    private fun updateGoldAssets(newGoldAssets: List<String>) {
        this.goldAssets = newGoldAssets
        recyclerView.adapter = PortfolioAdapter(goldAssets, this)
    }
}
