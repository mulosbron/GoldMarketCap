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
import com.mulosbron.goldmarketcap.view.APIService

class ForgotPasswordFragment : Fragment() {

    private lateinit var etEmailForReset: EditText
    private lateinit var apiService: APIService

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_forgot_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etEmailForReset = view.findViewById(R.id.etEmailForReset)
        apiService = APIService(this)

        val btnSendInstructions: Button = view.findViewById(R.id.btnSendInstructions)
        btnSendInstructions.setOnClickListener {
            performForgotPassword()
        }
    }

    private fun performForgotPassword() {
        val email = etEmailForReset.text.toString().trim()

        if (email.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter your email address", Toast.LENGTH_SHORT).show()
            return
        }

        apiService.forgotPassword(email)
    }
}