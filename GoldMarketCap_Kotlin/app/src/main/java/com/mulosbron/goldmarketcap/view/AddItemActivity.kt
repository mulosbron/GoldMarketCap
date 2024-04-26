package com.mulosbron.goldmarketcap.view

import android.os.Bundle
import com.mulosbron.goldmarketcap.R

class AddItemActivity : FooterActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)
        setupFooterNavigation()
    }
}