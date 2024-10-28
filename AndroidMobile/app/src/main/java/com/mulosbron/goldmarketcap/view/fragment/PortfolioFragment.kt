package com.mulosbron.goldmarketcap.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.mulosbron.goldmarketcap.adapter.PortfolioAdapter
import com.mulosbron.goldmarketcap.databinding.FragmentPortfolioBinding
import com.mulosbron.goldmarketcap.service.ApiService
import com.mulosbron.goldmarketcap.view.MainActivity
import io.reactivex.disposables.CompositeDisposable

class PortfolioFragment : Fragment(), PortfolioAdapter.Listener {

    private var _binding: FragmentPortfolioBinding? = null
    private val binding get() = _binding!!
    private lateinit var apiService: ApiService
    private var goldAssets: List<String> = listOf()
    private var averagePrices: Map<String, Double> = emptyMap()
    private var profits: Map<String, Double> = emptyMap()
    private var compositeDisposable: CompositeDisposable? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPortfolioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        apiService = ApiService(this)
        compositeDisposable = CompositeDisposable()

        binding.rvPortfolio.layoutManager = LinearLayoutManager(context)

        binding.btnAddAsset.setOnClickListener {
            (activity as? MainActivity)?.replaceFragment(AddTransactionFragment())
        }
        apiService.fetchGoldTransactions { transactions ->
            val newGoldAssets = transactions.keys.toList()
            averagePrices = apiService.calculateAverageBuyingPrices(transactions)
            goldAssets = newGoldAssets
            apiService.fetchGoldPrices(compositeDisposable!!) { goldPrices ->
                profits = apiService.calculateProfits(goldPrices, transactions)
                val totalPortfolioValue =
                    apiService.calculateTotalPortfolioValue(goldPrices, transactions)
                binding.tvTotalValue.text = String.format("%.0f", totalPortfolioValue)
                binding.rvPortfolio.adapter = PortfolioAdapter(goldAssets, averagePrices, profits, this)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable?.clear()
        _binding = null
    }

    override fun onItemClick(goldAsset: String) {
        val bundle = Bundle()
        bundle.putString("goldAsset", goldAsset)
        val transactionFragment = TransactionFragment()
        transactionFragment.arguments = bundle
        (activity as? MainActivity)?.replaceFragment(transactionFragment)
    }
}
