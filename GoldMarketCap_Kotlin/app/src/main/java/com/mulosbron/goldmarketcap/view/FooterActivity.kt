package com.mulosbron.goldmarketcap.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.mulosbron.goldmarketcap.R

open class FooterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    protected fun setupFooterNavigation() {
        val marketsButton: Button = findViewById(R.id.marketsButton)
        marketsButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val portfolioButton: Button = findViewById(R.id.portfolioButton)
        portfolioButton.setOnClickListener {
            val isUserLoggedIn = checkUserLoggedIn()
            if (isUserLoggedIn) {
                val intent = Intent(this, EmptyPortfolioActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }

        /*
        val settingsButton: Button = findViewById(R.id.settingsButton)
        settingsButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
        */
    }

    private fun checkUserLoggedIn(): Boolean {
        val sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("auth_token", null)
        return token != null
    }
}
