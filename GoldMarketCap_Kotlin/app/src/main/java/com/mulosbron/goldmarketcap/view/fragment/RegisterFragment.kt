package com.mulosbron.goldmarketcap.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.mulosbron.goldmarketcap.R
import com.mulosbron.goldmarketcap.view.APIService
import com.mulosbron.goldmarketcap.view.MainActivity

class RegisterFragment : Fragment() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var apiService: APIService

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etEmail = view.findViewById(R.id.etEmail)
        etPassword = view.findViewById(R.id.etPassword)
        etConfirmPassword= view.findViewById(R.id.etConfirmPassword)
        apiService = APIService(this)

        val btnSignup: Button = view.findViewById(R.id.btnSignUp)
        btnSignup.setOnClickListener {
            performRegister()
        }

        val tvLogin: TextView = view.findViewById(R.id.tvLogin)
        tvLogin.setOnClickListener {
            (activity as? MainActivity)?.replaceFragment(LoginFragment())
        }
    }

    private fun performRegister() {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val confirmPassword = etConfirmPassword.text.toString().trim()

        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(requireContext(), "Email and password fields cannot be left blank", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != confirmPassword) {
            Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT).show()
            return
        }

        apiService.registerUser(email, password)
    }
}
