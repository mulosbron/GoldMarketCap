package com.mulosbron.goldmarketcap.view.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mulosbron.goldmarketcap.R
import com.mulosbron.goldmarketcap.adapter.AddTransactionAdapter
import com.mulosbron.goldmarketcap.service.ApiService
import com.mulosbron.goldmarketcap.view.MainActivity
import io.reactivex.disposables.CompositeDisposable

class AddTransactionFragment : Fragment() {

    private lateinit var searchEditText: EditText
    private lateinit var goldAssetsRecyclerView: RecyclerView
    private lateinit var apiService: ApiService
    private var compositeDisposable: CompositeDisposable? = null
    private lateinit var adapter: AddTransactionAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_transaction, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchEditText = view.findViewById(R.id.etSearchGold)
        goldAssetsRecyclerView = view.findViewById(R.id.rvGoldItems)
        apiService = ApiService(this)
        compositeDisposable = CompositeDisposable()

        goldAssetsRecyclerView.layoutManager = LinearLayoutManager(context)
        adapter = AddTransactionAdapter { selectedGoldName ->
            val bundle = Bundle()
            bundle.putString("goldName", selectedGoldName)
            val addAssetFragment = AddAssetFragment()
            addAssetFragment.arguments = bundle
            (activity as? MainActivity)?.replaceFragment(addAssetFragment)
        }
        goldAssetsRecyclerView.adapter = adapter

        apiService.fetchGoldAssets(compositeDisposable!!) { goldAssets ->
            adapter.updateGoldAssets(goldAssets)
        }

        searchEditText.addTextChangedListener(object : TextWatcher {
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

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable?.clear()
    }
}
