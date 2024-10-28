package com.mulosbron.goldmarketcap.service

import com.mulosbron.goldmarketcap.model.AuthRequest
import com.mulosbron.goldmarketcap.model.AuthResponse
import com.mulosbron.goldmarketcap.model.ForgotPasswordRequest
import com.mulosbron.goldmarketcap.model.ForgotPasswordResponse
import com.mulosbron.goldmarketcap.model.ResetPasswordRequest
import com.mulosbron.goldmarketcap.model.ResetPasswordResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UserAPI {
    @POST("api/users/login")
    fun loginUser(@Body authRequest: AuthRequest): Call<AuthResponse>

    @POST("api/users/register")
    fun registerUser(@Body authRequest: AuthRequest): Call<AuthResponse>

    @POST("api/users/forgot-password")
    fun forgotPassword(@Body forgotPasswordRequest: ForgotPasswordRequest): Call<ForgotPasswordResponse>

    @POST("api/users/reset-password")
    fun resetPassword(@Body resetPasswordRequest: ResetPasswordRequest): Call<ResetPasswordResponse>
}