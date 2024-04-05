package com.mulosbron.goldmarketcap.view

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import com.mulosbron.goldmarketcap.R


class LoginActivity : FooterActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setupFooterNavigation()

        val tvRegister: TextView = findViewById(R.id.tvRegister)
        tvRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
