package com.mulosbron.goldmarketcap.view

import android.util.Log
import android.widget.ArrayAdapter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.mulosbron.goldmarketcap.model.ResetPasswordRequest
import com.mulosbron.goldmarketcap.model.ResetPasswordResponse
import com.mulosbron.goldmarketcap.model.AuthRequest
import com.mulosbron.goldmarketcap.model.AuthResponse
import com.mulosbron.goldmarketcap.model.DailyPercentage
import com.mulosbron.goldmarketcap.model.ForgotPasswordRequest
import com.mulosbron.goldmarketcap.model.ForgotPasswordResponse
import com.mulosbron.goldmarketcap.model.GoldPrice
import com.mulosbron.goldmarketcap.model.Transaction
import com.mulosbron.goldmarketcap.model.TransactionResponse
import com.mulosbron.goldmarketcap.service.DailyPercentagesAPI
import com.mulosbron.goldmarketcap.service.GoldPricesAPI
import com.mulosbron.goldmarketcap.service.PortfolioAPI
import com.mulosbron.goldmarketcap.service.UserAPI
import com.mulosbron.goldmarketcap.service.QueryAPI
import com.mulosbron.goldmarketcap.view.fragment.EmptyPortfolioFragment
import com.mulosbron.goldmarketcap.view.fragment.ResetPasswordFragment

class APIService(private val context: Fragment) {
    private val baseURL = "https://goldmarketcap.xyz/"

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun registerUser(email: String, password: String) {
        val userAPI = getRetrofit().create(UserAPI::class.java)
        val registerRequest = AuthRequest(email, password)
        userAPI.registerUser(registerRequest).enqueue(object : Callback<AuthResponse> {
            override fun onResponse(
                call: Call<AuthResponse>,
                response: Response<AuthResponse>
            ) {
                if (response.isSuccessful) {
                    val registerResponse = response.body()!!
                    Toast.makeText(context.requireContext(), "Registration successful: " + registerResponse.message, Toast.LENGTH_SHORT).show()
                    context.requireActivity().supportFragmentManager.popBackStack()
                } else {
                    Toast.makeText(context.requireContext(), "Registration unsuccessful: " + response.errorBody()?.string(), Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(
                call: Call<AuthResponse>,
                t: Throwable
            ) {
                Toast.makeText(context.requireContext(), "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun loginUser(email: String, password: String) {
        val userAPI = getRetrofit().create(UserAPI::class.java)
        userAPI.loginUser(AuthRequest(email, password)).enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                if (response.isSuccessful && response.body()?.token != null) {
                    (context.activity as? MainActivity)?.saveAuthToken(response.body()!!.token!!)
                    (context.activity as? MainActivity)?.saveUsername(response.body()!!.message)
                    Toast.makeText(context.requireContext(), "Login successful: ${response.body()!!.message.split(" ").last()}", Toast.LENGTH_SHORT).show()
                    (context.activity as? MainActivity)?.replaceFragment(EmptyPortfolioFragment())
                } else {
                    Toast.makeText(context.requireContext(), "Login unsuccessful: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Toast.makeText(context.requireContext(), "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun resetPassword(token: String, newPassword: String) {
        val userAPI = getRetrofit().create(UserAPI::class.java)
        userAPI.resetPassword(ResetPasswordRequest(token, newPassword)).enqueue(object : Callback<ResetPasswordResponse> {
            override fun onResponse(
                call: Call<ResetPasswordResponse>,
                response: Response<ResetPasswordResponse>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(context.requireContext(), "Password successfully reset", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context.requireContext(), "Password reset unsuccessful", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<ResetPasswordResponse>, t: Throwable) {
                Toast.makeText(context.requireContext(), "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun forgotPassword(email: String){
        val userAPI = getRetrofit().create(UserAPI::class.java)
        userAPI.forgotPassword(ForgotPasswordRequest(email)).enqueue(object : Callback<ForgotPasswordResponse> {
            override fun onResponse(call: Call<ForgotPasswordResponse>, response: Response<ForgotPasswordResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(context.requireContext(), "Instructions sent to your email.", Toast.LENGTH_SHORT).show()
                    (context.activity as? MainActivity)?.replaceFragment(ResetPasswordFragment())
                } else {
                    Toast.makeText(context.requireContext(), "Failed to send instructions. Please try again.", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<ForgotPasswordResponse>, t: Throwable) {
                Toast.makeText(context.requireContext(), "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun fetchGoldTransactions(callback: (List<String>) -> Unit) {
        val portfolioAPI = getRetrofit().create(PortfolioAPI::class.java)
        val username = (context.activity as? MainActivity)?.getUsername()
        if (username != null) {
            portfolioAPI.getTransactions(username).enqueue(object : Callback<Map<String, List<Transaction>>> {
                override fun onResponse(
                    call: Call<Map<String, List<Transaction>>>,
                    response: Response<Map<String, List<Transaction>>>
                ) {
                    if (response.isSuccessful) {
                        val newGoldTypes = response.body()?.keys?.toList() ?: listOf()
                        callback(newGoldTypes)
                    } else {
                        Toast.makeText(context.requireContext(), "Error: ${response.errorBody()?.string()}", Toast.LENGTH_LONG).show()
                    }
                }
                override fun onFailure(call: Call<Map<String, List<Transaction>>>, t: Throwable) {
                    Toast.makeText(context.requireContext(), "Network error: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        }
    }

    fun fetchGoldPrices(callback: (Map<String, GoldPrice>) -> Unit) {
        val goldPricesAPI = getRetrofit().create(GoldPricesAPI::class.java)
        goldPricesAPI.getLatestGoldPrices().enqueue(object : Callback<Map<String, GoldPrice>> {
            override fun onResponse(
                call: Call<Map<String, GoldPrice>>,
                response: Response<Map<String, GoldPrice>>
            ) {
                if (response.isSuccessful) {
                    val goldPrices = response.body() ?: emptyMap()
                    callback(goldPrices)
                } else {
                    Toast.makeText(context.requireContext(), "Failed to load gold prices", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<Map<String, GoldPrice>>, t: Throwable) {
                Toast.makeText(context.requireContext(), "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    fun fetchDailyPercentages(callback: (Map<String, DailyPercentage>) -> Unit) {
        val dailyPercentagesAPI = getRetrofit().create(DailyPercentagesAPI::class.java)
        dailyPercentagesAPI.getLatestDailyPercentages().enqueue(object : Callback<Map<String, DailyPercentage>> {
            override fun onResponse(
                call: Call<Map<String, DailyPercentage>>,
                response: Response<Map<String, DailyPercentage>>
            ) {
                if (response.isSuccessful) {
                    val dailyPercentages = response.body() ?: emptyMap()
                    callback(dailyPercentages)
                } else {
                    Toast.makeText(context.requireContext(), "Failed to load daily percentages", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<Map<String, DailyPercentage>>, t: Throwable) {
                Toast.makeText(context.requireContext(), "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    fun fetchGoldAssets(callback: (ArrayAdapter<String>) -> Unit) {
        val goldPricesAPI = getRetrofit().create(GoldPricesAPI::class.java)
        goldPricesAPI.getLatestGoldPrices().enqueue(object : Callback<Map<String, GoldPrice>> {
            override fun onResponse(call: Call<Map<String, GoldPrice>>, response: Response<Map<String, GoldPrice>>) {
                if (response.isSuccessful) {
                    val goldPrices = response.body() ?: emptyMap()
                    val goldNames = goldPrices.keys.toList()
                    val adapter = ArrayAdapter(context.requireContext(), android.R.layout.simple_list_item_1, goldNames)
                    callback(adapter)
                } else {
                    Toast.makeText(context.requireContext(), "Failed to load gold prices", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<Map<String, GoldPrice>>, t: Throwable) {
                Toast.makeText(context.requireContext(), "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    fun searchGoldItems(query: String, callback: (ArrayAdapter<String>) -> Unit) {
        val queryAPI = getRetrofit().create(QueryAPI::class.java)
        if (query.isNotEmpty()) {
            queryAPI.searchGoldPriceNames(query).enqueue(object : Callback<List<String>> {
                override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                    if (response.isSuccessful) {
                        val goldNames = response.body() ?: emptyList()
                        val adapter = ArrayAdapter(context.requireContext(), android.R.layout.simple_list_item_1, goldNames)
                        callback(adapter)
                    } else {
                        Toast.makeText(context.requireContext(), "Failed to load gold names", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<List<String>>, t: Throwable) {
                    Toast.makeText(context.requireContext(), "Error: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        }
    }

    // calismiyor
    fun fetchGoldPrice(productName: String, updateUI: (GoldPrice) -> Unit) {
        val goldPricesAPI = getRetrofit().create(GoldPricesAPI::class.java)

        goldPricesAPI.getGoldPrice(productName).enqueue(object : Callback<GoldPrice> {
            override fun onResponse(call: Call<GoldPrice>, response: Response<GoldPrice>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        updateUI(it)
                    }
                } else {
                    Toast.makeText(context.requireContext(), "Failed to fetch gold price", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<GoldPrice>, t: Throwable) {
                Toast.makeText(context.requireContext(), "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    // calismiyor
    fun sendTransaction(username: String, productName: String, transaction: Transaction) {
        val portfolioAPI = getRetrofit().create(PortfolioAPI::class.java)

        portfolioAPI.addTransaction(username, productName, transaction).enqueue(object : Callback<TransactionResponse> {
            override fun onResponse(call: Call<TransactionResponse>, response: Response<TransactionResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(context.requireContext(), response.body()?.message, Toast.LENGTH_LONG).show()
                } else {
                    val errorBody = response.errorBody()?.string()
                    Toast.makeText(context.requireContext(), "Failed to add transaction: $errorBody", Toast.LENGTH_SHORT).show()
                    Log.e("APIService", "Failed to add transaction: $errorBody")
                }
            }
            override fun onFailure(call: Call<TransactionResponse>, t: Throwable) {
                Toast.makeText(context.requireContext(), "Error: ${t.message}", Toast.LENGTH_LONG).show()
                Log.e("APIService", "Network error: ${t.message}", t)
            }
        })
    }
}

