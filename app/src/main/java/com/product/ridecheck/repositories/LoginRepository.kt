package com.product.ridecheck.repositories

import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.product.ridecheck.UserAuth
import com.product.ridecheck.network.APIClient
import com.product.ridecheck.network.LoginAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginRepository {
    private val _userAuthLiveData = MutableLiveData<UserAuth?>()
    val userAuthLiveData = _userAuthLiveData

    fun loginUser(userData: JsonObject) {
        val retrofit = APIClient.getClient().create(LoginAPI::class.java)
        retrofit.loginUser(userData).enqueue(
            object : Callback<UserAuth> {
                override fun onResponse(call: Call<UserAuth>?, response: Response<UserAuth>?) {
                    val userAuth = response?.body()
                    _userAuthLiveData.postValue(userAuth)
                }

                override fun onFailure(call: Call<UserAuth>?, t: Throwable?) {
                    _userAuthLiveData.postValue(null)
                }
            }
        )
    }
}