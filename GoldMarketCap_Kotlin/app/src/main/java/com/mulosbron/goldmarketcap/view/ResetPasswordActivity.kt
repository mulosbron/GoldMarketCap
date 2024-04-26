package com.mulosbron.goldmarketcap.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.mulosbron.goldmarketcap.R
import com.mulosbron.goldmarketcap.model.ResetPasswordRequest
import com.mulosbron.goldmarketcap.model.ResetPasswordResponse
import com.mulosbron.goldmarketcap.service.UserAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ResetPasswordActivity : FooterActivity() {

    private val baseURL = "https://goldmarketcap.xyz/"
    //private val baseURL = "http://10.0.2.2:5000/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)
        setupFooterNavigation()

        val btnResetPassword: Button = findViewById(R.id.btnResetPassword)
        btnResetPassword.setOnClickListener {
            performResetPassword()
        }
    }

    private fun performResetPassword() {
        val etToken: EditText = findViewById(R.id.etToken)
        val etNewPassword: EditText = findViewById(R.id.etNewPassword)
        val etConfirmNewPassword: EditText = findViewById(R.id.etConfirmNewPassword)

        val token = etToken.text.toString().trim()
        val newPassword = etNewPassword.text.toString().trim()
        val confirmNewPassword = etConfirmNewPassword.text.toString().trim()

        if(token.isEmpty() || newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
            Toast.makeText(this, "Tüm alanları doldurun", Toast.LENGTH_SHORT).show()
            return
        }

        if(newPassword != confirmNewPassword) {
            Toast.makeText(this, "Şifreler eşleşmiyor", Toast.LENGTH_SHORT).show()
            return
        }

        val userAPI = Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserAPI::class.java)

        userAPI.resetPassword(ResetPasswordRequest(token, newPassword)).enqueue(object :
            Callback<ResetPasswordResponse> {
            override fun onResponse(call: Call<ResetPasswordResponse>, response: Response<ResetPasswordResponse>) {
                if (response.isSuccessful) {
                    val resetPasswordResponse = response.body()!!
                    Toast.makeText(this@ResetPasswordActivity, resetPasswordResponse.message, Toast.LENGTH_SHORT).show()
                    val loginIntent = Intent(this@ResetPasswordActivity, LoginActivity::class.java)
                    startActivity(loginIntent)
                } else {
                    Toast.makeText(this@ResetPasswordActivity, "Şifre sıfırlama başarısız", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResetPasswordResponse>, t: Throwable) {
                Toast.makeText(this@ResetPasswordActivity, "Ağ hatası: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
