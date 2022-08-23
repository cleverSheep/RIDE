package com.product.ridecheck.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.google.gson.JsonObject
import com.product.ridecheck.UserAuth
import com.product.ridecheck.repositories.LoginRepository

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private var loginRepository: LoginRepository = LoginRepository()
    private var _userAuthLiveData: LiveData<UserAuth?> = loginRepository.userAuthLiveData
    val userAuthLiveData = _userAuthLiveData

    /**
     * EXECUTE ADDITIONAL BUSINESS LOGIC WHEN PERFORMING REQUEST
     */

    fun loginUser(user: JsonObject) {
        loginRepository.loginUser(user)
    }
}