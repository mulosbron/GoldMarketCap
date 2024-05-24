package com.mulosbron.goldmarketcap.service

import android.widget.Toast
import androidx.fragment.app.Fragment
import com.mulosbron.goldmarketcap.model.AuthRequest
import com.mulosbron.goldmarketcap.model.AuthResponse
import com.mulosbron.goldmarketcap.model.DailyPercentage
import com.mulosbron.goldmarketcap.model.ForgotPasswordRequest
import com.mulosbron.goldmarketcap.model.ForgotPasswordResponse
import com.mulosbron.goldmarketcap.model.GoldPrice
import com.mulosbron.goldmarketcap.model.ResetPasswordRequest
import com.mulosbron.goldmarketcap.model.ResetPasswordResponse
import com.mulosbron.goldmarketcap.model.Transaction
import com.mulosbron.goldmarketcap.model.TransactionResponse
import com.mulosbron.goldmarketcap.model.TransactionUpdateModel
import com.mulosbron.goldmarketcap.view.MainActivity
import com.mulosbron.goldmarketcap.view.fragment.PortfolioFragment
import com.mulosbron.goldmarketcap.view.fragment.ResetPasswordFragment
import com.mulosbron.goldmarketcap.view.fragment.TransactionFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ApiService(private val context: Fragment) {
    private val baseURL = "https://goldmarketcap.xyz/"

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
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
                    Toast.makeText(
                        context.requireContext(),
                        "Registration successful: " + registerResponse.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    context.requireActivity().supportFragmentManager.popBackStack()
                } else {
                    Toast.makeText(
                        context.requireContext(),
                        "Registration unsuccessful: " + response.errorBody()?.string(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(
                call: Call<AuthResponse>,
                t: Throwable
            ) {
                Toast.makeText(
                    context.requireContext(),
                    "Network error: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
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
                    Toast.makeText(
                        context.requireContext(),
                        "Login successful: ${response.body()!!.message.split(" ").last()}",
                        Toast.LENGTH_SHORT
                    ).show()
                    (context.activity as? MainActivity)?.replaceFragment(PortfolioFragment())
                } else {
                    Toast.makeText(
                        context.requireContext(),
                        "Login unsuccessful: ${response.errorBody()?.string()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Toast.makeText(
                    context.requireContext(),
                    "Network error: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    fun resetPassword(token: String, newPassword: String) {
        val userAPI = getRetrofit().create(UserAPI::class.java)
        userAPI.resetPassword(ResetPasswordRequest(token, newPassword))
            .enqueue(object : Callback<ResetPasswordResponse> {
                override fun onResponse(
                    call: Call<ResetPasswordResponse>,
                    response: Response<ResetPasswordResponse>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            context.requireContext(),
                            "Password successfully reset",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            context.requireContext(),
                            "Password reset unsuccessful",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ResetPasswordResponse>, t: Throwable) {
                    Toast.makeText(
                        context.requireContext(),
                        "Network error: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    fun forgotPassword(email: String) {
        val userAPI = getRetrofit().create(UserAPI::class.java)
        userAPI.forgotPassword(ForgotPasswordRequest(email))
            .enqueue(object : Callback<ForgotPasswordResponse> {
                override fun onResponse(
                    call: Call<ForgotPasswordResponse>,
                    response: Response<ForgotPasswordResponse>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            context.requireContext(),
                            "Instructions sent to your email.",
                            Toast.LENGTH_SHORT
                        ).show()
                        (context.activity as? MainActivity)?.replaceFragment(ResetPasswordFragment())
                    } else {
                        Toast.makeText(
                            context.requireContext(),
                            "Failed to send instructions. Please try again.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ForgotPasswordResponse>, t: Throwable) {
                    Toast.makeText(
                        context.requireContext(),
                        "Network error: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    fun fetchGoldTransactions(callback: (Map<String, List<Transaction>>) -> Unit) {
        val portfolioAPI = getRetrofit().create(PortfolioAPI::class.java)
        val username = (context.activity as? MainActivity)?.getUsername()
        if (username != null) {
            portfolioAPI.getTransactions(username)
                .enqueue(object : Callback<Map<String, List<Transaction>>> {
                    override fun onResponse(
                        call: Call<Map<String, List<Transaction>>>,
                        response: Response<Map<String, List<Transaction>>>
                    ) {
                        if (response.isSuccessful) {
                            callback(response.body() ?: emptyMap())
                        } else {
                            Toast.makeText(
                                context.requireContext(),
                                "Error: ${response.errorBody()?.string()}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                    override fun onFailure(
                        call: Call<Map<String, List<Transaction>>>,
                        t: Throwable
                    ) {
                        Toast.makeText(
                            context.requireContext(),
                            "Network error: ${t.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
        }
    }

    fun fetchGoldPrices(
        compositeDisposable: CompositeDisposable,
        callback: (Map<String, GoldPrice>) -> Unit
    ) {
        val goldPricesAPI = getRetrofit().create(GoldPricesAPI::class.java)
        compositeDisposable.add(
            goldPricesAPI.getLatestGoldPrices()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    callback(response)
                }, { error ->
                    Toast.makeText(
                        context.requireContext(),
                        "Error: ${error.message}",
                        Toast.LENGTH_LONG
                    ).show()
                })
        )
    }

    fun fetchDailyPercentages(
        compositeDisposable: CompositeDisposable,
        callback: (Map<String, DailyPercentage>) -> Unit
    ) {
        val dailyPercentagesAPI = getRetrofit().create(DailyPercentagesAPI::class.java)
        compositeDisposable.add(
            dailyPercentagesAPI
                .getLatestDailyPercentages()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    callback(response)
                }, { error ->
                    Toast.makeText(
                        context.requireContext(),
                        "Error: ${error.message}",
                        Toast.LENGTH_LONG
                    ).show()
                })
        )
    }

    fun fetchGoldAssets(
        compositeDisposable: CompositeDisposable,
        callback: (List<String>) -> Unit
    ) {
        val goldPricesAPI = getRetrofit().create(GoldPricesAPI::class.java)
        compositeDisposable.add(
            goldPricesAPI
                .getLatestGoldPrices()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ goldPrices ->
                    val goldNames = goldPrices.keys.toList()
                    callback(goldNames)
                }, { error ->
                    Toast.makeText(
                        context.requireContext(),
                        "Failed to load gold prices: ${error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                })
        )
    }

    fun searchGoldItems(query: String, callback: (List<String>) -> Unit) {
        val queryAPI = getRetrofit().create(QueryAPI::class.java)
        if (query.isNotEmpty()) {
            queryAPI.searchGoldPriceNames(query).enqueue(object : Callback<List<String>> {
                override fun onResponse(
                    call: Call<List<String>>,
                    response: Response<List<String>>
                ) {
                    if (response.isSuccessful) {
                        val goldNames = response.body() ?: emptyList()
                        callback(goldNames)
                    } else {
                        Toast.makeText(
                            context.requireContext(),
                            "Failed to load gold names",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<List<String>>, t: Throwable) {
                    Toast.makeText(
                        context.requireContext(),
                        "Error: ${t.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        }
    }


    fun fetchGoldPrice(productName: String, updatePrice: (GoldPrice) -> Unit) {
        val goldPricesAPI = getRetrofit().create(GoldPricesAPI::class.java)
        goldPricesAPI.getGoldPrice(productName).enqueue(object : Callback<GoldPrice> {
            override fun onResponse(call: Call<GoldPrice>, response: Response<GoldPrice>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        updatePrice(it)
                    }
                } else {
                    Toast.makeText(
                        context.requireContext(),
                        "Failed to fetch gold price",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<GoldPrice>, t: Throwable) {
                Toast.makeText(context.requireContext(), "Error: ${t.message}", Toast.LENGTH_LONG)
                    .show()
            }
        })
    }

    fun sendTransaction(username: String, productName: String, transaction: Transaction) {
        val portfolioAPI = getRetrofit().create(PortfolioAPI::class.java)
        portfolioAPI.addTransaction(username, productName, transaction)
            .enqueue(object : Callback<TransactionResponse> {
                override fun onResponse(
                    call: Call<TransactionResponse>,
                    response: Response<TransactionResponse>
                ) {
                    if (response.isSuccessful) {
                        (context.activity as? MainActivity)?.replaceFragment(PortfolioFragment())
                        Toast.makeText(
                            context.requireContext(),
                            response.body()?.message,
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(
                            context.requireContext(),
                            "Failed to add transaction",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<TransactionResponse>, t: Throwable) {
                    Toast.makeText(
                        context.requireContext(),
                        "Error: ${t.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
    }

    fun calculateAverageBuyingPrices(transactions: Map<String, List<Transaction>>): Map<String, Double> {
        val averagePrices = mutableMapOf<String, Double>()
        transactions.forEach { (asset, transactionsList) ->
            val filteredTransactions = transactionsList.filter { it.transactionType == "buy" }
            if (filteredTransactions.isNotEmpty()) {
                val totalAmount = filteredTransactions.sumOf { it.amount }
                val totalPrice = filteredTransactions.sumOf { it.amount * it.price }
                val averagePrice = totalPrice.toDouble() / totalAmount
                averagePrices[asset] = averagePrice
            } else {
                averagePrices[asset] = 0.0
            }
        }
        return averagePrices
    }

    fun calculateProfits(
        goldPrices: Map<String, GoldPrice>,
        transactions: Map<String, List<Transaction>>
    ): Map<String, Double> {
        val profits = mutableMapOf<String, Double>()
        transactions.forEach { (asset, transactionsList) ->
            val buyTransactions = transactionsList.filter { it.transactionType == "buy" }
            val sellTransactions = transactionsList.filter { it.transactionType == "sell" }

            val totalBuyAmount = buyTransactions.sumOf { it.amount }
            val totalSellAmount = sellTransactions.sumOf { it.amount }
            val totalAmount = totalBuyAmount - totalSellAmount

            val totalPrice = buyTransactions.sumOf { it.amount * it.price }
            val averageBuyPrice = totalPrice / totalBuyAmount

            val currentSellingPrice = goldPrices[asset]?.sellingPrice ?: 0
            val profit = (totalAmount * currentSellingPrice) - (totalAmount * averageBuyPrice)
            profits[asset] = profit
        }
        return profits
    }

    fun calculateTotalPortfolioValue(
        goldPrices: Map<String, GoldPrice>,
        transactions: Map<String, List<Transaction>>
    ): Double {
        var totalValue = 0.0
        transactions.forEach { (asset, transactionsList) ->
            val totalAmount = transactionsList.sumOf { transaction ->
                if (transaction.transactionType == "buy") transaction.amount else -transaction.amount
            }
            val currentPrice = (goldPrices[asset]?.sellingPrice ?: 0).toDouble()
            totalValue += totalAmount * currentPrice
        }
        return totalValue
    }

    fun deleteGoldType(goldType: String, callback: () -> Unit) {
        val portfolioAPI = getRetrofit().create(PortfolioAPI::class.java)
        val username = (context.activity as? MainActivity)?.getUsername() ?: return
        portfolioAPI.deleteGoldType(username, goldType)
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        callback()
                    } else {
                        Toast.makeText(
                            context.requireContext(),
                            "Failed to delete GoldType: ${response.errorBody()?.string()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(
                        context.requireContext(),
                        "Network error: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    fun deleteTransaction(goldType: String, transactionId: String, callback: () -> Unit) {
        val portfolioAPI = getRetrofit().create(PortfolioAPI::class.java)
        val username = (context.activity as? MainActivity)?.getUsername() ?: return
        portfolioAPI.deleteTransaction(username, goldType, transactionId)
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        callback()
                    } else {
                        Toast.makeText(
                            context.requireContext(),
                            "Failed to delete transaction: ${response.errorBody()?.string()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(
                        context.requireContext(),
                        "Network error: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    fun getTransaction(transactionId: String, callback: (Transaction?) -> Unit) {
        val portfolioAPI = getRetrofit().create(PortfolioAPI::class.java)
        val username = (context.activity as? MainActivity)?.getUsername() ?: return
        portfolioAPI.getTransaction(username, transactionId)
            .enqueue(object : Callback<Transaction> {
                override fun onResponse(call: Call<Transaction>, response: Response<Transaction>) {
                    if (response.isSuccessful) {
                        callback(response.body())
                    } else {
                        Toast.makeText(
                            context.requireContext(),
                            "Failed to fetch transaction: ${response.errorBody()?.string()}",
                            Toast.LENGTH_SHORT
                        ).show()
                        callback(null)
                    }
                }

                override fun onFailure(call: Call<Transaction>, t: Throwable) {
                    Toast.makeText(
                        context.requireContext(),
                        "Network error: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    callback(null)
                }
            })
    }

    fun updateTransaction(transactionId: String, updateModel: TransactionUpdateModel, callback: () -> Unit) {
        val portfolioAPI = getRetrofit().create(PortfolioAPI::class.java)
        val username = (context.activity as? MainActivity)?.getUsername() ?: return
        portfolioAPI.updateTransaction(username, transactionId, updateModel)
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        (context.activity as MainActivity).replaceFragment(PortfolioFragment())
                        callback()
                    } else {
                        Toast.makeText(
                            context.requireContext(),
                            "Failed to update transaction: ${response.errorBody()?.string()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(
                        context.requireContext(),
                        "Network error: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}

