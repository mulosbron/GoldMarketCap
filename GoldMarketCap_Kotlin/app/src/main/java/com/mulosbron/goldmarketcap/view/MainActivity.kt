package com.mulosbron.goldmarketcap.view

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.mulosbron.goldmarketcap.R
import com.mulosbron.goldmarketcap.databinding.ActivityMainBinding
import com.mulosbron.goldmarketcap.view.fragment.*
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(MarketFragment())

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.navigation_markets -> {
                    replaceFragment(MarketFragment())
                    true
                }
                R.id.navigation_portfolio, R.id.navigation_settings -> {
                    if (checkUserLoggedIn()) {
                        replaceFragment(if (item.itemId == R.id.navigation_portfolio) PortfolioFragment() else SettingsFragment())
                        true
                    } else {
                        replaceFragment(LoginFragment())
                        true
                    }
                }
                else -> false
            }
        }
    }

    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.frameLayout, fragment).commit()
    }

    private fun checkUserLoggedIn(): Boolean {
        val sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        return sharedPreferences.getString("auth_token", null) != null
    }

    fun saveAuthToken(token: String) {
        val sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("auth_token", token).apply()
    }

    fun saveUsername(message: String) {
        val username = message.split(" ").last()
        val sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("username", username).apply()
    }

    fun getUsername(): String {
        val sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        return sharedPreferences.getString("username", "defaultUsername") ?: "defaultUsername"
    }

    fun logOutUser() {
        val sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        sharedPreferences.edit().remove("auth_token").apply()
        sharedPreferences.edit().remove("username").apply()
        replaceFragment(LoginFragment())
    }
}