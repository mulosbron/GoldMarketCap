package com.mulosbron.goldmarketcap.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.mulosbron.goldmarketcap.adapter.TransactionAdapter
import com.mulosbron.goldmarketcap.databinding.FragmentTransactionBinding
import com.mulosbron.goldmarketcap.service.ApiService
import com.mulosbron.goldmarketcap.view.MainActivity
import io.reactivex.disposables.CompositeDisposable

class TransactionFragment : Fragment() {

    private var _binding: FragmentTransactionBinding? = null
    private val binding get() = _binding!!
    private lateinit var apiService: ApiService
    private var compositeDisposable: CompositeDisposable? = null
    private lateinit var adapter: TransactionAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        apiService = ApiService(this)
        compositeDisposable = CompositeDisposable()

        binding.rvTransactions.layoutManager = LinearLayoutManager(context)

        val goldAsset = arguments?.getString("goldAsset")
        if (goldAsset != null) {
            apiService.fetchGoldTransactions { transactions ->
                val transactionList = transactions[goldAsset] ?: listOf()
                adapter = TransactionAdapter(transactionList, apiService, goldAsset, { transactionId ->
                    apiService.deleteTransaction(goldAsset, transactionId) {
                        Toast.makeText(context, "Transaction deleted", Toast.LENGTH_SHORT).show()
                        fetchTransactions(goldAsset)
                    }
                }, { transactionId ->
                    val bundle = Bundle().apply {
                        putString("transactionId", transactionId)
                        putString("goldType", goldAsset)
                    }
                    val updateFragment = UpdateTransactionFragment().apply {
                        arguments = bundle
                    }
                    (activity as? MainActivity)?.replaceFragment(updateFragment)
                })
                binding.rvTransactions.adapter = adapter
            }
        }

        binding.btnDeleteGoldAsset.setOnClickListener {
            if (goldAsset != null) {
                apiService.deleteGoldType(goldAsset) {
                    Toast.makeText(context, "Gold asset deleted", Toast.LENGTH_SHORT).show()
                    (activity as? MainActivity)?.replaceFragment(PortfolioFragment())
                }
            }
        }
    }

    private fun fetchTransactions(goldAsset: String) {
        apiService.fetchGoldTransactions { transactions ->
            val transactionList = transactions[goldAsset] ?: listOf()
            adapter.updateTransactions(transactionList)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable?.clear()
        _binding = null
    }
}
