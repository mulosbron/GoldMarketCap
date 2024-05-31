package com.mulosbron.goldmarketcap.view.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.mulosbron.goldmarketcap.adapter.AddTransactionAdapter
import com.mulosbron.goldmarketcap.databinding.FragmentAddTransactionBinding
import com.mulosbron.goldmarketcap.service.ApiService
import com.mulosbron.goldmarketcap.view.MainActivity
import io.reactivex.disposables.CompositeDisposable

class AddTransactionFragment : Fragment() {

    private var _binding: FragmentAddTransactionBinding? = null
    private val binding get() = _binding!!
    private lateinit var apiService: ApiService
    private var compositeDisposable: CompositeDisposable? = null
    private lateinit var adapter: AddTransactionAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        apiService = ApiService(this)
        compositeDisposable = CompositeDisposable()

        binding.rvGoldItems.layoutManager = LinearLayoutManager(context)
        adapter = AddTransactionAdapter { selectedGoldName ->
            val bundle = Bundle()
            bundle.putString("goldName", selectedGoldName)
            val addAssetFragment = AddAssetFragment()
            addAssetFragment.arguments = bundle
            (activity as? MainActivity)?.replaceFragment(addAssetFragment)
        }
        binding.rvGoldItems.adapter = adapter

        apiService.fetchGoldAssets(compositeDisposable!!) { goldAssets ->
            adapter.updateGoldAssets(goldAssets)
        }

        binding.etSearchGold.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrBlank()) {
                    apiService.fetchGoldAssets(compositeDisposable!!) { goldAssets ->
                        adapter.updateGoldAssets(goldAssets)
                    }
                } else {
                    apiService.searchGoldItems(s.toString()) { goldAssets ->
                        adapter.updateGoldAssets(goldAssets)
                    }
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable?.clear()
        _binding = null
    }
}
