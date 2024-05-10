package com.mulosbron.goldmarketcap.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.mulosbron.goldmarketcap.R
import com.mulosbron.goldmarketcap.view.APIService
import com.mulosbron.goldmarketcap.view.MainActivity

class LoginFragment : Fragment() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var apiService: APIService

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etEmail = view.findViewById(R.id.etEmail)
        etPassword = view.findViewById(R.id.etPassword)
        apiService = APIService(this)

        val tvRegister: TextView = view.findViewById(R.id.tvRegister)
        tvRegister.setOnClickListener {
            (activity as? MainActivity)?.replaceFragment(RegisterFragment())
        }

        val tvForgotPassword: TextView = view.findViewById(R.id.tvForgotPassword)
        tvForgotPassword.setOnClickListener {
            (activity as? MainActivity)?.replaceFragment(ForgotPasswordFragment())
        }

        val btnLogin: Button = view.findViewById(R.id.btnLogIn)
        btnLogin.setOnClickListener {
            performLogin()
        }
    }

    private fun performLogin() {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

        apiService.loginUser(email, password)
    }
}
