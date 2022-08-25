package com.product.ridecheck.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.JsonObject
import com.product.ridecheck.R
import com.product.ridecheck.Utils
import com.product.ridecheck.viewmodels.LoginViewModel
import com.product.ridecheck.viewmodels.TripsViewModel

class LoginFragment : Fragment() {
    private lateinit var btnSignIn: MaterialButton
    private lateinit var emailField: TextInputEditText
    private lateinit var passwordField: TextInputEditText
    private lateinit var loginViewModel: LoginViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (Utils.AUTH_CODE != "") {
            findNavController().navigate(R.id.action_loginFragment_to_scheduledTripsFragment)
        }
        btnSignIn = view.findViewById(R.id.btnSignIn)
        emailField = view.findViewById(R.id.edtSignInEmail)
        passwordField = view.findViewById(R.id.edtSignInPassword)
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        btnSignIn.setOnClickListener {
            val userObject = JsonObject()
            userObject.addProperty(
                "username",
                emailField.text.toString()
            )
            userObject.addProperty(
                "password",
                passwordField.text.toString()
            )
            loginViewModel.loginUser(userObject)
            loginViewModel.userAuthLiveData.observe(
                viewLifecycleOwner
            ) {
                if (it != null) {
                    Utils.AUTH_CODE = it.authorization
                    findNavController().navigate(
                        R.id.action_loginFragment_to_scheduledTripsFragment
                    )
                } else {
                    Toast.makeText(activity, "Unable to login!", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

}