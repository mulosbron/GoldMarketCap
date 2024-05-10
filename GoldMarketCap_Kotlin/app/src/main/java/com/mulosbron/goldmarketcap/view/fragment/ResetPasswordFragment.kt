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

class ResetPasswordFragment : Fragment() {

    private lateinit var etToken: EditText
    private lateinit var etNewPassword: EditText
    private lateinit var etConfirmNewPassword: EditText
    private lateinit var apiService: APIService

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_reset_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etToken = view.findViewById(R.id.etToken)
        etNewPassword = view.findViewById(R.id.etNewPassword)
        etConfirmNewPassword = view.findViewById(R.id.etConfirmNewPassword)
        apiService = APIService(this)

        val btnResetPassword: Button = view.findViewById(R.id.btnResetPassword)
        btnResetPassword.setOnClickListener {
            performResetPassword()
        }
    }

    private fun performResetPassword() {

        val token = etToken.text.toString().trim()
        val newPassword = etNewPassword.text.toString().trim()
        val confirmNewPassword = etConfirmNewPassword.text.toString().trim()

        if(token.isEmpty() || newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
            Toast.makeText(requireContext(), "Fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        if(newPassword != confirmNewPassword) {
            Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT).show()
            return
        }

        apiService.resetPassword(token, newPassword)
    }
}