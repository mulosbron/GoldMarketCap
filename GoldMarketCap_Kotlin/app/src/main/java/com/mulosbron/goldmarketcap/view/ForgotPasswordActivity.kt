package com.mulosbron.goldmarketcap.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.mulosbron.goldmarketcap.R
import com.mulosbron.goldmarketcap.service.UserAPI
import com.mulosbron.goldmarketcap.model.ForgotPasswordRequest
import com.mulosbron.goldmarketcap.model.ForgotPasswordResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ForgotPasswordActivity : FooterActivity() {

    private val baseURL = "https://goldmarketcap.xyz/"
    //private val baseURL = "http://10.0.2.2:5000/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        setupFooterNavigation()

        val btnSendInstructions: Button = findViewById(R.id.btnSendInstructions)
        btnSendInstructions.setOnClickListener {
            performForgotPassword()
        }
    }

    private fun performForgotPassword() {
        val email = findViewById<EditText>(R.id.etEmailForReset).text.toString()

        if (email.isEmpty()) {
            Toast.makeText(this, "Please enter your email address", Toast.LENGTH_SHORT).show()
            return
        }

        val userAPI = Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserAPI::class.java)

        userAPI.forgotPassword(ForgotPasswordRequest(email)).enqueue(object :
            Callback<ForgotPasswordResponse> {
            override fun onResponse(call: Call<ForgotPasswordResponse>, response: Response<ForgotPasswordResponse>) {
                if (response.isSuccessful) {
                    val forgotPasswordResponse = response.body()!!
                    Toast.makeText(this@ForgotPasswordActivity, forgotPasswordResponse.message, Toast.LENGTH_SHORT).show()

                    val intent = Intent(this@ForgotPasswordActivity, ResetPasswordActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this@ForgotPasswordActivity, "An error occurred. Please try again", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ForgotPasswordResponse>, t: Throwable) {
                Toast.makeText(this@ForgotPasswordActivity, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}