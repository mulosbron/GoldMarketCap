package com.mulosbron.goldmarketcap.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.mulosbron.goldmarketcap.R
import com.mulosbron.goldmarketcap.view.APIService
import android.text.Editable
import android.text.TextWatcher
import com.mulosbron.goldmarketcap.view.MainActivity

class AddTransactionFragment : Fragment() {

    private lateinit var goldAssetsListView: ListView
    private lateinit var searchEditText: EditText
    private lateinit var apiService: APIService

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_transaction, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        goldAssetsListView = view.findViewById(R.id.lvGoldItems)
        searchEditText = view.findViewById(R.id.etSearchGold)
        apiService = APIService(this)

        apiService.fetchGoldAssets { adapter ->
            goldAssetsListView.adapter = adapter
        }

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                apiService.searchGoldItems(s.toString()) { adapter ->
                    goldAssetsListView.adapter = adapter
                }
            }
        })

        goldAssetsListView.setOnItemClickListener { adapterView, view, position, id ->
            val selectedGoldName = adapterView.getItemAtPosition(position) as String
            val bundle = Bundle()
            bundle.putString("goldName", selectedGoldName)
            (activity as? MainActivity)?.replaceFragment(AddAssetFragment())
        }
    }
}
