package com.mulosbron.goldmarketcap.view

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import com.mulosbron.goldmarketcap.R

class RegisterActivity : FooterActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        setupFooterNavigation()

        val tvRegister: TextView = findViewById(R.id.tvLogin)
        tvRegister.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}

