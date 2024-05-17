package com.mulosbron.goldmarketcap.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.mulosbron.goldmarketcap.R
import com.mulosbron.goldmarketcap.service.ApiService

class ForgotPasswordFragment : Fragment() {

    private lateinit var etEmailForReset: EditText
    private lateinit var btnForgotPassword: Button
    private lateinit var apiService: ApiService

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_forgot_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etEmailForReset = view.findViewById(R.id.etEmailForReset)
        btnForgotPassword = view.findViewById(R.id.btnForgotPassword)
        apiService = ApiService(this)

        btnForgotPassword.setOnClickListener {
            performForgotPassword()
        }
    }

    private fun performForgotPassword() {
        val email = etEmailForReset.text.toString().trim()

        if (email.isEmpty()) {
            Toast.makeText(requireContext(), "Fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        apiService.forgotPassword(email)
    }
}