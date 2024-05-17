package com.mulosbron.goldmarketcap.view.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.mulosbron.goldmarketcap.R
import com.mulosbron.goldmarketcap.service.ApiService
import com.mulosbron.goldmarketcap.view.MainActivity
import io.reactivex.disposables.CompositeDisposable

class AddTransactionFragment : Fragment() {

    private lateinit var searchEditText: EditText
    private lateinit var goldAssetsListView: ListView
    private lateinit var apiService: ApiService
    private var compositeDisposable: CompositeDisposable? = null

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
        goldAssetsListView = view.findViewById(R.id.lvGoldItems)
        apiService = ApiService(this)
        compositeDisposable = CompositeDisposable()

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                apiService.searchGoldItems(s.toString()) { adapter ->
                    goldAssetsListView.adapter = adapter
                }
            }
        })
        goldAssetsListView.setOnItemClickListener { adapterView, _, position, _ ->
            val selectedGoldName = adapterView.getItemAtPosition(position) as String
            val bundle = Bundle()
            bundle.putString("goldName", selectedGoldName)
            val addAssetFragment = AddAssetFragment()
            addAssetFragment.arguments = bundle
            (activity as? MainActivity)?.replaceFragment(addAssetFragment)
        }
        apiService.fetchGoldAssets(compositeDisposable!!) { adapter ->
            goldAssetsListView.adapter = adapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        compositeDisposable?.clear()
    }
}
