package com.example.mobileapplication.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mobileapplication.R
import com.example.mobileapplication.api.RetrofitClient
import com.example.mobileapplication.model.LoginRequest
import com.example.mobileapplication.utils.TokenManager
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginButton: Button
    private lateinit var registerLink: TextView
    private lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        tokenManager = TokenManager(this)

        if (tokenManager.getToken() != null) {
            navigateToDashboard()
            return
        }

        emailInput = findViewById(R.id.emailInput)
        passwordInput = findViewById(R.id.passwordInput)
        loginButton = findViewById(R.id.loginButton)
        registerLink = findViewById(R.id.registerLink)

        loginButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "All fields are required.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            performLogin(email, password)
        }

        registerLink.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun performLogin(email: String, password: String) {
        lifecycleScope.launch {
            try {
                Log.d("LoginActivity", "Attempting login for: $email")
                val response = RetrofitClient.apiService.login(LoginRequest(email, password))

                Log.d("LoginActivity", "Response code: ${response.code()}")

                if (response.isSuccessful && response.body() != null) {
                    val token = response.body()!!.string()
                    Log.d("LoginActivity", "Login successful, token received")
                    tokenManager.saveToken(token)
                    Toast.makeText(this@LoginActivity, "Login successful!", Toast.LENGTH_SHORT).show()
                    navigateToDashboard()
                } else {
                    Log.e("LoginActivity", "Login failed: ${response.code()}")
                    Toast.makeText(this@LoginActivity, "Invalid credentials.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("LoginActivity", "Login error: ${e.message}", e)
                Toast.makeText(this@LoginActivity, "Connection failed: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun navigateToDashboard() {
        startActivity(Intent(this, DashboardActivity::class.java))
        finish()
    }
}